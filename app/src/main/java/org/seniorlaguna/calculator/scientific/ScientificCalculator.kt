package org.seniorlaguna.calculator.scientific

import android.text.Editable
import android.view.View
import com.udojava.evalex.Expression
import kotlinx.android.synthetic.main.fragment_scientific.*
import kotlinx.android.synthetic.main.fragment_scientific_display.*
import org.seniorlaguna.calculator.MainActivity
import org.seniorlaguna.calculator.R
import org.seniorlaguna.calculator.customviews.ExtendedViewPager
import org.seniorlaguna.calculator.scientific.db.Calculation
import java.math.BigDecimal
import kotlin.math.max

class ScientificCalculator(
    private val mainActivity: MainActivity,
    private val scientificFragment: ScientificFragment,
    private val displayFragment: DisplayFragment,
    private val historyFragment: HistoryFragment
) {

    private var currentCalculation: Editable
        get() = displayFragment.display.text
        set(value) {
            displayFragment.display.text = value
        }

    private var currentKeyboardPage = 1
    private var currentCursorPosition = 0
        get() = displayFragment.display.selectionStart

    private var equalPressed = false

    private fun onActionClick(stringResourceId: Int) {

        when (stringResourceId) {

            R.string.math_clear_one -> clearOne()
            R.string.math_clear -> clearAll()
            R.string.math_switch_page -> switchKeyboard()
            R.string.math_equals -> {
                equalPressed = true
                showResult()
            }

        }

    }

    private fun clearOne() = currentCalculation.replace(max(currentCursorPosition - 1, 0), currentCursorPosition, "")

    private fun clearAll() = currentCalculation.clear()


    private fun switchKeyboard() {
        scientificFragment.setKeyboard(if (currentKeyboardPage == 1) keyboard2 else keyboard1)
        currentKeyboardPage = if (currentKeyboardPage == 1) 2 else 1
    }

    private fun showResult() {

        // save the calculation in history
        saveInHistory()

        val result = calc()
        displayFragment.display_result.text = ""
        currentCalculation.clear()
        currentCalculation.insert(0, result)

        // save the result in history
        if (mainActivity.scientificViewModel.settings.historySaveResults) saveInHistory()
    }

    private fun saveInHistory() {
        if ((currentCalculation.toString() == mainActivity.getString(R.string.scientific_calculator_error)) or
                currentCalculation.toString().isBlank() or
                currentCalculation.toString().isEmpty()) return
        mainActivity.scientificViewModel.addCalculation(Calculation(0, "", currentCalculation.toString()))
    }


    private fun calc(): String {

        val term = translate()

        if (term.isBlank() or term.isEmpty()) return ""

        val expression = Expression(term).apply {
            setPrecision(mainActivity.scientificViewModel.settings.precision)
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

        try {
            var result = expression.eval()
            result = result.setScale(
                mainActivity.scientificViewModel.settings.decimalPlaces,
                if (mainActivity.scientificViewModel.settings.roundUp) BigDecimal.ROUND_UP else BigDecimal.ROUND_DOWN
            )
            return removeZeros(result.toPlainString())

        } catch (e: ArithmeticException) {
            return mainActivity.getString(R.string.scientific_calculator_error)
        } catch (e: Expression.ExpressionException) {
            return mainActivity.getString(R.string.scientific_calculator_error)
        }

    }

    private fun removeZeros(number: String): String {

        var result = number

        while (result.contains(".") and (result.endsWith("0") or result.endsWith("."))) {
            result = result.substring(0, result.lastIndex)
        }

        return result
    }

    private fun translate() : String {
        var term = currentCalculation.toString()

        // map scientific functions

        mapOf(
            "π" to "pi",
            "!(" to "fact(",
            "√(" to "sqrt(",
            "ln(" to "log(",
            "lg(" to "log10("
        ).forEach {
            term = term.replace(it.key, it.value)
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
            term = term.replace(it, "custom_$it${if (mainActivity.scientificViewModel.settings.rad) -1 else 1},")
        }

        return term
    }

    // public interface
    fun showCurrentResult() {

        try {
            // set visibility
            displayFragment.display_result.visibility =
                if (mainActivity.scientificViewModel.settings.instantResult) View.VISIBLE else View.INVISIBLE

            // return if not wanted
            if (!mainActivity.scientificViewModel.settings.instantResult) return

            val result = calc()

            displayFragment.display_result.text =
                if ((result == mainActivity.getString(R.string.scientific_calculator_error)) or (result == currentCalculation.toString())) "" else result
        }
        // just happens on app start and can't be fixed
        catch (e: Exception) {
        }


    }

    fun onClick(stringResourceId: Int, stringToInsert: String?) {

        // in history
        if (mainActivity.scientificFragment.fragment_viewpager.currentItem == ExtendedViewPager.HISTORY_TAB) {
            if (mainActivity.scientificViewModel.settings.historySwitchOnInput) mainActivity.scientificFragment.fragment_viewpager.currentItem =
                ExtendedViewPager.DISPLAY_TAB
            if (mainActivity.scientificViewModel.settings.historyDisableInput) return
        }

        // check for actions to do
        if (stringToInsert == null) {
            onActionClick(stringResourceId)
        }

        // insert symbol
        else {

            // delete calculation
            if (mainActivity.scientificViewModel.settings.autoDelete and equalPressed) {
                clearAll()
                equalPressed = false
            }

            insert(stringToInsert)
        }

        displayFragment.display.setSelection(currentCursorPosition)
    }

    fun insert(calculation: String, clear: Boolean = false) {

        // clear if wanted
        if (clear or (currentCalculation.toString() == mainActivity.getString(R.string.scientific_calculator_error))) currentCalculation.clear()

        currentCalculation.insert(currentCursorPosition, calculation)
    }

    fun replace(calculation: String) = insert(calculation, true)
}

