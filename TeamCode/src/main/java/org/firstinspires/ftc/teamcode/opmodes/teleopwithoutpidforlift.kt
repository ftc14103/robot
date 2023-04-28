package org.firstinspires.ftc.teamcode.opmodes

import android.os.Build
import androidx.annotation.RequiresApi
import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.lib.Robot

@Config
@TeleOp(name = "teleopnopid")
class teleopwithoutpidforlift : LinearOpMode() {
  private var b_state = false
  private var slowmode = false
  private var k = 1.0
  private var distance = 0
  private var flipState = false
  private var flip_thread = 0

  @RequiresApi(Build.VERSION_CODES.N)
  override fun runOpMode() {
    var disable_rumble = false
    var double_ramble = Gamepad.RumbleEffect.Builder()
      .addStep(1.0, 1.0, 250)
      .addStep(0.0, 0.0, 300)
      .addStep(1.0, 1.0, 250)
      .build()

    var robot = Robot(this)
    //var lift = LiftIntake(this)
    /*var flip = thread(true) {
      telemetry.log()
      when (flip_thread) {
        -1 -> {
          robot.flipPID(-260.0)
          robot.flipPID(-260.0)
          robot.flipPID(80.0)
          flip_thread = 0
        }
        1 -> {
          robot.flipPID(160.0)
          flip_thread = 0
        }
        else -> {
        
        }
      }
    }*/

    //robot.left_rear.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    //robot.left_front.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    //robot.right_front.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    //robot.right_rear.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    //robot.motor_up2.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    //robot.motor_up1.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

    //robot.motor_up2.direction = DcMotorSimple.Direction.REVERSE

    waitForStart()

    while (opModeIsActive()) {
      /**
       * Второй геймпад:
       * Левый стик - поднятие направляющих
       * A - захват
       * X - отхват
       * Y - переворот
       */
      if (gamepad2.left_stick_y > 0.0f) {// Направляющие
        distance += 1
        robot.motor_up1.power = -1.0 * gamepad2.left_stick_y
        robot.motor_up2.power = 1.0 * gamepad2.left_stick_y
      } else if (gamepad2.left_stick_y < 0.0f) {
        distance -= 1
        robot.motor_up1.power = -1.0 * gamepad2.left_stick_y
        robot.motor_up2.power = 1.0 * gamepad2.left_stick_y
      } else {
        robot.motor_up1.power = .015
        robot.motor_up2.power = -.015
      }

      if (gamepad2.a) {
        robot.set_take(0.14)
      }

      if (gamepad2.x) {//захват
        robot.set_take(0.46)
      }

      if (gamepad2.y) {
        if (flipState) {
          robot.flipPID(-430.0)
          //robot.flipPID(-180.0)
          //flip_thread = -1
        } else {
          robot.flipPID(200.0)
          //flip_thread = 1
        }
        flipState = !flipState
      }

      /*if (gamepad1.dpad_up && lift.is_stable()) {
        var a = lift.get_target()
        lift.set_level_number(lift.get_level_number() - 5.5)
        if (a == 0.0) {
          lift.set_target(a)
        }
        sleep(100)
      }
      
      if (gamepad1.dpad_down && lift.is_stable()) {
        var b = lift.get_target()
        lift.set_level_number(lift.get_level_number() + 5.5)
        if (b == 0.0) {
          lift.set_target(b)
        }
        sleep(100)
      }*/

      if (gamepad1.b && !b_state) {
        if (slowmode) {
          slowmode = false
          k = 0.7
          /*if (!disable_rumble) {
            gamepad1.runRumbleEffect(double_ramble)
          }*/
        } else {
          slowmode = true
          k = 0.6
          /*if (!disable_rumble) {
            gamepad1.rumble(1.0, 1.0, 500)
          }*/
        }
      }
      b_state = gamepad1.b

      robot.drive(
        -k * gamepad1.left_stick_x * 1.1,
        -k * (gamepad1.left_stick_y).toDouble(),
        k * 65 / 100 * (gamepad1.right_trigger - gamepad1.left_trigger),
      )

      /*telemetry.addData("lift target", lift.get_target())
      telemetry.addData("lift err", lift.get_avg_err())
      telemetry.addData("lift level", lift.get_level_number())
      telemetry.addData("motor_up1 power", robot.motor_up1.power)
      telemetry.addData("motor_up2 power", robot.motor_up2.power)
      telemetry.addData("Slowmode", slowmode)
      telemetry.addData("motor_up1", robot.motor_up1.currentPosition)
      telemetry.addData("motor_up2", robot.motor_up2.currentPosition)*/
      telemetry.addData("flip", robot.flip.currentPosition)
      telemetry.update()
      sleep(5)
    }

    //lift.disable()
  }
}