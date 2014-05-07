package com.example.fiveinrowparse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class MyPushReciver extends BroadcastReceiver {
    public static final String ACTION_NEW_DRIVING_RECIVED = "com.busck.NEW_DRIVE";
    public static final String ACTION_SYSTEM_BECOMES_ACTIVE = "com.busck.SYSTEM_BECOMES_ACTIVE";
    public static final String ACTION_SYSTEM_BECOMES_INACTIVE = "com.busck.SYSTEM_BECOMES_INACTIVE";

    public  static final String ACTION_NEW_GAME_INVITE = "com.example.NEW_GAME_INVITE";
    public MyPushReciver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
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

                   Context appContext = context.getApplicationContext();
                   Intent acceptInvite = new Intent(appContext, OnlineGameActivity.class);
                   acceptInvite.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   acceptInvite.putExtra("gameId", gameId);
                   acceptInvite.putExtra("opponent", fromUser);
                   acceptInvite.putExtra("challanged", true);
                   acceptInvite.putExtra("fromUserId", fromUserId);
                   if (appContext != null) {
                       appContext.startActivity(acceptInvite);
                   }

               }

           }
       }
    }
}
