package org.firstinspires.ftc.teamcode.lib

import android.os.Build
import androidx.annotation.RequiresApi
import com.acmerobotics.dashboard.DashboardWebSocket
import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.robotcore.external.navigation.Orientation
import org.firstinspires.ftc.teamcode.lib.hardware.Motor
import org.firstinspires.ftc.teamcode.lib.hardware.Servo
import org.firstinspires.ftc.teamcode.lib.utils.Utils
import org.firstinspires.ftc.teamcode.util.DashboardUtil
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.*
/*
Control Hub:
  Motors:
  0. motor_up1
  1. left_front
  2. left_rear
  3. flip
  Servo:
Expansion:
Motors:
0. right_rear
1. right_front
2. motor_up2
Servo:
*/
@Config
class Robot(val op_mode: LinearOpMode) {
  var hardware: MutableMap<String, Any> = mutableMapOf()
  var ex: ExecutorService = Executors.newSingleThreadExecutor()
  
  var db: FtcDashboard = FtcDashboard.getInstance()
  var flipFront: Int = 0
  var flipRear: Int = 255
  var flipState = true
  var dm: Telemetry = Telemetry(op_mode)
  

  class InitContext {
    var hardware: MutableMap<String, Any> = mutableMapOf()

    infix fun String.to(other: Any) {
      hardware[this] = other
    }
  }

  fun init(cb: InitContext.(OpMode) -> Unit) {
    val ctx = InitContext()

    cb(ctx, op_mode)

    val new_hw: MutableMap<String, Any> = mutableMapOf()
    for ((k, v) in ctx.hardware) {
      new_hw[k] = this.op_mode.hardwareMap.get(v::class.java, k)
    }
  }

  // 0.08, 0.71
  var take: Double
    get() = servo_take.position
    set(value) {
      if (value in 0.0..1.0) servo_take.position = value
    }

  fun set_take(v: Double) { servo_take.position = v }
  fun get_take() = servo_take.position

  var imu: BNO055IMU

  private var angle: Double = .0
  private var last_angle: Orientation = Orientation()

  /*private var _button: DigitalChannel = op_mode.hardwareMap!!.digitalChannel!!.get("button")

  var button: Boolean
      get() = _button.state
      set(value) {
          _button.state = value
      }*/

  //region:Config
  companion object {
    const val DEFAULT_MOTOR_POWER = .5
    const val TICK_PER_REV = 1120
    const val WHEEL_RADIUS = .2
    const val flipkP = 0.003
    const val flipkD = 0.002
    const val showDashmetry = true
  }
  
  //endregion
  var right_front = Motor(op_mode.hardwareMap!!.dcMotor!!.get("right_front"))
  var right_rear = Motor(op_mode.hardwareMap!!.dcMotor!!.get("right_rear"))
  var left_front = Motor(op_mode.hardwareMap!!.dcMotor!!.get("left_front"))
  var left_rear = Motor(op_mode.hardwareMap!!.dcMotor!!.get("left_rear"))
  
  var motor_up1 = Motor(op_mode.hardwareMap!!.dcMotor!!.get("motor_up1"))
  var motor_up2 = Motor(op_mode.hardwareMap!!.dcMotor!!.get("motor_up2"))
  public var liftParam: PIDParameters = PIDParameters(0.0, 0.0, 0.0,
    0.0, 0.0, 5.0, 10.0,
    {power -> expand(power)}, {motor_up1.currentPosition},
    {motor_up1.velocity}, true)
  var liftPID: PIDRegulator = PIDRegulator(liftParam, op_mode)
  
  var servo_take = Servo(op_mode.hardwareMap!!.servo!!.get("servo_take"))
  
  var flip =  Motor(op_mode.hardwareMap!!.dcMotor!!.get("flip"))
  @RequiresApi(Build.VERSION_CODES.N)
  var flipParam: PIDParameters = PIDParameters(0.01, 0.01, 0.01,
    1.0, 1.0, 5.0, 10.0,
    { flip.power }, {flip.currentPosition},
    {flip.velocity}, true)
  @RequiresApi(Build.VERSION_CODES.N)
  var flipPID: PIDRegulator = PIDRegulator(flipParam, op_mode)
  


  init {
    right_front.direction = DcMotorSimple.Direction.REVERSE
    right_rear.direction = DcMotorSimple.Direction.REVERSE
    flip.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

    imu = op_mode.hardwareMap.get(BNO055IMU::class.java, "imu")
    val params = BNO055IMU.Parameters()
    params.angleUnit = BNO055IMU.AngleUnit.DEGREES
    imu.initialize(params)
    
  }

  fun drive(x: Double, y: Double, r: Double) {
    set_powers(Utils.calc_powers(x, y, r))
  }

  /**
   * Field centric drive
   *
   */
  fun drive_fc(x: Double, y: Double, r: Double) {
    val heading = -imu.angularOrientation.firstAngle
    val rx = x * cos(heading) - y * sin(heading)
    val ry = x * sin(heading) + y * cos(heading)
    val den = (abs(y) + abs(x) + abs(r)).coerceAtLeast(1.0)

    set_powers(Utils.calc_powers(rx, ry, r, den))
  }

  fun set_mode(mode: DcMotor.RunMode) {
    left_front.mode  = mode
    left_rear.mode   = mode
    right_front.mode = mode
    right_rear.mode  = mode
  }

  fun is_busy(): Boolean {
    val busy = left_front.isBusy &&
      left_rear.isBusy &&
      right_front.isBusy &&
      right_rear.isBusy

    val pows = (abs(left_front.power)  +
      abs(left_rear.power)   +
      abs(right_front.power) +
      abs(right_rear.power)) > .05

    return busy && pows
  }

  fun set_target_position(target: Int, pows: DoubleArray) {
    left_front.targetPosition = (target * sign(pows[0])).toInt()
    left_rear.targetPosition = (target * sign(pows[1])).toInt()
    right_front.targetPosition = (target * sign(pows[2])).toInt()
    right_rear.targetPosition = (target * sign(pows[3])).toInt()
  }

  fun set_target_position(target: IntArray) {
    left_front.targetPosition = target[0]
    left_rear.targetPosition = target[1]
    right_front.targetPosition = target[2]
    right_rear.targetPosition = target[3]
  }

  fun set_target_position(target: Double) {
    left_front.targetPosition = target.toInt()
    left_rear.targetPosition = target.toInt()
    right_front.targetPosition = target.toInt()
    right_rear.targetPosition = target.toInt()
  }

  fun set_powers(pows: DoubleArray) {
    left_front.power = pows[0]
    left_rear.power = pows[1]
    right_front.power = pows[2]
    right_rear.power = pows[3]
  }

  fun set_powers(pow: Double) {
    left_front.power = pow
    left_rear.power = pow
    right_front.power = pow
    right_rear.power = pow
  }

  fun set_powers(pow: Int) {
    left_front.power = pow.toDouble()
    left_rear.power = pow.toDouble()
    right_front.power = pow.toDouble()
    right_rear.power = pow.toDouble()
  }

  fun set_zero_power_behavior(zpb: DcMotor.ZeroPowerBehavior) {
    left_rear.zeroPowerBehavior = zpb
    left_front.zeroPowerBehavior = zpb
    right_front.zeroPowerBehavior = zpb
    right_rear.zeroPowerBehavior = zpb
  }

  fun move(ticks: Int, x: Double = 0.0, y: Double = 0.0, r: Double = 0.0) {
    set_mode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)

    var powers = Utils.calc_powers(x, y, r)
    set_target_position(ticks, powers)
    set_powers(powers)
    set_mode(DcMotor.RunMode.RUN_TO_POSITION)

    while (is_busy() && op_mode.opModeIsActive()) {}

    set_powers(0)

    set_mode(DcMotor.RunMode.RUN_WITHOUT_ENCODER)
  }

  fun move(cm: Double, power: Double = DEFAULT_MOTOR_POWER) {
    set_mode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)

    val target = -(cm * .3937) / (WHEEL_RADIUS * sqrt(2.0)) * TICK_PER_REV
    set_target_position(target)
    set_powers(doubleArrayOf(-power, -power, power, power))
    set_mode(DcMotor.RunMode.RUN_TO_POSITION)

    while (is_busy() && op_mode.opModeIsActive()) {}

    set_powers(0.0)
    set_mode(DcMotor.RunMode.RUN_USING_ENCODER)
  }

  fun turn(deg: Double, power: Double = DEFAULT_MOTOR_POWER) {
    set_mode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)

    val target = deg / 360.0 * TICK_PER_REV
    set_target_position(target)
    set_powers(doubleArrayOf(power, power, power, power))
    set_mode(DcMotor.RunMode.RUN_TO_POSITION)

    while (is_busy() && op_mode.opModeIsActive()) {}

    set_powers(0.0)
    set_mode(DcMotor.RunMode.RUN_USING_ENCODER)
  }
  // max = 5300 tick
  // 5500
  // 4200
  // 2700
  // 600
  fun expand(tick: Int, power: Double = DEFAULT_MOTOR_POWER) {
    motor_up1.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
    motor_up2.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

    val target = (tick * sign(power)).toInt()

    if (target > 5300 || motor_up1.currentPosition > 5300) {
      return
    }

    motor_up1.targetPosition = (tick * sign(power)).toInt()
    motor_up2.targetPosition = (tick * -sign(power)).toInt()
    motor_up1.power = power
    motor_up2.power = -power

    motor_up1.mode = DcMotor.RunMode.RUN_TO_POSITION
    motor_up2.mode = DcMotor.RunMode.RUN_TO_POSITION

    while (motor_up1.isBusy && motor_up1.power > .05 &&
           motor_up2.isBusy && motor_up2.power > .05 && op_mode.opModeIsActive()) {}

    motor_up1.power = 0.0
    motor_up2.power = 0.0

    motor_up1.mode = DcMotor.RunMode.RUN_USING_ENCODER
    motor_up2.mode = DcMotor.RunMode.RUN_USING_ENCODER
  }
  fun expand(power:Number){
    motor_up1.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
    motor_up2.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
    motor_up1.power = power.toDouble()
    motor_up2.power = -power.toDouble()
  }

  fun reset_angle() {
    last_angle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES)
    angle = 0.0
  }

  fun actual_angle(): Double {
    val angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES)
    var dangle = angles.firstAngle - last_angle.firstAngle

    if (dangle < -180) {
      dangle += 360
    } else if (dangle > 180) {
      dangle -= 360
    }

    angle += dangle
    last_angle = angles

    return angle
  }

  fun rotate(degrees: Double, power: Double = DEFAULT_MOTOR_POWER) {
    set_mode(DcMotor.RunMode.RUN_USING_ENCODER)

    while (op_mode.opModeIsActive()) {
      if (degrees > 0) {
        if (actual_angle() > degrees) break
        set_powers(doubleArrayOf(power, power, -power, -power))
      } else {
        if (actual_angle() < degrees) break
        set_powers(doubleArrayOf(-power, -power, power, power))
      }
    }
    set_powers(0.0)

    op_mode.sleep(5L)

    reset_angle()
  }
  fun flipFront(power:Double){
    flip.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
    flip.targetPosition = -260
    flip.mode = DcMotor.RunMode.RUN_TO_POSITION
    flip.power = -power
    while (op_mode.opModeIsActive() && (flip.currentPosition >= flip.targetPosition)){}
    flip.power = 0.0
  }
  fun flipRear(power: Double){
    flip.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
    flip.targetPosition = 260
    flip.mode = DcMotor.RunMode.RUN_TO_POSITION
    flip.power = -power
    while (op_mode.opModeIsActive() && (flip.currentPosition <= flip.targetPosition)){}
    flip.power = 0.0
    
  }
  fun flipPID(setValue: Double) {
    flip.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
    flip.targetPosition = setValue.toInt()
    flip.mode = DcMotor.RunMode.RUN_TO_POSITION
    var tm = Telemetry(op_mode)
    var d0 = setValue - flip.currentPosition
    var d1: Double
    val calcTime = ElapsedTime()
    val setTime = ElapsedTime()
    var dIntegral = 0.0
    var dDerivative: Double
    var u: Double
    setTime.reset()
    calcTime.reset()
    while (op_mode.opModeIsActive() && setTime.seconds() < 0.5 &&
      (abs(d0) > 1 ||
        abs(flip.velocity) > 0.2)) {
      d1 = setValue - flip.currentPosition
      dDerivative = (d1 - d0) / (calcTime.nanoseconds() * 10e-9)
      u = flipkP * d1 + flipkD * dDerivative
      flip.power = u
      d0 = setValue - flip.currentPosition
      if (showDashmetry) {
        tm.addData("SettingTime", setTime.seconds())
        tm.addData("deltaTime", calcTime.nanoseconds() * 10e-9)
        tm.addLine("")
        tm.addData("SetValue", setValue)
        tm.addData("Value", flip.currentPosition)
        tm.addData("Do", d0)
        tm.addData("D1", d1)
        tm.addLine("")
        tm.addData("dDerivative", dDerivative)
        tm.addData("dIntegral", dIntegral)
        tm.addLine("")
        tm.addData("U", u)
        tm.addData("uP", flipkP * d1)
        tm.addData("uD", flipkD * dDerivative)
        tm.update()
      }
      calcTime.reset()
    }
    flip.power = 0.0
    setTime.reset()
    }
  
    class flipFrontAutomate(): Runnable{
      override fun run() {
        var flip =  Motor(hardwareMap!!.dcMotor!!.get("flip"))
        flip.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        flip.targetPosition = 260
        flip.mode = DcMotor.RunMode.RUN_TO_POSITION
        var d0 = 260 - flip.currentPosition
        var d1: Double
        val calcTime = ElapsedTime()
        val setTime = ElapsedTime()
        var dDerivative: Double
        var u: Double
        setTime.reset()
        calcTime.reset()
        while ( setTime.seconds() < 3 && (abs(d0) > 1 || abs(flip.velocity) > 0.2)) {
          d1 = (260 - flip.currentPosition).toDouble()
          dDerivative = (d1 - d0) / (calcTime.nanoseconds() * 10e-9)
          u = flipkP * d1 + flipkD * dDerivative
          flip.power = u
          d0 = 260 - flip.currentPosition
        }
        flip.power = 0.0
      }
  }
}