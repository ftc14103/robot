package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.lib.Robot
import org.firstinspires.ftc.teamcode.lib.recognition.SleeveRecognition
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation

@Autonomous(name = "left1")
class left1: LinearOpMode() {
  override fun runOpMode() {
    var robot = Robot(this)
    
    
    val cam_id = hardwareMap.appContext.resources.getIdentifier(
      "cameraMonitorViewId", "id", hardwareMap.appContext.packageName
    )
    val camera = OpenCvCameraFactory.getInstance().createWebcam(
      hardwareMap.get(WebcamName::class.java, "Webcam 1"), cam_id
    )
    var slrecog = SleeveRecognition()
    camera.setPipeline(slrecog)
    
    class Listener: OpenCvCamera.AsyncCameraOpenListener {
      override fun onOpened() {
        camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
      }
      
      override fun onError(err: Int) {}
    }
    
    camera.openCameraDeviceAsync(Listener())
    
    waitForStart()
    
    robot.set_take(0.4)
    sleep(2000)
    var cp = slrecog.cyaPercent
    var yp = slrecog.yelPercent
    var mp = slrecog.magPercent
    //var enc_val = 0
    //var prev_enc = robot.motor_up2.currentPosition
    
    telemetry.addData("cp", cp)
    telemetry.addData("yp", yp)
    telemetry.addData("mp", mp)
    telemetry.addData("2 zone", (cp>mp) && (cp>yp))
    telemetry.addData("3 zone", (mp>cp) && (mp>yp))
    telemetry.addData("enc1",robot.motor_up2.currentPosition)
    telemetry.update()
  
    robot.set_powers(0.3)//вперед
    sleep(100)
    robot.set_powers(0)
    
    robot.set_powers(doubleArrayOf(-.5, +.5, +.5, -.5))//влево
    sleep(2100)
    robot.set_powers(0)
    
    robot.set_powers(-0.3)//назад
    sleep(700)
    robot.set_powers(0)
    
    robot.set_powers(0.3)//вперед
    sleep(1900)
    
    robot.set_powers(0)
    
  
    robot.set_powers(doubleArrayOf(-.4, +.4, +.4, -.4))//влево
    sleep(800)
    robot.set_powers(0)
  
    robot.motor_up1.power=0.9//вверх
    robot.motor_up2.power=-0.9
    robot.set_powers(doubleArrayOf(+.4, -.4, -.4, +.4))//вправо
    sleep(400)
    robot.set_powers(0)
    
    robot.motor_up1.power=0.8//вверх
    robot.motor_up2.power=-0.8
    robot.set_powers(doubleArrayOf(+.5, +.5, -.5, -.5))//на 45 градусов
    sleep(370)
    robot.set_powers(0)
    
    robot.motor_up1.power = .007
    robot.motor_up2.power = -.007
    robot.set_powers(0.3)
    sleep(800)
    
    robot.set_powers(0)
    
    robot.motor_up1.power = .007
    robot.motor_up2.power = -.007
    robot.set_powers(-0.3)
    sleep(100)
    robot.set_powers(0)
    
    robot.motor_up1.power=-0.4//вниз
    robot.motor_up2.power=0.4
    sleep(200)
  
    robot.motor_up1.power = .007
    robot.motor_up2.power = -.007
    robot.set_take(0.11)//поставили конус
    sleep(2000)
    
    robot.motor_up1.power=0.4//вверх
    robot.motor_up2.power=-0.4
    sleep(400)
  
    robot.motor_up1.power = .007
    robot.motor_up2.power = -.007
    robot.set_powers(-0.3)
    sleep(450)
    robot.set_powers(0)
    sleep(200)
    robot.set_powers(doubleArrayOf(-.5, -.5, +.5, +.5))//на 135 градусов
    sleep(760)
    
    //robot.set_powers(-0.3)
    //sleep(200)
    robot.set_powers(-0.3)
    sleep(180)
    robot.set_powers(doubleArrayOf(+.4, -.4, -.4, +.4))//вправо
    sleep(1100)
    robot.set_powers(0)
    sleep(200)
    
  
    robot.set_powers(0.3)//прямо за конусом
    sleep(1400)
    robot.set_powers(0)
  
    robot.motor_up1.power=-0.4//вниз
    robot.motor_up2.power=0.4
    sleep(300)
    robot.motor_up1.power = .007
    robot.motor_up2.power = -.007
    robot.set_take(0.4)
    sleep(500)
    robot.motor_up1.power=0.4//вверх
    robot.motor_up2.power=-0.4
    sleep(700)
  
    robot.motor_up1.power = .007
    robot.motor_up2.power = -.007
    robot.set_powers(-0.3)//назад
    sleep(500)
    robot.set_powers(-0.5)
    sleep(1600)
    
    robot.set_powers(0)
    
    robot.motor_up1.power = .007
    robot.motor_up2.power = -.007
    robot.set_powers(doubleArrayOf(+.5, +.5, -.5, -.5))//на 45 градусов
    sleep(270)
    robot.set_powers(0)
    robot.motor_up1.power=0.8//вверх
    robot.motor_up2.power=-0.8
    sleep(1350)
  
    robot.motor_up1.power = .007
    robot.motor_up2.power = -.007
    robot.set_powers(0.3)
    sleep(400)
    robot.set_powers(0)
  
    robot.motor_up1.power=-0.4//вниз
    robot.motor_up2.power=0.4
    sleep(200)
  
    robot.motor_up1.power = .007
    robot.motor_up2.power = -.007
    robot.set_take(0.11)//поставили конус
    sleep(500)
  
    robot.motor_up1.power=0.2//вверх
    robot.motor_up2.power=-0.2
    sleep(400)
    robot.set_powers(-0.3)//назад
    sleep(500)
  
    robot.set_powers(doubleArrayOf(-.5, -.5, +.5, +.5))//на 45 градусов
    sleep(270)
    robot.set_powers(0)
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*if ((yp>cp) && (yp>mp) && (yp>5)) {
      robot.set_powers(0.4)
      sleep(1700)//1 зона
      
    } else if ((mp<5) && (cp>5))  {
      robot.set_powers(0.4)
      sleep(830)//2 зона
      
    }*/
    
    
    
  }
}