package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.lib.Robot
import org.firstinspires.ftc.teamcode.lib.recognition.OpenCV

@Autonomous(name = "Autonomous", group = "Autonomous - Main")
class Auto: LinearOpMode() {
  override fun runOpMode() {
    val robot = Robot(this)
    val recog = OpenCV(this)

    waitForStart()

    robot.set_powers(0.3)

    sleep(1700)

    robot.set_powers(0)
  }
}