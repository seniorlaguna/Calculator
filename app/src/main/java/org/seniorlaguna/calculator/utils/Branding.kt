package org.seniorlaguna.calculator.utils

import android.content.Intent
import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.core.view.LayoutInflaterCompat
import androidx.preference.PreferenceManager
import org.seniorlaguna.calculator.R

private val APP_STARTS_BEFORE_ASKING_KEY = "APP_STARTS_BEFORE_ASKING_KEY"
private val APP_STARTS_BEFORE_ASKING = 5

private val playstore_market_package_url = "market://details?id=org.seniorlaguna.calculator"
private val playstore_http_package_url = "https://play.google.com/store/apps/details?id=org.seniorlaguna.calculator"
private val playstore_market_developer_url = "market://play.google.com/store/apps/developer?id=Senior+Laguna"
private val playstore_http_developer_url = "https://play.google.com/store/apps/developer?id=Senior+Laguna"
private val github_url = "https://github.com/seniorlaguna?tab=repositories"

fun openPlaystore(context: Context, developer : Boolean = true) {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(if (developer) playstore_market_developer_url else playstore_market_package_url)
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(if (developer) playstore_http_developer_url else playstore_http_package_url)
        context.startActivity(intent)
    }
}

fun openGithub(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(github_url)
    context.startActivity(intent)
}

fun askForAppRating(context: Context) {

    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    val starts = prefs.getInt(APP_STARTS_BEFORE_ASKING_KEY, 0)

    // already asked for rating
    if (starts < 0) return

    // don't ask yet wait some time
    if (starts < 5) {
        prefs.edit().putInt(APP_STARTS_BEFORE_ASKING_KEY, starts + 1).commit()
    }
    else {
        prefs.edit().putInt(APP_STARTS_BEFORE_ASKING_KEY, -1).commit()

        // show request
        val builder = AlertDialog.Builder(context)
        builder.setView(R.layout.app_rating)
        builder.setNegativeButton(R.string.app_rating_request_no, null)
        builder.setPositiveButton(R.string.app_rating_request_yes, {_, _ -> openPlaystore(context, false)})
        builder.show()
    }

}