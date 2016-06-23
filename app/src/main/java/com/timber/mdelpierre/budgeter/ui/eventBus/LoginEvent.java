package com.timber.mdelpierre.budgeter.ui.eventBus;

import com.timber.mdelpierre.budgeter.enumeration.LoginEventEnum;

/**
 * Created by Matthieu on 22/06/2016.
 */
public class LoginEvent {
    private LoginEventEnum type;

    public LoginEvent(LoginEventEnum type) {
        this.type = type;
    }

    public LoginEventEnum getType() {
        return type;
    }
}
