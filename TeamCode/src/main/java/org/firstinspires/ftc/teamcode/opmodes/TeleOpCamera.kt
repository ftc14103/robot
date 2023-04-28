package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.lib.recognition.SleeveRecognition
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation


@TeleOp(name = "TeleOp camera", group = "TeleOp")
class TeleOpCamera : LinearOpMode() {
  override fun runOpMode() {
    val cam_id = hardwareMap.appContext.resources.getIdentifier(
      "cameraMonitorViewId", "id", hardwareMap.appContext.packageName
    )
    val camera = OpenCvCameraFactory.getInstance().createWebcam(
      hardwareMap.get(WebcamName::class.java, "Webcam 1"), cam_id
    )
    var slrecog = SleeveRecognition()
    camera.setPipeline(slrecog)

    class Listener : OpenCvCamera.AsyncCameraOpenListener {
      override fun onOpened() {
        camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
      }

      override fun onError(err: Int) {}
    }

    camera.openCameraDeviceAsync(Listener())

    while (!isStarted) {
      telemetry.addData("ROTATION", slrecog.position)
      telemetry.addData("yelPercent", slrecog.yelPercent)
      telemetry.addData("cyaPercent", slrecog.cyaPercent)
      telemetry.addData("magPercent", slrecog.magPercent)
      telemetry.update()
    }

    waitForStart()
  }
}
