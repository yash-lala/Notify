package com.example.alienware.notify;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alienware on 02-03-2017.
 */

public class CreateGroup extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    JSONObject jsonObject;
    TheSessionKeeper theSessionKeeper;
    FloatingActionButton creategroup;
    EditText groupName;
    CoordinatorLayout coordinatorLayout;
    ChangeFrag changeFrag;
    Homepage homepage = new Homepage();


    List<JSONObject> jsonObjectList = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        changeFrag = (ChangeFrag)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.creategroup,container,false);
        coordinatorLayout = (CoordinatorLayout)view.findViewById(R.id.creategroup_coordinator);
        recyclerView = (RecyclerView)view.findViewById(R.id.allusers_recycler);
        groupName = (EditText)view.findViewById(R.id.create_groupname);
        creategroup = (FloatingActionButton)view.findViewById(R.id.creategroup_FAB);
        theSessionKeeper = TheSessionKeeper.getInstance(getContext());
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TheAdapterUsers(jsonObjectList);
        recyclerView.setAdapter(adapter);
        jsonObject = new JSONObject();
        new AsyncConnect(getContext(),getString(R.string.link_allUsers), null, new AsyncConnect.AsyncRevert() {
            @Override
            public void getJsonResponse(JSONObject jsonObject) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                    for(int i=0;i<jsonArray.length();i++){
                        jsonObjectList.add(jsonArray.getJSONObject(i));
                        System.out.println(jsonObjectList.get(i));
                    }
                    adapter.notifyDataSetChanged();
                }catch (JSONException je){je.printStackTrace();}

            }
        }).execute();

        creategroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TheAdapterUsers.getSelected().length()!=0){
                    if(!HouseKeeping.areFieldsEmpty(groupName)){
                        try {
                            jsonObject.put("uid",theSessionKeeper.get("uid"));
                            jsonObject.put("group_name",groupName.getText().toString());
                            jsonObject.put("members",TheAdapterUsers.getSelected());
                        }catch (JSONException je){je.printStackTrace();}
                        System.out.println("TO SERVER->"+jsonObject);
                        new AsyncConnect(getContext(), getString(R.string.link_createGroup), jsonObject, new AsyncConnect.AsyncRevert() {
                            @Override
                            public void getJsonResponse(JSONObject jsonObject) {
                                try {
                                    if(jsonObject.getBoolean("error")){
                                        Snackbar.make(coordinatorLayout,"Errrrr",Snackbar.LENGTH_SHORT).show();
                                    }else {
                                        new AllGroups();
                                        changeFrag.bringChange(homepage);
                                    }
                                }catch (JSONException je){je.printStackTrace();}

                            }
                        }).execute();
                    }else{
                        Snackbar.make(coordinatorLayout,"We apparently don\'t believe in the saying whats in a Name",Snackbar.LENGTH_SHORT).show();

                    }
                }else {
                    Snackbar.make(coordinatorLayout,"Ohhh boiii, You CRAZYYYYY",Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }
}
