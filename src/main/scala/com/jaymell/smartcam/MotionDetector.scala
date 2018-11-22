package com.jaymell.smartcam

import akka.NotUsed
import akka.stream.scaladsl.Flow

abstract class MotionDetector {
  def detectMotion(f: SFrame): Boolean
  def flow: Flow[SFrame, SFrame, NotUsed]
}
