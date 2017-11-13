package edu.models.whatsapp2;

import android.app.Application;
import android.preference.PreferenceManager;

import whatsapp_sequential.Machine;

public class App extends Application {
    private static Machine machine;
    private static Integer currentUserId;

    @Override
    public void onCreate() {
        super.onCreate();
        machine = new Machine();
        currentUserId = PreferenceManager.getDefaultSharedPreferences(this)
                .getInt("user_id", 0);
    }

    public static Machine getMachine() {
        return machine;
    }

    public static Integer getCurrentUserId() {
        return currentUserId;
    }

    public static void setCurrentUserId(Integer currentUserId) {
        App.currentUserId = currentUserId;
    }
}
