package org.firstinspires.ftc.teamcode.lib.recognition

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.opencv.core.Mat
import org.opencv.objdetect.QRCodeDetector
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvPipeline

class OpenCV(
  val op_mode: OpMode
) {
  val webcam: OpenCvCamera = OpenCvCameraFactory.getInstance().createWebcam(
    op_mode.hardwareMap.get(WebcamName::class.java, "Webcam 1"),
    op_mode.hardwareMap.appContext.resources.getIdentifier(
      "cameraMonitorViewId",
      "id",
      op_mode.hardwareMap.appContext.packageName
    )
  )
  val pipeline = Pipeline(webcam)

  val result: Pipeline.Position
    get() = pipeline.get_result()

  fun start() {
    webcam.setPipeline(pipeline)
    webcam.openCameraDeviceAsync(object : OpenCvCamera.AsyncCameraOpenListener {
      override fun onOpened() {
        // 1280x720
        webcam.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT)
      }

      override fun onError(errorCode: Int) {
        op_mode.telemetry.log().add("Error: $errorCode")
      }
    })
  }

  fun stop() {
    webcam.stopStreaming()
  }

  class Pipeline(
    private val webcam: OpenCvCamera
  ) : OpenCvPipeline() {
    var paused = false
    var data: String? = null

    enum class Position {
      LEFT, CENTER, RIGHT
    }

    fun get_result(): Position {
      return when (data) {
        "https://t.me/sputnik14103" -> Position.LEFT
        "https://sputnik.lab244.ru/" -> Position.CENTER
        "https://rr.noordstar.me/79535689" -> Position.RIGHT
        else -> Position.CENTER
      }

      //return data.orEmpty()
    }

    override fun processFrame(input: Mat): Mat {
      val detector = QRCodeDetector()
      data = detector.detectAndDecodeCurved(input)
      return input
    }

    override fun onViewportTapped() {
      paused = !paused

      if (paused) {
        webcam.pauseViewport()
      } else {
        webcam.resumeViewport()
      }
    }
  }
}