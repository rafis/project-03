package edu.models.whatsapp2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import whatsapp_sequential.Machine;

public class ChatsActivity extends AppCompatActivity {
    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
    }

    public void buuttonClick(View view) {
        Machine machine = App.getMachine();
        Integer index = machine.get_nextindex();
        Integer u1 = 1;
        Integer u2 = 2;
        machine.get_create_chat_session().run_create_chat_session(u1, u2);
        machine.get_select_chat().run_select_chat(u1, u2);
        machine.get_chatting().run_chatting(index, u1, u2);
    }
}
