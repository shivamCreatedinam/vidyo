package com.createdinam.vidyo.model;

import java.io.Serializable;

public class UserInfo implements Serializable {

    String UserID,UserType,UserNumber;

    UserInfo(){

    }

    public UserInfo(String userID, String userType, String userNumber) {
        UserID = userID;
        UserType = userType;
        UserNumber = userNumber;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getUserNumber() {
        return UserNumber;
    }

    public void setUserNumber(String userNumber) {
        UserNumber = userNumber;
    }
}