package org.seniorlaguna.calculator.basic

import com.udojava.evalex.Expression
import org.seniorlaguna.calculator.*
import java.math.BigDecimal

/**
 * The Calculator class evaluates
 * expressions given as a string according
 * to app specific conventions and rules
 * applying user preferences
 */

class Calculator(private val settings : GlobalSettings.BasicCalculatorSettings) {

    fun calculate(expression : String) : String {

        // prepare expression
        var term = expression
        term = translate(term)

        if (term.isBlank() or term.isEmpty()) return ""

        val exp = Expression(term).apply {
            setPrecision(settings.precision)
            addFunction(custom_sin)
            addFunction(custom_asin)
            addFunction(custom_sinh)
            addFunction(custom_asinh)

            addFunction(custom_cos)
            addFunction(custom_acos)
            addFunction(custom_cosh)
            addFunction(custom_acosh)

            addFunction(custom_tan)
            addFunction(custom_atan)
            addFunction(custom_tanh)
            addFunction(custom_atanh)

            addFunction(custom_cot)
            addFunction(custom_acot)
            addFunction(custom_coth)
            addFunction(custom_acoth)
        }


        var result = exp.eval()
        result = result.setScale(
            settings.decimalPlaces,
            if (settings.roundUp) BigDecimal.ROUND_UP else BigDecimal.ROUND_DOWN
        )
        return removeZeros(result.toPlainString())


    }

    private fun translate(expression: String) : String {

        var result = expression

        mapOf(
            "π" to "pi",
            "!(" to "fact(",
            "√(" to "sqrt(",
            "ln(" to "log(",
            "lg(" to "log10("
        ).forEach {
            result = result.replace(it.key, it.value)
        }

        // map trigonometric functions
        arrayOf(
            "sin(",
            "cos(",
            "tan(",
            "cot(",
            "asin(",
            "acos(",
            "atan(",
            "acot(",
            "sinh(",
            "cosh(",
            "tanh(",
            "coth(",
            "asinh(",
            "acosh(",
            "atanh(",
            "acoth("
        ).forEach {
            // term = term.replace(it, "custom_$it${if (mainActivity.scientificViewModel.settings.rad) -1 else 1},")
            result = result.replace(it, "custom_$it${if (true) -1 else 1},")
        }

        return result
    }

    private fun removeZeros(expression: String) : String {

        var result = expression

        while (result.contains(".") and (result.endsWith("0") or result.endsWith("."))) {
            result = result.substring(0, result.lastIndex)
        }

        return result

    }

}



