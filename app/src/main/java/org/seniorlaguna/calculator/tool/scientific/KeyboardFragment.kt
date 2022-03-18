package org.seniorlaguna.calculator.tool.scientific

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import androidx.core.content.ContextCompat
import androidx.core.view.updateMargins
import androidx.lifecycle.ViewModelProvider
import org.seniorlaguna.calculator.GlobalViewModel
import org.seniorlaguna.calculator.R
import org.seniorlaguna.calculator.tool.basic.KeyboardFragment
import org.seniorlaguna.calculator.tool.basic.keyboard2
import org.seniorlaguna.calculator.tool.basic.keyboard3

class KeyboardFragment : KeyboardFragment() {

    var page1 = true

    private lateinit var globalViewModel: GlobalViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // get view models
        globalViewModel = ViewModelProvider(requireParentFragment())[GlobalViewModel::class.java]
        toolViewModel = ViewModelProvider(requireParentFragment())[ScientificViewModel::class.java]

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

            else -> super.onClick(resId, insert)
        }
    }

    override fun styleButton(btn : Button, textRes : Int, height : Int, width : Int, margin : Int, fontSize : Float) {
        btn.text = getString(textRes)
        btn.textSize = fontSize

        btn.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))

        val typedValue = TypedValue()
        btn.setBackgroundColor(
            ContextCompat.getColor(requireContext(), when(textRes) {
                R.string.math_equals -> {
                    requireContext().theme.resolveAttribute(R.attr.calculatorButtonEqualsColor, typedValue, true)
                    typedValue.resourceId
                }
                R.string.math_clear -> {
                    requireContext().theme.resolveAttribute(R.attr.calculatorButtonDeleteAllColor, typedValue, true)
                    typedValue.resourceId
                }
                R.string.math_switch_page -> {
                    requireContext().theme.resolveAttribute(R.attr.calculatorButtonSwitchPageColor, typedValue, true)
                    typedValue.resourceId
                }
                else -> {
                    requireContext().theme.resolveAttribute(R.attr.calculatorButtonColor, typedValue, true)
                    typedValue.resourceId
                }
            }))
        btn.isAllCaps = false
        val params = TableRow.LayoutParams(width, height)
        params.updateMargins(margin, margin, margin, margin)
        params.weight = 1f
        btn.layoutParams = params
    }

}