package com.jaymell.smartcam

import akka.NotUsed
import akka.stream._
import akka.stream.scaladsl.GraphDSL.Implicits._
import akka.stream.scaladsl.{Broadcast, BroadcastHub, GraphDSL, Keep, Sink, Source}
import org.bytedeco.javacv.CanvasFrame

import scala.concurrent.ExecutionContext

object Graph {
  def source(motionSwitch: Boolean => Unit)(implicit ec: ExecutionContext) = {
    Source.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>

      val source = FrameSource.source()
      val motionDetectionFlow = builder.add(new FrameDiffMotionDetector(motionSwitch).flow())
      val canvas = new CanvasFrame("Webcam")
      val frameBroadcast = builder.add(Broadcast[SFrame](2))
//      val frameWriterSink = PubSubFrameWriter.sink()
      val rawStreamSink = Sink.foreach[SFrame] { f: SFrame =>
        println(f.timestamp)
        canvas.showImage(f.frame)
      }

      source ~>
        frameBroadcast ~>
        rawStreamSink
      frameBroadcast ~>
        motionDetectionFlow

      SourceShape(motionDetectionFlow.out)
    })
  }
}
