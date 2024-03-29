package org.seniorlaguna.calculator.tool.basic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.seniorlaguna.calculator.Calculation
import org.seniorlaguna.calculator.GlobalViewModel
import org.seniorlaguna.calculator.R
import kotlin.math.max

open class DisplayFragment : Fragment() {

    protected open val calculationType = Calculation.TYPE_BASIC

    protected lateinit var globalViewModel: GlobalViewModel
    protected lateinit var toolViewModel: BasicViewModel
    private lateinit var display : EditText
    private lateinit var displayResult : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // get view models
        globalViewModel = ViewModelProvider(requireActivity())[GlobalViewModel::class.java]
        toolViewModel = ViewModelProvider(requireParentFragment())[BasicViewModel::class.java]

        return inflater.inflate(R.layout.fragment_basic_display, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // save references to views
        display = view.findViewById(R.id.display)
        displayResult = view.findViewById(R.id.display_result)

        // register observer
        registerObserver()

    }

    override fun onResume() {
        super.onResume()

        //apply new settings to display
        requireActivity().findViewById<TextView>(R.id.display_result).visibility = if (toolViewModel.settings.instantResult) View.VISIBLE else View.INVISIBLE

        // evaluate expression again
        requestResult()
    }

    private fun registerObserver() {

        // observe display commands
        toolViewModel.displayCommand.observe(requireParentFragment(), Observer {

            if (it.clear or showsError()) onClear()

            if (it.clearOne) onClearOne()

            if (it.insert != null) onInsert(it.insert)

            if (it.equals) onEquals()
            else requestResult()

        })

        // observe result
        toolViewModel.result.observe(requireParentFragment(), Observer {
            onResult(it)
        })

    }

    /**
     * Evaluates the expression shown in display by
     * passing it to the tool view model's calculator.
     * The result will be received using live data
     * and shown in the display result text view.
     */
    private fun requestResult() {
        Log.d("Display", "Request Result")

        toolViewModel.calculate(display.text.toString())
    }

    /**
     * Saves the current expression and the result
     * in global database depending on user settings.
     */
    private fun saveInHistory() {
        Log.d("Display", "Save in history")

        // histoy disabled
        if (!toolViewModel.settings.history) return

        // result shall be saved
        if (toolViewModel.settings.historySaveResults) {
            globalViewModel.database.insertCalculation(Calculation(0, "", displayResult.text.toString(), calculationType))
        }

        // save expression
        globalViewModel.database.insertCalculation(Calculation(0, "", display.text.toString(), calculationType))


        // adjust history size
        Log.d("DISPLAY", "DB ADJUSTED")
        globalViewModel.database.adjustSize(calculationType, toolViewModel.settings.historyLength)
    }

    /**
     * Resolve error message to localized error message
     */
    private fun resolveErrorMessage(error : String) : String {

        if (error.contains("Mismatched parentheses")) {
            return getString(R.string.basic_calculator_parentheses_error)
        }
        else {
            return getString(R.string.basic_calculator_error)
        }

    }


    /**
     * Determine if the display shows an error
     */

    private fun showsError() : Boolean {
        return display.text.startsWith("Error: ")
    }

    /**
     * display command handler
     */
    private fun onClear() {
        Log.d("Display", "Clear")

        display.text.clear()
        toolViewModel.result.value = ""
    }

    private fun onClearOne() {
        Log.d("Display", "Clear One")

        display.apply {
            text.replace(max(selectionStart - 1, 0), selectionStart, "")
        }
    }

    private fun onInsert(text : String) {
        Log.d("Display", "Insert")

        // insert normally at cursor position
        display.apply {
            this.text.insert(selectionStart, text)
        }
    }

    private fun onReplace(text : String) {
        Log.d("Display", "Replace")
        onClear()
        onInsert(text)
    }

    /**
     * Moves the string from display result to
     * display and saves expression or/and result in history
     */
    private fun onEquals() {
        Log.d("Display", "Equals")

        // if instant result is turned on
        if (toolViewModel.settings.instantResult) {

            if (displayResult.text.isBlank() or toolViewModel.errorInExpression) {
                displayResult.visibility = View.VISIBLE
                return
            }
        }

        // save result in history if no error occured
        if (!toolViewModel.errorInExpression) {
            saveInHistory()
        }

        onReplace(displayResult.text.toString())
    }

    /**
     * result handler
     */

    private fun onResult(result : String) {
        Log.d("DISPLAY", "Result")

        // if result contains error message do not show preview
        if (globalViewModel.settings.instantResult) {
            displayResult.visibility = if (result.startsWith("Error: ")) View.INVISIBLE else View.VISIBLE
        }

        // result differs from term
        if (display.text.toString() != result) {

            // modify error message if given
            displayResult.text = if (result.startsWith("Error: ")) resolveErrorMessage(result) else result
        }

        // it's the same
        else {

            // it's not necessary to show a result
            displayResult.text = ""
        }

    }

}