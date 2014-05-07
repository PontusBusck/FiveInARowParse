package com.example.fiveinrowparse;

import android.app.Application;

import com.parse.Parse;
import com.parse.PushService;

/**
 * Created by pontusbusck on 2014-03-27.
 */
public class MainApplication extends Application {
    private static boolean isOnlineGameVisible;
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "LSxp6patelOF7SoERDRuOx0hiqGf1oSmbtyzjTKn", "NvFtCaEjvEUQEEIXDBatsdakwnIoFtKJoI2A9QzP");
        PushService.setDefaultPushCallback(this, CreateGameActivity.class);
    }

    public static boolean isIsOnlineGameVisible() {
        return isOnlineGameVisible;
    }

    public static void onlineGameIsVisibe() {
        isOnlineGameVisible = true;
    }

    public static void onlineGameIsNotVisible() {
        isOnlineGameVisible = false;
    }
}