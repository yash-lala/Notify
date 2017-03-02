package com.example.alienware.notify;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Alienware on 02-03-2017.
 */

public class TheNotificationAdapter extends RecyclerView.Adapter<TheNotificationAdapter.TheViewHolder> {

    List<JSONObject> jsonObjectList;

    public TheNotificationAdapter(List<JSONObject> jsonObjectList){this.jsonObjectList = jsonObjectList;}

    public class TheViewHolder extends RecyclerView.ViewHolder{
        TextView message,group,time;
        public TheViewHolder(View itemView) {
            super(itemView);
            message = (TextView)itemView.findViewById(R.id.notificationCard_message);
            group = (TextView)itemView.findViewById(R.id.notificationCard_group);
            time = (TextView)itemView.findViewById(R.id.notificationCard_time);

        }
    }

    @Override
    public TheViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card,parent,false);
        return new TheViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TheViewHolder holder, int position) {
        JSONObject jsonObject = jsonObjectList.get(position);
        try {
            holder.message.setText(jsonObject.getString("message"));
            holder.group.setText(jsonObject.getString("group_name"));
            holder.time.setText(jsonObject.getString("created_at"));
        }catch (JSONException je){je.printStackTrace();}
    }

    @Override
    public int getItemCount() {
        return jsonObjectList.size();
    }


}
