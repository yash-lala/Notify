package com.example.alienware.notify;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Alienware on 22-01-2017.
 */

public class AsyncConnect extends AsyncTask<String,Void,JSONObject> {
    HttpURLConnection httpURLConnection;
    URL url;
    String inputBuffer;
    StringBuilder serverResponse;
    JSONObject json;
    JSONObject toReturn;
    JSONObject failed;
    Context context;
    String link;
    ProgressDialog progressDialog;

    public interface AsyncRevert{
        void getJsonResponse(JSONObject jsonObject);
    }

    public AsyncRevert asyncRevert = null;

    AsyncConnect(Context c, String link, JSONObject json, AsyncRevert asyncRevert){
        context = c;
        progressDialog = new ProgressDialog(context);
        this.link = link;
        this.json = json;
        this.asyncRevert = asyncRevert;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("\tWait for it...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }



    @Override
        protected JSONObject doInBackground(String... strings) {
                try {
                    failed = new JSONObject();
                    failed.put("error",true);
                    failed.put("message","can't connect");
                }catch (JSONException e){e.printStackTrace();}

                        if(isUrlReachable(link)){
                            try {
                            url = new URL(link);
                            httpURLConnection = (HttpURLConnection) url.openConnection();
                            httpURLConnection.setReadTimeout(10000 /*milliseconds*/);
                            httpURLConnection.setConnectTimeout(15000 /* milliseconds */);
                            httpURLConnection.setRequestMethod("POST");
                            httpURLConnection.setRequestMethod("GET");
                            httpURLConnection.setDoInput(true);
                            httpURLConnection.setDoOutput(true);
                            httpURLConnection.connect();
                                if(json!=null){
                                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(httpURLConnection.getOutputStream());
                                    bufferedOutputStream.write(json.toString().getBytes());
                                    bufferedOutputStream.flush();
                                }


                            serverResponse = new StringBuilder();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                            while ((inputBuffer = bufferedReader.readLine()) != null) {
                                serverResponse.append(inputBuffer + "\n");
                            }
                            bufferedReader.close();
                            inputBuffer = serverResponse.toString();
                            System.out.println("String from server->" + inputBuffer);
                            toReturn = new JSONObject(inputBuffer);

                        } catch (Exception e){System.out.println(e);}
                    finally {
                        httpURLConnection.disconnect();
                    }
                    return toReturn;
                }else { return failed; }
        }





    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        progressDialog.dismiss();
        asyncRevert.getJsonResponse(jsonObject);
    }



    public static boolean isUrlReachable(String address){
        try {
            URL url = new URL(address);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(3000);
            http.connect();
            if(http.getResponseCode()==200){
                http.disconnect();
                return true;
            }else { http.disconnect(); return false;}
        }catch (MalformedURLException e){e.printStackTrace();}
        catch (IOException e){e.printStackTrace();}
        return false;
    }
}
