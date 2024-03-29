package edu.models.whatsapp2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import edu.models.whatsapp2.events.ChatsEvent;
import eventb_prelude.BRelation;
import eventb_prelude.BSet;
import eventb_prelude.Pair;
import whatsapp_sequential.Machine;

public class ChatsActivity extends AppCompatActivity {
    private Machine machine = App.getMachine();
    private Integer u1 = App.getCurrentUserId();

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

    private ChatListItem chatToModify;

    @OnItemLongClick(R.id.chats_list)
    public boolean OnItemLongClick(int position) {
        chatToModify = (ChatListItem) adapter.getItem(position);
        String muteButtonText;
        if (machine.get_muted().contains(new Pair<>(u1, chatToModify.getU2())))
            muteButtonText = "unmute";
        else
            muteButtonText = "mute";
        new AlertDialog.Builder(this)
                .setMessage("What do you want with this chat?")
                .setPositiveButton("delete", dialogClickListener)
                .setNegativeButton(muteButtonText, dialogClickListener)
                .setNeutralButton("nope", dialogClickListener)
                .show();
        return true;
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == DialogInterface.BUTTON_POSITIVE) {
                machine.get_delete_chat_session().run_delete_chat_session(u1, chatToModify.getU2());
            } else if (i == DialogInterface.BUTTON_NEGATIVE) {
                if (machine.get_muted().contains(new Pair<>(u1, chatToModify.getU2())))
                    machine.get_unmute_chat().run_unmute_chat(u1, chatToModify.getU2());
                else
                    machine.get_mute_chat().run_mute_chat(u1, chatToModify.getU2());
            }
            chatToModify = null;
        }
    };

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
            Boolean toRead = machine.get_toread().contains(chat);
            Boolean muted = machine.get_muted().contains(chat);
            BSet<Pair<Integer, Integer>> lastMessages = machine.get_chatcontent().restrictRangeTo(new BSet<>(chat)).domain();
            Pair<Integer, Integer> lastMessage = null;
            if (lastMessages.size() != 0)
                lastMessage = lastMessages.last();
            String lastText;
            if (lastMessage != null && machine.get_messages().containsKey(lastMessage.fst()))
                lastText = machine.get_messages().get(lastMessage.fst());
            else lastText = "";
            String name;
            if (machine.get_users_names().containsKey(u2))
                name = machine.get_users_names().get(u2);
            else
                name = "Loading...";
            listItems.add(new ChatListItem(u2, name, lastText, toRead, muted));
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
            ChatListItem chat = chats.get(i);
            int color;
            if (chat.getMuted())
                color = R.color.muted;
            else if (chat.getToRead())
                color = R.color.toread;
            else color = R.color.inactive;

            listItem.getText1().setText(chat.getName());
            listItem.getText2().setText(chat.getLastMessage());
            listItem.setBackgroundResource(color);

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
        private Boolean toRead;
        private Boolean muted;


        public ChatListItem(Integer u2, String name, String lastMessage, Boolean toRead, Boolean muted) {
            this.u2 = u2;
            this.name = name;
            this.lastMessage = lastMessage;
            this.toRead = toRead;
            this.muted = muted;
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

        public Boolean getToRead() {
            return toRead;
        }

        public Boolean getMuted() {
            return muted;
        }
    }
}
