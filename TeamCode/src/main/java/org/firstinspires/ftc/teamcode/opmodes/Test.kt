package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.firstinspires.ftc.teamcode.lib.Intake
import org.firstinspires.ftc.teamcode.lib.Robot

@TeleOp(name = "Testssstststs")
class Test: LinearOpMode() {
  
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