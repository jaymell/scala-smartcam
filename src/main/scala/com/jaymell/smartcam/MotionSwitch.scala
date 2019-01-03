package com.jaymell.smartcam

import akka.stream.scaladsl.{Keep, Sink, Source}
import akka.stream.{ActorMaterializer, KillSwitches, UniqueKillSwitch}
import akka.{Done, NotUsed}
import com.jaymell.smartcam.Main.logger
import org.bytedeco.javacv.CanvasFrame

import scala.concurrent.{ExecutionContext, Future}

class MotionSwitch(source: Source[Option[SFrame], NotUsed], implicit val ec: ExecutionContext, implicit val materializer: ActorMaterializer) {

  var switch: Option[UniqueKillSwitch] = None

  def tempMotionSink(canvas: CanvasFrame): Sink[Option[SFrame], Future[Done]] =
    Sink.foreach((f: Option[SFrame]) => {
      f.foreach(f => {
        canvas.showImage(f.frame)
      })
    })

  def start() = switch match {
    case None => {
      logger.info("Starting.")
      try {
        val tempMotionCanvas = new CanvasFrame("TempMotion")
        switch = Some(
          source
            // FIXME: clone
            .viaMat(KillSwitches.single)(Keep.right)
            .to(tempMotionSink(tempMotionCanvas))
            .run()
        )
      } catch {
        case e: Exception => logger.error("${e}")
      }
    }
    case _ => logger.info("switch is not None")
  }

  def stop() = switch match {
    case Some(i) => {
      logger.info("Stopping.")
      switch.foreach(_.shutdown())
      switch = None
    }
    case _ => logger.error("stop called on None")
  }
}