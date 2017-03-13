package com.example.alienware.notify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alienware on 01-03-2017.
 */

public class Notifications extends Fragment {

    TextView noData;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    JSONObject jsonObject;
    TheSessionKeeper theSessionKeeper;
    List<JSONObject> jsonObjectList =  new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notifications,container,false);
        noData = (TextView)view.findViewById(R.id.notifications_noData);
        theSessionKeeper = TheSessionKeeper.getInstance(getContext());
        recyclerView = (RecyclerView)view.findViewById(R.id.notifications_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TheNotificationAdapter(jsonObjectList);
        recyclerView.setAdapter(adapter);
        jsonObject = new JSONObject();
        try {
            jsonObject.put("uid",theSessionKeeper.get("uid"));
        }catch (JSONException je){je.printStackTrace();}
        new AsyncConnect(getContext(), getString(R.string.link_allNotifications), jsonObject, new AsyncConnect.AsyncRevert() {
            @Override
            public void getJsonResponse(JSONObject jsonObject) {
                try {
                    if(jsonObject!=null){
                        noData.setVisibility(TextView.INVISIBLE);
                        recyclerView.setVisibility(RecyclerView.VISIBLE);
                            JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                            System.out.println("JSON ARRAY ->" + jsonArray);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObjectList.add(jsonArray.getJSONObject(i));
                                System.out.println(jsonObjectList.get(i));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    else {
                        noData.setVisibility(TextView.VISIBLE);
                        recyclerView.setVisibility(RecyclerView.INVISIBLE);
                    }
                }catch (JSONException je){je.printStackTrace();}
            }
        }).execute();
        return view;
    }
}
