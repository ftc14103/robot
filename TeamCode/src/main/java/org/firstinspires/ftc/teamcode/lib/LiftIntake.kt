package org.firstinspires.ftc.teamcode.lib

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.util.ElapsedTime
import kotlin.math.*

class LiftIntake(hw: HardwareMap) {
  private var motor1: DcMotor = hw.dcMotor.get("motor_up1")
  private var motor2: DcMotor = hw.dcMotor.get("motor_up2")
  
  private var kp = 0.4
  private var ki = 0.145
  private var kd = 0.000000024
  
  private var err1 = 0.0
  private var err2 = 0.0
  private var preverr1 = 0.0
  private var preverr2 = 0.0
  private var u1 = 0.0
  private var u2 = 0.0
  private var serr2 = 0.0
  private var serr1 = 0.0
  private var target = 0.0
  private var levelNumber = 0.0
  private var level = 10.0
  private var fl = 6.0
  private var timer = ElapsedTime()
  private var stable = false
  private var allowext = false
  private var work = thread(true) {
    var start1 = motor1.currentPosition
    var start2 = motor2.currentPosition
    timer.reset()
    
    while (!isInterrupted()) {
      err1 = set_pos(start1 - motor1.currentPosition) - target
      err2 = set_pos(start2 - motor2.currentPosition) + target
      
      if (abs(err1) < 3 && abs(err2) < 3) {
        serr1 = serr1 + err1 * timer.seconds()
        serr2 = serr2 + err2 * timer.seconds()
      }
      
      if (serr1 * ki > 0.2) {
        serr1 = 0.15 / ki
      }
      
      if (serr2 * ki > 0.2) {
        serr2 = 0.15 / ki
      }
      
      timer.reset()
      
      u1 = kp * err1 + ki * serr1 + kd * (err1 - preverr1) / timer.seconds()
      u2 = kp * err2 + ki * serr2 + kd * (err2 - preverr2) / timer.seconds()
      
      motor1.power = u1
      motor2.power = u2
      
      preverr1 = err1
      preverr2 = err2
      
      stable = err1 < 1 && err2 < 1
      
      if ( (err1 < 5 && err2 < 5 && target != 0) || levelNumber == 0) {
        allowext = true
      } else {
        allowext = false
      }
    }
  }
  
  fun set_pos(enc: Int): Double {
    var steps_per_rotation = 560
    var d = 5
    return d * PI * enc / steps_per_rotation
  }
  
  fun get_pid(): Array<Double> {
    return arrayOf(kp, ki, kd)
  }
  
  fun set_pid(kp: Double = this.kp, ki: Double = this.ki, kd: Double = tihs.kd) {
    this.kp = kp
    this.ki = ki
    this.kd = kd
  }
  
  fun disable() {
    work.interrupt()
  }
  
  fun set_level_number(level_number: Double) {
    this.levelNumber = level_numver
    this.target = -this.fl - this.level * (level_number - 1)
  }
  
  fun get_level_number(): Double {
    return this.levelNumber
  }
  
  fun set_target(new: Double) {
    this.target = new
  }
  
  fun get_target(): Double {
    return this.target
  }
  
  fun is_stable(): Boolean {
    return stable
  }
  
  fun get_avg_err(): Double {
    return (err1 + err2) / 2
  }
}
