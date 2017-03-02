package com.example.alienware.notify;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Alienware on 17-01-2017.
 */

public class TheSessionKeeper {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private static TheSessionKeeper theSessionKeeper;
    private static final String file = "Notify";
    private static final String IsFirstTimeLaunch = "IsFirstTimeLaunch";
    private static String isLoggedIn = "LoggedIn";
    private static final String TAG = "Token";
    private static final String MESSAGE = "MESSAGE";
    private static final int mode =0;
    private static final String[] fields = {"uid","name","user_name","email","dateOfCreation"};

    public TheSessionKeeper(Context c){
        this.context = c;
        sharedPreferences = context.getSharedPreferences(file,mode);
    }

    public static synchronized TheSessionKeeper getInstance(Context context){
        if(theSessionKeeper==null) {
            theSessionKeeper = new TheSessionKeeper(context);
        }
        return theSessionKeeper;
    }

    public void setFirstTime(boolean bool){
        editor = sharedPreferences.edit();
        editor.putBoolean(IsFirstTimeLaunch,bool);
        editor.apply();
    }

    public boolean isFirstTimeLaunch(){
        return sharedPreferences.getBoolean(IsFirstTimeLaunch,true);
    }

    public void setIsLoggedIn(boolean loggedIn,String... info){
        editor = sharedPreferences.edit();
        editor.putBoolean(isLoggedIn,loggedIn);
        for (int i =0 ;i < info.length;i++){
            editor.putString(fields[i],info[i]);
            editor.apply();
        }

    }

    public void logOut(){
        editor = sharedPreferences.edit();
        editor.putBoolean(isLoggedIn,false);
        editor.apply();
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(isLoggedIn, false);
    }

    public String get(String toGet){
        return sharedPreferences.getString(toGet,null);
    }

    public void setToken(String token){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG,token);
        editor.apply();
    }

    public String getToken(){
        return sharedPreferences.getString(TAG,null);
    }

    public void setMessage(String message,String from, long time){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("mess",message);

        editor.commit();
        System.out.println("(SET)GETTING->"+ sharedPreferences.getString("mess",null));
    }





    public String getMessage(){
        /*String holder = sharedPreferences.getString(MESSAGE,null);
        System.out.println("(GET)GETTING->"+ sharedPreferences.getString(MESSAGE,null));

            if(holder!=null && !holder.toString().equals("{}")){
                try {
                    return new JSONObject(holder);
                }catch (JSONException je){
                    je.printStackTrace();}
            }
           return null; */
        return sharedPreferences.getString(MESSAGE,null);
    }

    /*public void bomb(){
        for (String holder:fields){
            editor.putString(holder,"");
        }

    }*/

}


