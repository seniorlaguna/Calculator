package org.seniorlaguna.calculator.tool.basic

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.seniorlaguna.calculator.R
import org.seniorlaguna.calculator.customviews.ExtendedViewPager

open class KeyboardFragment : Fragment() {

    protected lateinit var toolViewModel : BasicViewModel
    protected lateinit var tableLayout : TableLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // get view models
        toolViewModel = ViewModelProvider(requireParentFragment())[BasicViewModel::class.java]

        tableLayout = TableLayout(requireContext())
        return tableLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initKeyboard(tableLayout, keyboard1)
    }

    fun initKeyboard(table : TableLayout, keyboardLayout : KeyboardLayout) {

        val size = resources.displayMetrics.widthPixels / 51 * 10
        val margin = (resources.displayMetrics.widthPixels - (5 * size)) / 10
        table.removeAllViews()

        keyboardLayout.portraitLayout.forEach { row ->

            val tableRow = TableRow(table.context)

            row.forEach { column ->
                val btn = Button(table.context)
                styleButton(btn, column[0] as Int, size, size, margin, 22f)
                btn.setOnClickListener { onClick(column[0] as Int, column[1] as String?) }
                tableRow.addView(btn)
            }

            table.addView(tableRow, TableLayout.LayoutParams().apply { setMargins(0); updateMargins(0,0,0,0) })
        }

    }

    protected open fun onClick(resId : Int, insert : String?) {
        // in history tab
        if (toolViewModel.viewPagerTab.value == ExtendedViewPager.HISTORY_TAB) {

            // change to display wanted
            if (toolViewModel.settings.historySwitchOnInput) {
                toolViewModel.viewPagerTab.value = ExtendedViewPager.DISPLAY_TAB
            }

            // no input wanted
            if (toolViewModel.settings.historyDisableInput) return
        }

        if (toolViewModel.equalsPressed and toolViewModel.settings.autoDelete) {
            toolViewModel.clearAll()
            toolViewModel.equalsPressed = false
        }

        if (insert != null) {
            toolViewModel.insert(insert)
        }
        else {
            when (resId) {
                R.string.math_clear -> toolViewModel.clearAll()
                R.string.math_clear_one -> toolViewModel.clearOne()
                R.string.math_equals -> toolViewModel.equals()
            }
        }
    }

    open protected fun styleButton(btn : Button, textRes : Int, height : Int, width : Int, margin : Int, fontSize : Float) {
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