package org.seniorlaguna.calculator.customviews

import android.content.Context
import android.os.Build
import android.util.AttributeSet

class ExtendedEditText(context: Context, attrs: AttributeSet) : androidx.appcompat.widget.AppCompatEditText(context, attrs) {

    // TODO: make the cursor moveable and the keyboard never appear
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            showSoftInputOnFocus = false
        }
        else {
            inputType = 0
        }
    }

}
