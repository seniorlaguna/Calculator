package org.seniorlaguna.calculator.settings

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import org.seniorlaguna.calculator.GlobalViewModel
import org.seniorlaguna.calculator.R
import org.seniorlaguna.calculator.utils.openContact
import org.seniorlaguna.calculator.utils.openPrivacyPolicy
import org.seniorlaguna.calculator.utils.openTermsOfUse

class SettingsOverviewFragment: PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {

    // global view model
    private lateinit var globalViewModel: GlobalViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        // get global view model
        globalViewModel = ViewModelProvider(this)[GlobalViewModel::class.java]

        addPreferencesFromResource(R.xml.settings_overview)
        initOnPreferenceClickListener()
        initPreferenceIconSpace()
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        when (preference.key) {
            getString(R.string.settings_privacy_key) -> openPrivacyPolicy(requireContext())
            getString(R.string.settings_terms_key) -> openTermsOfUse(requireContext())
        getString(R.string.settings_contact_key) -> openContact(requireContext())
        }
        return true
    }

    private fun initOnPreferenceClickListener() {
        listOf<Preference?>(
            findPreference<Preference>(getString(R.string.settings_privacy_key)),
            findPreference<Preference>(getString(R.string.settings_terms_key)),
            findPreference<Preference>(getString(R.string.settings_contact_key))
        ).forEach {
            it?.onPreferenceClickListener = this
        }
    }

    private fun initPreferenceIconSpace(enabled : Boolean = false) {

        for (i in 0 until preferenceScreen.preferenceCount) {
            preferenceScreen.getPreference(i).run {
                isIconSpaceReserved = enabled
            }

        }

    }

}