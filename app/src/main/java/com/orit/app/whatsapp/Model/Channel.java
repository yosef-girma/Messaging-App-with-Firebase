package com.orit.app.whatsapp.Model;

/**
 * Created by Joseph on 7/12/2019.
 */

public class Channel  {

    private String profilePic,name;

    public Channel() {

    }

    public Channel(String profilePic, String name) {
        this.profilePic = profilePic;
        this.name = name;
    }



    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
