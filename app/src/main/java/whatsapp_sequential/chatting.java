package whatsapp_sequential; 

import eventb_prelude.*;
import Util.Utilities;

public class chatting{
	/*@ spec_public */ private machine3 machine; // reference to the machine 

	/*@ public normal_behavior
		requires true;
		assignable \everything;
		ensures this.machine == m; */
	public chatting(machine3 m) {
		this.machine = m;
	}

	/*@ public normal_behavior
		requires true;
 		assignable \nothing;
		ensures \result <==> (machine.get_user().has(u1) && machine.get_user().has(u2) && machine.get_chat().has(new Pair<Integer,Integer>(u1,u2)) && !machine.get_muted().has(new Pair<Integer,Integer>(u1,u2)) && !machine.get_muted().has(new Pair<Integer,Integer>(u2,u1)) && machine.get_active().has(new Pair<Integer,Integer>(u1,u2)) && machine.CONTENT.difference(machine.get_content()).has(c)); */
	public /*@ pure */ boolean guard_chatting( Integer c, Integer u1, Integer u2) {
		return (machine.get_user().has(u1) && machine.get_user().has(u2) && machine.get_chat().has(new Pair<Integer,Integer>(u1,u2)) && !machine.get_muted().has(new Pair<Integer,Integer>(u1,u2)) && !machine.get_muted().has(new Pair<Integer,Integer>(u2,u1)) && machine.get_active().has(new Pair<Integer,Integer>(u1,u2)) && machine.CONTENT.difference(machine.get_content()).has(c));
	}

	/*@ public normal_behavior
		requires guard_chatting(c,u1,u2);
		assignable machine.content, machine.chat, machine.chatcontent, machine.toread, machine.owner, machine.nextindex, machine.contentorder;
		ensures guard_chatting(c,u1,u2) &&  machine.get_content().equals(\old((machine.get_content().union(new BSet<Integer>(c))))) &&  machine.get_chat().equals(\old((machine.get_chat().union(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u2,u1)))))) &&  machine.get_chatcontent().equals(\old((machine.get_chatcontent().union(new BRelation<Pair<Integer,Integer>,Pair<Integer,Integer>>(new Pair<ERROR,Pair<Integer,Integer>>(new Pair<Integer,Integer>(c,u1),new Pair<Integer,Integer>(u1,u2))).union(new BRelation<Pair<Integer,Integer>,Pair<Integer,Integer>>(new Pair<ERROR,Pair<Integer,Integer>>(new Pair<Integer,Integer>(c,u1),new Pair<Integer,Integer>(u2,u1)))))))) &&  machine.get_toread().equals(\old((machine.get_toread().union(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u2,u1)))).difference(machine.get_active()))) &&  machine.get_owner().equals(\old((machine.get_owner().union(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u1)))))) &&  machine.get_nextindex() == \old(new Integer(machine.get_nextindex() + 1)) &&  machine.get_contentorder().equals(\old((machine.get_contentorder().union(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,machine.get_nextindex())))))); 
	 also
		requires !guard_chatting(c,u1,u2);
		assignable \nothing;
		ensures true; */
	public void run_chatting( Integer c, Integer u1, Integer u2){
		if(guard_chatting(c,u1,u2)) {
			BSet<Integer> content_tmp = machine.get_content();
			BRelation<Integer,Integer> chat_tmp = machine.get_chat();
			BRelation<Pair<Integer,Integer>,Pair<Integer,Integer>> chatcontent_tmp = machine.get_chatcontent();
			BRelation<Integer,Integer> toread_tmp = machine.get_toread();
			BRelation<Integer,Integer> owner_tmp = machine.get_owner();
			Integer nextindex_tmp = machine.get_nextindex();
			BRelation<Integer,Integer> contentorder_tmp = machine.get_contentorder();

			machine.set_content((content_tmp.union(new BSet<Integer>(c))));
			machine.set_chat((chat_tmp.union(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u2,u1)))));
			machine.set_chatcontent((chatcontent_tmp.union(new BRelation<Pair<Integer,Integer>,Pair<Integer,Integer>>(new Pair<Pair<Integer,Integer>,Pair<Integer,Integer>>(new Pair<Integer,Integer>(c,u1),new Pair<Integer,Integer>(u1,u2))).union(new BRelation<Pair<Integer,Integer>,Pair<Integer,Integer>>(new Pair<Pair<Integer,Integer>,Pair<Integer,Integer>>(new Pair<Integer,Integer>(c,u1),new Pair<Integer,Integer>(u2,u1)))))));
			machine.set_toread((toread_tmp.union(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u2,u1)))).difference(machine.get_active()));
			machine.set_owner((owner_tmp.union(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u1)))));
			machine.set_nextindex(new Integer(nextindex_tmp + 1));
			machine.set_contentorder((contentorder_tmp.union(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,nextindex_tmp)))));

			System.out.println("chatting executed c: " + c + " u1: " + u1 + " u2: " + u2 + " ");
		}
	}

}
