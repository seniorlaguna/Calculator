package org.seniorlaguna.calculator.scientific

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import org.seniorlaguna.calculator.Calculation
import org.seniorlaguna.calculator.GlobalViewModel
import org.seniorlaguna.calculator.R
import org.seniorlaguna.calculator.basic.DisplayFragment

class DisplayFragment : DisplayFragment() {

    override val calculationType = Calculation.TYPE_SCIENTIFIC

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // get view models
        globalViewModel = ViewModelProviders.of(requireActivity())[GlobalViewModel::class.java]
        toolViewModel = ViewModelProviders.of(requireParentFragment())[ScientificViewModel::class.java]

        return inflater.inflate(R.layout.fragment_basic_display, container, false)
    }

}