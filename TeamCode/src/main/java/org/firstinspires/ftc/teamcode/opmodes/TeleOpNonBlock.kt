package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.firstinspires.ftc.teamcode.lib.Intake
import org.firstinspires.ftc.teamcode.lib.Robot
import org.firstinspires.ftc.teamcode.lib.utils.Utils
import kotlin.properties.Delegates

@TeleOp(name = "TeleOpNonBlocking")
class TeleOpNonBlock: LinearOpMode() {
  var motor_up1_min by Delegates.notNull<Float>()
  var motor_up2_min by Delegates.notNull<Float>()
  var motor_up1_var by Delegates.notNull<Float>()
  var motor_up2_var by Delegates.notNull<Float>()
  suspend fun intake_handler(robot: Robot, intake: Intake) {
    if (gamepad2.left_stick_y != 0f) {
      motor_up1_var += robot.motor_up1.currentPosition - motor_up1_min
      motor_up2_var += robot.motor_up2.currentPosition - motor_up2_min
      
      robot.motor_up1.power = -0.7 * gamepad2.left_stick_y
      robot.motor_up2.power = 0.7 * gamepad2.left_stick_y
    } else {
      robot.motor_up1.power = .007
      robot.motor_up2.power = -.007
    }
    
    if (gamepad2.a) {
      robot.set_take(0.12)
    }
    
    if (gamepad2.x) {
      robot.set_take(0.4)
    }
    
    //if (motor_up1_var <= motor_up1_min) {
    //  robot.motor_up1.power = .007
    //}
    
    //if (motor_up2_var <= motor_up2_min) {
    //  robot.motor_up2.power = .007
    //}
    
    telemetry.addData("motor_up1_var", motor_up1_var)
    telemetry.addData("motor_up2_var", motor_up2_var)
    telemetry.addData("motor_up1_min", motor_up1_min)
    telemetry.addData("motor_up2_min", motor_up2_min)
    
    telemetry.addData("motor_up1.power", robot.motor_up1.power)
    telemetry.addData("motor_up2.power", robot.motor_up2.power)
    
    telemetry.addData("motor_up1.curpos", robot.motor_up1.currentPosition)
    telemetry.addData("motor_up2.curpos", robot.motor_up2.currentPosition)
    
    intake.step()
  }
  
  var slowmode = false
  var b_state = false
  suspend fun drive_handler(robot: Robot) {
    var k = .8
    if (gamepad1.b && !b_state) {
      if (slowmode) {
        slowmode = false
        k = .8
      } else {
        slowmode = true
        k = .5
      }
    }
    b_state = gamepad1.b
    
    robot.drive(
      k * gamepad1.left_stick_x * 1.1,
      k * (-gamepad1.left_stick_y).toDouble(),
      k * 2 / 3 * (gamepad1.right_trigger - gamepad1.left_trigger)
    )
  }
  
  override fun runOpMode() {
    var robot = Robot(this)
    robot.set_zero_power_behavior(DcMotor.ZeroPowerBehavior.BRAKE)
    motor_up1_var = robot.motor_up1.currentPosition.toFloat()
    motor_up2_var = robot.motor_up2.currentPosition.toFloat()
    
    motor_up1_min = motor_up1_var
    motor_up2_min = motor_up2_var
    
    var intake = Intake()
    
    waitForStart()
    
    while (opModeIsActive()) {
      runBlocking {
        launch {
          delay(5)
          intake_handler(robot, intake)
        }
        
        launch {
          delay(5)
          drive_handler(robot)
        }
        
        telemetry.update()
      }
    }
  }
}