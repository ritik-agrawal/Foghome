package com.foghome.v1.represents;

public class GrantAccess {
    private String userName, userId;

    public GrantAccess() {
    }

    public GrantAccess(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
