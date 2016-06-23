package com.timber.mdelpierre.budgeter.ui.eventBus;

import com.timber.mdelpierre.budgeter.enumeration.AccountEventTypeEnum;

/**
 * Created by Matthieu on 23/06/2016.
 */
public class AccountEvent {
    private AccountEventTypeEnum type;

    public AccountEvent(AccountEventTypeEnum type) {
        this.type = type;
    }

    public AccountEventTypeEnum getType() {
        return type;
    }
}
