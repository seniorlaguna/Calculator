package org.seniorlaguna.calculator.settings

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import org.seniorlaguna.calculator.Calculation
import org.seniorlaguna.calculator.GlobalViewModel
import org.seniorlaguna.calculator.R
import org.seniorlaguna.calculator.utils.openContact
import org.seniorlaguna.calculator.utils.openPrivacyPolicy
import org.seniorlaguna.calculator.utils.openTermsOfUse

class SettingsFragment: PreferenceFragmentCompat(), Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    // global view model
    private lateinit var globalViewModel: GlobalViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        // get global view model
        globalViewModel = ViewModelProvider(this)[GlobalViewModel::class.java]

        addPreferencesFromResource(R.xml.settings)
        initOnPreferenceClickListener()
        initOnPreferenceChangeListener()
        initPreferenceIconSpace()
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {

        return try {
            when (preference.key) {
                getString(R.string.prefs_basic_calculator_decimal_places_key) -> isGreaterThanZero((newValue as String).toInt())
                getString(R.string.prefs_basic_calculator_history_length_key) -> adjustDatabase((newValue as String).toInt())
                getString(R.string.prefs_basic_calculator_precision_key) -> isGreaterThanZero((newValue as String).toInt())
                getString(R.string.theme_key) -> changeTheme()
                else -> true
            }
        } catch (e : Exception) {
            false
        }

    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        when (preference.key) {
            getString(R.string.settings_privacy_key) -> openPrivacyPolicy(requireContext())
            getString(R.string.settings_terms_key) -> openTermsOfUse(requireContext())
            getString(R.string.settings_contact_key) -> openContact(requireContext())
            getString(R.string.prefs_basic_calculator_history_clear_key) -> clearHistory()
        }
        return true
    }

    private fun initOnPreferenceClickListener() {
        listOf<Preference?>(
            findPreference<Preference>(getString(R.string.settings_privacy_key)),
            findPreference<Preference>(getString(R.string.settings_terms_key)),
            findPreference<Preference>(getString(R.string.settings_contact_key)),
            findPreference<Preference>(getString(R.string.prefs_basic_calculator_history_clear_key))
        ).forEach {
            it?.onPreferenceClickListener = this
        }
    }

    private fun initOnPreferenceChangeListener() {
        listOf<Preference?>(
            findPreference(getString(R.string.prefs_basic_calculator_decimal_places_key)),
            findPreference(getString(R.string.prefs_basic_calculator_history_length_key)),
            findPreference(getString(R.string.prefs_basic_calculator_precision_key)),
            findPreference(getString(R.string.theme_key)),
            findPreference(getString(R.string.prefs_basic_calculator_precision_key))
        ).forEach {
            it?.onPreferenceChangeListener = this
        }
    }

    private fun adjustDatabase(value : Int) : Boolean {
        if (value < 0) return false;

        globalViewModel.database.adjustSize(Calculation.TYPE_BASIC, value)
        globalViewModel.database.adjustSize(Calculation.TYPE_SCIENTIFIC, value)
        return true
    }

    private fun changeTheme() : Boolean {
        globalViewModel.settings.themeChanged = true
        (requireActivity() as SettingsActivity).run {
            recreate()
        }
        return true
    }

    private fun clearHistory() {
        globalViewModel.database.clearCalculationHistory(Calculation.TYPE_BASIC)
        globalViewModel.database.clearCalculationHistory(Calculation.TYPE_SCIENTIFIC)
        Toast.makeText(requireContext(), getString(R.string.history_cleared_toast), Toast.LENGTH_SHORT).show()
    }

    private fun isGreaterThanZero(value : Int) = 0 <= value

    private fun initPreferenceIconSpace(enabled : Boolean = false) {

        for (i in 0 until preferenceScreen.preferenceCount) {
            preferenceScreen.getPreference(i).run {
                isIconSpaceReserved = enabled

                // remove space from children
                val category = (this as PreferenceCategory)
                for (j in 0 until category.preferenceCount) {
                    category.getPreference(j).run {
                        isIconSpaceReserved = enabled
                    }
                }
            }

        }

    }

}