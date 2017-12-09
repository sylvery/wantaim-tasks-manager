package com.sudotechpng.android.wantaim;

/**
 * Created by syagi on 12/06/2017.
 */

public class User {
    String userID;
    String userName;

    public User() {}

    public User(String userID, String userName) {
        this.userID = userID;
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
