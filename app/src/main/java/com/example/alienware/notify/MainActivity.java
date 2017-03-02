package com.example.alienware.notify;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements ChangeFrag {


    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    Login login;
    TheSessionKeeper theSessionKeeper;
    CoordinatorLayout coordinatorLayout;
    static FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getIntent().getExtras()!=null){
            /*try {
                TheSessionKeeper.getInstance(getApplicationContext()).setMessage(new JSONObject().put("message",getIntent().getExtras().getString("message")).put("from",getIntent().getExtras().getString("from")).put("time",getIntent().getExtras().getString("sentTime")));
            } catch (JSONException e) {e.printStackTrace();} */
            TheSessionKeeper.getInstance(this).setMessage(getIntent().getExtras().getString("message"),getIntent().getExtras().getString("from"),0);

        }
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.mainCoordinator);
        frameLayout = (FrameLayout)findViewById(R.id.frame);
        fragmentManager = getSupportFragmentManager();
        login = new Login();
        theSessionKeeper = TheSessionKeeper.getInstance(getApplicationContext());
        if(theSessionKeeper.isFirstTimeLaunch()){
            bringChange(new IntroSlider());
        }else{
            if (theSessionKeeper.isLoggedIn() ) {
                //frag for login_success
                bringChange(new Homepage());
            }
            else {
                //frag for login
                bringChange(login);
            }
        }

    }



    @Override
    public void bringChange(Fragment fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

}