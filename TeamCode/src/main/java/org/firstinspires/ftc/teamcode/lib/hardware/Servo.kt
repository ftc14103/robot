package org.firstinspires.ftc.teamcode.lib.hardware

import com.qualcomm.robotcore.hardware.Servo.Direction
import com.qualcomm.robotcore.hardware.ServoImpl

data class Servo(val servo: com.qualcomm.robotcore.hardware.Servo,
                 val dir: Direction? = null):
        ServoImpl(servo.controller, servo.portNumber, dir ?: servo.direction)