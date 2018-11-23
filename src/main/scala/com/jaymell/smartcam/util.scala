package com.jaymell.smartcam

import com.google.api.core.{ApiFuture, ApiFutureCallback, ApiFutures}

import scala.concurrent.{Future, Promise}

object util {

  implicit class RichListenableFuture[T](lf: ApiFuture[T]) {
    def asScala: Future[T] = {
      val p = Promise[T]()
      ApiFutures.addCallback(lf, new ApiFutureCallback[T] {
        def onFailure(t: Throwable): Unit = p failure t

        def onSuccess(result: T): Unit = p success result
      })
      p.future
    }
  }
}
