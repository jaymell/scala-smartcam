name := "smartcam"

version := "0.1"

scalaVersion := "2.12.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.5.18",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.18" % Test
//  "org.bytedeco" % "javacv-platform" % "1.4.3"
)

javaCppPresetLibs ++= Seq(
  "ffmpeg" -> "3.4.1"
)