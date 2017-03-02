package com.example.alienware.notify;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alienware on 20-01-2017.
 */

public class Login extends Fragment implements View.OnTouchListener{

    EditText password, user_name;
    Button login;
    TextView sign_up;
    TheSessionKeeper theSessionKeeper;
    CoordinatorLayout coordinatorLayout;
    SignUp signUp;
    HouseKeeping houseKeeping;
    ChangeFrag tc;
    boolean error = true;
    String errorMessage = null;
    String uid,name,userName,email,dateOfCreation;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        tc = (ChangeFrag)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //loginSuccess = new LoginSuccess();
        signUp = new SignUp();
        houseKeeping = new HouseKeeping();
        theSessionKeeper = TheSessionKeeper.getInstance(getContext());
        View view = inflater.inflate(R.layout.login, container, false);
        coordinatorLayout = (CoordinatorLayout)view.findViewById(R.id.login_coordinator);
        password = (EditText)view.findViewById(R.id.password);
        user_name = (EditText)view.findViewById(R.id.user_name);


        login = (Button) view.findViewById(R.id.login);
        sign_up = (TextView) view.findViewById(R.id.sign_up);
        login.setOnTouchListener(this);
        sign_up.setOnTouchListener(this);
        return view;

    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                switch (view.getId()){
                    case R.id.login:
                        if(houseKeeping.areFieldsEmpty(user_name, password)) {
                            Snackbar.make(coordinatorLayout,"Fill The Fields You Muppet!", Snackbar.LENGTH_SHORT).show();
                            //Toast.makeText(getContext(), "Fill the fields you Muppet!", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        else {
                            try {
                                handleTheOperation();
                            }//frag for successful login
                            catch(Exception e){System.out.print(e);}
                        }
                        return true;

                    case R.id.sign_up:
                        tc.bringChange(signUp);
                        //frag for sign up
                        return true;
                }
        }
        return true;
    }




    void handleTheOperation(){
        String un = user_name.getText().toString();
        String pass = password.getText().toString();
        JSONObject jsonObject = houseKeeping.createJson("firebase_id",theSessionKeeper.getToken(),"user_name",un,"password",HouseKeeping.passwordToSend(un,pass));
         new AsyncConnect(getContext(), getString(R.string.link_login), jsonObject, new AsyncConnect.AsyncRevert() {
            @Override
            public void getJsonResponse(JSONObject jsonObject) {
                try {
                    error = jsonObject.getBoolean("error");
                    if(error){
                        errorMessage = jsonObject.getString("message");
                        Snackbar.make(coordinatorLayout,errorMessage, Snackbar.LENGTH_SHORT).show();
                        //Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT).show();
                    }else {

                        uid = jsonObject.getString("uid");
                        JSONObject innerJson = jsonObject.getJSONObject("user");
                        name = innerJson.getString("name");
                        userName = innerJson.getString("user_name");
                        email = innerJson.getString("email");
                        dateOfCreation = innerJson.getString("DateOfCreation");
                        theSessionKeeper.setIsLoggedIn(true,uid,name,userName,email,dateOfCreation);
                        tc.bringChange(new Homepage());
                    }

                }catch (JSONException e){System.out.println(e);}
            }
        }).execute();
    }

}
