package org.firstinspires.ftc.teamcode.lib

import com.acmerobotics.dashboard.FtcDashboard

class Dashmetry {
  fun addData(caption: String?, value: Double) {
    FtcDashboard.getInstance().telemetry.addData(caption, value)
  }
  
  fun addData(caption: String?, value: Int) {
    FtcDashboard.getInstance().telemetry.addData(caption, value)
  }
  
  fun addData(caption: String?, value: Boolean) {
    FtcDashboard.getInstance().telemetry.addData(caption, value)
  }
  
  fun addLine(caption: String?) {
    FtcDashboard.getInstance().telemetry.addLine(caption)
  }
  
  fun update() {
    FtcDashboard.getInstance().telemetry.update()
  }
}