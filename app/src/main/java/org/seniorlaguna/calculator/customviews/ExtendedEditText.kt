package org.seniorlaguna.calculator.customviews

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.os.Build
import android.text.InputType
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doBeforeTextChanged
import java.lang.StringBuilder

class ExtendedEditText(context: Context, attrs: AttributeSet) : EditText(context, attrs) {

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
