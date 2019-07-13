package com.orit.app.whatsapp.Model;

/**
 * Created by Joseph on 4/29/2019.
 */

public class Chat {


    // pic,user,date,message,time
   private String profile,user,date,message,time;

   public Chat()
   {

   }
   public Chat(String profile,String user,String message,String date,String time)
   {
    this.profile = profile;
    this.user   = user;
    this.date   = date;
    this.message = message;
    this.time = time;
   }
   public Chat(String user,String message,String date,String time)
   {
       this.user   = user;
       this.date   = date;
       this.message = message;
       this.time = time;

   }
    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
