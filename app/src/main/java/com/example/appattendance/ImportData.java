package com.example.appattendance;

public class ImportData {

    public String UserName;
    public String UserEmail;
    public String UserId;
    public String UserContact;
    public String UserClass;

    public ImportData() {
    }

    public ImportData(String userName, String userEmail, String userId, String userContact, String userClass) {
        UserName = userName;
        UserEmail = userEmail;
        UserId = userId;
        UserContact = userContact;
        UserClass = userClass;
    }

    public String getUserName() {
        return UserName;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public String getUserId() {
        return UserId;
    }

    public String getUserContact() {
        return UserContact;
    }

    public String getUserClass() {
        return UserClass;
    }
}
