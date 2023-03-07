package org.firstinspires.ftc.teamcode.lib

import java.util.function.Consumer
import java.util.function.Supplier

/** Класс параметра PID регулятора
 * Нужна внимательность к особенностям различных Consumer. Нужно подавать именно то, что они просят.
 */
class PIDParameters(_kP: Double, _kI: Double, _kD: Double,
                    _valueTolerance: Double, _velocityTolerance: Double,
                    _maxSettingTime: Double, _integralDelta: Double,
                    _setControlAction: Consumer<Double>,
                    _actualValue: Supplier<Number>, _velocityValue: Supplier<Number>,
                    _showDashmetry: Boolean) : Cloneable {
  var kP = _kP
  var kI = _kI
  var kD = _kD
  var valueTolerance = _valueTolerance
  var velocityTolerance = _velocityTolerance
  var maxSettingTime = _maxSettingTime
  var integralDelta = _integralDelta
  var setControlAction: Consumer<Double> = _setControlAction
  var actualValue: Supplier<Number>? = _actualValue
  var velocityValue: Supplier<Number>? = _velocityValue
  var showDashmetry = _showDashmetry
}

