package org.seniorlaguna.calculator.settings

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.preference.*
import org.seniorlaguna.calculator.Calculation
import org.seniorlaguna.calculator.GlobalViewModel
import org.seniorlaguna.calculator.R
import java.lang.Exception


class HistorySettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {

    // global view model
    private lateinit var globalViewModel: GlobalViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        // get global view model
        globalViewModel = ViewModelProviders.of(this)[GlobalViewModel::class.java]

        addPreferencesFromResource(R.xml.history_preferences)
        initOnPreferenceClickListener()
        initPreferenceIconSpace(false)
    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        globalViewModel.database.clearCalculationHistory(Calculation.TYPE_BASIC)
        globalViewModel.database.clearCalculationHistory(Calculation.TYPE_SCIENTIFIC)
        return true
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