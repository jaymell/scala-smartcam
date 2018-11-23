package com.jaymell.smartcam

import akka.Done
import akka.stream.scaladsl.Sink
import com.google.api.core.ApiFuture
import com.google.cloud.pubsub.v1.Publisher
import com.google.protobuf.ByteString
import com.google.pubsub.v1.PubsubMessage
import com.jaymell.smartcam.util.RichListenableFuture
import org.bytedeco.javacpp.opencv_core.{IPL_DEPTH_8U, Mat}
import org.bytedeco.javacv.Frame

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


object PubSubFrameWriter extends FrameWriter {

  val topic = Main.settings.pubSubTopic
  val publisher = Publisher.newBuilder(topic).build()

  def sink()(implicit ec: ExecutionContext): Sink[Option[SFrame], Future[Done]] = Sink.foreach[Option[SFrame]](f => {
    f.foreach { f =>
      val curImage: Mat = SFrame.matConverter.convert(new Frame(f.width, f.height, IPL_DEPTH_8U, 1))
      try {
        val data = ByteString.copyFrom(curImage.asByteBuffer())
        val pubsubMessage: PubsubMessage = PubsubMessage.newBuilder.setData(data).build
        val messageIdFuture: ApiFuture[String] = publisher.publish(pubsubMessage)

        new RichListenableFuture[String](messageIdFuture).asScala.onComplete {
          case Success(_) =>
          case Failure(e) => println("Publish Message Failed: ", e)
        }
      } catch {
        case e: Exception => println("FAILED: ", e)
      }
    }
  })
}
