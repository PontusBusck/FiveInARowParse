package com.example.fiveinrowparse;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;



public class PlayWithFriendActivity extends Activity {
    String MY_FRIEND_STRING = "myFriends";
    String MY_PREFS_STRING = "myPrefs";
    String MY_FRIEND_USERNAME_STRING = "myFriendUserName";
    String OPPONENT_USER_NAME = "opponentUsername";
    String OPPONENT_ID = "opponentId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play_with_friend);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.play_with_friend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openAddFriendsActivity(View view) {
        Intent intent = new Intent(this, addFriendActivity.class);
        startActivity(intent);
    }



    public void playWithFriend(View view) {
        SharedPreferences prefs = this.getSharedPreferences(MY_PREFS_STRING, 0);
        String opponentUserName = prefs.getString(MY_FRIEND_USERNAME_STRING, null);
        String opponentId = prefs.getString(MY_FRIEND_STRING, null);


        Intent intent = new Intent(this, OnlineGameActivity.class);
        intent.putExtra(OPPONENT_USER_NAME, opponentUserName);
        intent.putExtra(OPPONENT_ID, opponentId);


        startActivity(intent);
    }

    public void removeSharedPrefs(View view) {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_STRING, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }
}
