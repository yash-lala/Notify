package com.example.alienware.notify;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
 * Created by Alienware on 17-01-2017.
 */

public class SignUp extends Fragment implements View.OnTouchListener{

    TextView not_equal;
    EditText name, user_name, password, confirm_password,email;
    Button register;
    TextView loginGateway;
    Login login;
    HouseKeeping houseKeeping;
    ChangeFrag tc;
    boolean error;
    String errorMessage;
    CoordinatorLayout coordinatorLayout;
    boolean passwordPattern, emailPattern, user_namePattern;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        tc = (ChangeFrag) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup,container,false);
        coordinatorLayout = (CoordinatorLayout)view.findViewById(R.id.signup_coordinator);
        name = (EditText) view.findViewById(R.id.name);
        user_name = (EditText) view.findViewById(R.id.user_name);
        email = (EditText)view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);
        confirm_password = (EditText) view.findViewById(R.id.confirm_password);
        register = (Button) view.findViewById(R.id.register);
        not_equal = (TextView)view.findViewById(R.id.passwords_not_equal);
        loginGateway = (TextView)view.findViewById(R.id.loginGateway);
        login = new Login();
        houseKeeping = new HouseKeeping();

        register.setOnTouchListener(this);
        loginGateway.setOnTouchListener(this);

        confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!(password.getText().toString().equals(confirm_password.getText().toString())))
                    not_equal.setVisibility(EditText.VISIBLE);
                else
                    not_equal.setVisibility(EditText.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        return view;
    }



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                switch (view.getId()) {
                    case R.id.register:
                        if (houseKeeping.areFieldsEmpty(name,user_name,email,password,confirm_password)){
                            Snackbar.make(coordinatorLayout,"You're a special kind of idiot", Snackbar.LENGTH_SHORT).show();
                        }else{
                            if(not_equal.getVisibility()== EditText.INVISIBLE) {
                                handleTheOperation();

                            }
                        }
                        return true;
                    case R.id.loginGateway:
                        tc.bringChange(login);
                        return true;
                }
        }
        return true;
    }


    void handleTheOperation(){
        String un = user_name.getText().toString();
        String pass = password.getText().toString();
        JSONObject jsonObject = houseKeeping.createJson("firebase_id",""+TheSessionKeeper.getInstance(getContext()).getToken(),"name",name.getText().toString(),"user_name",un,"email",email.getText().toString(),"password",HouseKeeping.passwordToSend(un,pass));
         new AsyncConnect(getContext(), getString(R.string.link_signUp), jsonObject, new AsyncConnect.AsyncRevert() {
            @Override
            public void getJsonResponse(JSONObject jsonObject) {
                try {
                    error = jsonObject.getBoolean("error");
                    errorMessage = jsonObject.getString("message");
                    Snackbar.make(coordinatorLayout,errorMessage, Snackbar.LENGTH_SHORT).show();
                    //Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT).show();
                    if(!error){
                        Snackbar.make(coordinatorLayout,"You have successfully registered!,I may warn you there is no Exit :{", Snackbar.LENGTH_SHORT).show();
                        tc.bringChange(login);
                    }
                }catch (JSONException e){System.out.println(e);}
            }
        }).execute();
    }
}


