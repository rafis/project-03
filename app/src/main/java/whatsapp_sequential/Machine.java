package whatsapp_sequential;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import edu.models.whatsapp2.events.ChatsEvent;
import edu.models.whatsapp2.events.MessagesEvent;
import edu.models.whatsapp2.events.UsersEvent;
import eventb_prelude.BRelation;
import eventb_prelude.BSet;
import eventb_prelude.Pair;

public class Machine extends machine3 {
    private static final String TAG = Machine.class.getSimpleName();

    Gson gson = new GsonBuilder().create();
    private Map<Integer, String> users_names;
    private Map<Integer, String> messages;

    public Machine() {
        super();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.addValueEventListener(valueEventListener);
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d("chatchange1", dataSnapshot.toString());
            if (dataSnapshot.getKey() == null) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    onDataChange(snapshot);
                return;
            }
            try {
                switch (dataSnapshot.getKey()) {
                    case "contentorder":
                        Machine.super.set_contentorder((gson.fromJson(dataSnapshot.getValue(String.class),
                                new TypeToken<BRelation<Integer, Integer>>() {
                                }.getType())));
                        break;
                    case "owner":
                        Machine.super.set_owner(gson.fromJson(dataSnapshot.getValue(String.class),
                                new TypeToken<BRelation<Integer, Integer>>() {
                                }.getType()));
                        break;
                    case "toread":
                        Machine.super.set_toread(gson.fromJson(dataSnapshot.getValue(String.class),
                                new TypeToken<BRelation<Integer, Integer>>() {
                                }.getType()));
                        EventBus.getDefault().post(new ChatsEvent());
                        break;
                    case "inactive":
                        Machine.super.set_inactive(gson.fromJson(dataSnapshot.getValue(String.class),
                                new TypeToken<BRelation<Integer, Integer>>() {
                                }.getType()));
                        EventBus.getDefault().post(new ChatsEvent());
                        break;
                    case "chatcontent":
                        Log.d("event32", dataSnapshot.toString());
                        Machine.super.set_chatcontent(gson.fromJson(dataSnapshot.getValue(String.class),
                                new TypeToken<BRelation<Pair<Integer, Integer>, Pair<Integer, Integer>>>() {
                                }.getType()));
                        EventBus.getDefault().postSticky(new MessagesEvent());
                        break;
                    case "chat":
                        Machine.super.set_chat(gson.fromJson(dataSnapshot.getValue(String.class),
                                new TypeToken<BRelation<Integer, Integer>>() {
                                }.getType()));
                        EventBus.getDefault().post(new ChatsEvent());
                        break;
                    case "nextindex":
                        Machine.super.set_nextindex(dataSnapshot.getValue(Integer.class));
                        break;
                    case "nextUserIndex":
                        Machine.super.set_nextUserIndex(dataSnapshot.getValue(Integer.class));
                        break;
                    case "user":
                        Machine.super.set_user(gson.fromJson(dataSnapshot.getValue(String.class),
                                new TypeToken<BSet<Integer>>() {
                                }.getType()));
                        EventBus.getDefault().post(new UsersEvent());
                        break;
                    case "active":
                        Machine.super.set_active(gson.fromJson(dataSnapshot.getValue(String.class),
                                new TypeToken<BRelation<Integer, Integer>>() {
                                }.getType()));
                        EventBus.getDefault().post(new ChatsEvent());
                        break;
                    case "muted":
                        Machine.super.set_muted(gson.fromJson(dataSnapshot.getValue(String.class),
                                new TypeToken<BRelation<Integer, Integer>>() {
                                }.getType()));
                        break;
                    case "content":
                        Machine.super.set_content(gson.fromJson(dataSnapshot.getValue(String.class),
                                new TypeToken<BSet<Integer>>() {
                                }.getType()));
                        break;
                    case "users_names":
                        Map<Integer, String> users_names = new HashMap<>();
                        for (DataSnapshot child : dataSnapshot.getChildren())
                            users_names.put(Integer.parseInt(child.getKey()), child.getValue(String.class));
                        Machine.this.set_users_names(users_names);
                        EventBus.getDefault().post(new UsersEvent());
                        break;
                    case "messages":
                        Map<Integer, String> messages = new HashMap<>();
                        for (DataSnapshot child : dataSnapshot.getChildren())
                            messages.put(Integer.parseInt(child.getKey()), child.getValue(String.class));
                        Machine.this.set_messages(messages);
                        EventBus.getDefault().post(new MessagesEvent());
                        break;
                }
            } catch (Exception ignored) {
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.d(TAG, "databaseError:" + databaseError.getMessage());
        }
    };

    public Map<Integer, String> get_users_names() {
        return this.users_names;
    }

    public void set_users_names(Map<Integer, String> users_names) {
        this.users_names = users_names;
    }

    public Map<Integer, String> get_messages() {
        return messages;
    }

    public void set_messages(Map<Integer, String> messages) {
        this.messages = messages;
    }

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
        Log.d("sendingmessage", "Json: " + gson.toJson(chatcontent));
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
