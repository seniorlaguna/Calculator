package org.seniorlaguna.calculator.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import org.seniorlaguna.calculator.R
import org.seniorlaguna.calculator.tool.basic.BasicFragment
import java.util.*

/**
 * GlobalSettings is a singleton.
 * It provides access to all preferences
 * using shared preferences.
 */

class GlobalSettings(context: Context) {

    // apply singleton pattern
    companion object {
        private var instance : GlobalSettings? = null

        fun getInstance(context: Context) : GlobalSettings {

            if (instance == null) {
                instance = GlobalSettings(context)
            }

            if (instance == null) throw Exception("Global settings can't be instantiated")
            return instance!!
        }
    }

    /**
     * attributes according to shared preferences
     */

    private val context : Context = context
    private val sharedPreferences : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    /**
     * Settings
     */

    // current visible tool
    private val currentToolKey = "currentTool"

    var currentTool : Int
        get() = sharedPreferences.getInt(currentToolKey, BasicFragment.TOOL_ID)
        set(value) { sharedPreferences.edit().putInt(currentToolKey, value).commit() }

    private val _theme : String
        get() = sharedPreferences.getString(context.getString(R.string.theme_key), "1")

    var themeChanged : Boolean = false

    val theme : Int
        get() {
            return when (_theme) {
                "1" -> R.style.AppTheme
                "2" -> R.style.AppTheme_Dark
                else -> R.style.AppTheme

            }
        }

    var adFreeUntil : Long
        get() = sharedPreferences.getLong(context.getString(R.string.ad_free_key), 0)
        set(v : Long) {
            sharedPreferences.edit().putLong(context.getString(R.string.ad_free_key), v).commit()
        }

    val isAdFree : Boolean
       get() = adFreeUntil > Calendar.getInstance().timeInMillis

    val decimalPlaces : Int
        get() = sharedPreferences.getString(context.getString(R.string.prefs_basic_calculator_decimal_places_key), "2")!!.toInt()

    val roundUp : Boolean
        get() = sharedPreferences.getBoolean(context.getString(R.string.prefs_basic_calculator_round_up_key), false)

    val autoDelete : Boolean
        get() = sharedPreferences.getBoolean(context.getString(R.string.prefs_basic_calculator_auto_delete_key), false)

    val instantResult : Boolean
        get() = sharedPreferences.getBoolean(context.getString(R.string.prefs_basic_calculator_instant_result_key), true)

    val history : Boolean
        get() = sharedPreferences.getBoolean(context.getString(R.string.prefs_basic_calculator_history_key), true)

    val historyLength : Int
        get() = sharedPreferences.getString(context.getString(R.string.prefs_basic_calculator_history_length_key), "20")!!.toInt()

    val historySaveResults : Boolean
        get() = sharedPreferences.getBoolean(context.getString(R.string.prefs_basic_calculator_history_save_results_key), false)

    val historyDisableInput : Boolean
        get() = sharedPreferences.getBoolean(context.getString(R.string.prefs_basic_calculator_history_disable_input_key), true)

    val historySwitchOnInput : Boolean
        get() = sharedPreferences.getBoolean(context.getString(R.string.prefs_basic_calculator_history_switch_on_input_key), true)

    val precision : Int
        get() = sharedPreferences.getString(context.getString(R.string.prefs_basic_calculator_precision_key), "4096")!!.toInt()

    val rad : Boolean
        get() = sharedPreferences.getBoolean(context.getString(R.string.prefs_prefs_scientific_calculator_rad_deg_key), true)

}