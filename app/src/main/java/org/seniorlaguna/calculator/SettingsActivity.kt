package org.seniorlaguna.calculator

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import org.seniorlaguna.calculator.basic.BasicFragment
import org.seniorlaguna.calculator.basic.BasicSettingsFragment
import org.seniorlaguna.calculator.example.DefaultSettingsFragment
import org.seniorlaguna.calculator.example.GraphFragment
import org.seniorlaguna.calculator.graph.GraphSettingsFragment
import org.seniorlaguna.calculator.scientific.ScientificFragment
import org.seniorlaguna.calculator.scientific.ScientificSettingsFragment

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_SELECTED_TOOL = "extra_selected_tool"
    }

    // global view model
    private lateinit var globalViewModel: GlobalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        // get global view model
        globalViewModel = ViewModelProviders.of(this)[GlobalViewModel::class.java]

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // show settings fragment
        val selection = intent.getIntExtra(EXTRA_SELECTED_TOOL, BasicFragment.TOOL_ID)

        supportFragmentManager.let { manager ->

            manager.beginTransaction().apply {

                // hide current fragment
                manager.findFragmentByTag(globalViewModel.settings.currentTool.toString())
                    ?.let { current ->
                        Log.d("FRAGMENT", "hide current fragment")
                        hide(current)
                    }

                // show selected tool if already added
                manager.findFragmentByTag(selection.toString())?.let { fragment ->
                    Log.d("FRAGMENT", "show already shown")
                    show(fragment)
                    commit()
                    return@apply
                }

                Log.d("FRAGMENT", "add fragment")
                // add fragment
                add(
                    R.id.fragment_container,
                    when (selection) {
                        BasicFragment.TOOL_ID -> BasicSettingsFragment()
                        ScientificFragment.TOOL_ID -> ScientificSettingsFragment()
                        GraphFragment.TOOL_ID -> GraphSettingsFragment()
                        else -> DefaultSettingsFragment()
                    },
                    selection.toString()
                )

                commit()

            }
        }

    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.back -> finish()
        }

    }

}
