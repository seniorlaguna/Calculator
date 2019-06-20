package org.seniorlaguna.calculator.scientific

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_scientific_display.*
import org.seniorlaguna.calculator.MainActivity
import org.seniorlaguna.calculator.R

class DisplayFragment(private val mainActivity: MainActivity) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scientific_display, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        display.doOnTextChanged { _, _, _, _ ->
            mainActivity.scientificFragment.calculator.showCurrentResult()
        }
    }

}