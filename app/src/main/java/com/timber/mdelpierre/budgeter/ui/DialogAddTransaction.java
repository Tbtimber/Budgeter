package com.timber.mdelpierre.budgeter.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.DialogTitle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timber.mdelpierre.budgeter.R;
import com.timber.mdelpierre.budgeter.model.Tag;
import com.timber.mdelpierre.budgeter.persistance.RealmHelper;
import com.timber.mdelpierre.budgeter.util.TagUtil;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmChangeListener;

/**
 * Created by Matthieu on 20/06/2016.
 */
public class DialogAddTransaction extends DialogFragment {

    @Bind(R.id.flow_tags)
    FlowLayout mFlowTags;

    private List<TextView> mEtTagList;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_transaction, null);

        ButterKnife.bind(this, view);

        populateFlowLayout();

        builder.setView(view);

        RealmHelper.attachListener(new RealmChangeListener() {
            @Override
            public void onChange(Object element) {
                populateFlowLayout();
            }
        });

        return builder.create();
    }




    private void populateFlowLayout() {
        List<Tag> tags = RealmHelper.getTags();
        mEtTagList = new ArrayList<>();
        int i=0;
        for(Tag tg: tags) {
            mEtTagList.add(TagUtil.createTVForTag(getActivity(),tg.name));
            mFlowTags.addView(mEtTagList.get(i));
            i++;

        }
    }

    @OnClick(R.id.bt_add_tag)
    void addTag() {
        DialogAddTag di = new DialogAddTag();
        di.show(getFragmentManager(), "");
    }
}
