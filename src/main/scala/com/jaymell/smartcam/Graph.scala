package com.jaymell.smartcam

import akka.NotUsed
import akka.stream.ClosedShape
import akka.stream.scaladsl.GraphDSL.Implicits._
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink}
import org.bytedeco.javacv.CanvasFrame

import scala.concurrent.ExecutionContext

object Graph {
  def graph()(implicit ec: ExecutionContext): RunnableGraph[NotUsed] = {
    RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>

      val source = FrameSource.source()
      val motionDetectionFlow = FrameDiffMotionDetector.flow()
      val canvas = new CanvasFrame("Webcam")
      val motionCanvas = new CanvasFrame("Motion")
      val frameBroadcast = builder.add(Broadcast[SFrame](2))
      val frameWriterSink = PubSubFrameWriter.sink()
      val rawStreamSink = Sink.foreach[SFrame] { f: SFrame =>
        println(f.timestamp)
        canvas.showImage(f.frame)
      }
      val motionFlow: Flow[Option[SFrame], Option[SFrame], NotUsed] =
        Flow.fromFunction[Option[SFrame], Option[SFrame]](f => {
          f.foreach(f => {
            motionCanvas.showImage(f.frame)
          })
          f
        })
      source ~>
        frameBroadcast ~>
        rawStreamSink
      frameBroadcast ~>
        motionDetectionFlow ~> motionFlow ~> frameWriterSink
      ClosedShape
    })
  }
}
