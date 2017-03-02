package com.example.alienware.notify;

import android.widget.EditText;

import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alienware on 05-02-2017.
 */

public class HouseKeeping {

//for creating Json from Strings
    JSONObject createJson(String... strings){
        JSONObject jsonObject = new JSONObject();
        int i =0;
        try {
            while (i<strings.length){
                jsonObject.put(strings[i],strings[i+1]);
                i+=2;
            }
            System.out.print("JSON->> "+jsonObject);
            return jsonObject;
        }catch (Exception e){e.printStackTrace();}
        return null;
    }


//to check if login and sign up fields are empty
    static boolean areFieldsEmpty(EditText... myStrings){
        for(EditText s: myStrings)
            if(s.getText().toString().equals(""))
                return true;
        return false;
    }


    static String passwordToSend(String user_name ,String password){
        StringBuilder result = new StringBuilder();
        int userLength = user_name.length();
        int mid = userLength/2;
        for (int i=0;i<userLength;i++){
            result.append(user_name.charAt(i));
            if(i==mid){
                for (int j = 0; j < password.length(); j++) result.append(password.charAt(j));
            }
        }
        result.append(password+user_name);
        String encode = user_name+password+result.toString();
        return sha256(encode);
        //return encode;
    }

    static String sha256(String toencode){
        String decoded=null;
        try {
            MessageDigest messageDigest =MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(toencode.toString().getBytes());
            decoded = new BigInteger(1,hash).toString(16);
        }catch (NoSuchAlgorithmException e){e.printStackTrace();}

        return decoded;
    }

    boolean matchesUser_namePattern(String user_name){
        Pattern pattern = Pattern.compile("^[a-z0-9_-]{3,15}$");
        Matcher matcher = pattern.matcher(user_name);
        return matcher.matches();
    }

    boolean matchesEmailPattern(String email){
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    boolean matchesPasswordPattern(String password){
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\\\S+$).{4,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

 //to encode and decode password

}
