package com.jaymell.smartcam

import akka.NotUsed
import akka.stream.ClosedShape
import akka.stream.scaladsl.GraphDSL.Implicits._
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Merge, RunnableGraph, Sink, Source}
import org.bytedeco.javacv.CanvasFrame

object Graph {
  def graph(source: Source[SFrame, NotUsed]): RunnableGraph[NotUsed] = {
    RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
      val out = Sink.ignore
      val canvas = new CanvasFrame("Webcam")
      val broadcast = builder.add(Broadcast[SFrame](2))
      val f1 = Sink.foreach[SFrame] { f: SFrame =>
        println(f.timestamp)
        canvas.showImage(f.frame)
      }
      source ~> broadcast ~> f1
      broadcast ~> out
      ClosedShape
    })
  }
}
