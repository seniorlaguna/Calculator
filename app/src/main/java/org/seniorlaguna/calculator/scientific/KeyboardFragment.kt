package org.seniorlaguna.calculator.scientific

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import androidx.core.content.ContextCompat
import androidx.core.view.updateMargins
import androidx.lifecycle.ViewModelProviders
import org.seniorlaguna.calculator.R
import org.seniorlaguna.calculator.basic.KeyboardFragment
import org.seniorlaguna.calculator.basic.keyboard2
import org.seniorlaguna.calculator.basic.keyboard3

class KeyboardFragment : KeyboardFragment() {

    var page1 = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // get view models
        toolViewModel = ViewModelProviders.of(requireParentFragment())[ScientificViewModel::class.java]

        tableLayout = TableLayout(requireContext())
        return tableLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initKeyboard(tableLayout, keyboard2)
    }

    override fun onClick(resId : Int, insert : String?) {
        super.onClick(resId, insert)

        when (resId) {
            R.string.math_switch_page -> {
                initKeyboard(tableLayout, if (page1) keyboard3 else keyboard2)
                page1 = !page1
            }
        }
    }

    override fun styleButton(btn : Button, textRes : Int, height : Int, width : Int, margin : Int, fontSize : Float) {
        btn.text = getString(textRes)
        btn.textSize = fontSize

        btn.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        btn.setBackgroundColor(
            ContextCompat.getColor(requireContext(), when(textRes) {
                R.string.math_equals -> android.R.color.holo_blue_light
                R.string.math_clear -> R.color.deleteAllColor
                R.string.math_switch_page -> android.R.color.holo_orange_dark
                else -> android.R.color.holo_blue_dark
            }))
        btn.isAllCaps = false
        val params = TableRow.LayoutParams(width, height)
        params.updateMargins(margin, margin, margin, margin)
        params.weight = 1f
        btn.layoutParams = params
    }

}