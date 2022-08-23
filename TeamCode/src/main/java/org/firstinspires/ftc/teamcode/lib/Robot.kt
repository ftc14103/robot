package org.firstinspires.ftc.teamcode.lib

import android.os.SystemClock.sleep
import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.robotcore.external.navigation.Orientation
import org.firstinspires.ftc.teamcode.lib.hardware.Motor
import org.firstinspires.ftc.teamcode.lib.utils.Utils
import kotlin.math.*

class Robot(val op_mode: OpMode) {
    private lateinit var left_front: Motor
    private lateinit var left_rear: Motor
    private lateinit var right_front: Motor
    private lateinit var right_rear: Motor
    private lateinit var imu: BNO055IMU

    private var angle: Double = 0.0
    private var last_angle: Orientation = Orientation()

    //region:Config
    val DEFAULT_MOTOR_POWER = .5
    val TICK_PER_REV = 1120
    val MAX_RPM = 160
    val WHEEL_RADIUS = .2
    val GEAR_RATIO = 2.0
    val TRACK_WIDTH = 10.87

    val MAX_VEL = 10.658975074679656
    val MAX_ACCEL = 14.241886696273728
    val MAX_ANG_VEL = Math.toRadians(75.06899724011039)
    val MAX_ANG_ACCEL = Math.toRadians(75.06899724011039)
    //endregion

    init {
        left_front = Motor(op_mode.hardwareMap.dcMotor.get("left_front"))
        left_rear = Motor(op_mode.hardwareMap.dcMotor.get("left_rear"))
        right_front = Motor(op_mode.hardwareMap.dcMotor.get("right_front"))
        right_rear = Motor(op_mode.hardwareMap.dcMotor.get("right_read"))

        right_front.direction = DcMotorSimple.Direction.REVERSE
        right_rear.direction = DcMotorSimple.Direction.REVERSE

        imu = op_mode.hardwareMap.get(BNO055IMU::class.java, "imu")
        var params = BNO055IMU.Parameters()
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
        val busy = left_front.isBusy()  &&
                   left_rear.isBusy()   &&
                   right_front.isBusy() &&
                   right_rear.isBusy()

        val pows = (abs(left_front.power)  +
                    abs(left_rear.power)   +
                    abs(right_front.power) +
                    abs(right_rear.power)) > .05

        return busy && pows
    }

    fun set_target_position(target: Double, pows: DoubleArray) {
        left_front.targetPosition = (target * sign(pows[0])).toInt()
        left_rear.targetPosition = (target * sign(pows[1])).toInt()
        right_front.targetPosition = (target * sign(pows[2])).toInt()
        right_rear.targetPosition = (target * sign(pows[3])).toInt()
    }

    fun set_target_position(target: DoubleArray) {
        left_front.power = target[0]
        left_rear.power = target[1]
        right_front.power = target[2]
        right_rear.power = target[3]
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

    fun move(cm: Double, power: Double = DEFAULT_MOTOR_POWER) {
        set_mode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)

        val target = -(cm * .3937) / (WHEEL_RADIUS * sqrt(2.0)) * TICK_PER_REV
        set_target_position(target)
        set_powers(doubleArrayOf(-power, -power, power, power))
        set_mode(DcMotor.RunMode.RUN_TO_POSITION)

        while (is_busy()) {}

        set_powers(0.0)
        set_mode(DcMotor.RunMode.RUN_USING_ENCODER)
    }

    fun strafe(cm: Double, power: Double = DEFAULT_MOTOR_POWER) {
        set_mode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)

        val target = -cm / (WHEEL_RADIUS * sqrt(2.0)) * TICK_PER_REV
        set_target_position(doubleArrayOf(target, -target, target, -target))
        set_powers(doubleArrayOf(power, power, -power, -power))
        set_mode(DcMotor.RunMode.RUN_TO_POSITION)

        while (is_busy()) {}

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

        if (degrees > 0) {
            // TODO: need tests
            while (actual_angle() < degrees) {
                set_powers(doubleArrayOf(power, power, -power, -power))
            }
        } else {
            while (actual_angle() > degrees) {
                set_powers(doubleArrayOf(-power, -power, power, power))
            }
        }

        set_powers(0.0)

        sleep(.25.toLong())

        reset_angle()
    }
}