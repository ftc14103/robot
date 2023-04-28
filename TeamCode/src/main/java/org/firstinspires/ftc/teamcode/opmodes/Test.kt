package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name = "Testssstststs")
class Test : LinearOpMode() {

  override fun runOpMode() {
    val motor = this.hardwareMap.dcMotor.get("flip")

    waitForStart()

    while (opModeIsActive()) {
      motor.power = gamepad1.left_stick_x.toDouble()

      telemetry.addData("power", motor.power)
      telemetry.update()
    }
  }
}