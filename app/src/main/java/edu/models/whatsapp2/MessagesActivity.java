package edu.models.whatsapp2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import edu.models.whatsapp2.events.MessagesEvent;
import whatsapp_sequential.Machine;

public class MessagesActivity extends AppCompatActivity {
    public static final String TAG = MessagesActivity.class.getSimpleName();

    Machine machine = App.getMachine();
    Integer u1, u2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        u1 = App.getCurrentUserId();
        u2 = machine.get_active().apply(u1);
        if (u2 == null) {
            finish();
            return;
        }
        setContentView(R.layout.activity_messages);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(machine.get_users_names().get(u2));
        update();
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

    private void unselect() {
        machine.get_unselect_chat().run_unselect_chat(u1, u2);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(MessagesEvent event) {
        update();
    }
}
