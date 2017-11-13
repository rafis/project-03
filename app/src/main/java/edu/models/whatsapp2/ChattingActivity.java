package edu.models.whatsapp2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.models.whatsapp2.events.Events;
import eventb_prelude.Pair;
import whatsapp_sequential.Machine;

public class ChattingActivity extends AppCompatActivity implements View.OnClickListener {
    private Machine machine = App.getMachine();

    @BindView(R.id.messages_list)
    ListView messagesList;
    @BindView(R.id.send_btn)
    Button send;
    @BindView(R.id.messageEdit)
    EditText messageEdit;

    ArrayAdapter<String> adapter;
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

        messagesList.setOnItemClickListener(chatChooseListener);
        send.setOnClickListener(this);
        registerForContextMenu(messagesList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        messagesList.setAdapter(adapter);
    }

    DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                //TODO
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                //Do nothing
                break;
        }
    };

    private ListView.OnItemClickListener chatChooseListener =
            (AdapterView<?> arg0, View view, int position, long arg3) -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Do you want to delete this message?")
                        .setPositiveButton("yeap", dialogClickListener)
                        .setNegativeButton("nope", dialogClickListener).show();
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
        adapter.clear();

        Map<Integer, String> messages = machine.get_messages();
        for (Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> chatContent : machine.get_chatcontent()) {
            if (chatContent.snd().fst().equals(u1) && chatContent.snd().snd().equals(u2)
                    && messages.containsKey(chatContent.fst().fst())) {
                adapter.add(messages.get(chatContent.fst().fst()));
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageReceived(Events.MessageReceived event) {
        update();
    }
}
