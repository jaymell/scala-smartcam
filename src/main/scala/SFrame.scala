package com.jaymell.smartcam

import java.time.{LocalDateTime, ZoneOffset}
import org.bytedeco.javacv.Frame

class SFrame(val frame: Frame) {
  // camera ID
  // image type -- ie 'jpeg'
  val timestamp = LocalDateTime.now(ZoneOffset.UTC)
  val height = frame.imageHeight
  val width = frame.imageWidth
}
