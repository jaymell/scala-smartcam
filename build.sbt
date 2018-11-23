name := "smartcam"

version := "0.1"

scalaVersion := "2.12.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.5.18",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.18" % Test,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
  "com.google.cloud" % "google-cloud-pubsub" % "1.53.0",
  "com.typesafe" % "config" % "1.3.2"
  //  "org.bytedeco" % "javacv-platform" % "1.4.3"
)

javaCppPresetLibs ++= Seq(
  "ffmpeg" -> "3.4.1"
)