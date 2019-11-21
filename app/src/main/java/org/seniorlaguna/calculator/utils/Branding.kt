package org.seniorlaguna.calculator.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import org.seniorlaguna.calculator.R

private const val APP_STARTS_BEFORE_ASKING_KEY = "APP_STARTS_BEFORE_ASKING_KEY"
private const val APP_STARTS_BEFORE_ASKING = 5

fun openPlaystore(context: Context, developer : Boolean = true) {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(if (developer) context.getString(R.string.playstore_market_developer_url) else context.getString(R.string.playstore_market_package_url))
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        openUrl(context, if (developer) context.getString(R.string.playstore_http_developer_url) else context.getString(R.string.playstore_http_package_url))
    }
}

fun openGithub(context: Context) {
    openUrl(context, context.getString(R.string.github_url))
}

private fun openUrl(context: Context, url : String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // TODO("Add exception handling if no activity found")
    }
}

fun askForAppRating(context: Context) {

    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    val starts = prefs.getInt(APP_STARTS_BEFORE_ASKING_KEY, 0)

    // already asked for rating
    if (starts < 0) return

    // don't ask yet wait some time
    if (starts < APP_STARTS_BEFORE_ASKING) {
        prefs.edit().putInt(APP_STARTS_BEFORE_ASKING_KEY, starts + 1).apply()
    }
    else {
        prefs.edit().putInt(APP_STARTS_BEFORE_ASKING_KEY, -1).apply()

        // show request
        val builder = AlertDialog.Builder(context)
        builder.setView(R.layout.app_rating)
        builder.setNegativeButton(R.string.app_rating_request_no, null)
        builder.setPositiveButton(R.string.app_rating_request_yes) { _, _ -> openPlaystore(context, false)}
        builder.show()
    }

}