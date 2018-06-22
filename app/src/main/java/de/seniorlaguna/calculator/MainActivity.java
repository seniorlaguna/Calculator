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
            mPrecision = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("precision", "8"));
        } catch (Exception e) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("precision", "8").apply();
            mPrecision = 8;
        }

        mDeleteResult = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("delete_after_equals", false);
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

            case R.id.btn_pi:
                insertIntoDisplay("π");
                break;

            case R.id.btn_equals:
                mEqualsPressed = true;
                calc();
                break;

            case R.id.btn_div:
                insertIntoDisplay("/");
                break;

            case R.id.btn_mult:
                insertIntoDisplay("*");
                break;

            case R.id.btn_root:
                insertIntoDisplay("√(");
                break;

            case R.id.btn_dot:
                insertIntoDisplay(".");
                break;

            case R.id.btn_fak:
                insertIntoDisplay("fak(");
                break;

            default:
                insertIntoDisplay(((Button) v).getText().toString());
                break;

        }

    }

    protected void deleteAll() {
        mDisplay.setText("");
    }

    protected void deleteOne() {

        if (mDisplay.getText().toString().contains("Error")) {
            mDisplay.setText("");
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
        term = term.replace("√", "sqrt");
        term = term.replace("π", "pi");

        Expression expression = new Expression(term);

        expression.addFunction(Functions.factorial);

        expression.setPrecision(mPrecision);

        try {
            mDisplay.setText(expression.eval().toPlainString());
        } catch (Exception e) {
            mDisplay.setText("Error");
        }
        mDisplay.setSelection(mDisplay.getText().length());
    }

    protected void insertIntoDisplay(String pText) {
        if (mDisplay.getText().toString().contains("Error") || (mDeleteResult && mEqualsPressed)) {
            deleteAll();
            mEqualsPressed = false;
        }

        int cursor = mDisplay.getSelectionStart();
        String oldText = mDisplay.getText().toString();
        String newText = "";
        try {
            newText = oldText.substring(0, cursor) + pText + oldText.substring(cursor);
        } catch (Exception e) {
            newText = oldText + pText;
        }
        mDisplay.setText(newText);
        mDisplay.setSelection(cursor+pText.length());
    }

}
