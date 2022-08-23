package org.firstinspires.ftc.teamcode.lib.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorImplEx

data class Motor(val motor: DcMotor): DcMotorImplEx(motor.controller, motor.portNumber)
