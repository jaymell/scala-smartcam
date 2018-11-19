package com.jaymell.smartcam

import scala.concurrent.duration._
import akka.NotUsed
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

  def getSource(): Source[Frame, NotUsed] = {
    return Source.repeat(NotUsed).map(_ => grabber.grab).throttle(1, 100.millisecond)
  }

}
