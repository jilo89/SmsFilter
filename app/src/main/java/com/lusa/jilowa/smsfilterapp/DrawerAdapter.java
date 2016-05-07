package com.lusa.jilowa.smsfilterapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * Created by JILOWA on 18-Oct-15.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.MyViewHolder> {
    private LayoutInflater Inflater;
    private ClickListner clickListner;
    List<NavItems> data = Collections.emptyList();
    private Context context;

    public DrawerAdapter(Context context, List<NavItems> data){
        this.context=context;
        Inflater=LayoutInflater.from(context);
        this.data=data;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = Inflater.inflate(R.layout.custom_row,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        NavItems current= data.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconId);

    }
    public void setClickListner(ClickListner clickListner){
        this.clickListner = clickListner;

    }
    @Override
    public int getItemCount() {

        return data.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView title;
            ImageView icon;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView)itemView.findViewById(R.id.listText);
            icon = (ImageView)itemView.findViewById(R.id.listIcon);


        }

        @Override
        public void onClick(View v) {

            context.startActivity(new Intent(context,MainActivity.class));
            if (clickListner != null){
                clickListner.itemClicked(v,getPosition());
            }
        }
    }
    public interface ClickListner{
        void itemClicked(View view, int position);
    }
}
