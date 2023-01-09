package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.firstinspires.ftc.teamcode.lib.Intake

@TeleOp(name = "Test_Coro")
class Test: LinearOpMode() {
  
  override fun runOpMode() {
    var intake = Intake()
    var log = telemetry.log()
    
    intake.on_ground {
      log.add("on grounds!!!")
      
      Intake.State.LOW
    }
    
    intake.on_low {
      log.add("lowww!!!!")
      
      Intake.State.MID
    }
    
    intake.on_mid {
      log.add("mid ‾\\_'.'_/‾")
    
      Intake.State.HIGH
    }
    
    intake.on_high {
      log.add("high!!! :000")
    
      Intake.State.GROUND
    }
    
    waitForStart()
    
    while (opModeIsActive()) {
      runBlocking {
        launch {
          for (k in 1..10) {
            log.add("Not blocked $k")
            delay(100)
          }
        }
        
        launch {
          intake.step()
        }
      }
    }
    
  }
}