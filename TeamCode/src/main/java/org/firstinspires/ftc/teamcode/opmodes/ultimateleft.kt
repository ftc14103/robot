package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.lib.Robot
import org.firstinspires.ftc.teamcode.lib.recognition.SleeveRecognition
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation

@Autonomous(name = "ultimateleft")
class ultimateleft: LinearOpMode() {
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
    sleep(100)
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
    sleep(2300)
    robot.set_powers(0)
    robot.set_powers(-0.4)//вперед
    sleep(300)
    robot.set_powers(0)
  
    robot.flipPID(260.0)
    sleep(2000)
  
    robot.set_powers(doubleArrayOf(+.4, +.4, -.4, -.4))//на 45 градусов
    sleep(300)
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
    sleep(300)
    robot.set_powers(0)
    
    //установка конуса из стопки
    robot.set_powers(-0.4)//вперед
    robot.motor_up1.power=-0.22
    robot.motor_up2.power=0.22
    sleep(1000)
    robot.set_powers(0)
  
    robot.motor_up1.power=-0.22
    robot.motor_up2.power=0.22
    sleep(300)
  
    robot.motor_up1.power = .022
    robot.motor_up2.power = -.022
    robot.set_take(0.4)
    sleep(500)
  
    robot.motor_up1.power=0.8//вверх
    robot.motor_up2.power=-0.8
    sleep(300)
    
    robot.motor_up1.power = .022
    robot.motor_up2.power = -.022
    robot.set_powers(0.4)//вперед
    sleep(600)
    robot.set_powers(0)
  
    robot.flipPID(260.0)
    sleep(1000)
  
    robot.set_powers(doubleArrayOf(-.4, -.4, +.4, +.4))//на 45 градусов
    sleep(300)
    robot.set_powers(0)
  
    robot.motor_up1.power=0.8//вверх
    robot.motor_up2.power=-0.8
    sleep(1500)
  
    robot.motor_up1.power = .02
    robot.motor_up2.power = -.02
    robot.set_powers(0.3)//к узлу
    sleep(600)
    robot.set_powers(0)
  
    robot.motor_up1.power = .02
    robot.motor_up2.power = -.02
    robot.set_powers(-0.3)//к узлу
    sleep(70)
    robot.set_powers(0)
    robot.motor_up1.power = .02
    robot.motor_up2.power = -.02
  
    sleep(1500)
  
    robot.motor_up1.power = .02
    robot.motor_up2.power = -.02
    robot.set_take(0.12)
    sleep(1000)
  
    robot.motor_up1.power = .02
    robot.motor_up2.power = -.02
    robot.set_powers(-0.3)//вперед
    sleep(600)
    robot.set_powers(0)
    robot.flipPID(260.0)
    sleep(2000)
  
    robot.set_powers(doubleArrayOf(+.4, +.4, -.4, -.4))//на 45 градусов
    sleep(300)
    robot.set_powers(0)
    //конец установки конуса
  
  
  
    if ((yp>cp) && (yp>mp)) {
      robot.set_powers(-0.3)
      sleep(800)
      robot.set_powers(0)
    } else if (mp>10) {
      robot.set_powers(0.3)
      sleep(800)
      robot.set_powers(0)
    }
    
    
  }
  
  
  
}
