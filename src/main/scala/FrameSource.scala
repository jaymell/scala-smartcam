package com.jaymell.smartcam

import scala.concurrent.duration._
import akka.NotUsed
import akka.stream.SourceShape
import akka.stream.scaladsl._
import org.bytedeco.javacpp.opencv_core._
import org.bytedeco.javacv.FrameGrabber.ImageMode
import org.bytedeco.javacv.{Frame, OpenCVFrameGrabber}

object FrameSource {

  val grabber = new OpenCVFrameGrabber(0)
  grabber.setImageWidth(1280)
  grabber.setImageHeight(480)
  grabber.setBitsPerPixel(CV_8U)
  grabber.setImageMode(ImageMode.COLOR)
  grabber.start()

  def frame(): SFrame = {
    new SFrame(grabber.grab())
  }

  def source(): Source[SFrame, NotUsed] =
    Source.repeat(NotUsed).map(_ => frame).throttle(1, 100.millisecond)
}

