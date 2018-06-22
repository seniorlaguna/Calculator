package de.seniorlaguna.calculator;

import android.content.Intent;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import com.udojava.evalex.Expression;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbar;
    EditText mDisplay;
    GridLayout mGridLayout;

    Integer mPrecision;
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

    protected void readPreferences() {
        try {
            mPrecision = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.prefs_precision_key), "8"));
        } catch (Exception e) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString(getString(R.string.prefs_precision_key), getString(R.string.prefs_precision_default)).apply();
            mPrecision = 8;
        }

        mDeleteResult = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.prefs_auto_delete_key), false);
    }

    protected void scaleButtons() {
        //Divide by 5.1 for space between buttons
        int size = getResources().getDisplayMetrics().widthPixels / 51 * 10;

        for (int i=0; i<mGridLayout.getChildCount(); i++) {
            Button button = (Button) mGridLayout.getChildAt(i);
            GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) button.getLayoutParams();
            layoutParams.width = size;
            layoutParams.height = size;

            //Elevation
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                button.setElevation(10000);
            }

            button.setLayoutParams(layoutParams);
        }
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
        term = term.replace(getString(R.string.square_root_symbol), getString(R.string.square_root));
        term = term.replace(getString(R.string.pi_symbol), getString(R.string.pi));

        Expression expression = new Expression(term);

        expression.addFunction(Functions.factorial);

        expression.setPrecision(mPrecision);

        try {
            mDisplay.setText(expression.eval().toPlainString());
        } catch (Exception e) {
            mDisplay.setText(getString(R.string.error));
        }
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

}
