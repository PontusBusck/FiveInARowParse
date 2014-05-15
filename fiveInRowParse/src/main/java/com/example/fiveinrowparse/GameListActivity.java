package com.example.fiveinrowparse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GameListActivity extends Activity {
    String MY_FRIEND_STRING = "myFriends";
    String MY_PREFS_STRING = "myPrefs";
    String MY_FRIEND_USERNAME_STRING = "myFriendUserName";
    String MY_CURRENT_GAME_ID = "myCurrentOnlineGame";
    String OPPONENT_USER_NAME = "opponentUsername";
    String OPPONENT_ID = "opponentId";
    String START_NEW_GAME_INTENT_STRING = "StartnewGame";

    private List<ParseObject> mAllGamesList;
    private GameListAdapter mGameAdapter;
    private ListView mGameListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_list);


    }

    @Override
    protected void onResume() {
        super.onResume();

        if(ParseUser.getCurrentUser() != null) {


            getAllGames();
        }


        handleLoginWindowVisability();
    }

    //Visar och döljer log in rutorna

    public void handleLoginWindowVisability(){

        ParseUser currentUser = ParseUser.getCurrentUser();
        Button toFriends = (Button) findViewById(R.id.open_friendlist_button);
        Button logoutButton = (Button) findViewById(R.id.logout_button);

        RelativeLayout loginLayout = (RelativeLayout) findViewById(R.id.login_layout);

        if (currentUser != null) {

            TextView usernameLabel = (TextView) findViewById(R.id.username_label);
            usernameLabel.setText("Welcome " + ParseUser.getCurrentUser().getUsername());
            loginLayout.setVisibility(View.INVISIBLE);
            toFriends.setVisibility(View.VISIBLE);

            logoutButton.setVisibility(View.VISIBLE);
            animateViewOnLogIn();
        } else {

            loginLayout.setVisibility(View.VISIBLE);
            toFriends.setVisibility(View.GONE);

            TextView usernameLabel = (TextView) findViewById(R.id.username_label);
            usernameLabel.setText("You have to login to continue");

            logoutButton.setVisibility(View.INVISIBLE);
            animateViewOnLogOut();

        }
    }


    public void loginUser(View view) {
        final ProgressBar progresSpinner = (ProgressBar) findViewById(R.id.progres_spinner_login);
        progresSpinner.setVisibility(View.VISIBLE);
        EditText usernameField = (EditText) findViewById(R.id.username_login_textfield);
        EditText passwordField = (EditText) findViewById(R.id.password_login_textfield);

        if(usernameField.getText() != null && passwordField.getText() != null) {
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();

            if(!username.equals("") && !password.equals("")) {
                ParseUser.logInInBackground(usernameField.getText().toString(), passwordField.getText().toString(), new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            progresSpinner.setVisibility(View.GONE);
                            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                            installation.put("userId", ParseUser.getCurrentUser().getObjectId());
                            installation.saveInBackground();
                            handleLoginWindowVisability();

                        } else {
                            Toast.makeText(GameListActivity.this, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();
                            progresSpinner.setVisibility(View.GONE);
                        }
                    }
                });
            } else{
                Toast.makeText(this, "You must enter both a password and a username!", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(this, "You must enter both a password and a username!", Toast.LENGTH_SHORT).show();

        }
    }

    public void animateViewOnLogIn (){

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;

        //Justerar vyn med spel
        RelativeLayout leftLayout = (RelativeLayout) findViewById(R.id.left_gamelist_Layout);
        leftLayout.getLayoutParams().width = (width/3)*2;
        leftLayout.requestLayout();


        //Animerar högra viewn (Kryper den och
        final RelativeLayout rightLayout = (RelativeLayout) findViewById(R.id.right_gamelist_Layout);


        Log.d("widt", Integer.toString(width));
        windowAnimateSmall anim = new windowAnimateSmall(rightLayout, width/3);
        anim.setDuration(400);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Button backButton = (Button) findViewById(R.id.back_button);
                backButton.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getAllGames();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rightLayout.startAnimation(anim);
        }

    public void animateViewOnLogOut (){

        //Animerar högra viewn
        final RelativeLayout rightLayout = (RelativeLayout) findViewById(R.id.right_gamelist_Layout);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;

        Log.d("widt", Integer.toString(width));
        windowAnimateSmall anim = new windowAnimateSmall(rightLayout, width);
        anim.setDuration(400);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Button backButton = (Button) findViewById(R.id.back_button);
                backButton.getLayoutParams().width = getPx(150);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        rightLayout.startAnimation(anim);
    }

    public void registerUser(View view) {

        EditText usernameField = (EditText) findViewById(R.id.username_login_textfield);
        EditText passwordField = (EditText) findViewById(R.id.password_login_textfield);

        if(usernameField.getText() != null && passwordField.getText() != null) {
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();
            ArrayList<String> friendArray = new ArrayList<String>();

            if(!username.equals("") && !password.equals("")) {
                ParseUser user = new ParseUser();
                user.setUsername(usernameField.getText().toString());
                user.setPassword(usernameField.getText().toString());
                user.put("friendIds", Arrays.toString(friendArray.toArray()));

                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {

                            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                            installation.put("userId", ParseUser.getCurrentUser().getObjectId());
                            installation.saveInBackground();

                            handleLoginWindowVisability();
                            getAllGames();
                            Toast.makeText(GameListActivity.this, "User created!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(GameListActivity.this, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }else{
                Toast.makeText(this, "You must choose both a password and a username!", Toast.LENGTH_SHORT).show();
            }

        } else{
            Toast.makeText(this, "You must choose both a password and a username!", Toast.LENGTH_SHORT).show();
        }
    }


    public void logoutUser(View view) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.remove("userId");
        installation.saveInBackground();

        ParseUser.logOut();
        mAllGamesList.clear();
        mGameAdapter.notifyDataSetChanged();

        handleLoginWindowVisability();
    }

    //Hämtar alla spel där man är spelare 1 eller 2

    private void getAllGames() {
        final ProgressBar progress = (ProgressBar) findViewById(R.id.progres_spinner_gamelist);
        progress.setVisibility(View.VISIBLE);
        ParseQuery<ParseObject> firstPlayerQuery = ParseQuery.getQuery("Games");
        firstPlayerQuery.whereEqualTo("playerOneName", ParseUser.getCurrentUser().getUsername());

        ParseQuery<ParseObject> secondPlayerQuery = ParseQuery.getQuery("Games");
        secondPlayerQuery.whereEqualTo("playerTwoName", ParseUser.getCurrentUser().getUsername());


        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(firstPlayerQuery);
        queries.add(secondPlayerQuery);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.orderByDescending("updatedAt");
        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> gameList, ParseException e) {

                progress.setVisibility(View.GONE);

                if (e == null) {
                    mAllGamesList = gameList;
                    mGameListView = (ListView) findViewById(R.id.game_list_view);
                    mGameAdapter = new GameListAdapter(GameListActivity.this, mAllGamesList);
                    mGameListView.setAdapter(mGameAdapter);
                    mGameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        //Vid klick på rad
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {
                            final ParseObject gameToOpen = (ParseObject) mGameAdapter.getItem(position); //Hämtar spelobjectet på den possitionen


                            //Om man är spelare 2 och inte har accepterat spelet

                            if(!gameToOpen.getBoolean("playerTwoAccepted") && gameToOpen.getString("playerTwoName").equals(ParseUser.getCurrentUser().getUsername())){

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(GameListActivity.this);
                                builder1.setMessage("Do you accept this game invite?");
                                builder1.setCancelable(true);
                                builder1.setPositiveButton("Yes",

                                        //Hämtar och uppdaterar spelet på parse om man klickar ja
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(final DialogInterface dialog, int id) {
                                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Games");
                                                query.getInBackground(gameToOpen.getObjectId(), new GetCallback<ParseObject>() {
                                                    public void done(ParseObject game, ParseException e) {
                                                        if (e == null) {
                                                            game.put("playerTwoAccepted", true);

                                                            game.saveInBackground(new SaveCallback() {
                                                                public void done(ParseException e) {
                                                                    if (e == null) {
                                                                        dialog.cancel(); //Stänger dialogen när det är uppdaterat på parse
                                                                        openGame(gameToOpen);

                                                                    } else {
                                                                        // The save failed.
                                                                        dialog.cancel(); //Stänger dialogen även om det misslyckades
                                                                        Log.d("tag", "User update error: " + e);
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                });

                                            }
                                        });
                                builder1.setNegativeButton("No, delete",

                                        //Raderar spelat om man klickar nej
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(final DialogInterface dialog, int id) {
                                                gameToOpen.deleteInBackground();
                                                dialog.dismiss();
                                                getAllGames();
                                            }
                                        });

                                AlertDialog acceptGameAlert = builder1.create();
                                acceptGameAlert.show();
                            }else { //Om man inte är spelare två eller redan accepterat spelet

                                openGame(gameToOpen);

                            }
                        }

                    });

                    Log.d("all games list", Integer.toString(mAllGamesList.size()));
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    //Öppnar spelet vid klick på en rad

    public void openGame(ParseObject gameToOpen){
        Intent intent = new Intent(GameListActivity.this, OnlineGameActivity.class);


        String opponentName = "";
        String opponentId = "";
        String gameId = gameToOpen.getObjectId();
        if (gameToOpen.getString("playerOneName").equals(ParseUser.getCurrentUser().getUsername())) {

            opponentName = gameToOpen.getString("playerTwoName");
            opponentId = gameToOpen.getString("playerTwoId");

        } else if (gameToOpen.getString("playerTwoName").equals(ParseUser.getCurrentUser().getUsername())) {
            opponentName = gameToOpen.getString("playerOneName");
            opponentId = gameToOpen.getString("playerOneId");
        }

        intent.putExtra(OPPONENT_USER_NAME, opponentName);
        intent.putExtra(OPPONENT_ID, opponentId);
        intent.putExtra(MY_CURRENT_GAME_ID, gameId);


        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_list, menu);
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

    public void openFriendList(View view) {

        if(ParseUser.getCurrentUser() != null){
        Intent intent = new Intent(this, FriendListActivity.class);
        startActivity(intent);

        }
    }

    public void back(View view) {
        finish();
    }

    //Omvandlar dp till pixlar
    public int getPx(float dp){
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int pixels = (int) (metrics.density * dp + 0.5f);
        return pixels;
    }


    static class GameListAdapter extends BaseAdapter {


        private Activity mActivity;
        private List<ParseObject> mGames;

        public GameListAdapter(Activity activity, List<ParseObject> games) {
            mActivity = activity;

            mGames = games;
        }

        @Override
        public int getCount() {
            return mGames.size();
        }

        @Override
        public Object getItem(int position) {
            return mGames.get(position);
        }


        @Override
        public long getItemId(int position) {
            //return mGames.get(position).getId();
            return 9; //temp
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.game_list_item, null);
            }

            ParseObject game = mGames.get(position);
            if (game.getString("playerOneName").equals(ParseUser.getCurrentUser().getUsername())) {

                ((TextView) convertView.findViewById(R.id.opponent_name_label)).setText(game.getString("playerTwoName"));

            } else if (game.getString("playerTwoName").equals(ParseUser.getCurrentUser().getUsername())) {
                ((TextView) convertView.findViewById(R.id.opponent_name_label)).setText(game.getString("playerOneName"));
            }


            if (game.getString("playersTurn").equals(ParseUser.getCurrentUser().getUsername())) {
                ((TextView) convertView.findViewById(R.id.player_turn_label)).setText("Your turn!");

            }else if(game.getNumber("winnerNumber").intValue() == 1){
                 ((TextView) convertView.findViewById(R.id.player_turn_label)).setText(game.getString("playerOneName") + " Won!");

                if(game.getString("playerOneName").equals(ParseUser.getCurrentUser().getUsername())){
                    ((TextView) convertView.findViewById(R.id.player_turn_label)).setText("You" + " Won!");
                }

           }else if(game.getNumber("winnerNumber").intValue() == 2){
                ((TextView) convertView.findViewById(R.id.player_turn_label)).setText(game.getString("playerTwoName") + " Won!");

                if(game.getString("playerTwoName").equals(ParseUser.getCurrentUser().getUsername())){
                    ((TextView) convertView.findViewById(R.id.player_turn_label)).setText("You" + " Won!");
                }

            }else{
                ((TextView) convertView.findViewById(R.id.player_turn_label)).setText(game.getString("playersTurn") + " Turn!");
            }


            //Kollar om spelet är accepterat av motståndaren
            TextView rightTextView = ((TextView) convertView.findViewById(R.id.right_text_label));

            if(!game.getBoolean("playerTwoAccepted")){
                if(game.getString("playerOneName").equals(ParseUser.getCurrentUser().getUsername())){
                    rightTextView.setText("Challanger has \nnot accepted");
                    rightTextView.setTextColor(Color.parseColor("#9C191C"));
                } else if(game.getString("playerTwoName").equals(ParseUser.getCurrentUser().getUsername())){
                    rightTextView.setText("You have \nnot accepted");
                    rightTextView.setTextColor(Color.parseColor("#9C191C"));
                }

            }else if(game.getBoolean("playerTwoAccepted")){
                rightTextView.setTextColor(Color.parseColor("#FFFFFF"));
                rightTextView.setText(game.getString("playerOneName") + ": " + game.getNumber("playerOneWins").toString() + "\n" + game.getString("playerTwoName") + ": " + game.getNumber("playerTwoWins").toString());
            }








            return convertView;
        }


    }

    public class windowAnimateSmall extends Animation
    {
        private int mWidth;
        private int mStartWidth;
        private View mView;

        public windowAnimateSmall(View view, int width)
        {
            mView = view;
            mWidth = width;
            mStartWidth = view.getWidth();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation transformation)
        {
            int newWidth =  mStartWidth - (int) ((mStartWidth - mWidth) * interpolatedTime);

            mView.getLayoutParams().width = newWidth;
            mView.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight)
        {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds()
        {
            return true;
        }
    }

    public class windowAnimateLarge extends Animation
    {
        private int mWidth;
        private int mStartWidth;
        private View mView;

        public windowAnimateLarge(View view, int width)
        {
            mView = view;
            mWidth = width;
            mStartWidth = view.getWidth();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation transformation)
        {
            int newWidth =  mStartWidth + (int) ((mWidth - mStartWidth) * interpolatedTime);

            mView.getLayoutParams().width = newWidth;
            mView.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight)
        {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds()
        {
            return true;
        }
    }

}
