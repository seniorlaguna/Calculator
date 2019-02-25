package org.seniorlaguna.calculator;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    Preference mClearHistoryButton;
    Preference mMoreAppsButton;
    Preference mMoreSeniorLagunaButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        

        mClearHistoryButton = findPreference(getString(R.string.prefs_clear_history_key));
        mMoreAppsButton = findPreference(getString(R.string.prefs_more_apps_key));
        mMoreSeniorLagunaButton = findPreference(getString(R.string.prefs_more_seniorlaguna_key));

        mClearHistoryButton.setOnPreferenceClickListener(this);
        mMoreAppsButton.setOnPreferenceClickListener(this);
        mMoreSeniorLagunaButton.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        switch (preference.getTitleRes()) {

            case R.string.prefs_clear_history_title:
                clearHistory();
                break;

            case R.string.prefs_more_apps_title:
                openPlaystore();
                break;

            case R.string.prefs_more_seniorlaguna_title:
                openGithub();
                break;

        }

        return true;
    }

    protected void clearHistory() {
        getPreferenceManager().getSharedPreferences().edit().putString(MainActivity.PREFERENCE_NAME_HISTORY, "").apply();
    }

    protected void openPlaystore() {

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(getString(R.string.playstore_url_market)));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(getString(R.string.playstore_url_https)));
            startActivity(intent);
        }

    }

    protected void openGithub() {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getString(R.string.github_url)));
        startActivity(intent);

    }
}
