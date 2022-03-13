package org.seniorlaguna.calculator

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.seniorlaguna.calculator.settings.SettingsActivity
import org.seniorlaguna.calculator.tool.basic.BasicFragment
import org.seniorlaguna.calculator.tool.scientific.ScientificFragment
import org.seniorlaguna.calculator.utils.*
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // global view model
    private lateinit var globalViewModel: GlobalViewModel
    private var themeId : Int = 0

    // rewarded ads
    private var mRewardedAd : RewardedAd? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        // get global view model
        globalViewModel = ViewModelProviders.of(this)[GlobalViewModel::class.java]
        setTheme(globalViewModel.settings.theme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (!globalViewModel.settings.isAdFree) {
            initAds()
        }


        initToolbar()

        // on app start
        if (savedInstanceState == null) {
            startTool(globalViewModel.settings.currentTool)

            // ask for app rating
            askForAppRating(this)
        }

    }

    private fun initAds() {
        // ads
        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917", adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("MainActivity", adError.message)
                mRewardedAd = null
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                Log.d("MainActivity", "Ad was loaded.")
                mRewardedAd = rewardedAd
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // start settings activity
        startActivity(Intent(this, SettingsActivity::class.java))
        return true
    }

    private fun onRewardedAdsSuccess() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 30)
        globalViewModel.settings.adFreeUntil = calendar.timeInMillis
        recreate()
        Toast.makeText(this, R.string.removed_ads_toast, Toast.LENGTH_SHORT).show()
    }

    private fun showRewardedAds() {
        if (globalViewModel.settings.isAdFree) {
            Log.d("MainActivity", "ads already removed")
            Toast.makeText(this, R.string.already_removed_toast, Toast.LENGTH_SHORT).show()

            return
        }

        mRewardedAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d("MainActivity", "Ad was dismissed.")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.d("MainActivity", "Ad failed to show.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d("MainActivity", "Ad showed fullscreen content.")
                // Called when ad is dismissed.
                // Don't set the ad reference to null to avoid showing the ad a second time.
                mRewardedAd = null
            }
        }

        if (mRewardedAd != null) {
            mRewardedAd?.show(this) {
                Log.d("MainActivity", "reward earned")
                onRewardedAdsSuccess()
            }
        } else {
            Log.d("MainActivity", "The rewarded ad wasn't ready yet.")
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.navigation_drawer_remove_ads -> showRewardedAds()
            R.id.navigation_drawer_rate -> openPlaystore(this, false)
            R.id.navigation_drawer_more_apps -> openPlaystore(this)
            R.id.navigation_drawer_more_senior_laguna -> openGithub(this)

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
        globalViewModel.toolbarTitle.observe(this, {
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

    override fun setTheme(resId: Int) {
        super.setTheme(resId)
        themeId = resId
    }

    override fun onResume() {
        super.onResume()
        if (themeId != globalViewModel.settings.theme) {
            recreate()
        }
    }

    // start tool and save selection
    private fun startTool(toolId : Int) {

        // show selection in navigation drawer
        navigation_view.setCheckedItem(when (toolId) {
            ScientificFragment.TOOL_ID -> R.id.navigation_drawer_scientific_calculator
            else -> R.id.navigation_drawer_basic_calculator
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
                        ScientificFragment.TOOL_ID -> ScientificFragment()
                        else -> BasicFragment()
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
