package org.seniorlaguna.calculator

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_basic.*
import org.seniorlaguna.calculator.basic.BasicFragment
import org.seniorlaguna.calculator.basic.BasicViewModel
import org.seniorlaguna.calculator.scientific.ScientificFragment
import org.seniorlaguna.calculator.scientific.ScientificViewModel
import org.seniorlaguna.calculator.utils.askForAppRating
import org.seniorlaguna.calculator.utils.openGithub
import org.seniorlaguna.calculator.utils.openPlaystore
import org.seniorlaguna.calculator.utils.showInstruction

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        private val CURRENT_TOOL_KEY = "currentTool"
    }

    private var currentTool
        get() = PreferenceManager.getDefaultSharedPreferences(this).getInt(CURRENT_TOOL_KEY, R.id.navigation_drawer_basic_calculator)
    set(value) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(CURRENT_TOOL_KEY, value).commit()
    }

    // basic calculator
    lateinit var basicFragment: BasicFragment
    lateinit var basicViewModel: BasicViewModel

    // scientific calculator
    lateinit var scientificFragment: ScientificFragment
    lateinit var scientificViewModel: ScientificViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar()
        initViewModel()
        initFragments()

        startTool(currentTool, true)

        // ask for app rating
        askForAppRating(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        SettingsActivity.start(this, currentTool)
        return true
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.navigation_drawer_rate -> openPlaystore(this, false)
            R.id.navigation_drawer_more_apps -> openPlaystore(this)
            R.id.navigation_drawer_more_senior_laguna -> openGithub(this)
            else -> startTool(p0.itemId)
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

    private fun initViewModel() {
        basicViewModel = ViewModelProviders.of(this).get(BasicViewModel::class.java)
        scientificViewModel = ViewModelProviders.of(this).get(ScientificViewModel::class.java)
    }

    private fun initFragments() {
        basicFragment = BasicFragment(this)
        scientificFragment = ScientificFragment(this)
    }

    // init methods
    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close).run {
            drawerArrowDrawable.color = ContextCompat.getColor(this@MainActivity, android.R.color.white)
            drawer_layout.addDrawerListener(this)
            syncState()
        }

        navigation_view.setNavigationItemSelectedListener(this)
    }

    // start tool
    private fun startTool(navigationId : Int, firstRun : Boolean = false) {

        navigation_view.setCheckedItem(navigationId)

        val currentFragment = when (currentTool) {
            R.id.navigation_drawer_basic_calculator -> basicFragment
            R.id.navigation_drawer_scientific_calculator -> scientificFragment
            else -> basicFragment
        }

        currentTool = navigationId

        val chosenFragment = when (currentTool) {
                R.id.navigation_drawer_basic_calculator -> basicFragment
                R.id.navigation_drawer_scientific_calculator -> scientificFragment
                else -> basicFragment
            }

        supportFragmentManager.beginTransaction().apply {
            if (!firstRun) hide(currentFragment)

            if (supportFragmentManager.fragments.contains(chosenFragment)) {
                show(chosenFragment)
            }
            else add(R.id.fragment_container, chosenFragment)
            commit()
        }


    }


    // public interface
    fun setToolbarTitle(titleRes : Int) {
        toolbar_title.text = getString(titleRes)
    }
}
