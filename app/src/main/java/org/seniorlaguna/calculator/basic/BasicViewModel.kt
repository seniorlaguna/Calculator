package org.seniorlaguna.calculator.basic

import android.app.Application
import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.udojava.evalex.Expression
import org.seniorlaguna.calculator.GlobalDatabase
import org.seniorlaguna.calculator.GlobalSettings
import org.seniorlaguna.calculator.customviews.ExtendedViewPager
import java.util.concurrent.atomic.AtomicBoolean

open class BasicViewModel(application: Application) : AndroidViewModel(application) {

    open val settings = GlobalSettings.getInstance(application).basicCalculator
    open val calculator = Calculator(GlobalSettings.getInstance(application).basicCalculator)

    val displayCommand = SingleLiveEvent<DisplayCommand>()
    val result = MutableLiveData<String>()
    val viewPagerTab = MutableLiveData<Int>()

    // flags
    var equalsPressed = false
    var errorInExpression = false

    init {
        displayCommand.setValue(DisplayCommand(null,
            clear = false,
            clearOne = false,
            equals = false
        ))
        result.value = ""
        viewPagerTab.value = ExtendedViewPager.DISPLAY_TAB
    }

    /**
     * evaluate expression
     */
    fun calculate(expression : String) {

        try {
            result.value = calculator.calculate(expression)
            errorInExpression = false
        } catch (e : Expression.ExpressionException) {
            errorInExpression = true
            result.value = "Error: ${e.message}"
        } catch (e : ArithmeticException) {
            errorInExpression = true
            result.value = "Error: ${e.message}"
        } catch (e : NumberFormatException) {
            errorInExpression = true
            result.value = "Error: ${e.message}"
        }
    }

    /**
     * inserts text into expression
     */
    fun insert(text : String, replace : Boolean = false) {

        // replace expression
        displayCommand.setValue(DisplayCommand(text, replace, clearOne = false, equals = false))
    }

    /**
     * clears expression
     */
    fun clearAll() {
        displayCommand.setValue(DisplayCommand(null, clear = true, clearOne = false, equals = false))
    }

    /**
     * deletes one character
     */
    fun clearOne() {
        displayCommand.setValue(DisplayCommand(null, clear = false, clearOne = true, equals = false))
    }

    fun equals() {
        equalsPressed = true
        displayCommand.setValue(DisplayCommand(null, clear = false, clearOne = false, equals = true))
    }

    class DisplayCommand(val insert : String?, val clear : Boolean, val clearOne : Boolean, val equals : Boolean)
}

class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        })
    }

    @MainThread
    override fun setValue(value: T) {
        pending.set(true)
        super.setValue(value)
    }

}