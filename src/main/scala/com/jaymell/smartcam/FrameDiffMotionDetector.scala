package com.jaymell.smartcam

import java.time.temporal.ChronoUnit
import java.time.{Duration, LocalDateTime, ZoneOffset}

import akka.NotUsed
import akka.stream.scaladsl.Flow
import org.bytedeco.javacpp.opencv_core._
import org.bytedeco.javacpp.opencv_imgproc._
import org.bytedeco.javacv._

class IterativeMatVector(val mV: MatVector) extends AnyVal {
  def iter() = (0L until mV.size()).view.map(mV.get)
}

class FrameDiffMotionDetector(motionSwitch: MotionSwitch) extends MotionDetector {

  implicit def iterativeMatVector(m: MatVector) = new IterativeMatVector(m)

  val PixelThreshold = 25
  val MaxValue = 255
  val AreaThreshold = 200 // FIXME: this needs to be a function of image size
  //  val canvas = new CanvasFrame("DEBUGGER")
  val TimeoutWindow = Duration.of(5, ChronoUnit.SECONDS)
  var isInMotion = false

  var lastImg: Option[Mat] = None
  var lastMotionTime: LocalDateTime = null


  def detectMotion(f: SFrame): (Boolean, Option[SFrame]) = {
    val curImage: Mat = SFrame.matConverter.convert(new Frame(f.width, f.height, IPL_DEPTH_8U, 1))
    cvtColor(SFrame.matConverter.convert(f.frame), curImage, CV_RGB2GRAY)

    val contours = new MatVector()
    // if lastImg not None
    lastImg.foreach(li => {
      val diff: Mat = SFrame.matConverter.convert(new Frame(f.width, f.height, IPL_DEPTH_8U, 1))
      absdiff(curImage, li, diff)
      threshold(diff, diff, PixelThreshold, MaxValue, CV_THRESH_BINARY)
      dilate(diff, diff, Mat.ones(3, 3, CV_8UC1).asMat())
      //      canvas.showImage(SFrame.IplConverter.convert(diff))
      findContours(new Mat(diff), contours, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE)
    })

    val fc = contours.iter().filter(m => {
      contourArea(m) > AreaThreshold
    })

    lastImg = Option(curImage.clone())

    if (fc.isEmpty) {
      if (lastMotionTime == null) (false, None)
      else if (f.timestamp.minus(TimeoutWindow).isAfter(lastMotionTime)) {
        lastMotionTime = null
        motionSwitch.stop()
        (true, None)
      } else {
        (true, Some(f))
      }
    }
    else {
      motionSwitch.start()
      lastMotionTime = LocalDateTime.now(ZoneOffset.UTC)
      (true, Some(f))
    }
  }

  def flow(): Flow[SFrame, Option[SFrame], NotUsed] =
    Flow.fromFunction[SFrame, SFrame](f => f.clone())
      .map(detectMotion)
      .collect { case (found, frame) if (found) => {
        println("Motion detected!");
        frame
      }
      }
}

