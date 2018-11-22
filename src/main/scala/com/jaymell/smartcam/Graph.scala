package com.jaymell.smartcam

import akka.NotUsed
import akka.stream.ClosedShape
import akka.stream.scaladsl.GraphDSL.Implicits._
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Merge, RunnableGraph, Sink, Source}
import org.bytedeco.javacv.CanvasFrame

object Graph {
  def graph(): RunnableGraph[NotUsed] = {
    RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>

      val source = FrameSource.source()
      val motionDetectionFlow = FrameDiffMotionDetector.flow()
      val canvas = new CanvasFrame("Webcam")
      val frameBroadcast = builder.add(Broadcast[SFrame](2))
      val rawStreamSink = Sink.foreach[SFrame] { f: SFrame =>
        println(f.timestamp)
        canvas.showImage(f.frame)
      }
      source ~> frameBroadcast ~> rawStreamSink
      frameBroadcast ~> motionDetectionFlow ~> Sink.ignore
      ClosedShape
    })
  }
}