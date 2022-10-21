package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.lib.Robot

@TeleOp(name =  "TeleOp - Mainn")
class TeleOpmode: LinearOpMode() {
    var a_state = false
    var middle_val = 0.0
    var top_val = 0.0
    var take_val = 0.0

    override fun runOpMode() {
        var robot = Robot(this)

        robot.middle = 0.0
        robot.top = 0.0
        robot.take = 0.0

        waitForStart()

        while (opModeIsActive()) {
            if (gamepad1.dpad_up) {
                robot.middle += .001
            }

            if (gamepad1.dpad_down) {
                robot.middle -= .001
            }

            if (gamepad1.dpad_left) {
                robot.top += .005
            }

            if (gamepad1.dpad_right) {
                robot.top -= .005
            }

            if (gamepad1.a && !a_state) {
                robot.take_something()
            }
            a_state = gamepad1.a

            val k = 1.0
            robot.drive(
                    gamepad1.left_stick_x * -1.1,
                    (-gamepad1.left_stick_y).toDouble(),
                    k * (gamepad1.right_trigger - gamepad1.left_trigger),
            )

            sleep(5)
        }
    }
}