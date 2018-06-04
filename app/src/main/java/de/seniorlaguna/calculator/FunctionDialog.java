package de.seniorlaguna.calculator;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class FunctionDialog extends DialogFragment {

    protected MainActivity mMainActivity;

    public void setMainActivity(MainActivity pMainActivity) {
        mMainActivity = pMainActivity;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Choose");
        builder.setItems(R.array.functions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String func = getResources().getStringArray(R.array.functions)[which];

                if (func.length() > 2) {
                    mMainActivity.insertIntoDisplay(func.substring(0, func.length() - 2));
                }
                else {
                    mMainActivity.insertIntoDisplay(func);
                }

            }
        });

        return builder.create();

    }
}
