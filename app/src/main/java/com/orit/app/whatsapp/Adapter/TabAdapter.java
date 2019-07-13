package com.orit.app.whatsapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.orit.app.whatsapp.Fragment.ChatFragment;
import com.orit.app.whatsapp.Fragment.ChannelFragment;
import com.orit.app.whatsapp.Fragment.FavouriteFragment;
import com.orit.app.whatsapp.Fragment.GroupFragment;

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
                return new ChannelFragment();
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

                return "Channel";
            case 3:
                return  "Favourite";
            default:

                return  null;
        }


    }

}
