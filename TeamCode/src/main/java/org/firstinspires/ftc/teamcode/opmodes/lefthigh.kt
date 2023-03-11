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

@Autonomous(name = "lefthigh")
class lefthigh: LinearOpMode() {
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
    
    robot.set_take(0.4)
    sleep(2000)
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
  
    robot.set_powers(0.4)//вперед
    sleep(100)
    robot.set_powers(0)
    
    robot.motor_up1.power=0.8//вверх
    robot.motor_up2.power=-0.8
    sleep(100)
  
    robot.motor_up1.power = 0.0//вверх
    robot.motor_up2.power = 0.0
    
    robot.set_powers(0.4)//вперед
    sleep(2000)
    robot.set_powers(0)
  
    robot.flipPID(260.0)
    sleep(2000)
    
    robot.set_powers(doubleArrayOf(+.4, +.4, -.4, -.4))//на 45 градусов
    sleep(320)
    robot.set_powers(0)
  
    robot.motor_up1.power=0.8//вверх
    robot.motor_up2.power=-0.8
    sleep(1550)
  
    robot.flipPID(260.0)
    sleep(2000)
  
    robot.motor_up1.power = .022
    robot.motor_up2.power = -.022
    robot.set_powers(0.3)//к узлу
    sleep(600)
    robot.set_powers(0)
  
    robot.motor_up1.power = .022
    robot.motor_up2.power = -.022
    robot.set_powers(-0.3)//к узлу
    sleep(70)
    robot.set_powers(0)
    robot.motor_up1.power = .022
    robot.motor_up2.power = -.022
    
    sleep(1500)
  
    robot.motor_up1.power = .022
    robot.motor_up2.power = -.022
    robot.set_take(0.12)
    sleep(1000)
  
    robot.motor_up1.power = .022
    robot.motor_up2.power = -.022
    robot.set_powers(-0.3)//вперед
    sleep(600)
    robot.set_powers(0)
    robot.flipPID(260.0)
    sleep(2000)
    
    robot.set_powers(doubleArrayOf(+.4, +.4, -.4, -.4))//на 45 градусов
    sleep(320)
    robot.set_powers(0)
    
    robot.motor_up1.power=-0.3//вниз
    robot.motor_up2.power=0.3
    sleep(1550)
  
    robot.flipPID(-260.0)
    sleep(2000)
    
    if ((yp>cp) && (yp>mp)) {
      robot.set_powers(-0.4)
      sleep(1000)
      robot.set_powers(0)
    } else if ((cp>20) && (cp>mp)) {
      robot.set_powers(0)
    } else {
      robot.set_powers(0.4)
      sleep(1000)
      robot.set_powers(0)
    }
    
    
    }
    
    
    
  }
