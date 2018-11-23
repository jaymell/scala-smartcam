package com.jaymell.smartcam

import akka.{Done, NotUsed}
import akka.stream.scaladsl.Sink

import scala.concurrent.{ExecutionContext, Future}

abstract class FrameWriter {
  def sink()(implicit ec: ExecutionContext): Sink[Option[SFrame], Future[Done]]
}
