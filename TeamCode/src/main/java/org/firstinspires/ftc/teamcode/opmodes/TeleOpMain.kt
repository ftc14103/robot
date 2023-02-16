package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Gamepad
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
        var disable_rumble = false
        var double_ramble = Gamepad.RumbleEffect.Builder()
          .addStep(1.0, 1.0, 250)
          .addStep(0.0, 0.0, 300)
          .addStep(1.0, 1.0, 250)
          .build()
      
        var robot = Robot(this)
        var enc_val = 0
        var prev_enc = robot.motor_up2.currentPosition
      
        robot.left_rear.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        robot.left_front.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        robot.right_front.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        robot.right_rear.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        robot.motor_up2.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        robot.motor_up1.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
      
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
                    k = 1.0
                    if (!disable_rumble) {
                      gamepad1.runRumbleEffect(double_ramble)
                    }
                } else {
                    slowmode = true
                    k = 0.7
                    if (!disable_rumble) {
                      gamepad1.rumble(1.0, 1.0, 500)
                    }
                }
            }
            b_state = gamepad1.b

            robot.drive(
                    k * gamepad1.left_stick_x * 1.1,
                    k * (-gamepad1.left_stick_y).toDouble(),
                    k * 2/3 * (gamepad1.right_trigger - gamepad1.left_trigger),
            )
            
            telemetry.addData("Slowmode", slowmode)
            telemetry.addData("enc1",robot.motor_up2.currentPosition)
            telemetry.update()
            enc_val = robot.motor_up2.currentPosition - prev_enc
            sleep(5)
        }
    }
}