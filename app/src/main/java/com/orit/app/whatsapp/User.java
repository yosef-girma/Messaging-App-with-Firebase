package com.orit.app.whatsapp;

/**
 * Created by Joseph on 4/7/2019.
 */

public class User {


    String user;
    String status;
    String image;
    String uid;

     public  User()
    {

    }
    public User(String image,String name,String status,String uid)
    {


    }
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }






}
