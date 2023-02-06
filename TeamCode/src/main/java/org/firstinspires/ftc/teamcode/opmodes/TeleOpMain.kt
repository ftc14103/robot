package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.lib.Robot

@TeleOp(name =  "!Main TeleOp")
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
    var taken = false
    var distance = 0

    var no_button = true
    
    override fun runOpMode() {
        
        var robot = Robot(this)
        var enc_val = 0
        var prev_enc = robot.motor_up2.currentPosition
      
        robot.left_rear.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        robot.left_front.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        robot.right_front.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        robot.right_rear.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        waitForStart()

        while (opModeIsActive()) {
            if (gamepad2.left_stick_y > 0.0f) {
                distance += 1
                robot.motor_up1.power = -0.7 * gamepad2.left_stick_y
                robot.motor_up2.power = 0.7 * gamepad2.left_stick_y
            } else if (gamepad2.left_stick_y < 0.0f) {
                distance -= 1
                robot.motor_up1.power = -0.7 * gamepad2.left_stick_y
                robot.motor_up2.power = 0.7 * gamepad2.left_stick_y
            } else {
                robot.motor_up1.power = .007
                robot.motor_up2.power = -.007
            }

            if (gamepad2.a) {
                robot.set_take(0.12)
            }

            if (gamepad2.x) {
                robot.set_take(0.4)
            }

            if (gamepad1.b && !b_state) {
                if (slowmode) {
                    slowmode = false
                    k = 0.7
                } else {
                    slowmode = true
                    k = 0.4
                }
            }
            b_state = gamepad1.b

            robot.drive(
                    k * gamepad1.left_stick_x * 1.1,
                    k * (-gamepad1.left_stick_y).toDouble(),
                    k * 3/5 * (gamepad1.right_trigger - gamepad1.left_trigger),
            )
            
            telemetry.addData("enc1",robot.motor_up2.currentPosition)
            telemetry.update()
            enc_val = robot.motor_up2.currentPosition - prev_enc
            sleep(5)
        }
    }
}