package de.seniorlaguna.calculator;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import com.udojava.evalex.Expression;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbar;
    EditText mDisplay;
    GridLayout mGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "Einstellungen", Toast.LENGTH_SHORT).show();
        return true;
    }

    protected void scaleButtons() {
        int size = getResources().getDisplayMetrics().widthPixels / 5;

        for (int i=0; i<mGridLayout.getChildCount(); i++) {
            Button button = (Button) mGridLayout.getChildAt(i);
            GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) button.getLayoutParams();
            layoutParams.width = size;
            layoutParams.height = size;
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

            case R.id.btn_func:
                selectFunction();
                break;

            case R.id.btn_equals:
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

            default:
                insertIntoDisplay(((Button) v).getText().toString());
                break;

        }

    }

    protected void deleteAll() {
        mDisplay.setText("");
    }

    protected void deleteOne() {
        try {
            int cursor = mDisplay.getSelectionStart();
            String oldText = mDisplay.getText().toString();
            String newText = oldText.substring(0, cursor - 1) + oldText.substring(cursor);
            mDisplay.setText(newText);
            mDisplay.setSelection(cursor - 1);
        } catch (Exception e) {}
    }

    protected void selectFunction() {}

    protected void calc() {
        String term = mDisplay.getText().toString();
        term = term.replace("√", "sqrt");
        Expression expression = new Expression(term);
        expression.setPrecision(32);

        try {
            mDisplay.setText(expression.eval().toString());
        } catch (Exception e) {
            mDisplay.setText("Error");
        }
        mDisplay.setSelection(mDisplay.getText().length());
    }

    protected void insertIntoDisplay(String pText) {
        if (mDisplay.getText().toString().contains("Error")) {
            deleteAll();
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
