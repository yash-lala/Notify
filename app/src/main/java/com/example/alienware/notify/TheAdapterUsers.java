package com.example.alienware.notify;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

/**
 * Created by Alienware on 19-02-2017.
 */

public class TheAdapterUsers extends RecyclerView.Adapter<TheAdapterUsers.TheViewHolder> {
    private List<JSONObject> userList;
    static Vector<String> vector = new Vector();
    static JSONArray selected = new JSONArray();
    int selectedcolor = Color.parseColor("#FF338083");


    public class TheViewHolder extends RecyclerView.ViewHolder {
        public TextView name, user_name;
        RelativeLayout relativeLayout;
        public TheViewHolder(View view) {
            super(view);
            relativeLayout = (RelativeLayout)view.findViewById(R.id.usercard_relative);
            name = (TextView) view.findViewById(R.id.usercard_name);
            user_name = (TextView) view.findViewById(R.id.usercard_username);
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_UP:
                            try {
                                String buffer = userList.get(getAdapterPosition()).getString("user_name");
                                if(vector.contains(buffer)){
                                    vector.remove(buffer);
                                    relativeLayout.setBackgroundColor(Color.TRANSPARENT);
                                }else{
                                    relativeLayout.setBackgroundColor(selectedcolor);
                                    vector.add(buffer);
                                }

                                //removeAt(getAdapterPosition());

                                System.out.println(vector);
                            } catch (JSONException je) {
                                je.printStackTrace();
                            }
                            return true;
                        case MotionEvent.ACTION_DOWN:
                            return true;
                    }
                    return true;
                }
            });


        }
    }


    public TheAdapterUsers(List<JSONObject> userList) {
        this.userList = userList;
    }

    @Override
    public TheViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        return new TheViewHolder(view);
    }


    @Override
    public void onBindViewHolder(TheViewHolder holder, int position) {
        JSONObject jsonObject = userList.get(position);
        String name=null,userName=null;
        try {
            name = jsonObject.getString("nameOfUser");
            userName = jsonObject.getString("user_name");
        }catch (JSONException je){je.printStackTrace();}
        if(vector.contains(userName)){
            holder.relativeLayout.setBackgroundColor(selectedcolor);
        }else{
            holder.relativeLayout.setBackgroundColor(Color.TRANSPARENT);
        }
            holder.name.setText(name);
            holder.user_name.setText(userName);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public void removeAt(int position) {
        userList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, userList.size());
    }

    static public JSONArray getSelected(){
        //return vector.toArray(new String[vector.size()]);
        Enumeration<String> enumeration = vector.elements();
        while (enumeration.hasMoreElements())
        selected.put(enumeration.nextElement());
        return selected;
    }



}