package com.zeekay.hotelroombooker.models;

public class user {

    String firstname;
    String lastname;
    String email;
    String password;
    int type;
    String dp;
    String uid;

    user(){

    }

    public user(String firstname, String lastname, String email, String password, int type) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public user(String firstname, String lastname, String email, String password, int type, String dp, String uid) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.type = type;
        this.dp = dp;
        this.uid = uid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
