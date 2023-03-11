package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.lib.flipRearAutomate

@Autonomous(name="LiftPID", group="Test")
class LiftPIDTest: LinearOpMode() {
  override fun runOpMode(){
    super.start()
    var thread = Thread(flipRearAutomate)
    while (opModeIsActive()){
      if (gamepad1.a){
        thread.run()
      }
    }
  }

}