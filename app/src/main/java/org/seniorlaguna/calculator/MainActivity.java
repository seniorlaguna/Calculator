package org.seniorlaguna.calculator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import com.udojava.evalex.Expression;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    public static final int DEFAULT_PRECISION = 1024;
    public static final int DEFAULT_SCALE = 2;

    //Constants for app rating
    public static final String PREFERENCE_ID = "prefs";
    public static final String PREFERENCE_NAME = "rated";
    public static final Integer PREFERENCE_RATE_BORDER = 10;

    Toolbar mToolbar;
    EditText mDisplay;
    GridLayout mGridLayout;

    Integer mPrecision;
    Integer mScale;
    Boolean mRound;
    Boolean mDeleteResult;
    Boolean mEqualsPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.inflateMenu(R.menu.menu);

        mDisplay = (EditText) findViewById(R.id.display);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mDisplay.setShowSoftInputOnFocus(false);
        }
        else {
            mDisplay.setInputType(0);
        }

        mGridLayout = (GridLayout) findViewById(R.id.grid_layout);
        scaleButtons();

        //Long click for one delete
        findViewById(R.id.btn_delete_one).setOnLongClickListener(this);

        readPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        readPreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, SettingsActivity.class));
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_delete_all:
                deleteAll();
                break;

            case R.id.btn_delete_one:
                deleteOne();
                break;

            case R.id.btn_equals:
                mEqualsPressed = true;
                calc();
                break;

            case R.id.btn_pi:
                insertIntoDisplay(getString(R.string.pi_symbol));
                break;

            case R.id.btn_div:
                insertIntoDisplay(getString(R.string.divide));
                break;

            case R.id.btn_mult:
                insertIntoDisplay(getString(R.string.multiply));
                break;

            case R.id.btn_root:
                insertIntoDisplay(getString(R.string.square_root_function));
                break;

            case R.id.btn_dot:
                insertIntoDisplay(getString(R.string.dot));
                break;

            case R.id.btn_fak:
                insertIntoDisplay(getString(R.string.factorial_function));
                break;

            default:
                insertIntoDisplay(((Button) v).getText().toString());
                break;

        }

    }

    @Override
    public boolean onLongClick(View v) {

        switch (v.getId()) {

            case R.id.btn_delete_one:
                deleteAll();
                return true;

        }

        return false;
    }

    @Override
    public void onBackPressed() {

        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_ID, MODE_PRIVATE);
        int counter = sharedPreferences.getInt(PREFERENCE_NAME, 0);

        //ask user for app rating
        if (counter > PREFERENCE_RATE_BORDER) {
            new AppRatingAlert().show(getFragmentManager(), "");
            sharedPreferences.edit().putInt(PREFERENCE_NAME, -1).apply();
        }

        //user already saw this question so don't annoy him again
        else if (counter < 0) {
            super.onBackPressed();
        }

        //give the user more time to experience the app
        else {
            sharedPreferences.edit().putInt(PREFERENCE_NAME, counter + 1).apply();
            super.onBackPressed();
        }


    }

    protected void readPreferences() {

        //read precision
        try {
            mPrecision = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.prefs_precision_key), new Integer(DEFAULT_PRECISION).toString()));
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString(getString(R.string.prefs_precision_key), new Integer(mPrecision).toString()).apply();
        } catch (Exception e) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString(getString(R.string.prefs_precision_key), new Integer(DEFAULT_PRECISION).toString()).apply();
            mPrecision = DEFAULT_PRECISION;
        }

        //read scale
        try {
            mScale = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.prefs_scale_key), new Integer(DEFAULT_SCALE).toString()));
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString(getString(R.string.prefs_scale_key), new Integer(mScale).toString()).apply();
        } catch (Exception e) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString(getString(R.string.prefs_scale_key), new Integer(DEFAULT_SCALE).toString()).apply();
            mScale = DEFAULT_SCALE;
        }

        //read round
        mRound = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.prefs_round_key), true);

        //read auto delete
        mDeleteResult = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.prefs_auto_delete_key), false);
    }

    protected void scaleButtons() {
        //Divide by 5.1 for space between buttons
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int size = screenWidth / 51 * 10;
        int margin = (screenWidth - (size * 5)) / 10;

        for (int i=0; i<mGridLayout.getChildCount(); i++) {
            Button button = (Button) mGridLayout.getChildAt(i);
            GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) button.getLayoutParams();
            layoutParams.width = size;
            layoutParams.height = size;
            layoutParams.setMargins(margin, margin, margin, margin);

            //Elevation
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                button.setElevation(10000);
            }

            button.setLayoutParams(layoutParams);
        }
    }

    protected void deleteAll() {
        mDisplay.setText(getString(R.string.empty));
    }

    protected void deleteOne() {

        if (mDisplay.getText().toString().contains(getString(R.string.error))) {
            mDisplay.setText(getString(R.string.empty));
            return;
        }

        try {
            int cursor = mDisplay.getSelectionStart();
            String oldText = mDisplay.getText().toString();
            String newText = oldText.substring(0, cursor - 1) + oldText.substring(cursor);
            mDisplay.setText(newText);
            mDisplay.setSelection(cursor - 1);
        } catch (Exception e) {}
    }

    protected void calc() {
        String term = mDisplay.getText().toString();

        //Empty Term
        if (term.isEmpty()) {
            return;
        }

        //Replace symbols
        term = term.replace(getString(R.string.square_root_symbol), getString(R.string.square_root));
        term = term.replace(getString(R.string.pi_symbol), getString(R.string.pi));

        Expression expression = new Expression(term);
        expression.addFunction(Functions.factorial);
        expression.setPrecision(mPrecision);

        try {

            //Calculate and format result
            BigDecimal result = expression.eval();
            result = result.setScale(mScale, mRound ? BigDecimal.ROUND_HALF_UP : BigDecimal.ROUND_HALF_DOWN);
            result = result.stripTrailingZeros();

            mDisplay.setText(result.toPlainString());


        } catch (Exception e) {
            mDisplay.setText(getString(R.string.error));
        }

        //place cursor at the end
        mDisplay.setSelection(mDisplay.getText().length());
    }

    protected void insertIntoDisplay(String pText) {
        if (mDisplay.getText().toString().contains(getString(R.string.error)) || (mDeleteResult && mEqualsPressed)) {
            deleteAll();
            mEqualsPressed = false;
        }

        int cursor = mDisplay.getSelectionStart();
        String oldText = mDisplay.getText().toString();
        String newText = getString(R.string.empty);
        try {
            newText = oldText.substring(0, cursor) + pText + oldText.substring(cursor);
        } catch (Exception e) {
            newText = oldText + pText;
        }
        mDisplay.setText(newText);
        mDisplay.setSelection(cursor+pText.length());
    }


    /**
     * dialog for app rating
     */
    public static class AppRatingAlert extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.rate_app)
                    .setPositiveButton(R.string.rate_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.playstore_package_url))));
                }
            }).setNegativeButton(R.string.rate_no, null);

            return builder.create();
        }
    }
}
