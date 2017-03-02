package com.example.alienware.notify;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

/**
 * Created by Alienware on 16-02-2017.
 */

//this class is to receive messages and display them via notification
public class FirebaseMessageService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //make notification

       if(remoteMessage.getData().size() > 0){
           /*try{
               JSONObject jsonObject=null;

               jsonObject = new JSONObject()
                       .put("message",remoteMessage.getData().get("message").toString())
                       .put("from",remoteMessage.getFrom().toString())
                       .put("time",remoteMessage.getSentTime());
               //TheSessionKeeper.getInstance(getApplicationContext()).setMessage(new JSONObject().put("message",remoteMessage.getData().get("message")).put("from",remoteMessage.getFrom()).put("time",remoteMessage.getSentTime()));
               TheSessionKeeper.getInstance(getBaseContext()).setMessage(jsonObject);

           }catch (JSONException je){je.printStackTrace();} */

           TheSessionKeeper.getInstance(getApplicationContext()).setMessage(remoteMessage.getData().get("message"),remoteMessage.getFrom(),remoteMessage.getSentTime());
       }
       makeNotification(remoteMessage.getNotification().getBody());
    }

    private void makeNotification(String message){
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setContentTitle("Notify")
                .setSmallIcon(R.drawable.logo)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());
    }
}
