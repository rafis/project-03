package edu.models.whatsapp2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TwoLineListItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import edu.models.whatsapp2.events.ChatsEvent;
import edu.models.whatsapp2.events.MessagesEvent;
import eventb_prelude.BRelation;
import eventb_prelude.BSet;
import eventb_prelude.Pair;
import whatsapp_sequential.Machine;

public class ChatsActivity extends AppCompatActivity {
    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";
    private Machine machine = App.getMachine();

    @BindView(R.id.chats_list)
    ListView chatsList;

    ChatsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        ButterKnife.bind(this);
        adapter = new ChatsAdapter(this);
        chatsList.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Chat menu");

        menu.add("block");
        menu.add("unblock");
        menu.add("delete");
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

    @OnItemClick(R.id.chats_list)
    public void onItemClick(AdapterView<?> parent, int position) {
        Integer u1 = App.getCurrentUserId();
        Integer u2 = ((ChatListItem) adapter.getItem(position)).getU2();
        machine.get_select_chat().run_select_chat(u1, u2);
    }

    private void update() {
        Integer u1 = App.getCurrentUserId();
        if (machine.get_active().domain().contains(u1)) {
            startActivity(new Intent(this, ChattingActivity.class));
            return;
        }
        BRelation<Integer, Integer> chats = machine.get_chat().restrictDomainTo(new BSet<>(u1));
        List<ChatListItem> listItems = new ArrayList<>(chats.size());
        for (Pair<Integer, Integer> chat : chats) {
            Integer u2 = chat.snd();
            listItems.add(new ChatListItem(u2, machine.get_users_names().get(u2), "this is the last message"));
        }
        adapter.setChats(listItems);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChat(ChatsEvent event) {
        update();
    }

    @OnClick(R.id.new_chat_btn)
    public void newChatClick(View view) {
        startActivity(new Intent(this, ContactsActivity.class));
    }

    static class ChatsAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        private List<ChatListItem> chats;

        public ChatsAdapter(Context context) {
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return chats == null ? 0 : chats.size();
        }

        @Override
        public Object getItem(int i) {
            return chats.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TwoLineListItem listItem;

            if (view == null) {
                listItem = (TwoLineListItem) layoutInflater.inflate(android.R.layout.simple_list_item_2, null);
            } else {
                listItem = (TwoLineListItem) view;
            }

            listItem.getText1().setText(chats.get(i).getName());
            listItem.getText2().setText(chats.get(i).getLastMessage());

            return listItem;
        }

        public List<ChatListItem> getChats() {
            return chats;
        }

        public void setChats(List<ChatListItem> chats) {
            this.chats = chats;
            notifyDataSetChanged();
        }
    }

    static class ChatListItem {
        private Integer u2;
        private String name;
        private String lastMessage;

        public ChatListItem(Integer u2, String name, String lastMessage) {
            this.u2 = u2;
            this.name = name;
            this.lastMessage = lastMessage;
        }

        public Integer getU2() {
            return u2;
        }

        public String getName() {
            return name;
        }

        public String getLastMessage() {
            return lastMessage;
        }
    }
}
