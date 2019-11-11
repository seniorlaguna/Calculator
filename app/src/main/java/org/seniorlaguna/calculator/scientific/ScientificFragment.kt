package org.seniorlaguna.calculator.scientific

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import org.seniorlaguna.calculator.GlobalViewModel
import org.seniorlaguna.calculator.R
import org.seniorlaguna.calculator.basic.BasicFragment

class ScientificFragment : BasicFragment() {

    companion object {
        val TOOL_ID = 2
    }

    override val displayFragment : DisplayFragment by lazy (::DisplayFragment)
    override val historyFragment : HistoryFragment by lazy (::HistoryFragment)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // get view models
        globalViewModel =  ViewModelProviders.of(requireActivity())[GlobalViewModel::class.java]
        toolViewModel = ViewModelProviders.of(this)[ScientificViewModel::class.java]

        return inflater.inflate(R.layout.fragment_scientific, container, false)
    }
}