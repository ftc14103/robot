package org.firstinspires.ftc.teamcode.lib.recognition

import android.graphics.Bitmap
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.vuforia.PIXEL_FORMAT
import com.vuforia.Vuforia
import org.apache.commons.math3.util.FastMath.pow
import org.apache.commons.math3.util.FastMath.sqrt
import org.firstinspires.ftc.robotcore.external.ClassFactory
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer

class ColorRecognition(
  op_mode: OpMode
) {
  private var vuforia: VuforiaLocalizer
  private var webcam: WebcamName = op_mode.hardwareMap.get(WebcamName::class.java, "Webcam 1")

  init {
    val params = VuforiaLocalizer.Parameters(
      op_mode.hardwareMap.appContext.resources.getIdentifier(
        "cameraMonitorViewId",
        "id",
        op_mode.hardwareMap.appContext.packageName
      )
    )

    params.vuforiaLicenseKey = "AdeSWBz/////AAABmQM7FzHd2UISqz9MHaOV0yBylrk9bfGQJn/2vdP6LgKJDa2EK1VK2FtAqusmwFbBOscjLE8tPDYhAsZSW19IyXP7HERbWUE/rnGY+F5gIDRk1SPBRAl2EugJn0cUWPG0bVlQ7qmAG503Pkr+BSZKZIhZxTMLFKP5CPgEZC4kpqP7gjUn5KP4dFIb9hsCpFttr2AlxdOFeEvclnpSCCnlWAgXQ5zsX0q3wv0Oqs8DdCAARd2TJ7OMkrgBtYvS06PZykfj2UDJrqcP7JLOOFE6D9B/am2tMXdyqNXebnmmXV8v1yYou8i6sL1TCFi43v3DyrxPho0LrnGqpzy4niWHkPPfjFOXmbwAJU8R79C+L/gr"
    params.cameraName = webcam

    vuforia = ClassFactory.getInstance().createVuforia(params)
    vuforia.frameQueueCapacity = 6
    Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true)
  }

  fun get_bitmap(): Bitmap? {
    var frame = try {
      vuforia.frameQueue.take()
    } catch (e: Throwable) {
      null
    }

    if (frame != null) {
      return vuforia.convertFrameToBitmap(frame)
    }

    return null
  }

  val regex = "(\\w{1,2})".toRegex()
  fun get_lightness(color: String): Double {
    val match = regex.matchEntire(color)
    var r = 0
    var b = 0
    var g = 0
    if (match != null) {
      r = match.groupValues[0].toInt(16)
      b = match.groupValues[1].toInt(16)
      g = match.groupValues[2].toInt(16)
    }

    val den = 255 * sqrt(.299 + .587 + .111)
    return sqrt(.299 * r * r + .587 * g * g + .111 * b * b) / den
  }

  fun dominant(): String {
    val bitmap = get_bitmap()
    if (bitmap != null) {
      val color = get_dominant_color(bitmap)
      return String.format("#%06X", 0xFFFFFF and color)
    }

    return "null"
  }

  fun lightest(): String {
    val bitmap = get_bitmap()

    if (bitmap != null) {
      val color = get_lightest_color(bitmap)
      return String.format("#%06X", 0xFFFFFF and color)
    }

    return "null"
  }

  fun get_lightest_color(bitmap: Bitmap): Int {
    var pixels: IntArray = IntArray(bitmap.width * bitmap.height)
    bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

   return get_lightest_pixel(
     get_lightest_pixel_list(pixels)
   ).pixel;
  }

  fun get_lightest_pixel_list(pixels: IntArray): HashMap<Int, Pixel> {
    var map = HashMap<Int, Pixel>()

    for (pixel in pixels) {
      if (map.containsKey(pixel)) {
        map[pixel]!!.count += 1
      } else {
        map[pixel] = Pixel(
          pixel,
          1,
          get_lightness(String.format("%06X", pixel))
        )
      }
    }

    return map
  }

  fun get_lightest_pixel(map: Map<Int, Pixel>): Pixel {
    var lightest = Pixel(0, 0, .0)

    for (pixel in map) {
      if ((pixel.value.count > lightest.count) &&
          (pixel.value.light > lightest.light)) {
            lightest = pixel.value
          }
    }

    return lightest
  }

  fun get_dominant_color(bitmap: Bitmap): Int {
    var pixels: IntArray = IntArray(bitmap.width * bitmap.height)
    bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

    return get_dominant_pixel(
      get_dominant_pixel_list(pixels)
    ).pixel;
  }

  private fun get_dominant_pixel_list(pixels: IntArray): Map<Int, Pixel> {
    var map = HashMap<Int, Pixel>()

    for (pixel in pixels) {
      if (map.containsKey(pixel)) {
        map[pixel]!!.count += 1
      } else {
        map[pixel] = Pixel(pixel, 1)
      }
    }

    return map
  }

  private fun get_dominant_pixel(map: Map<Int, Pixel>): Pixel {
    var dominant_pixel = Pixel(0, 0)

    for (pixel in map) {
      if (pixel.value.count > dominant_pixel.count) {
        dominant_pixel = pixel.value
      }
    }

    return dominant_pixel
  }

  data class Pixel(
    val pixel: Int,
    var count: Int,
    var light: Double = 0.0
  )
}