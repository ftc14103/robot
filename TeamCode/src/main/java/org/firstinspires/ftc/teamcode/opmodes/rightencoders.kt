package org.firstinspires.ftc.teamcode.opmodes

import android.os.Build
import androidx.annotation.RequiresApi
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.lib.Robot
import org.firstinspires.ftc.teamcode.lib.recognition.SleeveRecognition
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation

@Autonomous(name = "rightencoders")
class rightencoders: LinearOpMode() {
  @RequiresApi(Build.VERSION_CODES.N)
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
    camera.openCameraDeviceAsync(object : OpenCvCamera.AsyncCameraOpenListener {
      override fun onOpened() {
        camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
      }
      
      override fun onError(err: Int) {}
    })
    
    waitForStart()
    
    robot.set_take(0.46)
    //sleep(2000)
    var cp = slrecog.cyaPercent
    var yp = slrecog.yelPercent
    var mp = slrecog.magPercent
    camera.stopStreaming()
    camera.closeCameraDevice()
    //var enc_val = 0
    //var prev_enc = robot.motor_up2.currentPosition
    
    telemetry.addData("cp", cp)
    telemetry.addData("yp", yp)
    telemetry.addData("mp", mp)
    telemetry.addData("2 zone", (cp>mp) && (cp>yp))
    telemetry.addData("3 zone", (mp>cp) && (mp>yp))
    telemetry.addData("enc1",robot.motor_up2.currentPosition)
    telemetry.update()
  
    robot.motor_up1.power=1.0//вверх
    robot.motor_up2.power=-1.0
    robot.move(1500,0.0,0.4,0.0)
    robot.motor_up1.power=1.0//вверх
    robot.motor_up2.power=-1.0
    robot.move(300,0.0,-0.4,0.0)
    
    robot.motor_up1.power=1.0//вверх
    robot.motor_up2.power=-1.0
    robot.move(500,0.0,0.0,-0.4)
    
    robot.motor_up1.power=0.015//вверх
    robot.motor_up2.power=-0.015
    
    
    robot.motor_up1.power=1.0//вверх
    robot.motor_up2.power=-1.0
    robot.set_powers(doubleArrayOf(-.4, -.4, +.4, +.4))//на 45 градусов
    sleep(280)
    robot.set_powers(0)
    
    robot.motor_up1.power=1.0//вверх
    robot.motor_up2.power=-1.0
    sleep(3000)
    
    robot.motor_up1.power = .015
    robot.motor_up2.power = -.015
    robot.set_powers(0.3)//к узлу
    sleep(900)
    robot.set_powers(0)
    
    robot.motor_up1.power = .015
    robot.motor_up2.power = -.015
    robot.set_powers(-0.3)//к узлу
    sleep(120)
    robot.set_powers(0)
    robot.motor_up1.power = .015
    robot.motor_up2.power = -.015
    
    sleep(1000)
    
    robot.motor_up1.power=-0.3//вниз
    robot.motor_up2.power=0.3
    sleep(150)
    robot.motor_up1.power = .015
    robot.motor_up2.power = -.015
    robot.set_take(0.14)
    sleep(500)
    
    robot.motor_up1.power=1.0
    robot.motor_up2.power=-1.0
    sleep(150)
    robot.motor_up1.power = .015
    robot.motor_up2.power = -.015
    robot.set_powers(-0.3)//вперед
    sleep(450)
    robot.set_powers(0)
    robot.flipPID(160.0)
    sleep(1000)
    
    robot.set_powers(doubleArrayOf(-.4, -.4, +.4, +.4))//на 45 градусов
    sleep(240)
    robot.set_powers(0)
    
    //robot.motor_up1.power=-0.3//вниз
    //robot.motor_up2.power=0.3
    //sleep(1100)
    
    //robot.motor_up1.power=-0.3//вниз
    //robot.motor_up2.power=0.3
    //robot.set_powers(doubleArrayOf(+.5, -.5, -.5, +.5))//вправо
    //sleep(200)
    
    robot.motor_up1.power=-0.33//вниз
    robot.motor_up2.power=0.33
    robot.set_powers(-0.5)
    sleep(1000)
    
    robot.motor_up1.power=-0.4
    robot.motor_up2.power=0.4
    sleep(1000)
    robot.motor_up1.power=0.0
    robot.motor_up2.power=0.0
    
    robot.take=0.46
    sleep(700)
    robot.set_powers(0.4)//вперед
    sleep(100)
    robot.set_powers(0)
    
    robot.motor_up1.power=1.0
    robot.motor_up2.power=-1.0
    sleep(250)
    
    robot.set_powers(0.5)
    robot.motor_up1.power=1.0
    robot.motor_up2.power=-1.0
    sleep(500)
    
    robot.motor_up1.power=0.015
    robot.motor_up2.power=-0.015
    robot.set_powers(doubleArrayOf(+.4, +.4, -.4, -.4))//на 90 градусов
    sleep(600)
    
    robot.set_powers(-0.5)
    robot.motor_up1.power=0.015
    robot.motor_up2.power=-0.015
    sleep(180)
    
    robot.set_powers(0.5)
    robot.motor_up1.power=0.015
    robot.motor_up2.power=-0.015
    sleep(40)
    
    robot.set_powers(0)
    sleep(500)
    
    robot.take=0.12
    robot.motor_up1.power=-0.2
    robot.motor_up2.power=0.2
    sleep(300)
    robot.motor_up1.power=0.33
    robot.motor_up2.power=-0.33
    sleep(500)
    
    robot.set_powers(0.5)
    robot.motor_up1.power=0.015
    robot.motor_up2.power=-0.015
    sleep(130)
    
    robot.motor_up1.power=0.015
    robot.motor_up2.power=-0.015
    robot.set_powers(doubleArrayOf(-.4, -.4, +.4, +.4))//на 90 градусов
    sleep(670)
    
    robot.motor_up1.power=0.015
    robot.motor_up2.power=-0.015
    robot.set_powers(-0.5)
    sleep(1000)
    
    robot.motor_up1.power=-0.4
    robot.motor_up2.power=0.4
    sleep(500)
    robot.motor_up1.power=0.0
    robot.motor_up2.power=0.0
    
    robot.take=0.46
    sleep(700)
    robot.set_powers(0.4)//вперед
    sleep(100)
    robot.set_powers(0)
    
    robot.motor_up1.power=1.0
    robot.motor_up2.power=-1.0
    sleep(300)
    
    robot.set_powers(0.5)
    robot.motor_up1.power=1.0
    robot.motor_up2.power=-1.0
    sleep(450)
    
    robot.motor_up1.power=0.015
    robot.motor_up2.power=-0.015
    robot.set_powers(doubleArrayOf(+.4, +.4, -.4, -.4))//на 90 градусов
    sleep(650)
    
    robot.set_powers(-0.5)
    robot.motor_up1.power=0.015
    robot.motor_up2.power=-0.015
    sleep(180)
    
    robot.set_powers(0)
    sleep(1000)
    
    robot.take=0.12
    robot.motor_up1.power=-0.2
    robot.motor_up2.power=0.2
    sleep(300)
    robot.motor_up1.power=0.33
    robot.motor_up2.power=-0.33
    sleep(500)
    
    robot.set_powers(0.5)
    robot.motor_up1.power=0.015
    robot.motor_up2.power=-0.015
    sleep(180)
    
    robot.motor_up1.power=0.015
    robot.motor_up2.power=-0.015
    robot.set_powers(doubleArrayOf(-.4, -.4, +.4, +.4))//на 90 градусов
    sleep(600)
    
    if ((yp>cp) && (yp>mp)) {
      robot.set_powers(0.4)
      sleep(1300)
      robot.set_powers(0)
    } else if (cp>mp) {
      robot.set_powers(0.4)
      sleep(300)
      robot.set_powers(0)
    } else {
      robot.set_powers(-0.4)
      sleep(700)
      robot.set_powers(0)
    }
    
    
  }
  
  
  
}


