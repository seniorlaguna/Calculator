package org.seniorlaguna.calculator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, ViewPager.OnPageChangeListener {

    public static final int DEFAULT_PRECISION = 1024;
    public static final int DEFAULT_SCALE = 2;

    //Constants for app rating
    public static final String PREFERENCE_ID = "prefs";
    public static final String PREFERENCE_NAME = "rated";
    public static final Integer PREFERENCE_RATE_BORDER = 10;

    public static final Integer VERTICAL_PAGES = 2;
    public static final Integer HORIZONTAL_PAGES = 1;


    Toolbar mToolbar;
    TextView mToolbarTitle;
    ViewPager mViewPager;
    FragmentSlider mFragmentSlider;
    ArrayList<String> mHistory;
    ResultHistory mResultHistory;

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

        initToolbar();
        initViewPager();
        initButtons();
        initHistory();
        readPreferences();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            DisplayFragment.display.setVisibility(View.INVISIBLE);
        } catch (NullPointerException e) {}
    }

    @Override
    protected void onResume() {
        super.onResume();
        readPreferences();
        showDisplay();
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        deleteAll();
        onItemLongClick(adapterView, view, i, l);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        insertIntoDisplay(((TextView) view.findViewById(R.id.list_text_result)).getText().toString());
        mViewPager.setCurrentItem(1);
        return true;
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                setToolbarTitle(getString(R.string.history));
                break;

            case 1:
                setToolbarTitle(getString(R.string.app_name));
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) { }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    /**
     * Initiate all different parts of the app
     */
    protected void initToolbar() {
        //toolbar setup: use custom toolbar layout, disable standard toolbar title
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.inflateMenu(R.menu.menu);
    }

    protected void initViewPager() {
        //fragment sliding setup: set default fragment to display(1)
        mViewPager = findViewById(R.id.view_pager);
        mFragmentSlider = new FragmentSlider(this, getSupportFragmentManager());
        mViewPager.setAdapter(mFragmentSlider);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(1);
        showDisplay();
    }

    protected void initHistory() {
        readHistory();
        mResultHistory = new ResultHistory(this, mHistory);
    }

    protected void initButtons() {
        //button setup: scale them to fit the screen more or less
        mGridLayout = (GridLayout) findViewById(R.id.grid_layout);
        scaleButtons();

        //Long click for one delete
        findViewById(R.id.btn_delete_one).setOnLongClickListener(this);
    }


    /**
     * A small focus hack for edit text
     */

    protected void showDisplay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DisplayFragment.display.setVisibility(View.VISIBLE);
            }
        }, 500);
    }

    /**
     * Manage saved values like preferences or history
     */
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

    protected void readHistory() {
        mHistory = new ArrayList<>();
    }

    protected void addResultToHistory(String pTerm) {
        if (mHistory.isEmpty() || !mHistory.get(mHistory.size()-1).equals(pTerm)) {
            mHistory.add(0, pTerm);
            mResultHistory.notifyDataSetChanged();
        }
    }

    protected void saveHistory() {

    }

    /**
     * Button manipulation to fit screen
     */
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


    /**
     * Calculation functions and display manipulation
     */

    protected void setToolbarTitle(String pTitle) {
        mToolbarTitle.setText(pTitle);
    }

    protected void deleteAll() {
        DisplayFragment.display.setText(getString(R.string.empty));
    }

    protected void deleteOne() {

        if (DisplayFragment.display.getText().toString().contains(getString(R.string.error))) {
            DisplayFragment.display.setText(getString(R.string.empty));
            return;
        }

        try {
            int cursor = DisplayFragment.display.getSelectionStart();
            String oldText = DisplayFragment.display.getText().toString();
            String newText = oldText.substring(0, cursor - 1) + oldText.substring(cursor);
            DisplayFragment.display.setText(newText);
            DisplayFragment.display.setSelection(cursor - 1);
        } catch (Exception e) {}
    }

    protected void calc() {
        String term = DisplayFragment.display.getText().toString();

        //Empty Term
        if (term.isEmpty()) {
            return;
        }

        //Add term to history
        addResultToHistory(term);

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

            DisplayFragment.display.setText(result.toPlainString());
            addResultToHistory(result.toPlainString());

        } catch (Exception e) {
            DisplayFragment.display.setText(getString(R.string.error));
        }

        //place cursor at the end
        DisplayFragment.display.setSelection(DisplayFragment.display.getText().length());
    }

    protected void insertIntoDisplay(String pText) {
        if (DisplayFragment.display.getText().toString().contains(getString(R.string.error)) || (mDeleteResult && mEqualsPressed)) {
            deleteAll();
            mEqualsPressed = false;
        }

        int cursor = DisplayFragment.display.getSelectionStart();
        String oldText = DisplayFragment.display.getText().toString();
        String newText = getString(R.string.empty);
        try {
            newText = oldText.substring(0, cursor) + pText + oldText.substring(cursor);
        } catch (Exception e) {
            newText = oldText + pText;
        }
        DisplayFragment.display.setText(newText);
        DisplayFragment.display.setSelection(cursor+pText.length());
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

    /**
     * viewpage adapter for scrolling between fragments
     */
    public static class FragmentSlider extends FragmentStatePagerAdapter {

        MainActivity mActivity;

        FragmentSlider(MainActivity pActivity, FragmentManager pFragmentManager) {
            super(pFragmentManager);
            mActivity = pActivity;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    HistoryFragment historyFragment = new HistoryFragment();
                    historyFragment.setMainActivity(mActivity);
                    return historyFragment;

                case 1:
                    DisplayFragment displayFragment = new DisplayFragment();
                    return displayFragment;
            }

            return new DisplayFragment();
        }

        @Override
        public int getCount() {
            return MainActivity.VERTICAL_PAGES;
        }
    }

    /**
     * array adapter for result history
     */
    public static class ResultHistory extends ArrayAdapter<String> {

        protected Context mContext;
        protected ArrayList<String> mResults;

        ResultHistory(Context pContext, ArrayList<String> pResults) {
            super(pContext, -1, pResults);
            mContext = pContext;
            mResults = pResults;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.result, parent, false);

            TextView resultText = (TextView) view.findViewById(R.id.list_text_result);
            resultText.setText(getItem(position));

            return view;

        }
    }
}
