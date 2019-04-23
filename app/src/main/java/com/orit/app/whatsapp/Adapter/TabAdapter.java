package com.orit.app.whatsapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.orit.app.whatsapp.ChatFragment;
import com.orit.app.whatsapp.ContactFragment;
import com.orit.app.whatsapp.FavouriteFragment;
import com.orit.app.whatsapp.GroupFragment;

/**
 * Created by Joseph on 4/1/2019.
 */

public class TabAdapter extends FragmentPagerAdapter {


    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;
            case 1:
                return new GroupFragment();
            case 2:
                return new ContactFragment();
            case 3:
                return new FavouriteFragment();
            default:
                return  null;


        }

    }

    @Override
    public int getCount() {
        return 4;
    }


    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:

                return "Chat";
            case 1:
                return "Group";
            case 2:
                return "Contact";
            case 3:
                return  "Favourite";
            default:

                return  null;
        }


    }

}
