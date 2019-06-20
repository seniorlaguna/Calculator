package org.seniorlaguna.calculator.scientific

import android.util.Log
import com.udojava.evalex.AbstractFunction
import com.udojava.evalex.Expression
import java.lang.ArithmeticException
import java.math.BigDecimal

private val DECIMAL_PLACES  = 4

// trigonomeetrical functions

val custom_sin = object : AbstractFunction("custom_sin", 2) {

    override fun eval(parameters: MutableList<BigDecimal>?): BigDecimal {

        if (parameters == null || parameters.size != 2) throw ArithmeticException()

        val rad = parameters[0].intValueExact() < 0
        val number = parameters[1].toPlainString()

        val e : Expression
        if (!rad) {
            e = Expression("sin($number)")
        }
        else {
            e = Expression("sin(deg($number))")
        }

        return e.eval().setScale(2, BigDecimal.ROUND_DOWN)
    }
}

val custom_cos = object : AbstractFunction("custom_cos", 2) {

    override fun eval(parameters: MutableList<BigDecimal>?): BigDecimal {

        if (parameters == null || parameters.size != 2) throw ArithmeticException()

        val rad = parameters[0].intValueExact() < 0
        val number = parameters[1].toPlainString()

        val e : Expression
        if (!rad) {
            e = Expression("cos($number)")
        }
        else {
            e = Expression("cos(deg($number))")
        }

        return e.eval().setScale(2, BigDecimal.ROUND_DOWN)
    }
}

val custom_tan = object : AbstractFunction("custom_tan", 2) {

    override fun eval(parameters: MutableList<BigDecimal>?): BigDecimal {

        if (parameters == null || parameters.size != 2) throw ArithmeticException()

        val rad = parameters[0].intValueExact() < 0
        val number = parameters[1].toPlainString()

        val e : Expression
        if (!rad) {
            e = Expression("tan($number)")
        }
        else {
            e = Expression("tan(deg($number))")
        }

        return e.eval().setScale(2, BigDecimal.ROUND_DOWN)
    }
}

val custom_cot = object : AbstractFunction("custom_cot", 2) {

    override fun eval(parameters: MutableList<BigDecimal>?): BigDecimal {

        if (parameters == null || parameters.size != 2) throw ArithmeticException()

        val rad = parameters[0].intValueExact() < 0
        val number = parameters[1].toPlainString()

        val e : Expression
        if (!rad) {
            e = Expression("cot($number)")
        }
        else {
            e = Expression("cot(deg($number))")
        }

        return e.eval().setScale(2, BigDecimal.ROUND_DOWN)
    }
}

// trigonometrical functions reversed

val custom_asin = object : AbstractFunction("custom_asin", 2) {

    override fun eval(parameters: MutableList<BigDecimal>?): BigDecimal {

        if (parameters == null || parameters.size != 2) throw ArithmeticException()

        val rad = parameters[0].intValueExact() < 0
        val number = parameters[1].toPlainString()

        val e : Expression
        if (!rad) {
            e = Expression("asin($number)")
        }
        else {
            e = Expression("asin(deg($number))")
        }

        return e.eval().setScale(2, BigDecimal.ROUND_DOWN)
    }
}

val custom_acos = object : AbstractFunction("custom_acos", 2) {

    override fun eval(parameters: MutableList<BigDecimal>?): BigDecimal {

        if (parameters == null || parameters.size != 2) throw ArithmeticException()

        val rad = parameters[0].intValueExact() < 0
        val number = parameters[1].toPlainString()

        val e : Expression
        if (!rad) {
            e = Expression("acos($number)")
        }
        else {
            e = Expression("acos(deg($number))")
        }

        return e.eval().setScale(2, BigDecimal.ROUND_DOWN)
    }
}

val custom_atan = object : AbstractFunction("custom_atan", 2) {

    override fun eval(parameters: MutableList<BigDecimal>?): BigDecimal {

        if (parameters == null || parameters.size != 2) throw ArithmeticException()

        val rad = parameters[0].intValueExact() < 0
        val number = parameters[1].toPlainString()

        val e : Expression
        if (!rad) {
            e = Expression("atan($number)")
        }
        else {
            e = Expression("atan(deg($number))")
        }

        return e.eval().setScale(2, BigDecimal.ROUND_DOWN)
    }
}

val custom_acot = object : AbstractFunction("custom_acot", 2) {

    override fun eval(parameters: MutableList<BigDecimal>?): BigDecimal {

        if (parameters == null || parameters.size != 2) throw ArithmeticException()

        val rad = parameters[0].intValueExact() < 0
        val number = parameters[1].toPlainString()

        val e : Expression
        if (!rad) {
            e = Expression("acot($number)")
        }
        else {
            e = Expression("acot(deg($number))")
        }

        return e.eval().setScale(2, BigDecimal.ROUND_DOWN)
    }
}

// trigonomeetrical functions hyperbolic

val custom_sinh = object : AbstractFunction("custom_sinh", 2) {

    override fun eval(parameters: MutableList<BigDecimal>?): BigDecimal {

        if (parameters == null || parameters.size != 2) throw ArithmeticException()

        val rad = parameters[0].intValueExact() < 0
        val number = parameters[1].toPlainString()

        val e : Expression
        if (!rad) {
            e = Expression("sinh($number)")
        }
        else {
            e = Expression("sinh(deg($number))")
        }

        return e.eval().setScale(2, BigDecimal.ROUND_DOWN)
    }
}

val custom_cosh = object : AbstractFunction("custom_cosh", 2) {

    override fun eval(parameters: MutableList<BigDecimal>?): BigDecimal {

        if (parameters == null || parameters.size != 2) throw ArithmeticException()

        val rad = parameters[0].intValueExact() < 0
        val number = parameters[1].toPlainString()

        val e : Expression
        if (!rad) {
            e = Expression("cosh($number)")
        }
        else {
            e = Expression("cosh(deg($number))")
        }

        return e.eval().setScale(2, BigDecimal.ROUND_DOWN)
    }
}

val custom_tanh = object : AbstractFunction("custom_tanh", 2) {

    override fun eval(parameters: MutableList<BigDecimal>?): BigDecimal {

        if (parameters == null || parameters.size != 2) throw ArithmeticException()

        val rad = parameters[0].intValueExact() < 0
        val number = parameters[1].toPlainString()

        val e : Expression
        if (!rad) {
            e = Expression("tanh($number)")
        }
        else {
            e = Expression("tanh(deg($number))")
        }

        return e.eval().setScale(2, BigDecimal.ROUND_DOWN)
    }
}

val custom_coth = object : AbstractFunction("custom_coth", 2) {

    override fun eval(parameters: MutableList<BigDecimal>?): BigDecimal {

        if (parameters == null || parameters.size != 2) throw ArithmeticException()

        val rad = parameters[0].intValueExact() < 0
        val number = parameters[1].toPlainString()

        val e : Expression
        if (!rad) {
            e = Expression("coth($number)")
        }
        else {
            e = Expression("coth(deg($number))")
        }

        return e.eval().setScale(2, BigDecimal.ROUND_DOWN)
    }
}


// trigonomeetrical functions hyperbolic inversed

val custom_asinh = object : AbstractFunction("custom_asinh", 2) {

    override fun eval(parameters: MutableList<BigDecimal>?): BigDecimal {

        if (parameters == null || parameters.size != 2) throw ArithmeticException()

        val rad = parameters[0].intValueExact() < 0
        val number = parameters[1].toPlainString()

        val e : Expression
        if (!rad) {
            e = Expression("asinh($number)")
        }
        else {
            e = Expression("asinh(deg($number))")
        }

        return e.eval().setScale(2, BigDecimal.ROUND_DOWN)
    }
}

val custom_acosh = object : AbstractFunction("custom_acosh", 2) {

    override fun eval(parameters: MutableList<BigDecimal>?): BigDecimal {

        if (parameters == null || parameters.size != 2) throw ArithmeticException()

        val rad = parameters[0].intValueExact() < 0
        val number = parameters[1].toPlainString()

        val e : Expression
        if (!rad) {
            e = Expression("acosh($number)")
        }
        else {
            e = Expression("acosh(deg($number))")
        }

        return e.eval().setScale(2, BigDecimal.ROUND_DOWN)
    }
}

val custom_atanh = object : AbstractFunction("custom_atanh", 2) {

    override fun eval(parameters: MutableList<BigDecimal>?): BigDecimal {

        if (parameters == null || parameters.size != 2) throw ArithmeticException()

        val rad = parameters[0].intValueExact() < 0
        val number = parameters[1].toPlainString()

        val e : Expression
        if (!rad) {
            e = Expression("atanh($number)")
        }
        else {
            e = Expression("atanh(deg($number))")
        }

        return e.eval().setScale(2, BigDecimal.ROUND_DOWN)
    }
}

val custom_acoth = object : AbstractFunction("custom_acoth", 2) {

    override fun eval(parameters: MutableList<BigDecimal>?): BigDecimal {

        if (parameters == null || parameters.size != 2) throw ArithmeticException()

        val rad = parameters[0].intValueExact() < 0
        val number = parameters[1].toPlainString()

        val e : Expression
        if (!rad) {
            e = Expression("acoth($number)")
        }
        else {
            e = Expression("acoth(deg($number))")
        }

        return e.eval().setScale(2, BigDecimal.ROUND_DOWN)
    }
}