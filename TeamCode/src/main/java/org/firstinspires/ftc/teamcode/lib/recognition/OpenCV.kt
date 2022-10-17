package org.firstinspires.ftc.teamcode.lib.recognition

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.opencv.core.Mat
import org.opencv.objdetect.QRCodeDetector
import org.openftc.easyopencv.*

class OpenCV(
  op_mode: OpMode
) {
  val webcam: OpenCvWebcam = OpenCvCameraFactory.getInstance().createWebcam(
    op_mode.hardwareMap.get(WebcamName::class.java, "Webcam 1"),
    op_mode.hardwareMap.appContext.resources.getIdentifier(
      "cameraMonitorViewId",
      "id",
      op_mode.hardwareMap.appContext.packageName
    )
  )
  val pipeline = Pipeline(webcam)

  val result: Pipeline.Posittion
    get() = pipeline.get_result()

  fun start() {
    webcam.setPipeline(pipeline)

    webcam.setMillisecondsPermissionTimeout(2500)
    webcam.openCameraDeviceAsync(object : OpenCvCamera.AsyncCameraOpenListener {
      override fun onOpened() {
        // 1280x720
        webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
      }

      override fun onError(errorCode: Int) {
        op_mode.telemetry.log().add("Error: $errorCode")
      }
    })
  }

  fun stop() {
    webcam.stopStreaming()
    webcam.closeCameraDevice()
  }

  class Pipeline(
    private val webcam: OpenCvWebcam
  ): OpenCvPipeline() {
    var paused = false
    var data: String? = null

    enum class Posittion {
      LEFT, CENTER, RIGHT
    }

    fun get_result(): Posittion {
      return when (data) {
        "https://t.me/sputnik14103" -> Posittion.LEFT
        "https://sputnik.lab244.ru/" -> Posittion.CENTER
        "RIGHT" -> Posittion.RIGHT
        else -> Posittion.CENTER
      }
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