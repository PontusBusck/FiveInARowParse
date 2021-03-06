package com.example.fiveinrowparse;

import android.app.Application;

import com.parse.Parse;
import com.parse.PushService;

/**
 * Created by pontusbusck on 2014-03-27.
 */
public class MainApplication extends Application {
    private static boolean isOnlineGameVisible;
    private static boolean isFriendListVisible;
    private static boolean isGameListVisible;
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "LSxp6patelOF7SoERDRuOx0hiqGf1oSmbtyzjTKn", "NvFtCaEjvEUQEEIXDBatsdakwnIoFtKJoI2A9QzP");
        PushService.setDefaultPushCallback(this, ChooseGametypeActivity.class);
    }

    //Variabler för om spelet är uppe
    public static boolean isIsOnlineGameVisible() {
        return isOnlineGameVisible;
    }

    public static void onlineGameIsVisibe() {
        isOnlineGameVisible = true;
    }

    public static void onlineGameIsNotVisible() {
        isOnlineGameVisible = false;
    }

    //Variabler för om spelListan är uppe
    public static boolean isGameListVisible() {
        return isGameListVisible;
    }

    public static void gameListIsVisibe() {
        isGameListVisible = true;
    }

    public static void gameListIsNotVisible() {
       isGameListVisible = false;
    }


    //Variabler för om vänner listan är öppen
    public static boolean isFriendListVisible() {
        return isFriendListVisible;
    }

    public static void friendListIsVisibe() {
        isFriendListVisible = true;
    }

    public static void friendLiIsNotVisible() {
        isFriendListVisible = false;
    }
}