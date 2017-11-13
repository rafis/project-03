package edu.models.whatsapp2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import edu.models.whatsapp2.events.UsersEvent;
import whatsapp_sequential.Machine;

public class ContactsActivity extends AppCompatActivity {
    public static final String TAG = ContactsActivity.class.getSimpleName();
    private Machine machine = App.getMachine();

    @BindView(R.id.contacts_list)
    ListView contactsList;

    ArrayAdapter<ContactListItem> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.start_new_conversation);
        ButterKnife.bind(this);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        contactsList.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        update();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void update() {
        adapter.clear();
        Map<Integer, String> users_names = machine.get_users_names();
        for (Integer user : machine.get_user())
            if (users_names.containsKey(user))
                adapter.add(new ContactListItem(users_names.get(user), user));
            else
                adapter.add(new ContactListItem("Loading...", user));
        adapter.notifyDataSetChanged();
    }

    @OnItemClick(R.id.contacts_list)
    public void onItemClick(AdapterView<?> parent, int position) {
        Integer u1 = App.getCurrentUserId();
        Integer u2 = adapter.getItem(position).getId();
        machine.get_create_chat_session().run_create_chat_session(u1, u2);
        machine.get_select_chat().run_select_chat(u1, u2);
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(UsersEvent event) {
        update();
    }

    static class ContactListItem {
        private String name;
        private Integer id;

        public ContactListItem(String name, Integer id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public Integer getId() {
            return id;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
