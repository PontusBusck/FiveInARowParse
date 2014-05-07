package com.example.fiveinrowparse;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;



public class addFriendActivity extends Activity {
    String mFriendList;
    String MY_PREFS_STRING = "myPrefs";
    String MY_FRIEND_STRING = "myFriends";
    String MY_FRIEND_USERNAME_STRING = "myFriendUserName";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_friend);
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_friend, menu);
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

    public void addAFriendButtonClicked(View view) {
        EditText friendName = (EditText) findViewById(R.id.search_friend_textfield);

        if(friendName.getText() != null){
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("username", friendName.getText().toString());
            query.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null && objects.size() > 0) {
                        Toast.makeText(addFriendActivity.this, "found user:" + objects.get(0).getUsername(), Toast.LENGTH_SHORT).show();
                        SharedPreferences prefs = getSharedPreferences(MY_PREFS_STRING, 0);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(MY_FRIEND_STRING, objects.get(0).getObjectId());
                        editor.putString(MY_FRIEND_USERNAME_STRING, objects.get(0).getUsername());
                        editor.commit();
                    } else {
                        Toast.makeText(addFriendActivity.this, "found no user with that username!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }



    }


}
