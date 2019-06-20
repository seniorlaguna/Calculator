package org.seniorlaguna.calculator.scientific.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import org.seniorlaguna.calculator.R

class ScientificSettings private constructor(context: Context) {

    companion object {

        private var instance : ScientificSettings? = null

        fun getInstance(context: Context) : ScientificSettings {

            if (instance == null) {
                instance = ScientificSettings(context)
            }

            return instance!!
        }

    }

    val applicationContext : Context
    val sharedPreferences : SharedPreferences

    init {
        this.applicationContext = context.applicationContext
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    val decimalPlaces : Int
        get() = sharedPreferences.getString(applicationContext.getString(R.string.prefs_scientific_calculator_decimal_places_key), "2")!!.toInt()

    val roundUp : Boolean
        get() = sharedPreferences.getBoolean(applicationContext.getString(R.string.prefs_scientific_calculator_round_up_key), false)

    val autoDelete : Boolean
        get() = sharedPreferences.getBoolean(applicationContext.getString(R.string.prefs_scientific_calculator_auto_delete_key), false)

    val instantResult : Boolean
        get() = sharedPreferences.getBoolean(applicationContext.getString(R.string.prefs_scientific_calculator_instant_result_key), true)

    val rad : Boolean
        get() = sharedPreferences.getBoolean(applicationContext.getString(R.string.prefs_prefs_scientific_calculator_rad_deg_key), true)

    val history : Boolean
        get() = sharedPreferences.getBoolean(applicationContext.getString(R.string.prefs_scientific_calculator_history_key), true)

    val historyLength : Int
        get() = sharedPreferences.getString(applicationContext.getString(R.string.prefs_scientific_calculator_history_length_key), "20")!!.toInt()

    val historySaveResults : Boolean
        get() = sharedPreferences.getBoolean(applicationContext.getString(R.string.prefs_scientific_calculator_history_save_results_key), false)

    val historyDisableInput : Boolean
        get() = sharedPreferences.getBoolean(applicationContext.getString(R.string.prefs_scientific_calculator_history_disable_input_key), true)

    val historySwitchOnInput : Boolean
        get() = sharedPreferences.getBoolean(applicationContext.getString(R.string.prefs_scientific_calculator_history_switch_on_input_key), true)

    val precision : Int
        get() = sharedPreferences.getString(applicationContext.getString(R.string.prefs_scientific_calculator_precision_key), "4096")!!.toInt()

}