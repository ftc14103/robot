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

        robot.take=0.645
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

        robot.set_powers(0.3)
        robot.up.power=0.6
        sleep(50)
        robot.up.power=0.0
        robot.set_powers(doubleArrayOf(-.4, +.4, +.4, -.4))//влево
        sleep(1200)
        robot.up.power=0.6
        robot.set_powers(0.3)//вперёд
        sleep(2550)
        robot.set_powers(0)
        robot.up.power=0.6
        sleep(950)
        robot.up.power=0.25
        sleep(500)

        robot.up.power=0.1
        robot.set_powers(doubleArrayOf(+.4, +.4, -.4, -.4))//на 45 градусов
        sleep(300)
        robot.up.power=0.1
        robot.set_powers(-0.3)
        sleep(500)
        robot.set_powers(0.3)
        sleep(90)
        robot.set_powers(0)
        sleep(3000)
        robot.up.power=0.0
        sleep(800)

        robot.up.power=0.1
        robot.take = 0.05
        sleep(2000)
        robot.up.power=0.1
        robot.set_powers(0.3)
        sleep(400)
        robot.set_powers(0)
        robot.up.power=0.0
        robot.set_powers(doubleArrayOf(-.4, -.4, +.4, +.4))//на 135 градусов
        sleep(800)
        robot.set_powers(0)

        if ((cp>mp) && (cp>yp) && (cp>30)){
            robot.set_powers(-0.3)
            sleep(1250)//2 зона
        } else if ((yp<cp) or (yp<mp) or (yp<30)) {
            robot.set_powers(-0.3)
            sleep(2600)//3 зона
        }


        robot.up.power=-0.4
        robot.set_powers(0)
        sleep(1200)

    }
}