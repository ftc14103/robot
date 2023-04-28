package org.firstinspires.ftc.teamcode.lib.recognition

import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.OpenCvPipeline

class SleeveRecognition : OpenCvPipeline() {
  enum class Position {
    LEFT, CENTER, RIGHT
  }

  val SLEEVE_TOPLEFT_ANCHOR_POINT = Point(145.0, 168.0)
  val REGION_WIDTH = 30
  val REGION_HEIGHT = 50

  val lower_yellow_bounds = Scalar(135.0, 135.0, .0, 255.0)
  val upper_yellow_bound = Scalar(255.0, 255.0, 130.0, 255.0)
  val lower_cyan_bounds = Scalar(.0, 130.0, 130.0, 255.0)
  val upper_cyan_bounds = Scalar(150.0, 255.0, 255.0, 255.0)
  val lower_magenta_bounds = Scalar(80.0, .0, 80.0, 255.0)
  val upper_magenta_bounds = Scalar(255.0, 60.0, 255.0, 255.0)

  val YELLOW = Scalar(255.0, 255.0, .0)
  val CYAN = Scalar(.0, 255.0, 255.0)
  val MAGENTA = Scalar(255.0, .0, 255.0)

  var yelPercent: Double = 0.0
  var cyaPercent: Double = 0.0
  var magPercent: Double = 0.0

  var yelMat = Mat()
  var cyaMat = Mat()
  var magMat = Mat()
  var blurredMat = Mat()
  var kernel = Mat()

  var sleeve_pointA = Point(
    SLEEVE_TOPLEFT_ANCHOR_POINT.x,
    SLEEVE_TOPLEFT_ANCHOR_POINT.y
  )
  var sleeve_pointB = Point(
    SLEEVE_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
    SLEEVE_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT
  )

  var position = Position.RIGHT

  override fun processFrame(input: Mat): Mat {
    Imgproc.blur(input, blurredMat, Size(5.0, 5.0))
    blurredMat = blurredMat.submat(Rect(sleeve_pointA, sleeve_pointB))

    kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, Size(3.0, 3.0))
    Imgproc.morphologyEx(blurredMat, blurredMat, Imgproc.MORPH_CLOSE, kernel)

    Core.inRange(blurredMat, lower_yellow_bounds, upper_yellow_bound, yelMat)
    Core.inRange(blurredMat, lower_cyan_bounds, upper_cyan_bounds, cyaMat)
    Core.inRange(blurredMat, lower_magenta_bounds, upper_magenta_bounds, magMat)

    yelPercent = Core.countNonZero(yelMat).toDouble()
    cyaPercent = Core.countNonZero(cyaMat).toDouble()
    magPercent = Core.countNonZero(magMat).toDouble()

    var maxPercent = yelPercent.coerceAtLeast(cyaPercent.coerceAtLeast(magPercent))

    when (maxPercent) {
      yelPercent -> {
        position = Position.LEFT
        Imgproc.rectangle(
          input,
          sleeve_pointA,
          sleeve_pointB,
          YELLOW, 2
        )
      }

      cyaPercent -> {
        position = Position.CENTER
        Imgproc.rectangle(
          input,
          sleeve_pointA,
          sleeve_pointB,
          CYAN, 2
        )
      }

      magPercent -> {
        position = Position.RIGHT
        Imgproc.rectangle(
          input,
          sleeve_pointA,
          sleeve_pointB,
          MAGENTA, 2
        )
      }
    }

    blurredMat.release()
    yelMat.release()
    cyaMat.release()
    magMat.release()
    kernel.release()

    return input
  }
}