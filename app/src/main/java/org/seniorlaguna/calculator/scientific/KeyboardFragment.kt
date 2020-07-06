package org.seniorlaguna.calculator.scientific

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.updateMargins
import androidx.lifecycle.ViewModelProviders
import org.seniorlaguna.calculator.Constant
import org.seniorlaguna.calculator.GlobalViewModel
import org.seniorlaguna.calculator.R
import org.seniorlaguna.calculator.basic.KeyboardFragment
import org.seniorlaguna.calculator.basic.keyboard2
import org.seniorlaguna.calculator.basic.keyboard3

class KeyboardFragment : KeyboardFragment() {

    // given constants
    private val givenConstants = listOf<Constant>(
        Constant("π", ""),
        Constant("e", "")
    )

    var page1 = true

    private lateinit var globalViewModel: GlobalViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // get view models
        globalViewModel = ViewModelProviders.of(requireParentFragment())[GlobalViewModel::class.java]
        toolViewModel = ViewModelProviders.of(requireParentFragment())[ScientificViewModel::class.java]

        tableLayout = TableLayout(requireContext())
        return tableLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initKeyboard(tableLayout, keyboard2)
    }

    override fun onClick(resId : Int, insert : String?) {

        when (resId) {
            R.string.math_switch_page -> {
                initKeyboard(tableLayout, if (page1) keyboard3 else keyboard2)
                page1 = !page1
            }

            R.string.math_my_constants -> showConstants()

            R.string.math_my_functions -> showFunctions()

            else -> super.onClick(resId, insert)
        }
    }

    private fun showConstants() {
        globalViewModel.database.getAllConstants()
            .plus(givenConstants).let {

            // setup the alert builder
            val builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.choose_a_constant)) // add a list

            val constants = it.map {
                if (it.value.isNotEmpty()) "${it.title}= ${it.value}" else "${it.title}"
            }

            builder.setItems(
                constants.toTypedArray()
            ) { _ : DialogInterface, which : Int ->
                super.onClick(0, it[which].title)
            }

            builder.show()
        }

    }

    private fun showFunctions() {
        globalViewModel.database.getAllFunctions().let {

            // setup the alert builder
            val builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.choose_a_function)) // add a list

            val constants = it.map { "${it.title}(x)= ${it.expression}" }

            builder.setItems(
                constants.toTypedArray()
            ) { _ : DialogInterface, which : Int ->
                super.onClick(0, "${it[which].title}(")
            }

            builder.show()
        }
    }

    override fun styleButton(btn : Button, textRes : Int, height : Int, width : Int, margin : Int, fontSize : Float) {
        btn.text = getString(textRes)
        btn.textSize = fontSize

        btn.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        btn.setBackgroundColor(
            ContextCompat.getColor(requireContext(), when(textRes) {
                R.string.math_equals -> R.color.calculatorButtonEqualsColor
                R.string.math_clear -> R.color.calculatorButtonDeleteAllColor
                R.string.math_switch_page -> R.color.calculatorButtonSwitchPageColor
                else -> R.color.calculatorButtonColor
            }))
        btn.isAllCaps = false
        val params = TableRow.LayoutParams(width, height)
        params.updateMargins(margin, margin, margin, margin)
        params.weight = 1f
        btn.layoutParams = params
    }

}