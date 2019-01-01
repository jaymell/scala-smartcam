package com.jaymell.smartcam

import akka.NotUsed
import akka.stream.scaladsl.Flow
import akka.util.ByteString

abstract class VideoRecorder {
  def flow: Flow[Option[SFrame], Option[ByteString], NotUsed]
}
