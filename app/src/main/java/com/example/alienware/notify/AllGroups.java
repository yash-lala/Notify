package com.example.alienware.notify;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alienware on 01-03-2017.
 */

public class AllGroups extends Fragment {

    TextView noData;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    JSONObject jsonObject;
    TheSessionKeeper theSessionKeeper;
    FloatingActionButton newGroup;
    ChangeFrag changeFrag;

    List<JSONObject> jsonObjectList = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        changeFrag = (ChangeFrag)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.allgroups,container,false);
        noData = (TextView)view.findViewById(R.id.allgroups_noData);
        recyclerView = (RecyclerView)view.findViewById(R.id.allgroups_recycler);
        theSessionKeeper = TheSessionKeeper.getInstance(getContext());
        newGroup = (FloatingActionButton)view.findViewById(R.id.allgroups_FAB);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TheAdapterGroups(getContext(),jsonObjectList);
        recyclerView.setAdapter(adapter);
        jsonObject = new JSONObject();

        try {
            jsonObject.put("uid",theSessionKeeper.get("uid"));
            System.out.println("TO SERVER->"+jsonObject);
        }catch (JSONException je){je.printStackTrace();}
        new AsyncConnect(getContext(),getString(R.string.link_allGroups),jsonObject, new AsyncConnect.AsyncRevert() {
            @Override
            public void getJsonResponse(JSONObject jsonObject) {
                try {

                    JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                    for(int i=0;i<jsonArray.length();i++){
                        jsonObjectList.add(jsonArray.getJSONObject(i));
                        System.out.println(jsonObjectList.get(i));
                    }
                    if(jsonObjectList.size()==0){
                        recyclerView.setVisibility(RecyclerView.INVISIBLE);
                        noData.setVisibility(TextView.VISIBLE);
                    }else
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){e.printStackTrace();}

            }
        }).execute();

        newGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFrag.bringChange(new CreateGroup());
            }
        });

        return view;
    }

}
