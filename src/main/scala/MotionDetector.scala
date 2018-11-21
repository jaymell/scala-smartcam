package com.jaymell.smartcam

import akka.NotUsed
import akka.stream.scaladsl.{Sink, Source}

abstract class MotionDetector {
  def detectMotion(): Unit
}

class FrameDiffMotionDetector(frameSource: Source[SFrame, NotUsed]) extends MotionDetector {
  def detectMotion(): Unit = {
    frameSource.to(Sink.ignore)
  }
}
