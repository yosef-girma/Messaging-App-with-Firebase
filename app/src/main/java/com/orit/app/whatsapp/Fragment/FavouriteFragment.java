package com.orit.app.whatsapp.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orit.app.whatsapp.R;


public class FavouriteFragment extends Fragment {

    View v;
     public FavouriteFragment()
        {

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            Toast.makeText(getContext(),"Favourite",Toast.LENGTH_LONG).show();

            v = inflater.inflate(R.layout.fragment_chat, container, false);

         return v;
        }
}
