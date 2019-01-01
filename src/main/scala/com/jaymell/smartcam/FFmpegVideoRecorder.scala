package com.jaymell.smartcam

import akka.NotUsed
import akka.stream.scaladsl.Flow
import akka.util.ByteString
import akka.util.ByteStringBuilder
import org.bytedeco.javacv.FFmpegFrameRecorder

//object FFmpegVideoRecorder extends VideoRecorder {
//  val bs: ByteStringBuilder = new ByteStringBuilder()
//  val fr: FFmpegFrameRecorder = new FFmpegFrameRecorder()
//
//  def flow: Flow[Option[SFrame], Option[ByteString], NotUsed] = {
//  }
//}
