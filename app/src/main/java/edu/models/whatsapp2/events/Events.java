package edu.models.whatsapp2.events;

/**
 * Created by me on 13.11.17.
 */
import org.greenrobot.eventbus.*;

public class Events {
    public static class ChatCreated{
        String username;
        String password;

        public ChatCreated(String username){
            //do something within your constructor
            this.username = username;
        }
        //Add your getters and setters
    }
    public static class MessageReceived{
        public String message;

        public MessageReceived(String message){
            //do something within your constructor
            this.message = message;
        }
    }
}