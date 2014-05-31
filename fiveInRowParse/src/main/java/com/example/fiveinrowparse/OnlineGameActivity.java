package com.example.fiveinrowparse;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class OnlineGameActivity extends Activity {
    private static final String SETTINGS_PREFS = "com.busck.fiveInRow.Settings";
    private static final String THEME_PREFS = "com.busck.fiveInRow.theme_prefs";
    private MyOnlineBoard mGameBoard;
    private int PLAYER_ONE = 1;
    private int PLAYER_TWO = 2;
    private int PLAYER_ONE_WON = 3;
    private int PLAYER_TWO_WON = 4;
    private String playerOneName;
    private String playerTwoName;
    private TextView currentPlayerString;
    private Button mSurrenderButton;
    private Button mPlayAgainButton;
    private Button mExitButton;
    private int mPlayerOneWins = 0;
    private int mPlayerTwoWins = 0;
    private TextView mPlayerOneScore;
    private TextView mPlayerTwoScore;

    //Online variables
    String MY_PREFS_STRING = "myPrefs";
    String MY_CURRENT_GAME_ID = "myCurrentOnlineGame";
    String DO_I_HAVE_A_GAME = "doIhaveAGame";
    private int[] mGameArray = new int[800];
    private String mCurrentGameId;
    private String mOpponent;
    String OPPONENT_USER_NAME = "opponentUsername";
    String START_NEW_GAME_INTENT_STRING = "StartnewGame";
    String OPPONENT_ID = "opponentId";
    private String mPlayerTurn;
    private String mOpponentUserId;
    Boolean mStartNewGame = true;
    Boolean mSomeOneWon = false;
    private Number mLatestMoveIndex = 900; //Utanför array index
    private Boolean mGameOver = false;
    private Boolean mMenuIsExpanded = false;
    private String mTheme;
    private ParseObject mCurrentGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_online_main);

        //mGameBoard = (MyOnlineBoard) findViewById(R.id.game_online_board_view);


        RelativeLayout MainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        mPlayerOneScore = (TextView) findViewById(R.id.player_one_score);
        mPlayerTwoScore = (TextView) findViewById(R.id.player_two_score);
        mSurrenderButton = (Button) findViewById(R.id.surrender_button);
        mPlayAgainButton = (Button) findViewById(R.id.play_again_button);
        mExitButton = (Button) findViewById(R.id.return_lobby_button);
        currentPlayerString = (TextView) findViewById(R.id.player_turn_string);
        //mGameBoard = (MyBoard) findViewById(R.id.game_board);
        //mGameBoard.setOnProgressChangeListener(changeListener);

        SharedPreferences settings = getSharedPreferences(SETTINGS_PREFS, 0);
        mTheme = settings.getString(THEME_PREFS, "dark");




            mStartNewGame = getIntent().getBooleanExtra(START_NEW_GAME_INTENT_STRING, false);
            mCurrentGameId = getIntent().getStringExtra(MY_CURRENT_GAME_ID);

            getIntent().removeExtra(START_NEW_GAME_INTENT_STRING); //Tar bort det värdet så att det inte skapas ett nytt spel om onCreate anropas igen

            /*
            mCurrentGameId = getIntent().getStringExtra(MY_CURRENT_GAME_ID);


            mOpponent = getIntent().getStringExtra(OPPONENT_USER_NAME);

            playerOneName = ParseUser.getCurrentUser().getUsername();
            playerTwoName = getIntent().getStringExtra(OPPONENT_USER_NAME);
            currentPlayerString.setText(playerOneName + "turn!");
            mOpponentUserId = getIntent().getStringExtra(OPPONENT_ID);

            mPlayerOneScore.setText(playerOneName + ": " + Integer.toString(mPlayerOneWins));
            mPlayerTwoScore.setText(playerTwoName + ": " + Integer.toString(mPlayerTwoWins));
            */

        startUpOnlineGame();

            //makeANewOnlineGame();



    }

    @Override
    protected void onResume() {
        super.onResume();
        MainApplication.onlineGameIsVisibe();
        registerReceiver(broadcastReceiver, new IntentFilter("com.busck.UPPDATE_THE_GAME"));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();



    }

    @Override
    protected void onPause() {
        super.onPause();
        MainApplication.onlineGameIsNotVisible();
        unregisterReceiver(broadcastReceiver);

    }




    public void uppdateTheGame(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Games");
        query.getInBackground(mCurrentGameId, new GetCallback<ParseObject>() {
            public void done(ParseObject game, ParseException e) {
                if (e == null) {
                    mCurrentGame = game;
                    mGameArray = gameStringToGameArray(game.getString("gameArray"));
                    mPlayerTurn = game.getString("playersTurn");
                    playerOneName = game.getString("playerOneName");
                    playerTwoName = game.getString("playerTwoName");
                    mPlayerOneWins = game.getNumber("playerOneWins").intValue();
                    mPlayerTwoWins = game.getNumber("playerTwoWins").intValue();
                    mLatestMoveIndex = game.getNumber("latestMoveIndex");
                    Boolean someOneWon = game.getBoolean("someOneWon");
                    mGameOver = game.getBoolean("gameOver");
                    int winningNumber = game.getNumber("winnerNumber").intValue();
                    if(playerOneName.equals(ParseUser.getCurrentUser().getUsername())){
                        mOpponent = playerTwoName;
                    }else{
                        mOpponent = playerOneName;
                    }

                    //Kollar om man är spelare 1 eller spelare 2
                    int myPlayerNumber;
                    if(playerOneName.equals(ParseUser.getCurrentUser().getUsername())){
                        myPlayerNumber = 1;
                    }else{
                        myPlayerNumber = 2;
                    }

                    //Ändrar info texterna
                    if(mPlayerTurn.equals(ParseUser.getCurrentUser().getUsername())){
                        currentPlayerString.setText("Your Turn!");
                    }else{

                    currentPlayerString.setText(mPlayerTurn + " " + "turn!");
                    }
                    mPlayerOneScore.setText(playerOneName + ": " + Integer.toString(mPlayerOneWins));
                    mPlayerTwoScore.setText(playerTwoName + ": " + Integer.toString(mPlayerTwoWins));

                    //Uppdaterar brädet
                    mGameBoard.uppdateGame(mGameArray, mPlayerTurn, myPlayerNumber, someOneWon, winningNumber, mLatestMoveIndex);

                    //Visar eller döljer spela igen knappen
                    if(!mGameOver){
                        mPlayAgainButton.setVisibility(View.INVISIBLE);
                        mPlayAgainButton.setText("Play again");

                    } else if(mGameOver){
                        mPlayAgainButton.setVisibility(View.VISIBLE);
                    }

                    //Visar eller döljer layouten som talar om att motspelaren vill spela igen
                    if(myPlayerNumber == 1){
                        if(game.getString("playerTwoVotePlayAgain").equals("yes")){
                            RelativeLayout playagainLagout = (RelativeLayout) findViewById(R.id.play_again_layout);
                            playagainLagout.setVisibility(View.VISIBLE);
                        } else if(game.getString("playerOneVotePlayAgain").equals("yes")){ //Ändrar text på knappen om man redan skickat invite
                            mPlayAgainButton.setText("Invite sent");
                        }
                    } else if (myPlayerNumber == 2) {
                        if (game.getString("playerOneVotePlayAgain").equals("yes")) {
                            RelativeLayout playagainLagout = (RelativeLayout) findViewById(R.id.play_again_layout);
                            playagainLagout.setVisibility(View.VISIBLE);
                        }else if(game.getString("playerTwoVotePlayAgain").equals("yes")){ //Ändrar text på knappen om man redan skickat invite
                            mPlayAgainButton.setText("Invite sent");
                        }
                    }


                } else {
                    Toast.makeText(OnlineGameActivity.this, "could not contact the server", Toast.LENGTH_SHORT).show();
                }

                //Ändrar tillbaka texten på knappen
                Button refreshButton = (Button) findViewById(R.id.refresh_game_button);
                refreshButton.setText("Refresh");
            }
        });
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            uppdateTheGame();
        }
    };


    public void startUpOnlineGame() {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_STRING, 0);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (mStartNewGame) {
            //mOpponent = playerTwoName;
            mPlayerTurn = ParseUser.getCurrentUser().getUsername();
            final ParseObject game = new ParseObject("Games");
            game.put("playerOneName", currentUser.getUsername());
            game.put("playerOneId", currentUser.getObjectId());
            game.put("playerTwoName", getIntent().getStringExtra(OPPONENT_USER_NAME));
            game.put("playerTwoId", getIntent().getStringExtra(OPPONENT_ID));
            game.put("playersTurn", currentUser.getUsername());
            game.put("someOneWon", false);
             game.put("gameOver", false);
            game.put("playerOneVotePlayAgain", "no");
            game.put("playerTwoVotePlayAgain", "no");
            game.put("winnerNumber", 0);
            game.put("latestMoveIndex", mLatestMoveIndex);
            game.put("gameArray", Arrays.toString(mGameArray));
            game.put("playerOneWins", 0);
            game.put("playerTwoWins", 0);
            game.put("playerTwoAccepted", false);
            game.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        mCurrentGame = game;
                        mCurrentGameId = game.getObjectId();
                        getIntent().putExtra(MY_CURRENT_GAME_ID, mCurrentGameId); //Lägger till current game Id i intentet, om man släcker skärmen så att det startas om
                        playerTwoName = game.getString("playerTwoName");
                        playerOneName = game.getString("playerOneName");
                        mOpponent = getIntent().getStringExtra(OPPONENT_USER_NAME);
                        mOpponentUserId = getIntent().getStringExtra(OPPONENT_ID);
                        mPlayerTurn = game.getString("playersTurn");

                        if(mPlayerTurn.equals(ParseUser.getCurrentUser().getUsername())){
                            currentPlayerString.setText("Your Turn!");
                        }else{

                            currentPlayerString.setText(mPlayerTurn + " " + "turn!");
                        }
                        mPlayerOneScore.setText(playerOneName + ": " + Integer.toString(mPlayerOneWins));
                        mPlayerTwoScore.setText(playerTwoName + ": " + Integer.toString(mPlayerTwoWins));

                        if (mGameBoard == null) {
                            Log.d("No board", "noBoard");
                            createTheBoard(mGameArray, false, 0, mTheme);
                            notifyTheOtherUserAboutTheGame(mOpponentUserId);
                        }
                        // Saved successfully.

                        mStartNewGame = false;


                    } else {
                        // The save failed.
                        Log.d("tag", "User update error: " + e);
                    }
                }
            });


        } else if (!mStartNewGame) {


            ParseQuery<ParseObject> query = ParseQuery.getQuery("Games");
            query.getInBackground(mCurrentGameId, new GetCallback<ParseObject>() {
                public void done(ParseObject game, ParseException e) {
                    if (e == null) {
                        mGameArray = gameStringToGameArray(game.getString("gameArray"));

                        if (mGameBoard == null) {
                            mPlayerTurn = game.getString("playersTurn");
                            mCurrentGame = game;

                            mOpponent = getIntent().getStringExtra(OPPONENT_USER_NAME);
                            mOpponentUserId = getIntent().getStringExtra(OPPONENT_ID);
                            playerOneName = game.getString("playerOneName");
                            playerTwoName = game.getString("playerTwoName");
                            mLatestMoveIndex = game.getNumber("latestMoveIndex");
                            mGameOver = game.getBoolean("gameOver");
                            mPlayerOneWins = game.getNumber("playerOneWins").intValue();
                            mPlayerTwoWins = game.getNumber("playerTwoWins").intValue();


                            Boolean someOneWon = game.getBoolean("someOneWon");
                            int winningNumber = game.getNumber("winnerNumber").intValue();


                            //Kollar om man är spelare 1 eller spelare 2
                            int myPlayerNumber;
                            if(playerOneName.equals(ParseUser.getCurrentUser().getUsername())){
                                myPlayerNumber = 1;
                            }else{
                                myPlayerNumber = 2;
                            }

                            //Visar eller döljer layouten som talar om att motspelaren vill spela igen
                            if(myPlayerNumber == 1){
                                if(game.getString("playerTwoVotePlayAgain").equals("yes")){
                                    RelativeLayout playagainLagout = (RelativeLayout) findViewById(R.id.play_again_layout);
                                    playagainLagout.setVisibility(View.VISIBLE);
                                } else if(game.getString("playerOneVotePlayAgain").equals("yes")){ //Ändrar text på knappen om man redan skickat invite
                                    mPlayAgainButton.setText("Invite sent");
                                }
                            } else if (myPlayerNumber == 2) {
                                if (game.getString("playerOneVotePlayAgain").equals("yes")) {
                                    RelativeLayout playagainLagout = (RelativeLayout) findViewById(R.id.play_again_layout);
                                    playagainLagout.setVisibility(View.VISIBLE);
                                }else if(game.getString("playerTwoVotePlayAgain").equals("yes")){ //Ändrar text på knappen om man redan skickat invite
                                    mPlayAgainButton.setText("Invite sent");
                                }
                            }

                            //Ändrar på texterna
                            if(mPlayerTurn.equals(ParseUser.getCurrentUser().getUsername())){
                                currentPlayerString.setText("Your Turn!");
                            }else{

                                currentPlayerString.setText(mPlayerTurn + " " + "turn!");
                            }

                            if(!mGameOver){
                                mPlayAgainButton.setVisibility(View.INVISIBLE);
                                mPlayAgainButton.setText("Play Again");

                            } else if(mGameOver){
                                mPlayAgainButton.setVisibility(View.VISIBLE);
                            }

                            mPlayerOneScore.setText(playerOneName + ": " + Integer.toString(mPlayerOneWins));
                            mPlayerTwoScore.setText(playerTwoName + ": " + Integer.toString(mPlayerTwoWins));
                            createTheBoard(mGameArray, someOneWon, winningNumber, mTheme);
                            Log.d("No board", "noBoard");
                        }

                    } else {
                        Toast.makeText(OnlineGameActivity.this, "Could not contact the server, trying again", Toast.LENGTH_SHORT).show();
                        startUpOnlineGame();
                    }
                }
            });

        }
    }

    public void ChangeThemeButton(View view){
        mTheme = (String) view.getTag();

        SharedPreferences settings = getSharedPreferences(SETTINGS_PREFS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(THEME_PREFS, mTheme);
        editor.commit();

        RelativeLayout container = (RelativeLayout) findViewById(R.id.game_online_board_view);
        container.removeAllViews();

        createTheBoard(mGameArray, false, 0, mTheme);

    }

    private void createTheBoard(int[] gameArray, Boolean someOneWon, int winningNumber, String theme) {
        int myPlayerNumber;

        if(playerOneName.equals(ParseUser.getCurrentUser().getUsername())){
            myPlayerNumber = 1;
        }else{
            myPlayerNumber = 2;
        }
        RelativeLayout container = (RelativeLayout) findViewById(R.id.game_online_board_view);
        mGameBoard = new MyOnlineBoard(this, gameArray, mPlayerTurn, myPlayerNumber, someOneWon, winningNumber, mLatestMoveIndex, theme);


        container.addView(mGameBoard);
        mGameBoard.setOnProgressChangeListener(changeListener);
        mGameBoard.uppdateGame(gameArray, mPlayerTurn, myPlayerNumber, someOneWon, winningNumber, mLatestMoveIndex);

    }


    private void notifyTheOtherUserAboutTheGame(String recivingUserId) {
        ParseQuery userQuery = ParseInstallation.getQuery();
        userQuery.whereEqualTo("userId", recivingUserId);

        JSONObject data = new JSONObject();
        try {
            data.put("action", "com.example.NEW_GAME_INVITE");
        data.put("fromUser", ParseUser.getCurrentUser().getUsername());
        data.put("gameId", mCurrentGameId);
            data.put("fromUserId", ParseUser.getCurrentUser().getObjectId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParsePush parsePush = new ParsePush();
        parsePush.setQuery(userQuery);
        parsePush.setMessage("New game invite");
        parsePush.setData(data);
        parsePush.sendInBackground();

    }

    //Förvandlar gamestringen från parse till array
    private int[] gameStringToGameArray(String gameString) {
        Log.d("GameArrayString", gameString);
        String currentGameString = gameString.replace("[", "");
        currentGameString = currentGameString.replace("]", "");
        currentGameString = currentGameString.replace(" ", "");
        String[] gameStringArray = currentGameString.split(",");

        int[] gameIntArray = new int[gameStringArray.length];
        for (int i = 0; i < gameStringArray.length; i++) {

            gameIntArray[i] = Integer.parseInt(gameStringArray[i]);

        }

        return gameIntArray;

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public void updateCurrentPlayerState(int playerCode, int[] gameArray) {

        if (playerCode == PLAYER_ONE) {
            if (playerOneName.equals(ParseUser.getCurrentUser().getUsername())) {
                currentPlayerString.setText("Your Turn!");
            } else {

                currentPlayerString.setText(playerOneName + " turn!");
            }
            uppdateGameOnParse(gameArray);
        } else if (playerCode == PLAYER_TWO) {
            if (playerTwoName.equals(ParseUser.getCurrentUser().getUsername())) {
                currentPlayerString.setText("Your Turn!");
            } else {

                currentPlayerString.setText(playerTwoName + " turn!");
            }
            uppdateGameOnParse(gameArray);
        } else if (playerCode == PLAYER_ONE_WON) {

            if (!mGameOver) {
                mPlayerOneWins++; //Om spelet inte var slut plussas det på en

            }
            mPlayerOneScore.setText(playerOneName + ": " + Integer.toString(mPlayerOneWins));
            someOneWon();
            currentPlayerString.setText(playerOneName + " wins!");
            uppdateGameOnParseWinner(gameArray, 1);


        } else if (playerCode == PLAYER_TWO_WON) {
            if (!mGameOver) {
                mPlayerTwoWins++; //Om spelet inte var slut plussas det på 1

            }
            mPlayerTwoScore.setText(playerTwoName + ": " + Integer.toString(mPlayerTwoWins));
            someOneWon();
            currentPlayerString.setText(playerTwoName + " wins!");
            uppdateGameOnParseWinner(gameArray, 2);


        }
    }

    private void uppdateGameOnParse(final int[] gameArray) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Games");
        query.getInBackground(mCurrentGameId, new GetCallback<ParseObject>() {
            public void done(ParseObject game, ParseException e) {
                if (e == null) {
                    game.put("playersTurn", mOpponent);
                    game.put("latestMoveIndex", mLatestMoveIndex);
                    game.put("gameArray", Arrays.toString(gameArray));
                    game.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                notifyTheOtherUserAboutTheMove(mOpponentUserId);


                            } else {
                                // The save failed.
                                Log.d("tag", "User update error: " + e);
                            }
                        }
                    });
                }
            }
        });

    }

    private void uppdateGameOnParseWinner(final int[] gameArray, final int winner) {
        if(!mGameOver) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Games");
            query.getInBackground(mCurrentGameId, new GetCallback<ParseObject>() {
                public void done(ParseObject game, ParseException e) {
                    if (e == null) {
                        game.put("playersTurn", mOpponent);
                        game.put("someOneWon", true);
                        game.put("winnerNumber", winner);
                        game.put("gameOver", true);
                        game.put("latestMoveIndex", mLatestMoveIndex);
                        game.put("playerOneWins", mPlayerOneWins);
                        game.put("playerTwoWins", mPlayerTwoWins);
                        game.put("gameArray", Arrays.toString(gameArray));
                        game.saveInBackground(new SaveCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    notifyTheOtherUserAboutTheMove(mOpponentUserId);


                                } else {
                                    // The save failed.
                                    Log.d("tag", "User update error: " + e);
                                }
                            }
                        });
                    }
                }
            });
        }

    }

    private void notifyTheOtherUserAboutTheMove(String recivingUserId) {
        ParseQuery userQuery = ParseInstallation.getQuery();
        userQuery.whereEqualTo("userId", recivingUserId);

        JSONObject data = new JSONObject();
        try {
            data.put("action", "com.busck.NEW_PLAYER_MOVE");
            data.put("fromUser", ParseUser.getCurrentUser().getUsername());
            data.put("gameId", mCurrentGameId);
            data.put("fromUserId", ParseUser.getCurrentUser().getObjectId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParsePush parsePush = new ParsePush();
        parsePush.setQuery(userQuery);
        parsePush.setMessage("New game invite");
        parsePush.setData(data);
        parsePush.sendInBackground();

    }

    private MyOnlineBoard.OnProgressChangeListener changeListener = new MyOnlineBoard.OnProgressChangeListener() {

        public void onNextPlayer(View v, int player, int[] gameArray, Number lastMoveIndex) {
            mLatestMoveIndex = lastMoveIndex;
            updateCurrentPlayerState(player, gameArray);
        }

        public void onSomeoneWon(View v, int winningPlayer, int[] gameArray, Number lastMoveIndex) {
            mLatestMoveIndex = lastMoveIndex;
            updateCurrentPlayerState(winningPlayer, gameArray);
        }

    };

    private void someOneWon() {


        //mExitButton.setVisibility(View.VISIBLE);
        mPlayAgainButton.setVisibility(View.VISIBLE);
        //mSurrenderButton.setVisibility(View.INVISIBLE);


    }

    public void playAgain(View view) {


        mPlayAgainButton.setText("Invite sent");
        RelativeLayout playAgainLayout = (RelativeLayout) findViewById(R.id.play_again_layout);
        playAgainLayout.setVisibility(View.INVISIBLE);




        ParseQuery<ParseObject> query = ParseQuery.getQuery("Games");

        query.getInBackground(mCurrentGameId, new GetCallback<ParseObject>() {
            public void done(ParseObject game, ParseException e) {
                if (e == null) {


                    if(playerOneName.equals(ParseUser.getCurrentUser().getUsername())){


                        if(game.getString("playerTwoVotePlayAgain").equals("yes")){
                            resetGame();
                        } else if (!game.getString("playerOneVotePlayAgain").equals("yes")){
                            game.put("playerOneVotePlayAgain", "yes");
                            game.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    notifyTheOtherUserAboutPlayAgain(mOpponentUserId);
                                }
                            });
                        }

                    }else{

                        if(game.getString("playerOneVotePlayAgain").equals("yes")){
                            resetGame();
                        } else if (!game.getString("playerTwoVotePlayAgain").equals("yes")){
                            game.put("playerTwoVotePlayAgain", "yes");
                            game.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    notifyTheOtherUserAboutPlayAgain(mOpponentUserId);
                                }
                            });
                        }
                    }
                }
            }
        });


    }

    private void notifyTheOtherUserAboutPlayAgain(String recivingUserId) {
        ParseQuery userQuery = ParseInstallation.getQuery();
        userQuery.whereEqualTo("userId", recivingUserId);

        JSONObject data = new JSONObject();
        try {
            data.put("action", "com.busck.PLAY_AGAIN");
            data.put("fromUser", ParseUser.getCurrentUser().getUsername());
            data.put("gameId", mCurrentGameId);
            data.put("fromUserId", ParseUser.getCurrentUser().getObjectId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParsePush parsePush = new ParsePush();
        parsePush.setQuery(userQuery);
        parsePush.setMessage(mOpponent + "wants to play again!");
        parsePush.setData(data);
        parsePush.sendInBackground();

    }


    private void resetGame (){

        //mGameBoard.resetTheGame();
        //mExitButton.setVisibility(View.INVISIBLE);
        mPlayAgainButton.setVisibility(View.INVISIBLE);
        mPlayAgainButton.setText("Play Again");
        //mSurrenderButton.setVisibility(View.VISIBLE);

        mGameArray = new int[800];
        mLatestMoveIndex = 900;


        final int myPlayerNumber;
        if(playerOneName.equals(ParseUser.getCurrentUser().getUsername())){
            myPlayerNumber = 1;
        }else{
            myPlayerNumber = 2;
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Games");
        query.getInBackground(mCurrentGameId, new GetCallback<ParseObject>() {
            public void done(ParseObject game, ParseException e) {
                if (e == null) {
                    game.put("playerOneVotePlayAgain", "no");
                    game.put("playerTwoVotePlayAgain", "no");
                    game.put("playersTurn", mOpponent);
                    game.put("someOneWon", false);
                    game.put("winnerNumber", 0);
                    game.put("latestMoveIndex", 900);
                    game.put("gameOver", false);
                    game.put("gameArray", Arrays.toString(mGameArray));
                    game.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                notifyTheOtherUserAboutPlayAgain(mOpponentUserId);
                                mGameBoard.uppdateGame(mGameArray, mOpponent, myPlayerNumber, false, 0, mLatestMoveIndex);
                                uppdateTheGame();
                            } else {
                                // The save failed.
                                Log.d("tag", "User update error: " + e);
                            }
                        }
                    });
                }
            }
        });

    }

    public void surrenderGame(View view) {
        someOneWon();
       // mGameBoard.surrenderTheGame();
    }

    public void exitGame(View view) {
        finish();
    }

    //Omvandlar dp till pixlar
    public int getPx(float dp){
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int pixels = (int) (metrics.density * dp + 0.5f);
        return pixels;
    }

    //startar animationen för att visa den utfällbara resultattavlan
    public void openMenu(View view) {

        RelativeLayout totalBar = (RelativeLayout) findViewById(R.id.menu_layout);


        if(!mMenuIsExpanded) {
            totalBar.setVisibility(View.VISIBLE);
            mMenuIsExpanded = true;
        }else{
            totalBar.setVisibility(View.INVISIBLE);
            mMenuIsExpanded = false;

        }
    }

    public void refreshGameButtonClicked(View view) {
        Button refreshButton = (Button) findViewById(R.id.refresh_game_button);
        refreshButton.setText("Loading...");
        uppdateTheGame();
    }
}
