package org.firstinspires.ftc.teamcode.lib.utils

import kotlin.math.abs

object Utils {
  fun calc_powers(x: Double, y: Double, r: Double, den: Double = (abs(y) + abs(x) + abs(r)).coerceAtLeast(1.0)): DoubleArray = doubleArrayOf(
    (y + x + r) / den,
    (y - x + r) / den,
    (y - x - r) / den,
    (y + x - r) / den
  )
}
