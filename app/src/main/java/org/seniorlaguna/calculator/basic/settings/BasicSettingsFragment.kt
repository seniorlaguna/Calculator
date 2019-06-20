package org.seniorlaguna.calculator.basic.settings

import android.os.Bundle
import androidx.preference.*
import org.seniorlaguna.calculator.R
import org.seniorlaguna.calculator.SettingsActivity
import java.lang.Exception


class BasicSettingsFragment(private val settingsActivity: SettingsActivity) : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.basic_calculator_preferences)
        initOnPreferenceChangeListener()
        initOnPreferenceClickListener()
        initPreferenceIconSpace(false)
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {

        try {
            return when (preference?.key) {
                getString(R.string.prefs_basic_calculator_decimal_places_key) -> isValidDecimalPlaces((newValue as String).toInt())
                getString(R.string.prefs_basic_calculator_history_length_key) -> isValidHistoryLength((newValue as String).toInt()).also { if (it) settingsActivity.basicViewModel.adjustSize((newValue).toInt()) }
                getString(R.string.prefs_basic_calculator_precision_key) -> isValidPrecision((newValue as String).toInt())
                else -> true
            }
        }
        catch (e : Exception) {
            return false
        }

    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        settingsActivity.basicViewModel.deleteAll()
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
            it?.setOnPreferenceChangeListener(this)
        }
    }

    private fun initOnPreferenceClickListener() {
        listOf<Preference?>(
            findPreference<Preference>(getString(R.string.prefs_basic_calculator_history_clear_key))
        ).forEach {
            it?.setOnPreferenceClickListener(this)
        }
    }

    private fun initPreferenceIconSpace(enabled : Boolean) {

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