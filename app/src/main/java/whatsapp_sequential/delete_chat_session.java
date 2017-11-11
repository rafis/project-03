package whatsapp_sequential; 

import eventb_prelude.*;
import Util.Utilities;

public class delete_chat_session{
	/*@ spec_public */ private machine3 machine; // reference to the machine 

	/*@ public normal_behavior
		requires true;
		assignable \everything;
		ensures this.machine == m; */
	public delete_chat_session(machine3 m) {
		this.machine = m;
	}

	/*@ public normal_behavior
		requires true;
 		assignable \nothing;
		ensures \result <==> (machine.get_user().has(u1) && machine.get_user().has(u2) && machine.get_chat().has(new Pair<Integer,Integer>(u1,u2))); */
	public /*@ pure */ boolean guard_delete_chat_session( Integer u1, Integer u2) {
		return (machine.get_user().has(u1) && machine.get_user().has(u2) && machine.get_chat().has(new Pair<Integer,Integer>(u1,u2)));
	}

	/*@ public normal_behavior
		requires guard_delete_chat_session(u1,u2);
		assignable machine.chat, machine.muted, machine.active, machine.chatcontent, machine.content, machine.inactive, machine.toread, machine.owner, machine.contentorder, machine.contenttoread;
		ensures guard_delete_chat_session(u1,u2) &&  machine.get_chat().equals(\old(machine.get_chat().difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))))) &&  machine.get_muted().equals(\old(machine.get_muted().difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))))) &&  machine.get_active().equals(\old(machine.get_active().difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))))) &&  machine.get_chatcontent().equals(\old(machine.get_chatcontent().rangeSubtraction(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))))) &&  machine.get_content().equals(\old(machine.get_chatcontent().rangeSubtraction(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))).domain().domain())) &&  machine.get_inactive().equals(\old(machine.get_inactive().difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))))) &&  machine.get_toread().equals(\old(machine.get_toread().difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))))) &&  machine.get_owner().equals(\old(machine.get_chatcontent().rangeSubtraction(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))).domain())) &&  machine.get_contentorder().equals(\old(machine.get_contentorder().restrictDomainTo(machine.get_chatcontent().rangeSubtraction(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))).domain().domain()))) &&  machine.get_contenttoread().equals(\old(machine.get_contenttoread().restrictDomainTo(machine.get_chatcontent().rangeSubtraction(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))).domain()))); 
	 also
		requires !guard_delete_chat_session(u1,u2);
		assignable \nothing;
		ensures true; */
	public void run_delete_chat_session( Integer u1, Integer u2){
		if(guard_delete_chat_session(u1,u2)) {
			BRelation<Integer,Integer> chat_tmp = machine.get_chat();
			BRelation<Integer,Integer> muted_tmp = machine.get_muted();
			BRelation<Integer,Integer> active_tmp = machine.get_active();
			BRelation<Pair<Integer,Integer>,Pair<Integer,Integer>> chatcontent_tmp = machine.get_chatcontent();
			BSet<Integer> content_tmp = machine.get_content();
			BRelation<Integer,Integer> inactive_tmp = machine.get_inactive();
			BRelation<Integer,Integer> toread_tmp = machine.get_toread();
			BRelation<Integer,Integer> owner_tmp = machine.get_owner();
			BRelation<Integer,Integer> contentorder_tmp = machine.get_contentorder();
			BRelation<Pair<Integer,Integer>,Integer> contenttoread_tmp = machine.get_contenttoread();

			machine.set_chat(chat_tmp.difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))));
			machine.set_muted(muted_tmp.difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))));
			machine.set_active(active_tmp.difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))));
			machine.set_chatcontent(chatcontent_tmp.rangeSubtraction(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))));
			machine.set_content(chatcontent_tmp.rangeSubtraction(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))).domain().domain());
			machine.set_inactive(inactive_tmp.difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))));
			machine.set_toread(toread_tmp.difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))));
			machine.set_owner(chatcontent_tmp.rangeSubtraction(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))).domain());
			machine.set_contentorder(contentorder_tmp.restrictDomainTo(chatcontent_tmp.rangeSubtraction(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))).domain().domain()));
			machine.set_contenttoread(contenttoread_tmp.restrictDomainTo(chatcontent_tmp.rangeSubtraction(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))).domain()));

			System.out.println("delete_chat_session executed u1: " + u1 + " u2: " + u2 + " ");
		}
	}

}
