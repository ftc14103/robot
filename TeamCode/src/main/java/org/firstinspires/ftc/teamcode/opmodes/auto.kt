package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.lib.Robot
import org.firstinspires.ftc.teamcode.lib.recognition.SleeveRecognition
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation

@Autonomous(name = "Auto")
class auto: LinearOpMode() {
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
    // Максим:
    // Этот автоном использует энкодеры
    // я не смог протестить работу этих функций, так как
    // move(тики, скорость_по_x, скорость_по_y, скопрость_по_r) -
    // x - положительный в сторону захвата
    // y - положительный в справа(со стороны экспеншна)
    // метод работает, но робота заносит, думаю это механическая проблема,
    // так как там слетела цепь и я и илья поставили обратно
    
    robot.move(3000, 0.0, 1.0, 0.0)
    sleep(1000)
    
    robot.set_powers(0)
  }
}