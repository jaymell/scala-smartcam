package com.jaymell.smartcam

import akka.NotUsed
import akka.stream.scaladsl.Flow
import org.bytedeco.javacpp.opencv_core._
import org.bytedeco.javacpp.opencv_imgproc._
import org.bytedeco.javacv._


object FrameDiffMotionDetector extends MotionDetector {

  var lastImg: Option[Mat] = None
  val PixelThreshold = 25
  val MaxValue = 255
  val AreaThreshold = 200 // FIXME: this needs to be a function of image size
  val canvas = new CanvasFrame("DEBUGGER")

    implicit class IterativeMatVector(val mV: MatVector) extends AnyVal {
    def iter() = (0L until mV.size()).view.map(mV.get)
  }

  def detectMotion(f: SFrame): Boolean = {
    val curImage: Mat = SFrame.matConverter.convert(new Frame(f.width, f.height, IPL_DEPTH_8U, 1))
    cvtColor(SFrame.matConverter.convert(f.frame), curImage, CV_RGB2GRAY)

    val contours = new MatVector()
    // if lastImg not None
    lastImg.foreach(li => {
      val diff: Mat = SFrame.matConverter.convert(new Frame(f.width, f.height, IPL_DEPTH_8U, 1))
      absdiff(curImage, li, diff)
      threshold(diff, diff, PixelThreshold, MaxValue, CV_THRESH_BINARY)
      dilate(diff, diff, Mat.ones(3, 3, CV_8UC1).asMat())
      canvas.showImage(SFrame.IplConverter.convert(diff))
      findContours(new Mat(diff), contours, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE)
    })

    val fc = contours.iter().filter(m => {
      contourArea(m) > AreaThreshold
    })

    lastImg = Option(curImage.clone())

    if (fc.isEmpty) false
    else true
  }

  def flow(): Flow[SFrame, SFrame, NotUsed] = {
    Flow.fromFunction[SFrame, SFrame](f => f).filter(f => {
      val found = detectMotion(f)
      if (found) println("Motion detected!")
      found
    })
  }
}
