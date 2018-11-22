package com.jaymell.smartcam

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Sink, Source}
import org.bytedeco.javacv._
import org.bytedeco.javacpp._
import org.bytedeco.javacpp.opencv_core._
import org.bytedeco.javacpp.opencv_imgproc._

object FrameDiffMotionDetector {

  var lastFrame: SFrame = null
  val Threshold = 25
  // clone
  // delta -- aka absdiff between previous and current ?
  // threshold
  // dilate
  // find contours

  def detectMotion(f: SFrame): Boolean = {
    cvThreshold(f.frame, Threshold, 255, CV_THRESH_BINARY)
  }

  def flow(): Flow[SFrame, SFrame, NotUsed] = {
    Flow.fromFunction[SFrame, SFrame](f => {
      f
    })
    //.filter(f => false)

  }
}
