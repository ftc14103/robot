package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.lib.Robot
import kotlin.concurrent.thread

@TeleOp(name = "TeleOpActual")
class TeleOpNotActual : LinearOpMode() {
  private var b_state = false
  private var slowmode = false
  private var k = 1.0
  private var flip_state = true
  private var disable_rumble = false
  private var double_ramble: Gamepad.RumbleEffect = Gamepad.RumbleEffect.Builder()
    .addStep(1.0, 1.0, 250)
    .addStep(0.0, 0.0, 300)
    .addStep(1.0, 1.0, 250)
    .build()

  private var y_state = false
  private fun flipper(robot: Robot) {
    if (gamepad2.y && !y_state) {
      if (flip_state) {
        robot.flipPID(-600.0)
      } else {
        robot.flipPID(650.0)
      }

      flip_state = !flip_state
    }
    y_state = gamepad2.y
  }

  private fun intake_handler(robot: Robot) {
    if (gamepad2.a) {
      robot.set_take(0.1)
    }

    if (gamepad2.x) {
      robot.set_take(0.46)
    }
  }

  private fun lift_handler(robot: Robot) {
    if (gamepad2.left_stick_y > 0.0f) {// Направляющие
      robot.motor_up1.power = -1.0 * gamepad2.left_stick_y
      robot.motor_up2.power = 1.0 * gamepad2.left_stick_y
    } else if (gamepad2.left_stick_y < 0.0f) {
      robot.motor_up1.power = -1.0 * gamepad2.left_stick_y
      robot.motor_up2.power = 1.0 * gamepad2.left_stick_y
    } else {
      robot.motor_up1.power = .015
      robot.motor_up2.power = -.015
    }
  }

  private fun drive_handler(robot: Robot) {
    if (gamepad1.b && !b_state) {
      if (slowmode) {
        slowmode = false
        k = 0.7

        if (!disable_rumble) {
          gamepad1.runRumbleEffect(double_ramble)
        }
      } else {
        slowmode = true
        k = 0.6

        if (!disable_rumble) {
          gamepad1.rumble(1.0, 1.0, 500)
        }
      }
    }

    robot.drive(
      -k * gamepad1.left_stick_x * 1.1,
      -k * (-gamepad1.left_stick_y).toDouble(),
      k * 60 / 100 * (gamepad1.right_trigger - gamepad1.left_trigger),
    )
  }

  override fun runOpMode() {
    val robot = Robot(this)
    val flipper_thread = thread(false) {
      while (opModeIsActive()) {
        flipper(robot)
      }
    }
    val intake_thread = thread(false) {
      while (opModeIsActive()) {
        intake_handler(robot)
      }
    }
    val lift_thread = thread(false) {
      while (opModeIsActive()) {
        lift_handler(robot)
      }
    }
    val drive_thread = thread(false) {
      while (opModeIsActive()) {
        drive_handler(robot)
      }
    }

    waitForStart()

    flipper_thread.start()
    intake_thread.start()
    lift_thread.start()
    drive_thread.start()

    while (opModeIsActive()) {
      telemetry.addData("flip pos", robot.flip.currentPosition)
      telemetry.addData("button", robot.button)
      telemetry.update()
    }
  }
}