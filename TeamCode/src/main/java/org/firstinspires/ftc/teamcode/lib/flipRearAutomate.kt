package org.firstinspires.ftc.teamcode.lib

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion
import org.firstinspires.ftc.teamcode.lib.hardware.Motor
import kotlin.math.abs


class flipRearAutomate : Runnable {
  companion object {
    const val pos = 260
    val flip = Motor(BlocksOpModeCompanion.hardwareMap!!.dcMotor!!.get("flip"))

  }

  override fun run() {
    flip.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
    flip.targetPosition = pos
    flip.mode = DcMotor.RunMode.RUN_TO_POSITION
    var d0 = pos - flip.currentPosition
    var d1: Double
    val calcTime = ElapsedTime()
    val setTime = ElapsedTime()
    var dDerivative: Double
    var u: Double
    setTime.reset()
    calcTime.reset()
    while (setTime.seconds() < 3 && (abs(d0) > 1 || abs(flip.velocity) > 0.2)) {
      d1 = (pos - flip.currentPosition).toDouble()
      dDerivative = (d1 - d0) / (calcTime.nanoseconds() * 10e-9)
      u = Robot.flipkP * d1 + Robot.flipkD * dDerivative
      flip.power = u
      d0 = 260 - flip.currentPosition
    }
    flip.power = 0.0
  }
}