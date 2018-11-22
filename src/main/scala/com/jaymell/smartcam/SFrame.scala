package com.jaymell.smartcam

import java.time.{LocalDateTime, ZoneOffset}
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.OpenCVFrameConverter


class SFrame(val frame: Frame) {
  // camera ID
  // image type -- ie 'jpeg'
  val timestamp = LocalDateTime.now(ZoneOffset.UTC)
  val height = frame.imageHeight
  val width = frame.imageWidth
//  lazy val mat = SFrame.converterToMat.convert(frame)
//  lazy val ipl = SFrame.converter(frame)
  // def clone()
}

object SFrame {
  val matConverter = new OpenCVFrameConverter.ToMat
  val IplConverter = new OpenCVFrameConverter.ToIplImage
}
