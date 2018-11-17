import org.bytedeco.javacv.OpenCVFrameGrabber
import org.bytedeco.javacpp.opencv_core._
import org.bytedeco.javacv.{ FrameGrabber, Frame }
import org.bytedeco.javacv.FrameGrabber.ImageMode

object Main extends App {
  val grabber = new OpenCVFrameGrabber(0)
  grabber.setImageWidth(640)
  grabber.setImageHeight(480)
  grabber.setBitsPerPixel(CV_8U)
  grabber.setImageMode(ImageMode.COLOR)
  grabber.start()


  grabber.grab()
}