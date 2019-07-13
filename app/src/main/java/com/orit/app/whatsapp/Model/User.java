package com.orit.app.whatsapp.Model;

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
    public User(String image,String user,String status,String uid)
    {

        this.image = image;
        this.user = user;
        this.status = status;
        this.uid = uid;

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
