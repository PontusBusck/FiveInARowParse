package com.example.fiveinrowparse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;


public class setupLocalGame extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setup_local_game);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setup_local_game, menu);
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

    public void goBack(View view) {
        finish();
    }

    public void startTheGame(View view) {
        EditText playerOne = (EditText) findViewById(R.id.player_one_textfield);
        EditText playerTwo = (EditText) findViewById(R.id.player_two_textfield);

        if(playerOne.getText() != null && playerTwo.getText() != null) {
            String playerOneName = playerOne.getText().toString();
            String playerTwoName = playerOne.getText().toString();

            if(!playerOneName.equals("") && !playerTwoName.equals("")) {
                Intent intent = new Intent(this, LocalGameActivity.class);
                intent.putExtra("playerOne", playerOne.getText().toString());
                intent.putExtra("playerTwo", playerTwo.getText().toString());
                startActivity(intent);
            }else{
                Toast.makeText(this, "You must choose names for all players!", Toast.LENGTH_SHORT).show();
            }

        } else{
            Toast.makeText(this, "You must choose names for all players!", Toast.LENGTH_SHORT).show();
        }
    }
}
