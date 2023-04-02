package org.seniorlaguna.calculator

import android.app.Activity
import android.app.Application
import android.content.Context
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
import androidx.lifecycle.*
import com.google.android.gms.ads.*
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.material.navigation.NavigationView
import org.seniorlaguna.calculator.databinding.ActivityMainBinding
import org.seniorlaguna.calculator.settings.SettingsActivity
import org.seniorlaguna.calculator.tool.basic.BasicFragment
import org.seniorlaguna.calculator.tool.scientific.ScientificFragment
import org.seniorlaguna.calculator.utils.askForAppRating
import org.seniorlaguna.calculator.utils.openGithub
import org.seniorlaguna.calculator.utils.openPlaystore
import java.util.*

private const val LOG_TAG = "AppOpenAdManager"
private const val AD_UNIT_ID = "ca-app-pub-7519220681088057/3826693588"

/** Application class that initializes, loads and show ads when activities change states. */
class CalculatorApplication : Application(), Application.ActivityLifecycleCallbacks, LifecycleObserver {

    private lateinit var appOpenAdManager: AppOpenAdManager
    private var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)

        // Log the Mobile Ads SDK version.
        Log.d(LOG_TAG, "Google Mobile Ads SDK Version: " + MobileAds.getVersion())

        MobileAds.initialize(this) {}
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        appOpenAdManager = AppOpenAdManager()
    }

    /** LifecycleObserver method that shows the app open ad when the app moves to foreground. */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        // Show the ad (if available) when the app moves to foreground.
        currentActivity?.let { appOpenAdManager.showAdIfAvailable(it) }
    }

    /** ActivityLifecycleCallback methods. */
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        // An ad activity is started when an ad is showing, which could be AdActivity class from Google
        // SDK or another activity class implemented by a third party mediation partner. Updating the
        // currentActivity only when an ad is not showing will ensure it is not an ad activity, but the
        // one that shows the ad.
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity
        }
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}

    /**
     * Shows an app open ad.
     *
     * @param activity the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener)
    }

    /**
     * Interface definition for a callback to be invoked when an app open ad is complete (i.e.
     * dismissed or fails to show).
     */
    interface OnShowAdCompleteListener {
        fun onShowAdComplete()
    }

    /** Inner class that loads and shows app open ads. */
    private inner class AppOpenAdManager {

        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd = false
        var isShowingAd = false

        /** Keep track of the time an app open ad is loaded to ensure you don't show an expired ad. */
        private var loadTime: Long = 0

        /**
         * Load an ad.
         *
         * @param context the context of the activity that loads the ad
         */
        fun loadAd(context: Context) {
            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable()) {
                return
            }

            isLoadingAd = true
            val request = AdRequest.Builder().build()
            AppOpenAd.load(
                context,
                AD_UNIT_ID,
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                object : AppOpenAd.AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    override fun onAdLoaded(ad: AppOpenAd) {
                        appOpenAd = ad
                        isLoadingAd = false
                        loadTime = Date().time
                        Log.d(LOG_TAG, "onAdLoaded.")
                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error.
                     */
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        isLoadingAd = false
                        Log.d(LOG_TAG, "onAdFailedToLoad: " + loadAdError.message)
                    }
                }
            )
        }

        /** Check if ad was loaded more than n hours ago. */
        private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
            val dateDifference: Long = Date().time - loadTime
            val numMilliSecondsPerHour: Long = 3600000
            return dateDifference < numMilliSecondsPerHour * numHours
        }

        /** Check if ad exists and can be shown. */
        private fun isAdAvailable(): Boolean {
            // Ad references in the app open beta will time out after four hours, but this time limit
            // may change in future beta versions. For details, see:
            // https://support.google.com/admob/answer/9341964?hl=en
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
        }

        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity the activity that shows the app open ad
         */
        fun showAdIfAvailable(activity: Activity) {
            showAdIfAvailable(
                activity,
                object : OnShowAdCompleteListener {
                    override fun onShowAdComplete() {
                        // Empty because the user will go back to the activity that shows the ad.
                    }
                }
            )
        }

        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity the activity that shows the app open ad
         * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
         */
        fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {
                Log.d(LOG_TAG, "The app open ad is already showing.")
                return
            }

            // If the app open ad is not available yet, invoke the callback then load the ad.
            if (!isAdAvailable()) {
                Log.d(LOG_TAG, "The app open ad is not ready yet.")
                onShowAdCompleteListener.onShowAdComplete()
                loadAd(activity)
                return
            }

            Log.d(LOG_TAG, "Will show ad.")

            appOpenAd!!.setFullScreenContentCallback(
                object : FullScreenContentCallback() {
                    /** Called when full screen content is dismissed. */
                    override fun onAdDismissedFullScreenContent() {
                        // Set the reference to null so isAdAvailable() returns false.
                        appOpenAd = null
                        isShowingAd = false
                        Log.d(LOG_TAG, "onAdDismissedFullScreenContent.")

                        onShowAdCompleteListener.onShowAdComplete()
                        loadAd(activity)
                    }

                    /** Called when fullscreen content failed to show. */
                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        appOpenAd = null
                        isShowingAd = false
                        Log.d(LOG_TAG, "onAdFailedToShowFullScreenContent: " + adError.message)

                        onShowAdCompleteListener.onShowAdComplete()
                        loadAd(activity)
                    }

                    /** Called when fullscreen content is shown. */
                    override fun onAdShowedFullScreenContent() {
                        Log.d(LOG_TAG, "onAdShowedFullScreenContent.")
                    }
                }
            )
            isShowingAd = true
            appOpenAd!!.show(activity)
        }
    }
}

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // global view model
    private lateinit var globalViewModel: GlobalViewModel
    private var themeId : Int = 0

    // view binding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        // get global view model
        globalViewModel = ViewModelProvider(this)[GlobalViewModel::class.java]
        setTheme(globalViewModel.settings.theme)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()

        // on app start
        if (savedInstanceState == null) {
            startTool(globalViewModel.settings.currentTool)

            // ask for app rating
            askForAppRating(this)
        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // start settings activity
        startActivity(Intent(this, SettingsActivity::class.java))
        return true
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
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

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        else {
            super.onBackPressed()
        }
    }

    // init methods
    private fun initToolbar() {

        // make toolbar visible in portrait and invisible in landscape mode
        binding.toolbar.visibility = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) View.VISIBLE else View.GONE


        // set support action bar without default title
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // register title observer
        globalViewModel.toolbarTitle.observe(this) {
            binding.toolbarTitle.text = it
        }

        // add navigation drawer
        ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close).run {
            drawerArrowDrawable.color = ContextCompat.getColor(this@MainActivity, android.R.color.white)
            binding.drawerLayout.addDrawerListener(this)
            syncState()
        }

        // set navigation drawer listener
        binding.navigationView.setNavigationItemSelectedListener(this)
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
        binding.navigationView.setCheckedItem(when (toolId) {
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
