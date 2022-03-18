package org.seniorlaguna.calculator.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.annotation.Keep
import androidx.lifecycle.ViewModelProviders
import androidx.preference.*
import org.seniorlaguna.calculator.Calculation
import org.seniorlaguna.calculator.GlobalViewModel
import org.seniorlaguna.calculator.R
import java.lang.Exception


@Keep
class DesignSettingsFragment: PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    // global view model
    private lateinit var globalViewModel: GlobalViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        // get global view model
        globalViewModel = ViewModelProviders.of(this)[GlobalViewModel::class.java]


        addPreferencesFromResource(R.xml.design_preferences)
        initOnPreferenceChangeListener()
        initPreferenceIconSpace()
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        globalViewModel.settings.themeChanged = true
        (requireActivity() as SettingsActivity).run {
            recreate()
        }
        return true
    }

    private fun initOnPreferenceChangeListener() {
        listOf<Preference?>(
            findPreference(getString(R.string.theme_key))
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