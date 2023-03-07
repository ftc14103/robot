package org.firstinspires.ftc.teamcode.lib

import android.os.Build
import androidx.annotation.RequiresApi
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.Telemetry
import kotlin.math.abs

class PIDRegulator(private val parameters: PIDParameters, private val opMode:LinearOpMode) {
  private var dm: Telemetry  = opMode.telemetry
  
  @RequiresApi(api = Build.VERSION_CODES.N)
    /**
     * Класс PID-регулятора
     * setValue - заданное значение
     * actualValue - реальная величина
     * kP, kI, kD - коэффициенты PID
     * d0 - ошибка
     * d1 - ошибка спустя время обновления показаний датчиков
     * dDerivative - производная ошибки
     * dIntegral - интеграл ошибки
     * u - управляющее воздействие
     * calcTime - время обновления показаний, подсчёта интеграла, производной
     * setTime - время установки на курс
     * deltaTime - время обновления показаний
     */
  fun set(setValue: Double) {
    var d0 = setValue - parameters.actualValue!!.get().toDouble()
    var d1: Double
    val calcTime = ElapsedTime()
    val setTime = ElapsedTime()
    var dIntegral = 0.0
    var dDerivative: Double
    var u: Double
    setTime.reset()
    calcTime.reset()
    while (opMode.opModeIsActive() && setTime.seconds() < parameters.maxSettingTime &&
        (abs(d0) > parameters.valueTolerance ||
        abs(parameters.velocityValue!!.get().toDouble()) > parameters.velocityTolerance)) {
      d1 = setValue - parameters.actualValue!!.get().toDouble()
      dDerivative = (d1 - d0) / (calcTime.nanoseconds() * 10e-9)
      if (d1 < parameters.integralDelta) {
        dIntegral += d1 * calcTime.nanoseconds() * 10e-9
      }
      u = parameters.kP * d1 + parameters.kI * dIntegral + parameters.kD * dDerivative
      parameters.setControlAction.accept(u)
      d0 = setValue - parameters.actualValue!!.get().toDouble()
      if (parameters.showDashmetry) {
        dm.addData("SettingTime", setTime.seconds())
        dm.addData("deltaTime", calcTime.nanoseconds() * 10e-9)
        dm.addLine("")
        dm.addData("SetValue", setValue)
        dm.addData("Value", parameters.actualValue!!.get().toDouble())
        dm.addData("D0", d0)
        dm.addData("D1", d1)
        dm.addLine("")
        dm.addData("dDerivative", dDerivative)
        dm.addData("dIntegral", dIntegral)
        dm.addLine("")
        dm.addData("U", u)
        dm.addData("uP", parameters.kP * d1)
        dm.addData("uI", parameters.kI * dIntegral)
        dm.addData("uD", parameters.kD * dDerivative)
        dm.update()
      }
      calcTime.reset()
    }
    parameters.setControlAction.accept(0.0)
    setTime.reset()
  }
  
}