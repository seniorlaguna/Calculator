package org.seniorlaguna.calculator.settings

import android.os.Bundle
import androidx.annotation.Keep
import androidx.lifecycle.ViewModelProviders
import androidx.preference.*
import org.seniorlaguna.calculator.Calculation
import org.seniorlaguna.calculator.GlobalViewModel
import org.seniorlaguna.calculator.R
import java.lang.Exception


@Keep
class GeneralSettingsFragment: PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    // global view model
    private lateinit var globalViewModel: GlobalViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        // get global view model
        globalViewModel = ViewModelProviders.of(this)[GlobalViewModel::class.java]

        addPreferencesFromResource(R.xml.general_preferences)
        initOnPreferenceChangeListener()
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