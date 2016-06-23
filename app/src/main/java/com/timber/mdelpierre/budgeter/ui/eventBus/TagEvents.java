package com.timber.mdelpierre.budgeter.ui.eventBus;

import android.widget.LinearLayout;

import com.timber.mdelpierre.budgeter.enumeration.TagEventTypeEnum;

/**
 * Created by Matthieu on 21/06/2016.
 */
public class TagEvents {
    private TagEventTypeEnum mType;

    private LinearLayout mTagLl;

    public TagEvents(TagEventTypeEnum mType) {
        this.mType = mType;
    }

    public TagEvents(LinearLayout mTagLl) {
        mType = TagEventTypeEnum.TAG_SELECTED;
        this.mTagLl = mTagLl;
    }

    public TagEventTypeEnum getmType() {
        return mType;
    }

    public LinearLayout getmTagLl() {
        return mTagLl;
    }
}
