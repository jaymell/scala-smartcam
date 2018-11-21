package com.jaymell.smartcam

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

object Main extends App {
  implicit val system = ActorSystem("smartcam")
  implicit val materializer = ActorMaterializer()

  Graph.graph().run()
}


