package org.firstinspires.ftc.teamcode.opmodes

import android.graphics.Color
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.lib.Robot
import org.firstinspires.ftc.teamcode.lib.recognition.ColorRecognition
import org.firstinspires.ftc.teamcode.lib.recognition.OpenCV
import org.firstinspires.ftc.teamcode.lib.recognition.Scanner

@TeleOp(name = "TeleOp Main", group = "TeleOp")
class TeleOpMain: OpMode() {
    //private lateinit var robot: Robot
    private lateinit var open_cv: OpenCV

    override fun init() {
        //robot = Robot(this)
        open_cv = OpenCV(this)
    }

    override fun init_loop() {
    }

    override fun start() {
        open_cv.start()
    }

    override fun loop() {
        val k = .5
        /*
        scanner.image.let {
            if (it != null) {
                telemetry.log().add("PicType = %d %d x %d", it.format, it.width, it.height)
                telemetry.log().add("Bytes = %d", it.pixels.remaining())
            }
        }
        */

        //robot.drive(
        //    gamepad1.left_stick_x * 1.1,
        //    (-gamepad1.left_stick_y).toDouble(),
        //    k * (gamepad1.right_trigger - gamepad1.left_trigger),
        //)
        //telemetry.update()
        telemetry.log().add("Data: ${open_cv.result}")

    }

    override fun stop() {
        open_cv.stop()
        //robot.set_powers(.0)
    }
}