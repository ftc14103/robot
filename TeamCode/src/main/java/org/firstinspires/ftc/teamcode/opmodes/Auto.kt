package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.lib.Robot

@Autonomous(name = "Autonomous", group = "Autonomous - Main")
class Auto: LinearOpMode() {
  override fun runOpMode() {
    val robot = Robot(this)


    waitForStart()

    robot.set_powers(0.5)

    sleep(1000)

    robot.set_powers(.0)
  }
}