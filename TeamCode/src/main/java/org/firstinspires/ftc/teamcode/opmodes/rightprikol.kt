package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.lib.Robot
import org.firstinspires.ftc.teamcode.lib.recognition.SleeveRecognition
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation

@Autonomous(name = "rightprikol")
class rightprikol : LinearOpMode() {
  override fun runOpMode() {
    var robot = Robot(this)

    val cam_id = hardwareMap.appContext.resources.getIdentifier(
      "cameraMonitorViewId", "id", hardwareMap.appContext.packageName
    )
    val camera = OpenCvCameraFactory.getInstance().createWebcam(
      hardwareMap.get(WebcamName::class.java, "Webcam 1"), cam_id
    )
    var slrecog = SleeveRecognition()
    camera.setPipeline(slrecog)

    camera.openCameraDeviceAsync(object : OpenCvCamera.AsyncCameraOpenListener {
      override fun onOpened() {
        camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
      }

      override fun onError(err: Int) {}
    })

    waitForStart()

    robot.set_take(0.46)
    //sleep(2000)
    var cp = slrecog.cyaPercent
    var yp = slrecog.yelPercent
    var mp = slrecog.magPercent
    camera.stopStreaming()
    camera.closeCameraDevice()
    //var enc_val = 0
    //var prev_enc = robot.motor_up2.currentPosition

    telemetry.addData("cp", cp)
    telemetry.addData("yp", yp)
    telemetry.addData("mp", mp)
    telemetry.addData("2 zone", (cp > mp) && (cp > yp))
    telemetry.addData("3 zone", (mp > cp) && (mp > yp))
    telemetry.addData("enc1", robot.motor_up2.currentPosition)
    telemetry.update()

    robot.motor_up1.power = 0.5
    robot.motor_up2.power = -0.5
    robot.move(2150, 0.0, 0.4, 0.0)

    robot.motor_up1.power = 1.0
    robot.motor_up2.power = -1.0
    robot.move(200, 0.0, -0.4, 0.0)
    robot.move(340, 0.0, 0.0, -0.4)

    //robot.motor_up1.power=0.8
    //robot.motor_up2.power=-0.8
    //sleep(1500)
    robot.motor_up1.power = 1.0
    robot.motor_up2.power = -1.0
    robot.move(600, 0.0, 0.3, 0.0)
    //robot.set_powers(0)
    //sleep(500)
    robot.move(50, 0.0, -0.2, 0.0)
    robot.motor_up1.power = .015
    robot.motor_up2.power = -.015
    robot.set_powers(0)
    sleep(2000)

    robot.motor_up1.power = -0.3
    robot.motor_up2.power = 0.3
    sleep(300)
    robot.motor_up1.power = .015
    robot.motor_up2.power = -.015
    robot.set_take(0.11)
    sleep(1000)
    sleep(21000)

    robot.motor_up1.power = 1.0
    robot.motor_up2.power = -1.0
    sleep(150)
    robot.motor_up1.power = .015
    robot.motor_up2.power = -.015
    robot.move(350, 0.0, -0.4, 0.0)
    robot.move(100, -0.4, 0.0, 0.0)
    robot.set_powers(0)
    robot.motor_up1.power = .015
    robot.motor_up2.power = -.015
    robot.flipPID(-650.0)
    sleep(800)

    robot.motor_up1.power = -0.25
    robot.motor_up2.power = 0.25
    robot.move(250, 0.0, 0.0, -0.4)


    if ((yp > cp) && (yp > mp)) {
      robot.move(1300, 0.0, 0.4, 0.0)
      robot.set_powers(0)
    } else if (cp > mp) {
      //robot.move(300, 0.0, 0.4, 0.0)
      robot.set_powers(0)
    } else {
      robot.move(700, 0.0, -0.4, 0.0)
      robot.set_powers(0)
    }

    robot.move(100, -0.4, 0.0, 0.0)

  }
}