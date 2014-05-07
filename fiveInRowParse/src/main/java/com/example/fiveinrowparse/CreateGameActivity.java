package com.example.fiveinrowparse;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;


public class CreateGameActivity extends Activity {


    private ChooseGameTypeFragment mChooseGameTypeFragment;
    private StartLocalGameFragment mStartLocalGameFragment;
    private StartOnlineGameFragment mStartOnlineGameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_game);
        ParseAnalytics.trackAppOpened(getIntent());

        mChooseGameTypeFragment = new ChooseGameTypeFragment();
        mStartLocalGameFragment = new StartLocalGameFragment();
        mStartOnlineGameFragment = new StartOnlineGameFragment();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, mChooseGameTypeFragment).commit();


    }

    @Override
    protected void onResume() {
        super.onResume();

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

    public void openLocalGameOptionScreen(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, mStartLocalGameFragment).commit();

    }

    public void goFromLocalToChooseScreen(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, mChooseGameTypeFragment).commit();

    }

    public void goFromOnlineToChooseScreen(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, mChooseGameTypeFragment).commit();
    }

    public void openOnlineGameOptionScreen(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, mStartOnlineGameFragment).commit();

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do nothing
        } else {
            Intent intent = new Intent(this, userRegisterOrLoginActivity.class);
            startActivity(intent);
        }

    }

    public void startRandomOnlineGame(View view) {

    }



    public void logoutUser(View view) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.logOut();
        Toast.makeText(this, "Logged out", Toast.LENGTH_LONG).show();
    }

    public void startGameWithFriendButton(View view) {
        Intent intent = new Intent(this, PlayWithFriendActivity.class);
        startActivity(intent);
    }

    public static class ChooseGameTypeFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.choose_gametype_fragment, container, false);
        }
    }

    public static class StartLocalGameFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.start_local_game_fragment, container, false);
        }
    }

    public static class StartOnlineGameFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.start_online_game_fragment, container, false);
        }
    }
}


//Test kod för att göra ett Game med Random spelare


/*
    public void startRandomOnlineGame(View view) {

        LiFilters wantRandomPlayer = new LiFilters(ActiveGamesData.LiFieldActiveGames.ActiveGamesWantRandomPlayer, LiFilters.Operation.EQUAL, true);
        LiFilters PlayerOne = new LiFilters(ActiveGamesData.LiFieldActiveGames.ActiveGamesFirstPlayer, LiFilters.Operation.NOT_EQUAL, User.getCurrentUser().getUserID());
        LiFilters acceptGames = new LiFilters(wantRandomPlayer, LiFilters.Condition.AND, PlayerOne);

        LiQuery query = new LiQuery();
        query.Lifilters = acceptGames;

        ActiveGames.getArrayWithQuery(query, LiRequestConst.QueryKind.FULL, new LiCallbackQuery.LiActiveGamesGetArrayCallback() {
            public void onGetActiveGamesFailure(LiErrorHandler error) {
                // Get failed
            }

            public void onGetActiveGamesComplete(List<ActiveGames> items) {
                int itemNumber = -1;
                int size = items.size();
                int listNumber = -1;
                Log.d("Tag", Integer.toString(size));


                if(size > 0 ){
                    Long firstDate = SystemClock.elapsedRealtime();
                    for(int i = 0; i < size; i++){

                        Date date = items.get(i).getActiveGamesLastUpdate().getTime();
                        Long dateTime = date.getTime();
                        Log.d("DateTag", String.valueOf(dateTime) );

                        if(i==0){
                            firstDate = dateTime;
                        }

                        if(firstDate >= dateTime){
                            firstDate = dateTime;
                            listNumber = i;
                        }
                    }
                }

                if(listNumber >= 0){
                    openGameActivity(items.get(listNumber));
                }else{
                    ActiveGames game = new ActiveGames();
                    game.setActiveGamesFirstPlayer(User.getCurrentUser().getUserID());
                    game.setActiveGamesWantRandomPlayer(true);

                    game.save(new LiCallback.LiCallbackAction() {
                        public void onFailure(LiErrorHandler error) {
                            // Save operation Failed
                        }

                        public void onComplete(LiErrorHandler.ApplicasaResponse response, String responseMessage, LiRequestConst.RequestAction action, String id, LiManager.LiObject className) {
                            // Save operation Succeded
                        }
                    });

                    openGameActivity(game);
                }


            }
        });




    }

    public void openGameActivity (ActiveGames game){
        String gameId = game.getActiveGamesID();
        ActiveGames.getByID(gameId, LiRequestConst.QueryKind.FULL, new LiCallbackQuery.LiActiveGamesGetByIDCallback() {
            public void onGetActiveGamesFailure(LiErrorHandler error) {
                // Get Foo Failed
            }

            public void onGetActiveGamesComplete(ActiveGames item) {
                if (item.ActiveGamesWantRandomPlayer){
                    item.setActiveGamesWantRandomPlayer(false);
                    item.save(new LiCallback.LiCallbackAction() {
                        public void onFailure(LiErrorHandler error) {
                            // Save operation Failed
                        }

                        public void onComplete(LiErrorHandler.ApplicasaResponse response, String responseMessage, LiRequestConst.RequestAction action, String id, LiManager.LiObject className) {
                            // Save operation Succeded
                        }
                    });
                } else {
                    startRandomOnlineGame(null);
                }

            }
        });



    }*/
