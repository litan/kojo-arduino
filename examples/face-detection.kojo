// Face detection using JavaCV

// For this to work, the required javacv jars need to be provided as an extension to Kojo
// Info on downloading/installing the above extension is coming soon

// This is work-in-progress
// It is meant to be a starting point for controlling robots
// with face-detection or object-detection

val fps = 30 // slow this down on an rpi as needed

import java.io. File
import java.awt.image.BufferedImage
import org.bytedeco.javacv._

def toBufferedImage(mat: Frame): BufferedImage = {
    //        val openCVConverter = new ToMat()
    val java2DConverter = new Java2DFrameConverter()
    java2DConverter.convert(mat)
}

var pic: Picture = _
var lastFrame = epochTimeMillis
def detectSequence(grabber: FrameGrabber): Unit = {
    val converter = new OpenCVFrameConverter.ToMat()
    val delay = 1000.0 / fps
    grabber.start()
    try {
        var frame = grabber.grab()
        while (frame != null) {
            frame = grabber.grab()
            val currTime = epochTimeMillis
            if (currTime - lastFrame > delay) {
                val image = converter.convert(frame)
                if (image != null) { // sometimes the first few frames are empty so we ignore them
                    annotateImage(image)
                    val pic2 = Picture.image(toBufferedImage(frame))
                    if (pic != null) { // show our frame in the preview
                        pic.erase()
                    }
                    pic = pic2
                    pic.draw()
                    lastFrame = currTime
                }
            }
        }
    }
    finally {
        grabber.stop()
    }
}

import org.bytedeco.opencv.global.opencv_core._
import org.bytedeco.opencv.global.opencv_dnn._
import org.bytedeco.opencv.opencv_core._
import org.bytedeco.javacpp.indexer._
import org.bytedeco.opencv.global.opencv_imgproc.rectangle

val rootDir = "/home/lalit/work/javacv/javacv-examples/OpenCV_Cookbook/"
val confidenceThreshold = 0.5
val modelConfiguration = new File(s"$rootDir/models/face_detection/deploy.prototxt")
val modelBinary = new File(s"$rootDir/models/face_detection/res10_300x300_ssd_iter_140000.caffemodel")
val inWidth = 300
val inHeight = 300
val inScaleFactor = 1.0
val meanVal = new Scalar(104.0, 177.0, 123.0, 128)

val markerColor = new Scalar(0, 255, 255, 0)

if (!modelConfiguration.exists()) {
    println(s"Cannot find model configuration: ${modelConfiguration.getCanonicalPath}")
}
if (!modelBinary.exists()) {
    println(s"Cannot find model file: ${modelConfiguration.getCanonicalPath}")
}

val net = readNetFromCaffe(modelConfiguration.getCanonicalPath, modelBinary.getCanonicalPath)

def annotateImage(image: Mat) {
    // We will need to scale results for display on the input image, we need its width and height
    val imageWidth = image.size(1)
    val imageHeight = image.size(0)

    // Convert image to format suitable for using with the net
    val inputBlob = blobFromImage(
        image, inScaleFactor, new Size(inWidth, inHeight), meanVal, false, false, CV_32F)

    // Set the network input
    net.setInput(inputBlob)

    // Make forward pass, compute output
    val t0 = epochTime
    val detections = net.forward()
    val t1 = epochTime
//    println(s"inference time - ${t1-t0}s") 

//    println(s"Number of detections: ${detections.size(2)}")
//    println(s" Considering only confidence above threshold: $confidenceThreshold")

    // Decode detected face locations
    val di = detections.createIndexer().asInstanceOf[FloatIndexer]
    val faceRegions = {
        for (i <- 0 until detections.size(2)) yield {
            val confidence = di.get(0, 0, i, 2)
            if (confidence > confidenceThreshold) {
//                println(s"$i confidence = $confidence")

                val x1 = (di.get(0, 0, i, 3) * imageWidth).toInt
                val y1 = (di.get(0, 0, i, 4) * imageHeight).toInt
                val x2 = (di.get(0, 0, i, 5) * imageWidth).toInt
                val y2 = (di.get(0, 0, i, 6) * imageHeight).toInt

                Option(new Rect(new Point(x1, y1), new Point(x2, y2)))
            }
            else {
                None
            }
        }
    }.flatten

//    println(s"Detected ${faceRegions.length} face regions")

    for (rect <- faceRegions) {
        rectangle(image, rect, markerColor)
    }

}

clear()
//val grabber = new OpenCVFrameGrabber(0)
val grabber = new FFmpegFrameGrabber("/dev/video0");
detectSequence(grabber)