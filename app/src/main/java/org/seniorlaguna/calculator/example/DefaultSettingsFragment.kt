package org.seniorlaguna.calculator.example

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.*
import org.seniorlaguna.calculator.Calculation
import org.seniorlaguna.calculator.GlobalViewModel
import org.seniorlaguna.calculator.R
import java.lang.Exception


class DefaultSettingsFragment: Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return TextView(this.requireContext()).apply {
            text = context.getString(R.string.nothing_to_configure)
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.MATCH_PARENT
            gravity = Gravity.CENTER
        }
    }

}