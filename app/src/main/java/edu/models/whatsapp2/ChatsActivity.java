package edu.models.whatsapp2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import edu.models.whatsapp2.events.MessagesEvent;
import whatsapp_sequential.Machine;

public class ChatsActivity extends AppCompatActivity {
    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";

    private Machine machine = App.getMachine();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        update();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void update() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(MessagesEvent event) {
        update();
    }

    /** Called when the user taps the New Chat button */
    public void newChat(View view) {
        startActivity(new Intent(this, ContactsActivity.class));
    }

}
