package edu.models.whatsapp2;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;
import java.util.prefs.Preferences;

import Util.Wrappers;
import butterknife.BindView;
import butterknife.ButterKnife;
import edu.models.whatsapp2.events.MessagesEvent;
import edu.models.whatsapp2.events.UsersEvent;
import eventb_prelude.BRelation;
import whatsapp_sequential.Machine;

public class ContactsActivity extends AppCompatActivity {
    public static final String TAG = ContactsActivity.class.getSimpleName();
    private Machine machine = App.getMachine();
    ObjectMapper mapper = new ObjectMapper();

    @BindView(R.id.contacts_list)
    ListView contactsList;

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);
//        contactsList = (ListView) findViewById(R.id.contacts_list);
        contactsList.setOnItemClickListener(contactChooseListener);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        contactsList.setAdapter(adapter);
        update();
    }

    private ListView.OnItemClickListener contactChooseListener =
            (AdapterView<?> arg0, View view, int position, long arg3) -> {
                Integer userId = PreferenceManager.getDefaultSharedPreferences(ContactsActivity.this)
                        .getInt("user_id", -1);
                Integer otherId = position + 1;
                if(machine.get_create_chat_session().guard_create_chat_session(userId, otherId)) {
                    machine.get_create_chat_session().run_create_chat_session(userId, otherId);
                    try {
                        FirebaseDatabase.getInstance().getReference()
                                .child("chat")
                                .setValue(mapper.writeValueAsString(machine.get_chat()));
                    } catch (JsonProcessingException e) {
                        Log.e("contacts", "objectMapper error");
                    }
                }
            };

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        adapter.clear();
        Map<String, String> users_names = machine.get_users_names();
        for(Integer user : machine.get_user()) {
            adapter.add(users_names.get(user.toString()));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void update() {
        FirebaseDatabase.getInstance().getReference("users_names").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        contactsList.setAdapter(adapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(UsersEvent event) {
        update();
    }
}
