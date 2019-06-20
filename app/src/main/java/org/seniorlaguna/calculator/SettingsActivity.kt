package org.seniorlaguna.calculator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import org.seniorlaguna.calculator.basic.BasicViewModel
import org.seniorlaguna.calculator.basic.settings.BasicSettingsFragment
import org.seniorlaguna.calculator.scientific.ScientificViewModel
import org.seniorlaguna.calculator.scientific.settings.ScientificSettingsFragment

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    companion object {

        private var currentSettings : Int = R.id.navigation_drawer_basic_calculator

        fun start(context: Context, navigationId : Int) {
            currentSettings = navigationId
            context.startActivity(Intent(context, SettingsActivity::class.java))
        }

    }

    // basic calculator
    lateinit var basicSettingsFragment: BasicSettingsFragment
    lateinit var basicViewModel: BasicViewModel

    // scientific calculator
    lateinit var scientificSettingsFragment : ScientificSettingsFragment
    lateinit var scientificViewModel: ScientificViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initViewModels()
        initFragments()

        supportFragmentManager.beginTransaction().add(R.id.fragment_container,
            when (currentSettings) {
                R.id.navigation_drawer_basic_calculator -> basicSettingsFragment
                R.id.navigation_drawer_scientific_calculator -> scientificSettingsFragment
                else -> basicSettingsFragment
            } as Fragment
        ).commit()
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.back -> finish()
        }

    }

    private fun initViewModels() {
        basicViewModel = ViewModelProviders.of(this).get(BasicViewModel::class.java)
        scientificViewModel = ViewModelProviders.of(this).get(ScientificViewModel::class.java)
    }

    private fun initFragments() {
        basicSettingsFragment = BasicSettingsFragment(this)
        scientificSettingsFragment = ScientificSettingsFragment(this)

    }

}
