package com.jaymell.smartcam

import java.time.{LocalDateTime, ZoneOffset}
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.OpenCVFrameConverter


class SFrame(val frame: Frame, val timestamp: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC)) {
  // camera ID
  // image type -- ie 'jpeg'
  val height = frame.imageHeight
  val width = frame.imageWidth
  override def clone(): SFrame = new SFrame(frame.clone(), timestamp)
}

object SFrame {
  val matConverter = new OpenCVFrameConverter.ToMat
  val IplConverter = new OpenCVFrameConverter.ToIplImage
}

