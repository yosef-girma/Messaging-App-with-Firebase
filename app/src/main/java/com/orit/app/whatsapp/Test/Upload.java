package com.orit.app.whatsapp.Test;

/**
 * Created by Joseph on 4/5/2019.
 */

public class Upload {


    public Upload()
    {

    }

    public Upload(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String name;
    public String url;



}
