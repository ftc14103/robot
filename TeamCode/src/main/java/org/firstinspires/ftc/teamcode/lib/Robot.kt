package org.firstinspires.ftc.teamcode.lib

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.robotcore.external.navigation.Orientation
import org.firstinspires.ftc.teamcode.lib.hardware.Motor
import org.firstinspires.ftc.teamcode.lib.hardware.Servo
import org.firstinspires.ftc.teamcode.lib.utils.Utils
import kotlin.math.*

class Robot(val op_mode: LinearOpMode) {
    var hardware: MutableMap<String, Any> = mutableMapOf()

    fun init() {
        init {
            "left_front" to DcMotor::class.java
            "left_rear" to DcMotor::class.java
            "right_front" to DcMotor::class.java
            "right_rear" to DcMotor::class.java
            "motor_up" to DcMotor::class.java
            "motor_down" to DcMotor::class.java

            "servo_take" to Servo::class.java

        }
    }

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
    }
    //endregion

    var right_front = Motor(op_mode.hardwareMap!!.dcMotor!!.get("right_front"))
    var right_rear = Motor(op_mode.hardwareMap!!.dcMotor!!.get("right_rear"))
    var left_front = Motor(op_mode.hardwareMap!!.dcMotor!!.get("left_front"))
    var left_rear = Motor(op_mode.hardwareMap!!.dcMotor!!.get("left_rear"))
    var motor_up1 = Motor(op_mode.hardwareMap!!.dcMotor!!.get("motor_up1"))
    var motor_up2 = Motor(op_mode.hardwareMap!!.dcMotor!!.get("motor_up2"))

    var servo_take = Servo(op_mode.hardwareMap!!.servo!!.get("servo_take"))


    init {
        right_front.direction = DcMotorSimple.Direction.REVERSE
        right_rear.direction = DcMotorSimple.Direction.REVERSE

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
    fun expand(tick: Double, power: Double = DEFAULT_MOTOR_POWER) {
        if (tick > 5300 || motor_up1.currentPosition > 5300) {
            return
        }

        motor_up1.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        motor_up1.targetPosition = (tick + sign(power)).toInt()
        motor_up1.power = power
        motor_up1.mode = DcMotor.RunMode.RUN_TO_POSITION

        while (motor_up1.isBusy && motor_up1.power > .05 && op_mode.opModeIsActive()) {}

        motor_up1.power = 0.0
        motor_up1.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    fun strafe(cm: Double, power: Double = DEFAULT_MOTOR_POWER) {
        set_mode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)

        //val target = -cm / (WHEEL_RADIUS * sqrt(2.0)) * TICK_PER_REV
        //set_target_position(doubleArrayOf(target, -target, target, -target))
        set_powers(doubleArrayOf(power, power, -power, -power))
        set_mode(DcMotor.RunMode.RUN_TO_POSITION)

        while (is_busy() && op_mode.opModeIsActive()) {}

        set_powers(0.0)
        set_mode(DcMotor.RunMode.RUN_USING_ENCODER)
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
}
