package org.seniorlaguna.calculator.settings

import android.os.Bundle
import androidx.annotation.Keep
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import org.seniorlaguna.calculator.GlobalViewModel
import org.seniorlaguna.calculator.R


@Keep
class AdvancedSettingsFragment : PreferenceFragmentCompat() {

    // global view model
    private lateinit var globalViewModel: GlobalViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        // get global view model
        globalViewModel = ViewModelProviders.of(this)[GlobalViewModel::class.java]

        addPreferencesFromResource(R.xml.advanced_preferences)
        initPreferenceIconSpace(false)
    }

    private fun initPreferenceIconSpace(enabled : Boolean) {

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