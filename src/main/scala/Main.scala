package com.jaymell.smartcam

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.bytedeco.javacv.CanvasFrame

object Main extends App {
  implicit val system = ActorSystem("smartcam")
  implicit val materializer = ActorMaterializer()

  val source = FrameSource.getSource()
  Graph.graph(source).run()
}


