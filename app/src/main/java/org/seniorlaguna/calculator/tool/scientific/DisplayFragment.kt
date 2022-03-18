package org.seniorlaguna.calculator.tool.scientific

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import org.seniorlaguna.calculator.Calculation
import org.seniorlaguna.calculator.GlobalViewModel
import org.seniorlaguna.calculator.R
import org.seniorlaguna.calculator.tool.basic.DisplayFragment

class DisplayFragment : DisplayFragment() {

    override val calculationType = Calculation.TYPE_SCIENTIFIC

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // get view models
        globalViewModel = ViewModelProvider(requireActivity())[GlobalViewModel::class.java]
        toolViewModel = ViewModelProvider(requireParentFragment())[ScientificViewModel::class.java]

        return inflater.inflate(R.layout.fragment_basic_display, container, false)
    }

}