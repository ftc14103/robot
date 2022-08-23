package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.lib.Robot

@TeleOp(name = "Example", group = "TeleOp - Test")
class Example: OpMode() {
    lateinit var robot: Robot

    override fun init() {
        robot = Robot(this)
    }

    override fun init_loop() {
        // initialize loop
    }

    override fun start() {
    }

    override fun loop() {
        robot.drive(
            gamepad1.left_stick_x * 1.1,
            (-gamepad1.left_stick_y).toDouble(),
            (gamepad1.right_trigger - gamepad1.left_trigger).toDouble(),
        )
    }

    override fun stop() {
        robot.set_powers(0.0)
    }
}