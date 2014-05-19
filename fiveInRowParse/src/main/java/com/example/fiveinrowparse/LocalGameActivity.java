package com.example.fiveinrowparse;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class LocalGameActivity extends Activity {
    private MyBoard mGameBoard;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        RelativeLayout MainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        mPlayerOneScore = (TextView) findViewById(R.id.player_one_score);
        mPlayerTwoScore = (TextView) findViewById(R.id.player_two_score);
        mSurrenderButton = (Button) findViewById(R.id.surrender_button);
        mPlayAgainButton = (Button) findViewById(R.id.play_again_button);
        mExitButton = (Button) findViewById(R.id.return_lobby_button);
        currentPlayerString = (TextView) findViewById(R.id.player_turn_string);
        mGameBoard = (MyBoard) findViewById(R.id.game_board);
        mGameBoard.setOnProgressChangeListener(changeListener);
        playerOneName = getIntent().getStringExtra("playerOne");
        playerTwoName = getIntent().getStringExtra("playerTwo");
        currentPlayerString.setText(playerOneName + "turn!");
        mPlayerOneScore.setText(playerOneName + ": " + Integer.toString(mPlayerOneWins));
        mPlayerTwoScore.setText(playerTwoName + ": " + Integer.toString(mPlayerTwoWins));



    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public void updateCurrentPlayerState(int playerCode){

        if(playerCode == PLAYER_ONE){
        currentPlayerString.setText(playerOneName +" turn!");
        }else if (playerCode==PLAYER_TWO){
            currentPlayerString.setText(playerTwoName +" turn!");
        } else if (playerCode == PLAYER_ONE_WON){
            mPlayerOneWins++;
            mPlayerOneScore.setText(playerOneName + ": " + Integer.toString(mPlayerOneWins));
            someOneWon();
            currentPlayerString.setText(playerOneName + " wins!");
        } else if(playerCode == PLAYER_TWO_WON){
            mPlayerTwoWins++;
            mPlayerTwoScore.setText(playerTwoName + ": " + Integer.toString(mPlayerTwoWins));
            someOneWon();
            currentPlayerString.setText(playerTwoName + " wins!");
        }
    }

    private MyBoard.OnProgressChangeListener changeListener = new MyBoard.OnProgressChangeListener() {

        public void onNextPlayer(View v, int player) {
            updateCurrentPlayerState(player);
        }

        public void onSomeoneWon(View v, int winningPlayer){
            updateCurrentPlayerState(winningPlayer);
        }

    };

    private void someOneWon(){


        mExitButton.setVisibility(View.VISIBLE);
        mPlayAgainButton.setVisibility(View.VISIBLE);
        mSurrenderButton.setVisibility(View.INVISIBLE);


    }

    public void resetGame(View view) {
        mGameBoard.resetTheGame();
        mExitButton.setVisibility(View.INVISIBLE);
        mPlayAgainButton.setVisibility(View.INVISIBLE);
        mSurrenderButton.setVisibility(View.VISIBLE);

    }

    public void surrenderGame(View view) {
        someOneWon();
        mGameBoard.surrenderTheGame();
    }

    public void exitGame(View view) {
        finish();
    }
}
