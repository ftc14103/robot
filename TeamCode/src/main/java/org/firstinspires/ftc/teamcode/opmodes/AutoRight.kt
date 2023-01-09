package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.lib.Robot
import org.firstinspires.ftc.teamcode.lib.recognition.SleeveRecognition
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation

@Autonomous(name = "AutoRight")
class AutoRight: LinearOpMode() {
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

        robot.motor_up1.power=0.4
        robot.motor_up2.power=-0.4
        robot.set_powers(0.3)
        sleep(100)
        robot.motor_up1.power = .007
        robot.motor_up2.power = -.007
        robot.set_powers(0)
        robot.set_powers(doubleArrayOf(-.3, +.3, +.3, -.3))//влево
        sleep(2200)
        robot.set_powers(0)
        //sleep(2000)
        robot.set_powers(-0.3)
        sleep(1000)
        robot.set_powers(0)

        robot.motor_up1.power=0.4
        robot.motor_up2.power=-0.4
        robot.set_powers(0.38)
        sleep(1200)
        robot.set_powers(0)

        robot.motor_up1.power=0.4
        robot.motor_up2.power=-0.4
        sleep(4100)
        robot.motor_up1.power=0.5
        robot.motor_up2.power=-0.5
        sleep(400)

        robot.motor_up1.power = .007
        robot.motor_up2.power = -.007

        robot.set_powers(doubleArrayOf(-.5, -.5, +.5, +.5))//на 45 градусов
        sleep(275)
        robot.motor_up1.power = .007
        robot.motor_up2.power = -.007
        robot.set_powers(0.3)
        sleep(860)
        robot.motor_up1.power = .007
        robot.motor_up2.power = -.007
        robot.set_powers(-0.3)
        sleep(90)
        robot.set_powers(0)
        robot.motor_up1.power = .007
        robot.motor_up2.power = -.007
        sleep(2000)

        robot.motor_up1.power=-0.1
        robot.motor_up2.power=0.1
        sleep(150)

        robot.motor_up1.power = .007
        robot.motor_up2.power = -.007
        robot.set_take(0.05)
        sleep(3000)

        robot.motor_up1.power = .007
        robot.motor_up2.power = -.007
        robot.set_powers(-0.35)
        robot.motor_up1.power=-0.2
        robot.motor_up2.power=0.2
        sleep(250)

        robot.motor_up1.power = .007
        robot.motor_up2.power = -.0071
        robot.set_powers(0)
        robot.motor_up1.power=0.2
        robot.motor_up2.power=-0.2
        sleep(500)

        robot.set_powers(doubleArrayOf(+.5, +.5, -.5, -.5))//на 45 градусов
        sleep(350)
        robot.set_powers(0)
      
        robot.set_powers(0.35)
        sleep(830)
        robot.set_powers(0)
        robot.set_powers(doubleArrayOf(+.5, +.5, -.5, -.5))//на 90 градусов
        sleep(470)


        if ((cp>mp) && (cp>yp) && (cp>5)){
            robot.set_powers(0.4)
            sleep(780)//2 зона
            robot.set_powers(0)

        } else if ((yp<cp) or (yp<mp) or (yp<30)) {
            robot.set_powers(0.4)
            sleep(1800)//3 зона
            robot.set_powers(0)
        }
        else {
          robot.set_powers(-0.4)
          sleep(100)
          robot.set_powers(0)
        }

        robot.set_powers(0)
    }
}
