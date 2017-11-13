package edu.models.whatsapp2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import eventb_prelude.BRelation;
import eventb_prelude.BSet;
import eventb_prelude.INT;
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
    Integer userId = -1;
    Integer otherId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        ButterKnife.bind(this);

        messagesList.setOnItemClickListener(chatChooseListener);
        send.setOnClickListener(this);
        registerForContextMenu(messagesList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        messagesList.setAdapter(adapter);

        userId = getIntent().getIntExtra("userId", -1);
        otherId = getIntent().getIntExtra("otherId", -1);
    }

    DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                //TODO: delete message
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
        Log.d("sendingmessage", "next index " + machine.get_messages().size());
        Log.d("sendingmessage", "other id " + otherId);
        int index = machine.get_messages().size() + 2;

        if(!messageEdit.getText().toString().equals("")) {
            machine.get_messages().put("" + index,
                    messageEdit.getText().toString());
            FirebaseDatabase.getInstance().getReference()
                    .child("messages")
                    .child("" + index)
                    .setValue(messageEdit.getText().toString());

            machine.get_chatcontent().add(new Pair<>(
                    new Pair<>(index, userId),
                    new Pair<>(userId, otherId)
            ));
            machine.get_chatcontent().add(new Pair<>(
                    new Pair<>(index, userId),
                    new Pair<>(otherId, userId)
            ));
            machine.set_chatcontent(machine.get_chatcontent());
        }
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

        Map<String, String> messages = machine.get_messages();
        for(Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> chatContent : machine.get_chatcontent()) {
            if (chatContent.snd().fst().equals(userId) && chatContent.snd().snd().equals(otherId)
                    && messages.containsKey(chatContent.fst().toString())) {
                adapter.add(messages.get(chatContent.fst().toString()));
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void messageReceived(Events.MessageReceived event) {
        Log.d("event33", "yay, update chatting" + machine.get_chatcontent());
        Toast.makeText(this, "yay", Toast.LENGTH_SHORT).show();

        update();
    }
}
