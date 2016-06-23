package com.timber.mdelpierre.budgeter.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.timber.mdelpierre.budgeter.R;
import com.timber.mdelpierre.budgeter.enumeration.TagEventTypeEnum;
import com.timber.mdelpierre.budgeter.global.ApplicationSharedPreferences;
import com.timber.mdelpierre.budgeter.model.Tag;
import com.timber.mdelpierre.budgeter.persistance.RealmHelper;
import com.timber.mdelpierre.budgeter.ui.eventBus.TagEvents;
import com.timber.mdelpierre.budgeter.util.TagUtil;

import org.apmem.tools.layouts.FlowLayout;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmChangeListener;

/**
 * Created by Matthieu on 20/06/2016.
 */
public class DialogAddTransaction extends DialogFragment {

    @Bind(R.id.transaction_value)
    EditText mEtTransactionValue;

    @Bind(R.id.flow_tags)
    FlowLayout mFlowTags;

    @Bind(R.id.cb_isIncome_addTransaction)
    CheckBox mCbIsIncome;

    private List<View> mEtTagList;

    private LinearLayout mSelectedTag;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_transaction, null);

        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        populateFlowLayout();

        builder.setView(view);


        builder.setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Double.parseDouble(mEtTransactionValue.getText().toString());
                } catch (Exception e) {
                    return;
                }
                boolean realmBool;
                double value = Double.parseDouble(mEtTransactionValue.getText().toString());
                if(!mCbIsIncome.isChecked()) {
                    value *= -1;
                }


                if(mSelectedTag == null) {
                    realmBool = RealmHelper.addTransactionToRealm(getActivity(), new Date(System.currentTimeMillis()),
                            value, "others");
                } else {
                    realmBool = RealmHelper.addTransactionToRealm(getActivity(), new Date(System.currentTimeMillis()),
                            value,
                            ((TextView) mSelectedTag.getChildAt(0)).getText().toString());
                }
                if(!realmBool) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_error_adding_transaction), Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setView(view).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void populateFlowLayout() {
        List<Tag> tags = RealmHelper.getTagsFromRealm(getActivity());
        mEtTagList = new ArrayList<>();
        int i=0;
        for(Tag tg: tags) {
            mEtTagList.add(TagUtil.createLLforTag(getActivity(),tg.name));
            mFlowTags.addView(mEtTagList.get(i));
            i++;

        }
    }

    private void refreshTag() {
        for(View v: mEtTagList) {
            LinearLayout cur = (LinearLayout) v;
            cur.removeAllViews();
            mFlowTags.removeView(cur);
        }

        populateFlowLayout();
    }

    @OnClick(R.id.bt_add_tag)
    void addTag() {
        DialogAddTag di = new DialogAddTag();
        di.show(getFragmentManager(), "");
    }

    // Event-Bus event
    // ---------------------------------------------------------------------------
    @Subscribe
    public void onEvent(TagEvents event) {
        if(event.getmType() == TagEventTypeEnum.TAG_ADDED_EVENT) {
            refreshTag();
        } else if (event.getmType() == TagEventTypeEnum.ALREADY_EXISTS){
            Toast.makeText(getActivity(),getResources().getString(R.string.tag_already_exists), Toast.LENGTH_LONG).show();
        } else if (event.getmType() == TagEventTypeEnum.TAG_SELECTED) {

            if(mSelectedTag == null) {
                mSelectedTag = event.getmTagLl();
                TagUtil.setTagLayoutToSelect(mSelectedTag);
            } else if(event.getmTagLl().equals(mSelectedTag)) {
                TagUtil.setTagLayouToNormal(mSelectedTag);
                mSelectedTag = null;
            } else {
                TagUtil.setTagLayouToNormal(mSelectedTag);
                mSelectedTag = event.getmTagLl();
                TagUtil.setTagLayoutToSelect(mSelectedTag);
            }
        }

    }
}
