package com.example.fiveinrowparse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseAnalytics;


public class ChooseGametypeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose_gametype_activity);
        ParseAnalytics.trackAppOpened(getIntent());





    }

    @Override
    protected void onResume() {
        super.onResume();

    }




    public void openOnlineGameOptionScreen(View view) {
            Intent intent = new Intent(this, GameListActivity.class);
            startActivity(intent);


    }

    public void openLocalGameOptionScreen(View view){
        Intent intent = new Intent(this, setupLocalGame.class);
        startActivity(intent);

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
