package org.seniorlaguna.calculator

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

class DisplayFragment : Fragment() {

    companion object {
        lateinit var display : EditText
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.fragment_display, container, false)
        display = view.findViewById<EditText>(R.id.display)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            display.setShowSoftInputOnFocus(false)
        } else {
            display.setInputType(0)
        }

        return view
    }
}