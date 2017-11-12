package edu.models.whatsapp2;

import android.app.Application;

import whatsapp_sequential.Machine;

public class App extends Application {
    private static Machine machine;

    @Override
    public void onCreate() {
        super.onCreate();
        machine = new Machine();
    }

    public static Machine getMachine() {
        return machine;
    }
}
