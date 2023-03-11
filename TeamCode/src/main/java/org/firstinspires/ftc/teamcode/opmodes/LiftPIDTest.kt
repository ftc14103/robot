//import com.qualcomm.robotcore.hardware.DcMotor
//import com.qualcomm.robotcore.util.ElapsedTime
//import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion
//import org.firstinspires.ftc.teamcode.lib.Robot
//import org.firstinspires.ftc.teamcode.lib.hardware.Motor
//import kotlin.math.abs
//
//object ThreadNoName {
//  @JvmStatic
//  fun main(args: Array<String>) {
//    val thread = Thread {
//      var flip =  Motor(BlocksOpModeCompanion.hardwareMap!!.dcMotor!!.get("flip"))
//      flip.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
//      flip.targetPosition = 260
//      flip.mode = DcMotor.RunMode.RUN_TO_POSITION
//      var d0 = 260 - flip.currentPosition
//      var d1: Double
//      val calcTime = ElapsedTime()
//      val setTime = ElapsedTime()
//      var dDerivative: Double
//      var u: Double
//      setTime.reset()
//      calcTime.reset()
//      while ( setTime.seconds() < 3 && (abs(d0) > 1 || abs(flip.velocity) > 0.2)) {
//        d1 = (260 - flip.currentPosition).toDouble()
//        dDerivative = (d1 - d0) / (calcTime.nanoseconds() * 10e-9)
//        u = Robot.flipkP * d1 + Robot.flipkD * dDerivative
//        flip.power = u
//        d0 = 260 - flip.currentPosition
//      }
////      flip.power = 0.0
//    }
//    thread.start()
//    val thread1: Thread = object : Thread() {
//      override fun run() {
//        println("hello")
//      }
//    }
//    thread1.start()
//  }
//}