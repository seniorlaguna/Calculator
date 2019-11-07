package org.seniorlaguna.calculator

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import org.seniorlaguna.calculator.basic.BasicFragment
import org.seniorlaguna.calculator.basic.BasicSettingsFragment
import org.seniorlaguna.calculator.scientific.ScientificSettingsFragment

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_SELECTED_TOOL = "extra_selected_tool"
    }

    // global view model
    private lateinit var globalViewModel: GlobalViewModel

    // tool settings fragments
    private val basicSettingsFragment: BasicSettingsFragment by lazy(::BasicSettingsFragment)
    private val scientificSettingsFragment : ScientificSettingsFragment by lazy(::ScientificSettingsFragment)

    override fun onCreate(savedInstanceState: Bundle?) {

        // get global view model
        globalViewModel = ViewModelProviders.of(this)[GlobalViewModel::class.java]

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // show selected fragment
        supportFragmentManager.beginTransaction().add(R.id.fragment_container,
            when (intent.getIntExtra(EXTRA_SELECTED_TOOL, BasicFragment.TOOL_ID)) {
                BasicFragment.TOOL_ID -> basicSettingsFragment
                else -> scientificSettingsFragment
            } as Fragment
        ).commit()
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.back -> finish()
        }

    }

}
