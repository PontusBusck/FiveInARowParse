package com.example.fiveinrowparse;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

public class MyPushReciver extends BroadcastReceiver {
    public static final String ACTION_NEW_DRIVING_RECIVED = "com.busck.NEW_DRIVE";
    public static final String ACTION_SYSTEM_BECOMES_ACTIVE = "com.busck.SYSTEM_BECOMES_ACTIVE";
    public static final String ACTION_SYSTEM_BECOMES_INACTIVE = "com.busck.SYSTEM_BECOMES_INACTIVE";
    public static final String ACTION_NEW_PLAYER_MOVE = "com.busck.NEW_PLAYER_MOVE";
    public  static final String ACTION_NEW_GAME_INVITE = "com.example.NEW_GAME_INVITE";
    public  static final String ACTION_NEW_FRIEND_REQUEST = "com.example.NEW_FRIEND_REQUEST";
    public  static final String ACTION_PLAY_AGAIN = "com.busck.PLAY_AGAIN";
    public  static final String ACTION_ACCEPTED_GAME_REQUEST = "com.example.ACCEPTED_FRIEND_REQUEST";
    public static int mNotificationId;
    public MyPushReciver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //Skapar ett nytt id varje gång ett push tas emot
        mNotificationId = (int) new Date().getTime();
        Log.d("mNotificationId", String.valueOf(mNotificationId));


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

                       } catch (JSONException e) {
                           e.printStackTrace();
                       }

                   }

                   if(MainApplication.isGameListVisible()){ //Uppdaterar spellistan om den är öppen
                       context.sendBroadcast(new Intent("com.busck.UPPDATE_THE_GAME"));

                   }else { //Annars visar jag en notification


                       Intent acceptInvite = new Intent(appContext, GameListActivity.class);
                       acceptInvite.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                       acceptInvite.putExtra("gameId", gameId);
                       acceptInvite.putExtra("opponent", fromUser);
                       acceptInvite.putExtra("challanged", true);
                       acceptInvite.putExtra("fromUserId", fromUserId);


                       PendingIntent pendingNotificationIntent = PendingIntent.getActivity(appContext, 1, acceptInvite, PendingIntent.FLAG_UPDATE_CURRENT);


                       NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(appContext)
                               .setAutoCancel(true)
                               .setLights(Color.parseColor("#B303A2"), 2000, 200)
                               .setSmallIcon(R.drawable.ic_launcher)
                               .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                               .setContentTitle("Challanged")
                               .setContentIntent(pendingNotificationIntent)
                               .setContentText("You have been challenged by " + fromUser + "!");


                       NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                       notificationManager.notify(mNotificationId, mBuilder.build());
                   }


               } else if (ACTION_NEW_PLAYER_MOVE.equals(action)){

                   String fromUser = "";
                   String gameId = "";
                   String fromUserId = "";

                   if(intent.getExtras() != null) {
                       try {
                           JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                           fromUser = json.getString("fromUser");
                           fromUserId = json.getString("fromUserId");
                           gameId = json.getString("gameId");

                       } catch (JSONException e) {
                           e.printStackTrace();
                       }

                   }

                   if(MainApplication.isIsOnlineGameVisible()){
                       //Skickar iväg en broadcast till min OnlineGameActivity om den är öppen
                       context.sendBroadcast(new Intent("com.busck.UPPDATE_THE_GAME"));

                   } else if(MainApplication.isGameListVisible()){ //Uppdaterar spellistan om den är öppen
                       context.sendBroadcast(new Intent("com.busck.UPPDATE_THE_GAME"));

                   }else {
                       //Skickar notification om den är stängd
                       Intent notificationIntent = new Intent(appContext, GameListActivity.class);
                       //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                       //notificationIntent.putExtra(getString(R.string.FRAGMENT_VALUE), 1);
                       notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);

                       PendingIntent pendingNotificationIntent = PendingIntent.getActivity(appContext, 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                       NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(appContext)
                                   .setAutoCancel(true)
                                   .setSmallIcon(R.drawable.ic_launcher)
                               .setLights(Color.parseColor("#B303A2"), 2000, 200)
                               .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                               .setContentTitle("New move!")
                                   .setContentIntent(pendingNotificationIntent)
                                   .setContentText("User " + fromUser + " did a move");


                       NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                       notificationManager.notify(mNotificationId, mBuilder.build());
                   }
               } else if (ACTION_NEW_FRIEND_REQUEST.equals(action)){


                   if(MainApplication.isFriendListVisible()) {
                       //Skickar iväg en broadcast till min FriendList om den är öppen
                       context.sendBroadcast(new Intent("com.busck.UPPDATE_FRIENDLIST"));


                   } else {
                       //Och skapar en notification om den är stängd
                       String fromUser = "";


                       if(intent.getExtras() != null) {
                           try {
                               JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                               fromUser = json.getString("fromUser");

                           } catch (JSONException e) {
                               e.printStackTrace();
                           }

                       }

                       Intent notificationIntent = new Intent(appContext, FriendListActivity.class);
                       //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                       //notificationIntent.putExtra(getString(R.string.FRAGMENT_VALUE), 1);
                       notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);

                       PendingIntent pendingNotificationIntent = PendingIntent.getActivity(appContext, 2, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                       NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(appContext)
                               .setAutoCancel(true)
                               .setSmallIcon(R.drawable.ic_launcher)
                               .setContentTitle("New friend request")
                               .setLights(Color.parseColor("#B303A2"), 2000, 200)
                               .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                               .setContentIntent(pendingNotificationIntent)
                               .setContentText("User " + fromUser + " send a friend request!");


                       NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                       notificationManager.notify(mNotificationId, mBuilder.build());
                   }




               } else if (ACTION_PLAY_AGAIN.equals(action)){

                   if(MainApplication.isIsOnlineGameVisible()){
                       //Skickar iväg en broadcast till min OnlineGameActivity om den är öppen
                       context.sendBroadcast(new Intent("com.busck.UPPDATE_THE_GAME"));

                   } else {
                       //Annars kommer en notification



                       String fromUser = "";


                       if(intent.getExtras() != null) {
                           try {
                               JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                               fromUser = json.getString("fromUser");

                           } catch (JSONException e) {
                               e.printStackTrace();
                           }

                       }

                       Intent notificationIntent = new Intent(appContext, GameListActivity.class);
                       //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                       //notificationIntent.putExtra(getString(R.string.FRAGMENT_VALUE), 1);
                       notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);

                       PendingIntent pendingNotificationIntent = PendingIntent.getActivity(appContext, 2, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                       NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(appContext)
                               .setAutoCancel(true)
                               .setSmallIcon(R.drawable.ic_launcher)
                               .setContentTitle("Play again?")
                               .setLights(Color.parseColor("#B303A2"), 2000, 200)
                               .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                               .setContentIntent(pendingNotificationIntent)
                               .setContentText(fromUser + " wants to play again!");


                       NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                       notificationManager.notify(mNotificationId, mBuilder.build());



                   }


               } else if(ACTION_ACCEPTED_GAME_REQUEST.equals(action)){

                   if(MainApplication.isFriendListVisible()) {
                       //Skickar iväg en broadcast till min FriendList om den är öppen
                       context.sendBroadcast(new Intent("com.busck.UPPDATE_FRIENDLIST"));


                   } else {
                       //Och skapar en notification om den är stängd
                       String fromUser = "";


                       if(intent.getExtras() != null) {
                           try {
                               JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                               fromUser = json.getString("fromUser");

                           } catch (JSONException e) {
                               e.printStackTrace();
                           }

                       }

                       Intent notificationIntent = new Intent(appContext, FriendListActivity.class);
                       //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                       //notificationIntent.putExtra(getString(R.string.FRAGMENT_VALUE), 1);
                       notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);

                       PendingIntent pendingNotificationIntent = PendingIntent.getActivity(appContext, 2, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                       NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(appContext)
                               .setAutoCancel(true)
                               .setSmallIcon(R.drawable.ic_launcher)
                               .setContentTitle("Accepted invite!")
                               .setLights(Color.parseColor("#B303A2"), 2000, 200)
                               .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                               .setContentIntent(pendingNotificationIntent)
                               .setContentText(fromUser + " has accepted your friend invite!");


                       NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                       notificationManager.notify(mNotificationId, mBuilder.build());
                   }

               }

           }
       }
    }
}
