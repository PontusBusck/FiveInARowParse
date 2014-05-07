package com.example.fiveinrowparse;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class MyPushReciver extends BroadcastReceiver {
    public static final String ACTION_NEW_DRIVING_RECIVED = "com.busck.NEW_DRIVE";
    public static final String ACTION_SYSTEM_BECOMES_ACTIVE = "com.busck.SYSTEM_BECOMES_ACTIVE";
    public static final String ACTION_SYSTEM_BECOMES_INACTIVE = "com.busck.SYSTEM_BECOMES_INACTIVE";
    public static final String ACTION_NEW_PLAYER_MOVE = "com.busck.NEW_PLAYER_MOVE";
    public  static final String ACTION_NEW_GAME_INVITE = "com.example.NEW_GAME_INVITE";
    public MyPushReciver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("recivertest", Boolean.toString(MainApplication.isIsOnlineGameVisible()));
        Context appContext = context.getApplicationContext();
        if(intent !=null) {

           if (ParseUser.getCurrentUser() != null) {
               String action = intent.getAction();
               if (ACTION_NEW_GAME_INVITE.equals(action)) {
                   String fromUser = "";
                   String gameId = "";
                   String fromUserId = "";

                   if(intent.getExtras() != null) {
                       try {
                           JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                           fromUser = json.getString("fromUser");
                           fromUserId = json.getString("fromUserId");
                           gameId = json.getString("gameId");
                           Log.d("com.busck.taxikurrir", "Min json: " + fromUser + " " + gameId);

                       } catch (JSONException e) {
                           e.printStackTrace();
                       }

                   }


                   Log.d("recivertest", gameId);
                   Intent acceptInvite = new Intent(appContext, OnlineGameActivity.class);
                   acceptInvite.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                   acceptInvite.putExtra("gameId", gameId);
                   acceptInvite.putExtra("opponent", fromUser);
                   acceptInvite.putExtra("challanged", true);
                   acceptInvite.putExtra("fromUserId", fromUserId);


                   //Tills vidare så skickar jag med flagga TODO skicka med unikt nummer (ist för 1) så att man kan få utmaningar av flera användare
                   PendingIntent pendingNotificationIntent = PendingIntent.getActivity(appContext, 1, acceptInvite, PendingIntent.FLAG_UPDATE_CURRENT);


                   NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(appContext)
                           .setAutoCancel(true)
                           .setSmallIcon(R.drawable.ic_launcher)
                           .setContentTitle("Challanged")
                           .setContentIntent(pendingNotificationIntent)
                           .setContentText("You have been challenged by " + fromUser + "!");


                   NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                   notificationManager.notify(1, mBuilder.build());



               } else if (ACTION_NEW_PLAYER_MOVE.equals(action)){
                   if(MainApplication.isIsOnlineGameVisible()){
                       //Skickar iväg en broadcast till min OnlineGameActivity om den är öppen
                       context.sendBroadcast(new Intent("com.busck.UPPDATE_THE_GAME"));
                       Log.d("recivertest", "AlTheWay");
                   } else {
                       //Skickar notification om den är stängd
                       Intent notificationIntent = new Intent(appContext, OnlineGameActivity.class);
                       //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                       //notificationIntent.putExtra(getString(R.string.FRAGMENT_VALUE), 1);
                       notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);

                       PendingIntent pendingNotificationIntent = PendingIntent.getActivity(appContext, 1, notificationIntent, 0);


                       NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(appContext)
                                   .setAutoCancel(true)
                                   .setSmallIcon(R.drawable.ic_launcher)
                                   .setContentTitle("New move!")
                                   .setContentIntent(pendingNotificationIntent)
                                   .setContentText("User x did a move");


                       NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                       notificationManager.notify(1, mBuilder.build());
                   }
               }

           }
       }
    }
}
