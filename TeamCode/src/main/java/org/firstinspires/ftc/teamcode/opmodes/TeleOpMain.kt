package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.lib.Robot

@TeleOp(name =  "TeleOp - Mainn")
class TeleOpMain: LinearOpMode() {
    var a_state = false
    var middle_val = 0.0
    var top_val = 0.0
    var take_val = 0.0
    var y_state = false
    var b_state = false
    var x_state = false
    var slowmode = false
    var k = 1.0
    var total = 0.0
    var click_x = -1
    var taken = false
    var distance = 0

    override fun runOpMode() {
        var robot = Robot(this)

        //robot.middle = 0.0
        //robot.top = 0.9
        //robot.take = 0.0

        waitForStart()

        while (opModeIsActive()) {
            if (total > 100.0) {
                total = 100.0
            }

            /*if (gamepad1.dpad_up) {
                total += 1.0
                robot.expand(total)
            }

            */
            if (!robot.lift_state) {
                if (gamepad2.left_stick_y > 0.0f) {
                    distance += 1
                    robot.up.power = -0.7 * gamepad2.left_stick_y
                } else if (gamepad2.left_stick_y < 0.0f) {
                    distance -= 1
                    robot.up.power = -0.7 * gamepad2.left_stick_y
                } else {
                    robot.up.power = .006
                }
            }

            if (gamepad2.a) {
                robot.take = 0.05
            }

            if (gamepad2.x) {
                robot.take = 0.61
            }
  /*
            if (gamepad2.x && !x_state) {
                if (taken) {
                    taken = false
                    robot.take = 0.1
                } else {
                    taken = true
                    robot.take = 0.63
                }
            }
*/
            if (gamepad1.b && !b_state) {
                if (slowmode) {
                    slowmode = false
                    k = 1.0
                } else {
                    slowmode = true
                    k = 0.5
                }
            }
            b_state = gamepad1.b

            robot.drive(
                    -k * gamepad1.left_stick_x * 1.1,
                    -k * (-gamepad1.left_stick_y).toDouble(),
                    k * (gamepad1.right_trigger - gamepad1.left_trigger),
            )

            telemetry.addData("Take", robot.take)
            telemetry.addData("Middle", robot.up.currentPosition)
            telemetry.addData("click_x", click_x)
            telemetry.addData("power_middle", robot.up.power)
            telemetry.addData("Distance", distance)
            telemetry.update()

            sleep(5)
        }
    }
}