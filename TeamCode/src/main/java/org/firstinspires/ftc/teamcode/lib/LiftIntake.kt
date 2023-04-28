package org.firstinspires.ftc.teamcode.lib

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.ElapsedTime
import kotlin.concurrent.thread
import kotlin.math.PI
import kotlin.math.abs

class LiftIntake(val op_mode: LinearOpMode) {
  private var motor1: DcMotor = op_mode.hardwareMap.dcMotor.get("motor_up1")
  private var motor2: DcMotor = op_mode.hardwareMap.dcMotor.get("motor_up2")

  fun extend_to(value: Double) {
    motor1.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
    motor1.targetPosition = value.toInt()
    motor1.mode = DcMotor.RunMode.RUN_TO_POSITION

    motor2.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
    motor2.targetPosition = value.toInt()
    motor2.mode = DcMotor.RunMode.RUN_TO_POSITION

    var d0_1 = value - motor1.currentPosition
    var d1_1 = 0.0

    var d0_2 = value - motor2.currentPosition
    var d1_2 = 0.0

    var di_1 = 0.0
    var di_2 = 0.0
    var dd_1 = 0.0
    var dd_2 = 0.0

    var kp = 0.0
    var kd = 0.0

    var calcTime = ElapsedTime()

    while (op_mode.opModeIsActive() &&
      ((abs(d0_1) > 1) && (abs(d0_2) > 1) ||
        (abs(motor1.power) > 0.2 && abs(motor2.power) > 0.2))) {
      d1_1 = value - motor1.currentPosition
      d1_2 = value - motor2.currentPosition

      dd_1 = (d1_1 - d0_1) / calcTime.seconds()
      dd_2 = (d1_2 - d0_2) / calcTime.seconds()

      motor1.power = kp * d1_1 + kd * dd_1
      motor2.power = kp * d1_2 + kd * dd_2

      d0_1 = value - motor1.currentPosition
      d0_2 = value - motor2.currentPosition
      calcTime.reset()
    }

    motor1.power = 0.0
    motor2.power = 0.0

  }

  private var kp = 2.0
  private var ki = 0.1
  private var kd = 0.1

  private var err1 = 0.0
  private var err2 = 0.0
  private var preverr1 = 0.0
  private var preverr2 = 0.0
  private var u1 = 0.0
  private var u2 = 0.0
  private var serr2 = 0.0
  private var serr1 = 0.0
  private var target = 0.0
  private var levelNumber = 0.0
  private var level = 10.0
  private var fl = 6.0
  private var timer = ElapsedTime()
  private var stable = false
  private var allowext = false
  private var thread_working = true
  private var work = thread(true) {
    var start1 = motor1.currentPosition
    var start2 = motor2.currentPosition
    timer.reset()
    op_mode.telemetry.log().add("thread is working")
    while (thread_working && op_mode.opModeIsActive()) {
      err1 = set_pos(start1 - motor1.currentPosition) - target
      err2 = set_pos(start2 - motor2.currentPosition) + target

      if (abs(err1) < 3 && abs(err2) < 3) {
        serr1 += err1 * timer.seconds()
        serr2 += err2 * timer.seconds()
      }

      if (serr1 * ki > 0.2) {
        serr1 = 0.15 / ki
      }

      if (serr2 * ki > 0.2) {
        serr2 = 0.15 / ki
      }

      timer.reset()

      u1 = kp * err1 + ki * serr1 + kd * (err1 - preverr1) / timer.seconds()
      u2 = kp * err2 + ki * serr2 + kd * (err2 - preverr2) / timer.seconds()

      motor1.power = u1
      motor2.power = u2

      preverr1 = err1
      preverr2 = err2

      stable = err1 < 1.0 && err2 < 1.0

      allowext = (err1 < 5.0 && err2 < 5.0 && target != 0.0) || levelNumber == 0.0
    }
  }

  fun set_pos(enc: Int): Double {
    var steps_per_rotation = 560
    var d = 2
    return d * PI * enc / steps_per_rotation
  }

  fun get_pid(): Array<Double> {
    return arrayOf(kp, ki, kd)
  }

  fun set_pid(kp: Double = this.kp, ki: Double = this.ki, kd: Double = this.kd) {
    this.kp = kp
    this.ki = ki
    this.kd = kd
  }

  fun disable() {
    thread_working = false
  }

  fun set_level_number(level_number: Double) {
    this.levelNumber = level_number
    this.target = -this.fl - this.level * (level_number - 1)
  }

  fun get_level_number(): Double {
    return this.levelNumber
  }

  fun set_target(new: Double) {
    this.target = new
  }

  fun get_target(): Double {
    return this.target
  }

  fun is_stable(): Boolean {
    return stable
  }

  fun get_avg_err(): Double {
    return (err1 + err2) / 2
  }
}
