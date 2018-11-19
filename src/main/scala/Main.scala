package com.jaymell.smartcam

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.bytedeco.javacv.CanvasFrame

object Main extends App {
  implicit val system = ActorSystem("smartcam")
  implicit val materializer = ActorMaterializer()

  val canvas = new CanvasFrame("Webcam")
  val source = FrameSource.getSource()
  source.runForeach(canvas.showImage)
}

