package edu.models.whatsapp2;

import android.app.Application;

import whatsapp_sequential.MachineWrapper;
import whatsapp_sequential.machine3;

public class App extends Application {
    private static MachineWrapper machine;

    @Override
    public void onCreate() {
        super.onCreate();
        machine = new MachineWrapper();
    }

    public static MachineWrapper getMachine() {
        return machine;
    }
}
