package org.seniorlaguna.calculator.example

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class ExampleFragment : Fragment() {

    companion object {
        const val TOOL_ID = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return TextView(this.requireContext()).apply {
            text = "Example Tool"
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.MATCH_PARENT
            gravity = Gravity.CENTER
        }
    }

}