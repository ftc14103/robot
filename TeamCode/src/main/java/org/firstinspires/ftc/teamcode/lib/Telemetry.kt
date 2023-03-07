package org.firstinspires.ftc.teamcode.lib

import com.acmerobotics.dashboard.FtcDashboard
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.Telemetry

class Telemetry(opMode: LinearOpMode) {
private val db: Telemetry? = FtcDashboard.getInstance().telemetry
  private val tm: Telemetry? = opMode.telemetry
  fun addDataDB(caption: String, value: Double) {
    db!!.addData(caption, value)
  }
  
  fun addDataDB(caption: String?, value: Int) {
    db!!.addData(caption, value)
  }
  
  fun addDataDB(caption: String, value: Boolean) {
    db!!.addData(caption, value)
  }
  
  fun addLineDB(caption: String?) {
    db!!.addLine(caption)
  }
  
  fun updateDB() {
    db!!.update()
  }
  fun addDataTM(caption: String?, value: Double) {
    tm!!.addData(caption, value)
  }
  
  fun addDataTM(caption: String?, value: Int) {
    tm!!.addData(caption, value)
  }
  
  fun addDataTM(caption: String?, value: Boolean) {
    tm!!.addData(caption, value)
  }
  
  fun addLineTM(caption: String?) {
    tm!!.addLine(caption)
  }
  
  fun updateTM() {
    tm!!.update()
  }
}