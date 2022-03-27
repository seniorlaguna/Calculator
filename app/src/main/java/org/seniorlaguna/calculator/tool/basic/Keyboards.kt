package org.seniorlaguna.calculator.tool.basic

import org.seniorlaguna.calculator.R

class KeyboardLayout(val rows : Int, val column : Int, val portraitLayout : ArrayList<ArrayList<out ArrayList<out Any?>>>)

// Basic keyboard portrait
val keyboard1 = KeyboardLayout(5,5,
    arrayListOf(
    arrayListOf(arrayListOf(R.string.math_pi, "π"), arrayListOf(R.string.math_bracket_open, "("), arrayListOf(R.string.math_bracket_close, ")"), arrayListOf(R.string.math_clear_one, null), arrayListOf(R.string.math_clear, null)),
    arrayListOf(arrayListOf(R.string.math_7, "7"), arrayListOf(R.string.math_8, "8"), arrayListOf(R.string.math_9, "9"), arrayListOf(R.string.math_plus, "+"), arrayListOf(R.string.math_minus, "-")),
    arrayListOf(arrayListOf(R.string.math_4, "4"), arrayListOf(R.string.math_5, "5"), arrayListOf(R.string.math_6, "6"), arrayListOf(R.string.math_times, "*"), arrayListOf(R.string.math_divide, "/")),
    arrayListOf(arrayListOf(R.string.math_1, "1"), arrayListOf(R.string.math_2, "2"), arrayListOf(R.string.math_3, "3"), arrayListOf(R.string.math_power, "^"), arrayListOf(R.string.math_square_root, "√(")),
    arrayListOf(arrayListOf(R.string.math_percentage, "%"), arrayListOf(R.string.math_0, "0"), arrayListOf(R.string.math_dot, "."), arrayListOf(R.string.math_factorial, "!("), arrayListOf(R.string.math_equals, null))
))

// Scientific keyboard 1
val keyboard2 = KeyboardLayout(5,5, arrayListOf(
    arrayListOf(arrayListOf(R.string.math_pi, "π"), arrayListOf(R.string.math_bracket_open, "("), arrayListOf(R.string.math_bracket_close, ")"), arrayListOf(R.string.math_clear_one, null), arrayListOf(R.string.math_clear, null)),
    arrayListOf(arrayListOf(R.string.math_7, "7"), arrayListOf(R.string.math_8, "8"), arrayListOf(R.string.math_9, "9"), arrayListOf(R.string.math_plus, "+"), arrayListOf(R.string.math_minus, "-")),
    arrayListOf(arrayListOf(R.string.math_4, "4"), arrayListOf(R.string.math_5, "5"), arrayListOf(R.string.math_6, "6"), arrayListOf(R.string.math_times, "*"), arrayListOf(R.string.math_divide, "/")),
    arrayListOf(arrayListOf(R.string.math_1, "1"), arrayListOf(R.string.math_2, "2"), arrayListOf(R.string.math_3, "3"), arrayListOf(R.string.math_power, "^"), arrayListOf(R.string.math_square_root, "√(")),
    arrayListOf(arrayListOf(R.string.math_switch_page, null), arrayListOf(R.string.math_0, "0"), arrayListOf(R.string.math_dot, "."), arrayListOf(R.string.math_factorial, "!("), arrayListOf(R.string.math_equals, null))
))

// Scientific keyboard 2
val keyboard3 = KeyboardLayout(5, 5,
    arrayListOf(
    arrayListOf(arrayListOf(R.string.math_e, "e"), arrayListOf(R.string.math_bracket_open, "("), arrayListOf(R.string.math_bracket_close, ")"), arrayListOf(R.string.math_clear_one, null), arrayListOf(R.string.math_clear, null)),
    arrayListOf(arrayListOf(R.string.math_sin, "sin("), arrayListOf(R.string.math_cos, "cos("), arrayListOf(R.string.math_tan, "tan("), arrayListOf(R.string.math_cot, "cot("), arrayListOf(R.string.math_lg, "lg(")),
    arrayListOf(arrayListOf(R.string.math_asin, "asin("), arrayListOf(R.string.math_acos, "acos("), arrayListOf(R.string.math_atan, "atan("), arrayListOf(R.string.math_acot, "acot("), arrayListOf(R.string.math_ln, "ln(")),
    arrayListOf(arrayListOf(R.string.math_sinh, "sinh("), arrayListOf(R.string.math_cosh, "cosh("), arrayListOf(R.string.math_tanh, "tanh("), arrayListOf(R.string.math_coth, "coth("), arrayListOf(R.string.math_abs, "abs(")),
    arrayListOf(arrayListOf(R.string.math_asinh, "asinh("), arrayListOf(R.string.math_acosh, "acosh("), arrayListOf(R.string.math_atanh, "atanh("), arrayListOf(R.string.math_acoth, "acoth("), arrayListOf(R.string.math_switch_page, null))
))