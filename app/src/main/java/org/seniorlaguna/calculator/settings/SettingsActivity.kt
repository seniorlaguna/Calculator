package org.seniorlaguna.calculator.settings

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import org.seniorlaguna.calculator.GlobalViewModel
import org.seniorlaguna.calculator.R

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    // global view model
    private lateinit var globalViewModel: GlobalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        // get global view model
        globalViewModel = ViewModelProviders.of(this)[GlobalViewModel::class.java]
        setTheme(globalViewModel.settings.theme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // show settings fragment
        if (!globalViewModel.settings.themeChanged) {
            Log.d("SettingsActivity", "Normal start")
            supportFragmentManager.beginTransaction().apply {
                add(
                    R.id.fragment_container,
                    SettingsOverviewFragment(),
                    "overview"
                )
                commit()
            }
        }
        else {
            Log.d("SettingsActivity", "theme changed start")
            globalViewModel.settings.themeChanged = false
        }

    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.back -> {
                if (supportFragmentManager.backStackEntryCount == 0) finish()
                supportFragmentManager.popBackStack()
            }
        }

    }

}
