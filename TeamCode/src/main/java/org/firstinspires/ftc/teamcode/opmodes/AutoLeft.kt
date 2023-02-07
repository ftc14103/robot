package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.lib.Robot
import org.firstinspires.ftc.teamcode.lib.recognition.SleeveRecognition
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import kotlinx.coroutines.*

@Autonomous(name = "AutoLeft")
class AutoLeft: LinearOpMode() {
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

    class Listener: OpenCvCamera.AsyncCameraOpenListener {
      override fun onOpened() {
        camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
      }

      override fun onError(err: Int) {}
    }

    camera.openCameraDeviceAsync(Listener())

    waitForStart()

    robot.set_take(0.4)
    sleep(2000)
    var cp = slrecog.cyaPercent
    var yp = slrecog.yelPercent
    var mp = slrecog.magPercent

    telemetry.addData("cp", cp)
    telemetry.addData("yp", yp)
    telemetry.addData("mp", mp)
    telemetry.addData("2 zone", (cp>mp) && (cp>yp))
    telemetry.addData("3 zone", (mp>cp) && (mp>yp))
    telemetry.update()
    var up1_encoder = robot.motor_up1.currentPosition
    var up2_encoder = robot.motor_up2.currentPosition
    runBlocking {
      launch {
        robot.expand(500, 0.4)
      }
      launch {
        robot.set_powers(0.3)
      }.join()
      robot.set_powers(0)
      robot.motor_up1.power = .007
      robot.motor_up2.power = -.007
      robot.set_powers(doubleArrayOf(+.3, -.3, -.3, +.3))//вправо
      sleep(2000)
      robot.set_powers(0)
      //sleep(2000)
      robot.set_powers(-0.3)
      sleep(1000)
      robot.set_powers(0)

      robot.motor_up1.power = 0.4
      robot.motor_up2.power = -0.4
      robot.set_powers(0.38)
      sleep(1200)
      robot.set_powers(0)

      robot.motor_up1.power = 0.4
      robot.motor_up2.power = -0.4
      sleep(3900)
      robot.motor_up1.power = 0.5
      robot.motor_up2.power = -0.5
      sleep(400)

      robot.motor_up1.power = .007
      robot.motor_up2.power = -.007

      robot.set_powers(doubleArrayOf(+.5, +.5, -.5, -.5))//на 45 градусов
      sleep(230)
      robot.motor_up1.power = .007
      robot.motor_up2.power = -.007
      robot.set_powers(0.3)
      sleep(770)
      robot.motor_up1.power = .007
      robot.motor_up2.power = -.007
      robot.set_powers(-0.3)
      sleep(90)
      robot.set_powers(0)
      robot.motor_up1.power = .007
      robot.motor_up2.power = -.007
      sleep(5000)

      robot.motor_up1.power = -0.1
      robot.motor_up2.power = 0.1
      sleep(150)

      robot.motor_up1.power = .007
      robot.motor_up2.power = -.007
      robot.set_take(0.05)
      sleep(3000)

      robot.motor_up1.power = .007
      robot.motor_up2.power = -.007
      robot.set_powers(-0.35)
      robot.motor_up1.power = -0.2
      robot.motor_up2.power = 0.2
      sleep(270)

      robot.motor_up1.power = .007
      robot.motor_up2.power = -.007
      robot.set_powers(0)
      robot.motor_up1.power = 0.2
      robot.motor_up2.power = -0.2
      sleep(500)

      robot.set_powers(doubleArrayOf(-.5, -.5, +.5, +.5))//на 45 градусов
      sleep(230)
      robot.set_powers(0)

      robot.set_powers(0.35)
      sleep(1070)
      robot.set_powers(0)
      robot.set_powers(doubleArrayOf(-.4, -.4, +.4, +.4))//на 90 градусов
      sleep(515)


      if ((yp > cp) && (yp > mp) && (yp > 5)) {
        robot.set_powers(0.4)
        sleep(1700)//1 зона
        robot.set_powers(0)
        robot.set_powers(doubleArrayOf(-.3, +.3, +.3, -.3))//влево
        sleep(400)
        robot.set_powers(0)


      } else if ((mp < 5) && (cp > 5)) {
        robot.set_powers(0.4)
        sleep(830)//2 зона
        robot.set_powers(0)
        robot.set_powers(doubleArrayOf(-.3, +.3, +.3, -.3))//влево
        sleep(250)
        robot.set_powers(0)

      }
    }


  }
}