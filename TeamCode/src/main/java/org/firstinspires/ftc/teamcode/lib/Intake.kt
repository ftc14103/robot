package org.firstinspires.ftc.teamcode.lib
import kotlinx.coroutines.*

class Intake {
  enum class State {
    GROUND, LOW, MID, HIGH, NOTHING
  }
  private var _state: State = State.GROUND
  private var _on_ground: () -> State = { State.NOTHING }
  private var _on_low: () -> State = { State.NOTHING }
  private var _on_mid: () -> State = { State.NOTHING }
  private var _on_high: () -> State = { State.NOTHING }
  
  fun on_ground(v: () -> State) { _on_ground = v }
  fun on_low(v: () -> State) { _on_low = v }
  fun on_mid(v: () -> State) { _on_mid = v }
  fun on_high(v: () -> State) { _on_high = v }
  
  suspend fun step() {
    when (_state) {
      State.GROUND -> _state = _on_ground()
      State.LOW -> _state = _on_low()
      State.MID -> _state = _on_mid()
      State.HIGH -> _state = _on_high()
      State.NOTHING -> {}
    }
  }
}