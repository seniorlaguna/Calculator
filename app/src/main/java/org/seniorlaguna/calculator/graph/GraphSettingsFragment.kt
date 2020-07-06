package org.seniorlaguna.calculator.graph

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.preference.*
import org.seniorlaguna.calculator.Calculation
import org.seniorlaguna.calculator.GlobalViewModel
import org.seniorlaguna.calculator.R
import java.lang.Exception


class GraphSettingsFragment: PreferenceFragmentCompat() {

    // global view model
    private lateinit var globalViewModel: GlobalViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        // get global view model
        globalViewModel = ViewModelProviders.of(this)[GlobalViewModel::class.java]

        addPreferencesFromResource(R.xml.graph_preferences)
        initPreferenceIconSpace()
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