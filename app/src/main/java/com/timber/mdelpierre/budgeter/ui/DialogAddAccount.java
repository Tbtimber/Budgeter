package com.timber.mdelpierre.budgeter.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.getkeepsafe.relinker.ApkLibraryInstaller;
import com.timber.mdelpierre.budgeter.R;
import com.timber.mdelpierre.budgeter.enumeration.AccountEventTypeEnum;
import com.timber.mdelpierre.budgeter.global.ApplicationSharedPreferences;
import com.timber.mdelpierre.budgeter.persistance.RealmHelper;
import com.timber.mdelpierre.budgeter.ui.eventBus.AccountEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Matthieu on 16/06/2016.
 */
public class DialogAddAccount extends DialogFragment {

    @Bind(R.id.et_add_account)
    EditText mEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_account, null);

        ButterKnife.bind(this, view);

        builder.setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ApplicationSharedPreferences.getInstance(getActivity()).setCurrentAccount(mEditText.getText().toString());
                EventBus.getDefault().post(new AccountEvent(AccountEventTypeEnum.ACCOUNT_ADDED));
                if(!RealmHelper.addAccountToRealm(getActivity(), mEditText.getText().toString())) {
                    Toast.makeText(getActivity(),getResources().getString(R.string.toast_error_creating_account), Toast.LENGTH_LONG).show();
                }
            }
        });
        if(!ApplicationSharedPreferences.getInstance(getActivity()).getFirstConnection()) {
            builder.setView(view).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        } else {
            setCancelable(false);
        }
        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
