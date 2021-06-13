package org.seniorlaguna.calculator.apc

import android.util.Log
import ch.obermuhlner.math.big.BigDecimalMath
import org.seniorlaguna.calculator.settings.GlobalSettings
import java.io.StringReader
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class Calculator(private val settings: GlobalSettings) {

    private val rad2degFactor = BigDecimalMath.pi(MathContext(4096)) / BigDecimal(180)
    private lateinit var parser : APCParser
    private lateinit var mathContext: MathContext

    fun eval(expression: String) : String {

        mathContext = MathContext(
            settings.precision,
            if (settings.roundUp) RoundingMode.UP else RoundingMode.DOWN
        )

        parser = APCParser(this, StringReader(expression))
        parser.yyparse()
        Log.d("Calculator", "Calculator result: ${parser.result.toPlainString()}")
        return parser.result.setScale(
            settings.decimalPlaces,
            if (settings.roundUp) RoundingMode.UP else RoundingMode.DOWN
        ).stripTrailingZeros().toPlainString()
    }

    fun resolveConstant(name : String) : BigDecimal {
        Log.d("Calculator", "resolve constant: $name")
        return when(name) {
            "π" -> BigDecimalMath.pi(mathContext)
            "e" -> BigDecimalMath.e(mathContext)
            else -> throw IllegalArgumentException("invalid constant: $name")
        }
    }

    fun callFunction(functionName: String, number: BigDecimal) : BigDecimal {

        Log.d("Calculator", "called function $functionName")
        return when(functionName) {
            "√" -> BigDecimalMath.sqrt(number, mathContext)

            "sin" -> BigDecimalMath.sin(if (settings.rad) number else number * rad2degFactor, mathContext)
            "cos" -> BigDecimalMath.cos(if (settings.rad) number else number * rad2degFactor, mathContext)
            "tan" -> BigDecimalMath.tan(if (settings.rad) number else number * rad2degFactor, mathContext)
            "cot" -> BigDecimalMath.cot(if (settings.rad) number else number * rad2degFactor, mathContext)

            "asin" -> BigDecimalMath.asin(if (settings.rad) number else number * rad2degFactor, mathContext)
            "acos" -> BigDecimalMath.acos(if (settings.rad) number else number * rad2degFactor, mathContext)
            "atan" -> BigDecimalMath.atan(if (settings.rad) number else number * rad2degFactor, mathContext)
            "acot" -> BigDecimalMath.acot(if (settings.rad) number else number * rad2degFactor, mathContext)

            "sinh" -> BigDecimalMath.sinh(if (settings.rad) number else number * rad2degFactor, mathContext)
            "cosh" -> BigDecimalMath.cosh(if (settings.rad) number else number * rad2degFactor, mathContext)
            "tanh" -> BigDecimalMath.tanh(if (settings.rad) number else number * rad2degFactor, mathContext)
            "coth" -> BigDecimalMath.coth(if (settings.rad) number else number * rad2degFactor, mathContext)

            "asinh" -> BigDecimalMath.asinh(if (settings.rad) number else number * rad2degFactor, mathContext)
            "acosh" -> BigDecimalMath.acosh(if (settings.rad) number else number * rad2degFactor, mathContext)
            "atanh" -> BigDecimalMath.atanh(if (settings.rad) number else number * rad2degFactor, mathContext)
            "acoth" -> BigDecimalMath.acoth(if (settings.rad) number else number * rad2degFactor, mathContext)

            "lg" -> BigDecimalMath.log10(number, mathContext)
            "ln" -> BigDecimalMath.log(number, mathContext)

            "abs" -> number.abs(mathContext)
            "!" -> BigDecimalMath.factorial(number, mathContext)

            else -> {
                Log.d("Calculator", "Illegal function: $functionName")
                throw IllegalArgumentException("Function $functionName not known")
            }
        }
    }

    fun toBigDecimal(number: String) : BigDecimal {
        return BigDecimalMath.toBigDecimal(number)
    }

    fun add(a: BigDecimal, b: BigDecimal) = a.add(b, mathContext)

    fun subtract(a: BigDecimal, b: BigDecimal) = a.subtract(b, mathContext)

    fun multiply(a: BigDecimal, b: BigDecimal) = a.multiply(b, mathContext)

    fun divide(a: BigDecimal, b: BigDecimal) = a.divide(b, mathContext)

    fun pow(v1: BigDecimal, v3: BigDecimal) : BigDecimal {
        val signOf2: Int = v3.signum()
        val dn1: Double = v1.toDouble()
        val v2 = v3.multiply(BigDecimal(signOf2)) // n2 is now positive

        val remainderOf2: BigDecimal = v2.remainder(BigDecimal.ONE)
        val n2IntPart: BigDecimal = v2.subtract(remainderOf2)
        val intPow: BigDecimal = v1.pow(n2IntPart.intValueExact(), mathContext)
        val doublePow = BigDecimal.valueOf(Math.pow(dn1, remainderOf2.toDouble()))

        var result = intPow.multiply(doublePow, mathContext)
        if (signOf2 == -1) {
            result = BigDecimal.ONE.divide(result, mathContext.getPrecision(), RoundingMode.HALF_UP)
        }
        return result
    }

    fun toPercentage(a : BigDecimal, b : BigDecimal?) : BigDecimal {
        if (b == null) {
            return a.divide(BigDecimal.valueOf(100))
        }

        return b.divide(BigDecimal.valueOf(100)).multiply(a)
    }
}