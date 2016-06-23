package com.timber.mdelpierre.budgeter.ui.eventBus;

import com.timber.mdelpierre.budgeter.enumeration.TransactionEventEnum;

/**
 * Created by Matthieu on 23/06/2016.
 */
public class TransactionEvent {

    private TransactionEventEnum mType;

    public TransactionEvent(TransactionEventEnum mType) {
        this.mType = mType;
    }

    public TransactionEventEnum getmType() {
        return mType;
    }
}
