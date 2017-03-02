package com.example.alienware.notify;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Alienware on 19-02-2017.
 */

public class TheAdapterGroups extends RecyclerView.Adapter<TheAdapterGroups.TheViewHolder> {
    private List<JSONObject> userList;
    Context context;
    Activity activity;
    JSONObject jsonObject;
    TheSessionKeeper theSessionKeeper;
    ImageView success;
    Button send;
    EditText message;
    AlertDialog dialog;

    public class TheViewHolder extends RecyclerView.ViewHolder {
        public TextView group,member_count,date;

        public TheViewHolder(View view) {
            super(view);

            group = (TextView) view.findViewById(R.id.group_name);
            member_count = (TextView) view.findViewById(R.id.group_members);
            date = (TextView)view.findViewById(R.id.group_members);

            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            return true;
                        case MotionEvent.ACTION_UP:

                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                            View sendmessage = activity.getLayoutInflater().inflate(R.layout.sendmessage,null);
                            success = (ImageView)sendmessage.findViewById(R.id.sucess);
                            message = (EditText)sendmessage.findViewById(R.id.message);
                            send = (Button)sendmessage.findViewById(R.id.sendmessage_button);
                            jsonObject = new JSONObject();

                            send.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        jsonObject.put("admin",theSessionKeeper.get("user_name"));
                                        jsonObject.put("message",message.getText().toString());
                                        jsonObject.put("groupName",group.getText().toString());
                                    }catch (JSONException je){je.printStackTrace();}

                                    if(!HouseKeeping.areFieldsEmpty(message)){
                                        new AsyncConnect(context, context.getString(R.string.link_sendMessage), jsonObject, new AsyncConnect.AsyncRevert() {
                                            @Override
                                            public void getJsonResponse(JSONObject jsonObject) {
                                                try {
                                                    if(!jsonObject.getBoolean("error")){
                                                        message.setVisibility(EditText.INVISIBLE);
                                                        send.setVisibility(Button.INVISIBLE);
                                                        success.setVisibility(ImageView.VISIBLE);
                                                    }
                                                }catch (JSONException je){je.printStackTrace();}

                                            }
                                        }).execute();
                                    }
                                }
                            });
                            builder.setView(sendmessage);
                            builder.setCancelable(true);
                            dialog = builder.create();
                            dialog.show();

                            return true;
                    }
                    return true;
                }
            });


        }
    }


    public TheAdapterGroups(Context context,List<JSONObject> userList) {
        this.context = context;
        activity = (Activity)context;
        this.userList = userList;
        theSessionKeeper  =TheSessionKeeper.getInstance(context);
    }

    @Override
    public TheViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_card, parent, false);
        return new TheViewHolder(view);
    }


    @Override
    public void onBindViewHolder(TheViewHolder holder, int position) {
        JSONObject jsonObject = userList.get(position);
        try {
            System.out.println("Inside OnBindViewHolder");

            holder.group.setText(jsonObject.getString("group_name"));
            holder.member_count.setText(jsonObject.getString("members"));
            holder.date.setText(jsonObject.getString("created_at"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }





}