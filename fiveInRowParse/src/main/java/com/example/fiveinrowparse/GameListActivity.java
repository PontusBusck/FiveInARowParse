package com.example.fiveinrowparse;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
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
        getAllGames();
    }

    private void getAllGames() {
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
                if (e == null) {
                    mAllGamesList = gameList;
                    mGameListView = (ListView) findViewById(R.id.game_list_view);
                    mGameAdapter = new GameListAdapter(GameListActivity.this, mAllGamesList);
                    mGameListView.setAdapter(mGameAdapter);
                    mGameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {
                            ParseObject gameToOpen = (ParseObject) mGameAdapter.getItem(position);
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

                    });

                    Log.d("all games list", Integer.toString(mAllGamesList.size()));
                    Log.d("username", ParseUser.getCurrentUser().getUsername());
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
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

    public void playWithFriend(View view) {
        SharedPreferences prefs = this.getSharedPreferences(MY_PREFS_STRING, 0);
        String opponentUserName = prefs.getString(MY_FRIEND_USERNAME_STRING, null);
        String opponentId = prefs.getString(MY_FRIEND_STRING, null);


        Intent intent = new Intent(this, OnlineGameActivity.class);
        intent.putExtra(OPPONENT_USER_NAME, opponentUserName);
        intent.putExtra(OPPONENT_ID, opponentId);
        intent.putExtra(START_NEW_GAME_INTENT_STRING, true);


        startActivity(intent);
    }

    public void openAddFriendsActivity(View view) {
        Intent intent = new Intent(this, addFriendActivity.class);
        startActivity(intent);
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

            } else {
                ((TextView) convertView.findViewById(R.id.player_turn_label)).setText(game.getString("playersTurn"));
            }

            ((TextView) convertView.findViewById(R.id.last_move_made)).setText("Last move " + String.valueOf(game.getCreatedAt()));


            return convertView;
        }


    }

}
