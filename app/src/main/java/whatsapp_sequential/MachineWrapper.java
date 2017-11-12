package whatsapp_sequential;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import edu.models.whatsapp2.events.UsersEvent;
import eventb_prelude.BRelation;
import eventb_prelude.BSet;
import eventb_prelude.Pair;

public class MachineWrapper extends machine3 {
    private static final String TAG = MachineWrapper.class.getSimpleName();

    private Gson gson = new GsonBuilder().create();

    public MachineWrapper() {
        super();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.addValueEventListener(valueEventListener);
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.getKey() == null) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    onDataChange(snapshot);
                return;
            }
            switch (dataSnapshot.getKey()) {
                case "contentorder":
                    MachineWrapper.super.set_contentorder(gson.fromJson(dataSnapshot.getValue(String.class), BRelation.class));
                    break;
                case "owner":
                    MachineWrapper.super.set_owner(gson.fromJson(dataSnapshot.getValue(String.class), BRelation.class));
                    break;
                case "toread":
                    MachineWrapper.super.set_toread(gson.fromJson(dataSnapshot.getValue(String.class), BRelation.class));
                    break;
                case "inactive":
                    MachineWrapper.super.set_inactive(gson.fromJson(dataSnapshot.getValue(String.class), BRelation.class));
                    break;
                case "chatcontent":
                    MachineWrapper.super.set_chatcontent(gson.fromJson(dataSnapshot.getValue(String.class), BRelation.class));
                    EventBus.getDefault().post(new UsersEvent());
                    break;
                case "chat":
                    MachineWrapper.super.set_chat(gson.fromJson(dataSnapshot.getValue(String.class), BRelation.class));
                    break;
                case "nextindex":
                    MachineWrapper.super.set_nextindex(dataSnapshot.getValue(Integer.class));
                    break;
                case "nextUserIndex":
                    MachineWrapper.super.set_nextUserIndex(dataSnapshot.getValue(Integer.class));
                    break;
                case "user":
                    MachineWrapper.super.set_user(gson.fromJson(dataSnapshot.getValue(String.class), BSet.class));
                    EventBus.getDefault().post(new UsersEvent());
                    break;
                case "active":
                    MachineWrapper.super.set_active(gson.fromJson(dataSnapshot.getValue(String.class), BRelation.class));
                    break;
                case "muted":
                    MachineWrapper.super.set_muted(gson.fromJson(dataSnapshot.getValue(String.class), BRelation.class));
                    break;
                case "content":
                    MachineWrapper.super.set_content(gson.fromJson(dataSnapshot.getValue(String.class), BSet.class));
                    break;
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.d(TAG, "databaseError:" + databaseError.getMessage());
        }
    };

    @Override
    public void set_contentorder(BRelation<Integer, Integer> contentorder) {
        super.set_contentorder(contentorder);
        FirebaseDatabase.getInstance().getReference("contentorder").setValue(gson.toJson(contentorder));
    }

    @Override
    public void set_owner(BRelation<Integer, Integer> owner) {
        super.set_owner(owner);
        FirebaseDatabase.getInstance().getReference("owner").setValue(gson.toJson(owner));
    }

    @Override
    public void set_toread(BRelation<Integer, Integer> toread) {
        super.set_toread(toread);
        FirebaseDatabase.getInstance().getReference("toread").setValue(gson.toJson(toread));
    }

    @Override
    public void set_inactive(BRelation<Integer, Integer> inactive) {
        super.set_inactive(inactive);
        FirebaseDatabase.getInstance().getReference("inactive").setValue(gson.toJson(inactive));
    }

    @Override
    public void set_chatcontent(BRelation<Pair<Integer, Integer>, Pair<Integer, Integer>> chatcontent) {
        super.set_chatcontent(chatcontent);
        FirebaseDatabase.getInstance().getReference("chatcontent").setValue(gson.toJson(chatcontent));
    }

    @Override
    public void set_chat(BRelation<Integer, Integer> chat) {
        super.set_chat(chat);
        FirebaseDatabase.getInstance().getReference("chat").setValue(gson.toJson(chat));
    }

    @Override
    public void set_nextindex(Integer nextindex) {
        super.set_nextindex(nextindex);
        FirebaseDatabase.getInstance().getReference("nextindex").setValue(nextindex);
    }

    @Override
    public void set_nextUserIndex(Integer nextUserIndex) {
        super.set_nextUserIndex(nextUserIndex);
        FirebaseDatabase.getInstance().getReference("nextUserIndex").setValue(nextUserIndex);
    }

    @Override
    public void set_active(BRelation<Integer, Integer> active) {
        super.set_active(active);
        FirebaseDatabase.getInstance().getReference("active").setValue(gson.toJson(active));
    }

    @Override
    public void set_muted(BRelation<Integer, Integer> muted) {
        super.set_muted(muted);
        FirebaseDatabase.getInstance().getReference("muted").setValue(gson.toJson(muted));
    }

    @Override
    public void set_user(BSet<Integer> user) {
        super.set_user(user);
        FirebaseDatabase.getInstance().getReference("user").setValue(gson.toJson(user));
    }

    @Override
    public void set_content(BSet<Integer> content) {
        super.set_content(content);
        FirebaseDatabase.getInstance().getReference("content").setValue(gson.toJson(content));
    }

    @Override
    public void set_contenttoread(BRelation<Pair<Integer, Integer>, Integer> contenttoread) {
        super.set_contenttoread(contenttoread);
        FirebaseDatabase.getInstance().getReference("contenttoread").setValue(gson.toJson(contenttoread));
    }

    public broadcast get_broadcast() {
        return evt_broadcast;
    }

    public unselect_chat get_unselect_chat() {
        return evt_unselect_chat;
    }

    public forward get_forward() {
        return evt_forward;
    }

    public delete_content get_delete_content() {
        return evt_delete_content;
    }

    public create_chat_session get_create_chat_session() {
        return evt_create_chat_session;
    }

    public reading_chat get_reading_chat() {
        return evt_reading_chat;
    }

    public delete_chat_session get_delete_chat_session() {
        return evt_delete_chat_session;
    }

    public unmute_chat get_unmute_chat() {
        return evt_unmute_chat;
    }

    public select_chat get_select_chat() {
        return evt_select_chat;
    }

    public mute_chat get_mute_chat() {
        return evt_mute_chat;
    }

    public chatting get_chatting() {
        return evt_chatting;
    }

    public remove_content get_remove_content() {
        return evt_remove_content;
    }

    public add_user get_add_user() {
        return evt_add_user;
    }
}
