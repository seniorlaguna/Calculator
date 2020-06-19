package org.seniorlaguna.calculator

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.seniorlaguna.calculator.basic.BasicFragment
import org.seniorlaguna.calculator.scientific.ScientificFragment
import org.seniorlaguna.calculator.utils.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // global view model
    private lateinit var globalViewModel: GlobalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        // get global view model
        globalViewModel = ViewModelProviders.of(this)[GlobalViewModel::class.java]

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar()

        // on app start
        if (savedInstanceState == null) {
            startTool(globalViewModel.settings.currentTool)

            // ask for app rating
            askForAppRating(this)

            // ask for donation
            askForDonation(this)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        // start settings activity depending on the current selected tool
        startActivity(Intent(this, SettingsActivity::class.java)
            .putExtra(SettingsActivity.EXTRA_SELECTED_TOOL,globalViewModel.settings.currentTool)
        )
        return true
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.navigation_drawer_rate -> openPlaystore(this, false)
            R.id.navigation_drawer_more_apps -> openPlaystore(this)
            R.id.navigation_drawer_more_senior_laguna -> openGithub(this)
            R.id.navigation_drawer_donation -> openPatreon(this)

            R.id.navigation_drawer_basic_calculator -> startTool(BasicFragment.TOOL_ID)
            R.id.navigation_drawer_scientific_calculator -> startTool(ScientificFragment.TOOL_ID)
        }

        onBackPressed()
        return true
    }

    override fun onBackPressed() {

        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        else {
            super.onBackPressed()
        }
    }

    // init methods
    private fun initToolbar() {

        // make toolbar visible in portrait and invisible in landscape mode
        toolbar.visibility = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) View.VISIBLE else View.GONE


        // set support action bar without default title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // register title observer
        globalViewModel.toolbarTitle.observe(this, Observer {
            toolbar_title.text = it
        })

        // add navigation drawer
        ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close).run {
            drawerArrowDrawable.color = ContextCompat.getColor(this@MainActivity, android.R.color.white)
            drawer_layout.addDrawerListener(this)
            syncState()
        }

        // set navigation drawer listener
        navigation_view.setNavigationItemSelectedListener(this)
    }

    // start tool and save selection
    private fun startTool(toolId : Int) {

        // show selection in navigation drawer
        navigation_view.setCheckedItem(when (toolId) {
            BasicFragment.TOOL_ID -> R.id.navigation_drawer_basic_calculator
            else -> R.id.navigation_drawer_scientific_calculator
        })

        supportFragmentManager.let { manager ->

            manager.beginTransaction().apply {

                // hide current fragment
                manager.findFragmentByTag(globalViewModel.settings.currentTool.toString())?.let { current ->
                    Log.d("FRAGMENT", "hide current fragment")
                    hide(current)
                }

                // show selected tool if already added
                manager.findFragmentByTag(toolId.toString())?.let { fragment ->
                    Log.d("FRAGMENT", "show already shown")
                    show(fragment)
                    commit()
                    return@apply
                }

                Log.d("FRAGMENT", "add fragment")
                // add fragment
                add(R.id.fragment_container,
                    when (toolId) {
                        BasicFragment.TOOL_ID  -> BasicFragment()
                        else -> ScientificFragment()
                    },
                    toolId.toString()
                )

                commit()

            }

        }


        // save new tool selection
        globalViewModel.settings.currentTool = toolId

    }

}
