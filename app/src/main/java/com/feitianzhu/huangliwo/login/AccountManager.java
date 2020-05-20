package com.feitianzhu.huangliwo.login;

import com.feitianzhu.huangliwo.login.model.UserInfo;

public class AccountManager {
    private static AccountManager accountManager = new AccountManager();
    private UserInfo userInfo = new UserInfo();

    private AccountManager() {
    }

    public AccountManager getInstance() {
        return accountManager;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
