package com.timber.mdelpierre.budgeter.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.timber.mdelpierre.budgeter.R;
import com.timber.mdelpierre.budgeter.enumeration.TagEventTypeEnum;
import com.timber.mdelpierre.budgeter.global.ApplicationSharedPreferences;
import com.timber.mdelpierre.budgeter.persistance.RealmHelper;
import com.timber.mdelpierre.budgeter.ui.eventBus.TagEvents;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Matthieu on 20/06/2016.
 */
public class DialogAddTag extends DialogFragment{
    @Bind(R.id.et_add_tag)
    EditText mEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_tag, null);

        ButterKnife.bind(this, view);

        builder.setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RealmHelper.addTagToRealm(getActivity(), mEditText.getText().toString());
                EventBus.getDefault().post(new TagEvents(TagEventTypeEnum.TAG_ADDED_EVENT));
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


}
