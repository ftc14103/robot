package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import kotlinx.coroutines.*
import org.firstinspires.ftc.teamcode.lib.Robot

@TeleOp(name = "TeleOpAsync")
class TeleOpAsync: LinearOpMode() {
  private var b_state = false
  private var slowmode = false
  private var k = 1.0
  private var flip_state = false
  private var disable_rumble = false
  private var double_ramble: Gamepad.RumbleEffect = Gamepad.RumbleEffect.Builder()
    .addStep(1.0, 1.0, 250)
    .addStep(0.0, 0.0, 300)
    .addStep(1.0, 1.0, 250)
    .build()

  private suspend fun flip_handler(robot: Robot) {
    if (gamepad2.y) {
      if (flip_state) {
        robot.flipPID(-260.0)
        robot.flipPID(-100.0)
      } else {
        robot.flipPID(200.0)
      }
    }

    flip_state = !flip_state
  }

  private suspend fun intake_handler(robot: Robot) {
    if (gamepad2.a) {
      robot.set_take(0.14)
    }

    if (gamepad2.x) {
      robot.set_take(0.46)
    }
  }

  private suspend fun lift_handler(robot: Robot) {
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

  private suspend fun drive_handler(robot: Robot) {
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
      k * 65 / 100 * (gamepad1.right_trigger - gamepad1.left_trigger),
    )
  }

  @OptIn(DelicateCoroutinesApi::class)
  override fun runOpMode() {
    val robot = Robot(this)
    val driving_thread = newSingleThreadContext("driving_thread")

    waitForStart()

    while (opModeIsActive()) {
      runBlocking {
        async {
          flip_handler(robot)
        }
        
        async {
          lift_handler(robot)
        }
        
        async {
          intake_handler(robot)
        }
        
        async(driving_thread) {
          drive_handler(robot)
        }
      }
      
      telemetry.update()
    }

    driving_thread.close()
  }
}