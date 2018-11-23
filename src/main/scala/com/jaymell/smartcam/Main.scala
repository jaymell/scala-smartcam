package com.jaymell.smartcam

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Supervision}
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger

import scala.concurrent.ExecutionContext

object Main extends App {
  implicit val system = ActorSystem("smartcam")
  implicit val materializer = ActorMaterializer()

  val settings = new Settings(ConfigFactory.load())

  val logger = Logger("smartcam")
   val decider: Supervision.Decider = { e =>
    logger.error("Unhandled exception in stream", e)
    Supervision.Stop
  }

  val materializerSettings = ActorMaterializerSettings(system).withSupervisionStrategy(decider)

  implicit val ec = ExecutionContext.global
  Graph.graph().run()
}

class Settings(config: Config) {
  val pubSubTopic = config.getString("smartcam.pubSubTopic")
}

