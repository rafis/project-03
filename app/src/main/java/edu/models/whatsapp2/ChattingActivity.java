package edu.models.whatsapp2;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TwoLineListItem;

import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemLongClick;
import edu.models.whatsapp2.events.ChatsEvent;
import edu.models.whatsapp2.events.MessagesEvent;
import eventb_prelude.Pair;
import whatsapp_sequential.Machine;

public class ChattingActivity extends AppCompatActivity implements View.OnClickListener {
    private Machine machine = App.getMachine();

    @BindView(R.id.messages_list)
    ListView messagesList;
    @BindView(R.id.send_btn)
    Button sendButton;
    @BindView(R.id.messageEdit)
    EditText messageEdit;

    ChatsAdapter adapter;
    Integer u1 = -1;
    Integer u2 = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        ButterKnife.bind(this);

        u1 = App.getCurrentUserId();
        u2 = machine.get_active().apply(u1);
        if (u2 == null) {
            finish();
            return;
        }

        sendButton.setOnClickListener(this);
        registerForContextMenu(messagesList);
        adapter = new ChatsAdapter(this);
        messagesList.setAdapter(adapter);
    }

    private MessageListItem messageToDelete;

    @OnItemLongClick(R.id.messages_list)
    public boolean OnItemLongClick(int position) {
        messageToDelete = (MessageListItem) adapter.getItem(position);
        new AlertDialog.Builder(this)
                .setMessage("Do you want to delete this message?")
                .setPositiveButton("yeap", dialogClickListener)
                .setNegativeButton("nope", dialogClickListener).show();
        return true;
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == DialogInterface.BUTTON_POSITIVE) {
                if (messageToDelete.getOwner().equals(u1))
                    machine.get_remove_content().run_remove_content(messageToDelete.content, messageToDelete.getSender(), messageToDelete.getReceiver());
                else
                    machine.get_delete_content().run_delete_content(messageToDelete.content, messageToDelete.getSender(), messageToDelete.getReceiver());
            }
            messageToDelete = null;
        }
    };


    @Override
    public void onClick(View view) {
        if (messageEdit.getText().toString().equals("")) return;
        Integer index = machine.get_nextindex();

        FirebaseDatabase.getInstance().getReference()
                .child("messages")
                .child("" + machine.get_nextindex())
                .setValue(messageEdit.getText().toString());

        machine.get_chatting().run_chatting(index, u1, u2);

        messageEdit.setText("");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                unselect();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        unselect();
    }

    private void unselect() {
        machine.get_unselect_chat().run_unselect_chat(u1, u2);
    }

    private void update() {
        boolean enabled = !machine.get_muted().contains(new Pair<>(u2, u1));
        sendButton.setEnabled(enabled);
        messageEdit.setEnabled(enabled);

        Map<Integer, String> messages = machine.get_messages();
        if (messages == null) messages = new HashMap<>();
        List<MessageListItem> messageList = new ArrayList<>(messages.size());
        for (Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> chatContent : machine.get_chatcontent()) {
            if (chatContent.snd().fst().equals(u1) && chatContent.snd().snd().equals(u2)
                    && messages.containsKey(chatContent.fst().fst())) {
                Integer sender = chatContent.fst().snd();
                messageList.add(new MessageListItem(sender,
                        chatContent.snd().fst(),
                        chatContent.snd().snd(),
                        chatContent.fst().fst(),
                        machine.get_users_names().get(sender),
                        messages.get(chatContent.fst().fst())
                ));
            }
        }

        adapter.setChats(messageList);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessagesEvent event) {
        update();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChatsEvent(ChatsEvent event) {
        update();
    }

    static class ChatsAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        private List<MessageListItem> chats;

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

            MessageListItem message = chats.get(i);

            listItem.getText1().setText(message.getName());
            listItem.getText2().setText(message.getMessage());

            int gravity;
            if (App.getCurrentUserId().equals(message.getOwner()))
                gravity = Gravity.END;
            else
                gravity = Gravity.START;

            listItem.getText1().setGravity(gravity);
            listItem.getText2().setGravity(gravity);

            return listItem;
        }

        public void setChats(List<MessageListItem> chats) {
            this.chats = chats;
            notifyDataSetChanged();
        }
    }

    static class MessageListItem {
        private Integer owner;
        private Integer sender;
        private Integer receiver;
        private Integer content;
        private String name;
        private String message;

        public MessageListItem(Integer owner, Integer sender, Integer receiver, Integer content, String name, String message) {
            this.owner = owner;
            this.sender = sender;
            this.receiver = receiver;
            this.content = content;
            this.name = name;
            this.message = message;
        }

        public Integer getOwner() {
            return owner;
        }

        public Integer getSender() {
            return sender;
        }

        public Integer getReceiver() {
            return receiver;
        }

        public Integer getContent() {
            return content;
        }

        public String getName() {
            return name;
        }

        public String getMessage() {
            return message;
        }
    }
}
