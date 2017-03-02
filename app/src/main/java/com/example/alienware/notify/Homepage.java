package com.example.alienware.notify;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alienware on 01-03-2017.
 */

public class Homepage extends Fragment implements Toolbar.OnMenuItemClickListener {

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    ChangeFrag changeFrag;
    AllGroups allGroups;
    Notifications notifications;
    TheSessionKeeper theSessionKeeper;
    CoordinatorLayout coordinatorLayout;
    JSONObject jsonObject;
    String errorMessage = "beeepboooppp! errrr!!!! Looks like you're trapped here for Ever :{) ";
    AlertDialog dialog;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        changeFrag = (ChangeFrag) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage,container,false);
        viewPager = (ViewPager)view.findViewById(R.id.homepage_viewPager);
        toolbar = (Toolbar)view.findViewById(R.id.homepage_toolbar);
        tabLayout = (TabLayout)view.findViewById(R.id.homepage_tab);
        coordinatorLayout = (CoordinatorLayout)view.findViewById(R.id.homepage_coordinator);
        //toolbar.setLogoDescription(R.string.app_name);
        toolbar.inflateMenu(R.menu.toolbar);
        toolbar.setOnMenuItemClickListener(this);
        allGroups = new AllGroups();
        notifications = new Notifications();

        theSessionKeeper = TheSessionKeeper.getInstance(getContext());

        if(viewPager!=null) setViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    void setViewPager(ViewPager viewPager){
        ThePager thePager = new ThePager(getFragmentManager());
        thePager.addFragment(allGroups,"Groups");
        thePager.addFragment(notifications,"Notifications");
        viewPager.setAdapter(thePager);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                View view = getActivity().getLayoutInflater().inflate(R.layout.profile,null);
                TextView name = (TextView)view.findViewById(R.id.profile_name);
                TextView user_name = (TextView)view.findViewById(R.id.profile_username);
                TextView email = (TextView)view.findViewById(R.id.profile_email);
                TextView date = (TextView)view.findViewById(R.id.profile_date);
                Button logout = (Button)view.findViewById(R.id.profile_logout);

                name.setText(theSessionKeeper.get("name"));
                user_name.setText(theSessionKeeper.get("user_name"));
                email.setText(theSessionKeeper.get("email"));
                date.setText(theSessionKeeper.get("dateOfCreation"));
                try {
                    jsonObject = new JSONObject();
                    jsonObject.put("uid", theSessionKeeper.get("uid"));
                }catch (JSONException e){e.printStackTrace();}
                logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AsyncConnect(getContext(),getString(R.string.link_logOut),jsonObject, new AsyncConnect.AsyncRevert() {
                            @Override
                            public void getJsonResponse(JSONObject jsonObject) {
                                try {
                                    if (jsonObject.getBoolean("error")) {
                                        dialog.dismiss();
                                        Snackbar.make(coordinatorLayout, errorMessage, Snackbar.LENGTH_SHORT).show();
                                        //Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT).show();
                                    }else {
                                        dialog.dismiss();
                                        theSessionKeeper.logOut();
                                        changeFrag.bringChange(new Login());
                                    }
                                }catch (JSONException e){e.printStackTrace();}

                            }
                        }).execute();
                    }
                });

                builder.setView(view);
                builder.setCancelable(true);
                dialog = builder.create();
                dialog.show();
                return true;
            case R.id.developer:
                //show developer
                return true;
        }
        return true;
    }
}
