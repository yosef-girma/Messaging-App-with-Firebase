package com.orit.app.whatsapp.Model;

/**
 * Created by Joseph on 5/6/2019.
 */

public class Message
{

    private String from,message,time;

    public Message(String from,String message,String time)
    {
        this.from = from;
        this.message = message;
        this.time = time;
    }
    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
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
