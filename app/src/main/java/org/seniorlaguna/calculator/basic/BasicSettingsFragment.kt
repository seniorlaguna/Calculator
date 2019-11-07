package org.seniorlaguna.calculator.basic

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.preference.*
import org.seniorlaguna.calculator.Calculation
import org.seniorlaguna.calculator.GlobalViewModel
import org.seniorlaguna.calculator.R
import java.lang.Exception


class BasicSettingsFragment: PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    // global view model
    private lateinit var globalViewModel: GlobalViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        // get global view model
        globalViewModel = ViewModelProviders.of(this)[GlobalViewModel::class.java]

        addPreferencesFromResource(R.xml.basic_calculator_preferences)
        initOnPreferenceChangeListener()
        initOnPreferenceClickListener()
        initPreferenceIconSpace()
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {

        return try {
            when (preference?.key) {
                getString(R.string.prefs_basic_calculator_decimal_places_key) -> isValidDecimalPlaces((newValue as String).toInt())
                getString(R.string.prefs_basic_calculator_history_length_key) -> isValidHistoryLength((newValue as String).toInt()).also { if (it) globalViewModel.database.adjustSize(Calculation.TYPE_BASIC, (newValue).toInt()) }
                getString(R.string.prefs_basic_calculator_precision_key) -> isValidPrecision((newValue as String).toInt())
                else -> true
            }
        } catch (e : Exception) {
            false
        }

    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        globalViewModel.database.clearCalculationHistory(Calculation.TYPE_BASIC)
        return true
    }

    private fun isValidDecimalPlaces(value : Int) = 0 <= value

    private fun isValidHistoryLength(value : Int) = 0 <= value

    private fun isValidPrecision(value : Int) = 0 <= value

    private fun initOnPreferenceChangeListener() {
        listOf<Preference?>(
            findPreference(getString(R.string.prefs_basic_calculator_decimal_places_key)),
            findPreference(getString(R.string.prefs_basic_calculator_history_length_key)),
            findPreference(getString(R.string.prefs_basic_calculator_precision_key))
        ).forEach {
            it?.onPreferenceChangeListener = this
        }
    }

    private fun initOnPreferenceClickListener() {
        listOf(
            findPreference<Preference>(getString(R.string.prefs_basic_calculator_history_clear_key))
        ).forEach {
            it?.onPreferenceClickListener = this
        }
    }

    private fun initPreferenceIconSpace(enabled : Boolean = false) {

        for (i in 0 until preferenceScreen.preferenceCount) {
            preferenceScreen.getPreference(i)?.run {
                isIconSpaceReserved = enabled

                // remove space from children
                val category = (this as PreferenceCategory)
                for (j in 0 until category.preferenceCount) {
                    category.getPreference(j)?.run {
                        isIconSpaceReserved = enabled
                    }
                }
            }

        }

    }
}