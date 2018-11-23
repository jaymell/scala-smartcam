package com.jaymell.smartcam

import akka.NotUsed
import akka.stream.scaladsl.Flow

abstract class MotionDetector {
  def detectMotion(f: SFrame): (Boolean, Option[SFrame])
  def flow: Flow[SFrame, Option[SFrame], NotUsed]
}
