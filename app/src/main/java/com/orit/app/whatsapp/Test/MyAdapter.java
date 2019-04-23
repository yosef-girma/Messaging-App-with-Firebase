package com.orit.app.whatsapp.Test;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orit.app.whatsapp.R;


import java.util.List;

/**
 * Created by Joseph on 4/5/2019.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
{

    public Context context;
    public List<Upload> uploadList;

    MyAdapter(Context context, List<Upload> uploadList)
    {

        this.context    = context;
        this.uploadList = uploadList;

    }


    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cardview,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;


    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {

        Upload upload = uploadList.get(position);

        holder.textViewName.setText(upload.getName());
        Glide.with(context).load(upload.getUrl()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return uploadList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewName;
        public ViewHolder(View itemview)
        {

            super(itemview);
           imageView    = itemview.findViewById(R.id.imageView);
           textViewName = itemview.findViewById(R.id.textViewName);

        }


    }
}
