package com.jaymell.smartcam

import akka.Done
import akka.actor.ActorSystem
import akka.stream.scaladsl.{BroadcastHub, Keep, Sink}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Supervision}
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger
import org.bytedeco.javacv.CanvasFrame

import scala.concurrent.{ExecutionContext, Future}

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

  val motionCanvas = new CanvasFrame("Motion")
  val motionSink: Sink[Option[SFrame], Future[Done]] =
    Sink.foreach((f: Option[SFrame]) => {
      f.foreach(f => {
        motionCanvas.showImage(f.frame)
      })
    })

  var stopped = false
  def motionKillSwitch(stop: Boolean) = {
    if (stop) {
      if (!stopped) {
        stopped = true
        logger.info("Starting.")
      }
    }
    else {
      logger.info("Killing.")
      stopped = false
    }
  }

  val motionSource = Graph.source(motionKillSwitch)
  val motionBroadcastSource = motionSource.toMat(BroadcastHub.sink(64))(Keep.right).run()
  // initial, permanent subscription:
  motionBroadcastSource.to(motionSink).run()

}

class Settings(config: Config) {
  val pubSubTopic = config.getString("smartcam.pubSubTopic")
}

