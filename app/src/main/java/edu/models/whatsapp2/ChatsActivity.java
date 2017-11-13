package edu.models.whatsapp2;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.models.whatsapp2.events.Events;
import eventb_prelude.BRelation;
import eventb_prelude.BSet;
import eventb_prelude.Pair;
import whatsapp_sequential.Machine;

public class ChatsActivity extends AppCompatActivity {
    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";
    private Machine machine = App.getMachine();

    @BindView(R.id.chats_list)
    ListView chatsList;

    ArrayAdapter<String> adapter;
    Map<Integer, Integer> posToId = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        ButterKnife.bind(this);

        chatsList.setOnItemClickListener(chatChooseListener);
        registerForContextMenu(chatsList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        chatsList.setAdapter(adapter);
//        machine.get_messages().put("0", "helloworld!");
        FirebaseDatabase.getInstance().getReference()
                .child("messages")
                .child("0").setValue("HelloWorld!");
    }

    private ListView.OnItemClickListener chatChooseListener =
            (AdapterView<?> arg0, View view, int position, long arg3) -> {

                Intent intent = new Intent(getBaseContext(), ChattingActivity.class);
                intent.putExtra("userId", posToId.get(position));
                intent.putExtra("otherId",
                        PreferenceManager.getDefaultSharedPreferences(ChatsActivity.this)
                                .getInt("user_id", -1)
                );
                startActivity(intent);

                Log.d("me", "test");
            };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        Log.d("chat2", "Entering Context Menu");

        menu.setHeaderTitle("Chat menu");

        menu.add("block");
        menu.add("unblock");
        menu.add("delete");
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
        adapter.clear();
        BRelation<Integer, Integer> currentUserChats =
                machine.get_chat().restrictDomainTo(new BSet<>(
                        PreferenceManager.getDefaultSharedPreferences(ChatsActivity.this)
                                .getInt("user_id", -1))
                );

        Map<String, String> users_names = machine.get_users_names();
        for(Pair<Integer, Integer> chat : currentUserChats) {
            posToId.put(adapter.getCount() + 1, chat.snd());
            adapter.add(
                    users_names
                            .get(
                                    chat.snd().toString()
                            )
            );
        }
        adapter.notifyDataSetChanged();
//        findViewById (R.layout.ac).invalidate();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onChatAdded(Events.ChatCreated event) {
        Log.d("event22", "yay, update" + machine.get_chat());
        Toast.makeText(this, "yay", Toast.LENGTH_SHORT).show();

        update();
    }

    /** Called when the user taps the New Chat button */
    public void newChat(View view) {
        startActivity(new Intent(this, ContactsActivity.class));
    }

}
