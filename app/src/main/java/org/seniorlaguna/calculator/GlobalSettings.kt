package org.seniorlaguna.calculator

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import org.seniorlaguna.calculator.basic.BasicFragment

/**
 * GlobalSettings is a singleton.
 * It provides access to each tool's preferences
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

            assert(instance != null)
            return instance!!
        }
    }

    /**
     * attributes according to shared preferences
     */

    private val sharedPreferences : SharedPreferences

    /**
     * attributes according to different tools
     */

    val basicCalculator : BasicCalculatorSettings
    val scientificCalculator : ScientificCalculatorSettings

    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        basicCalculator = BasicCalculatorSettings(context)
        scientificCalculator = ScientificCalculatorSettings(context)
    }

    /**
     * Global Settings
     */

    // current visible tool
    private val currentToolKey = "currentTool"
    var currentTool : Int
        get() = sharedPreferences.getInt(currentToolKey, BasicFragment.TOOL_ID)
        set(value) { sharedPreferences.edit().putInt(currentToolKey, value).commit() }


    /**
     * Settings according to the basic calculator
     */

    @Suppress("unused")
    inner class BasicCalculatorSettings(private val context: Context) {

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

    }

    /**
     * Settings according to the scientific calculator
     */

    @Suppress("unused")
    inner class ScientificCalculatorSettings(private val context: Context) {

        val decimalPlaces : Int
            get() = sharedPreferences.getString(context.getString(R.string.prefs_scientific_calculator_decimal_places_key), "2")!!.toInt()

        val roundUp : Boolean
            get() = sharedPreferences.getBoolean(context.getString(R.string.prefs_scientific_calculator_round_up_key), false)

        val autoDelete : Boolean
            get() = sharedPreferences.getBoolean(context.getString(R.string.prefs_scientific_calculator_auto_delete_key), false)

        val instantResult : Boolean
            get() = sharedPreferences.getBoolean(context.getString(R.string.prefs_scientific_calculator_instant_result_key), true)

        val rad : Boolean
            get() = sharedPreferences.getBoolean(context.getString(R.string.prefs_prefs_scientific_calculator_rad_deg_key), true)

        val history : Boolean
            get() = sharedPreferences.getBoolean(context.getString(R.string.prefs_scientific_calculator_history_key), true)

        val historyLength : Int
            get() = sharedPreferences.getString(context.getString(R.string.prefs_scientific_calculator_history_length_key), "20")!!.toInt()

        val historySaveResults : Boolean
            get() = sharedPreferences.getBoolean(context.getString(R.string.prefs_scientific_calculator_history_save_results_key), false)

        val historyDisableInput : Boolean
            get() = sharedPreferences.getBoolean(context.getString(R.string.prefs_scientific_calculator_history_disable_input_key), true)

        val historySwitchOnInput : Boolean
            get() = sharedPreferences.getBoolean(context.getString(R.string.prefs_scientific_calculator_history_switch_on_input_key), true)

        val precision : Int
            get() = sharedPreferences.getString(context.getString(R.string.prefs_scientific_calculator_precision_key), "4096")!!.toInt()

    }

}