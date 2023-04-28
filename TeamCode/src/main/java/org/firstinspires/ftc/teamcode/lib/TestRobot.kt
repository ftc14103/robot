package org.firstinspires.ftc.teamcode.lib

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.lib.hardware.Servo

class TestRobot(val op_mode: LinearOpMode) {
  var hardware: MutableMap<String, Any> = mutableMapOf()

  fun init() {
    init {
      "left_front" to DcMotor::class.java
      "left_rear" to DcMotor::class.java
      "right_front" to DcMotor::class.java
      "right_rear" to DcMotor::class.java
      "motor_up" to DcMotor::class.java
      "motor_down" to DcMotor::class.java

      "servo_take" to Servo::class.java

    }
  }

  class InitContext {
    var hardware: MutableMap<String, Any> = mutableMapOf()

    infix fun String.to(other: Any) {
      hardware[this] = other
    }
  }

  fun init(cb: InitContext.(OpMode) -> Unit) {
    var ctx = InitContext()

    cb(ctx, op_mode)

    var new_hw: MutableMap<String, Any> = mutableMapOf()
    for ((k, v) in ctx.hardware) {
      new_hw[k] = this.op_mode.hardwareMap.get(v::class.java, k)
    }
  }

  fun test() {
    this.op_mode.telemetry.log().add("Test")
  }
}