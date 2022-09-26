package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.lib.Robot
import org.firstinspires.ftc.teamcode.lib.recognition.Scanner

@TeleOp(name = "TeleOp Main", group = "TeleOp")
class TeleOpMain: OpMode() {
    lateinit var robot: Robot
    lateinit var scanner: Scanner

    override fun init() {
        robot = Robot(this)
        scanner = Scanner(this)

        var res = scanner.scan_aztek()
        if (res != null) {
            while (!res.isComplete) {}
            for (b in res.result) {
                this.telemetry.log().add("barcode: %s", b.rawValue)
            }
        }
    }

    override fun init_loop() {
        // initialize loop
    }

    override fun start() {
    }

    override fun loop() {
        var k = .5;
        /*
        scanner.image.let {
            if (it != null) {
                telemetry.log().add("PicType = %d %d x %d", it.format, it.width, it.height)
                telemetry.log().add("Bytes = %d", it.pixels.remaining())
            }
        }
        */

        robot.drive(
            gamepad1.left_stick_x * 1.1,
            (-gamepad1.left_stick_y).toDouble(),
            k * (gamepad1.right_trigger - gamepad1.left_trigger),
        )
        telemetry.update()
    }

    override fun stop() {
        robot.set_powers(.0)
    }
}