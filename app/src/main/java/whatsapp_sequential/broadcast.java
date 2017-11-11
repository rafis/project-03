package whatsapp_sequential; 

import eventb_prelude.*;
import Util.Utilities;

public class broadcast{
	/*@ spec_public */ private machine3 machine; // reference to the machine 

	/*@ public normal_behavior
		requires true;
		assignable \everything;
		ensures this.machine == m; */
	public broadcast(machine3 m) {
		this.machine = m;
	}

	/*@ public normal_behavior
		requires true;
 		assignable \nothing;
		ensures \result <==> (machine.get_user().has(u) && machine.CONTENT.difference(machine.get_content()).has(c) && ul.isSubset(machine.get_user()) && (BRelation.cross(new BSet<Integer>(u),ul).intersection(machine.get_muted())).equals(BSet.EMPTY) && (BRelation.cross(ul,new BSet<Integer>(u)).intersection(machine.get_muted())).equals(BSet.EMPTY) && !(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u)).intersection(machine.get_chatcontent().domain())).equals(BSet.EMPTY) && machine.get_chatcontent().range().domain().has(u) && !(BRelation.cross(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u)),BRelation.cross(new BSet<Integer>(u),machine.get_user())).intersection(machine.get_chatcontent())).equals(BSet.EMPTY)); */
	public /*@ pure */ boolean guard_broadcast( Integer c, Integer u, BSet<Integer> ul) {
		return (machine.get_user().has(u) && machine.CONTENT.difference(machine.get_content()).has(c) && ul.isSubset(machine.get_user()) && (BRelation.cross(new BSet<Integer>(u),ul).intersection(machine.get_muted())).equals(BSet.EMPTY) && (BRelation.cross(ul,new BSet<Integer>(u)).intersection(machine.get_muted())).equals(BSet.EMPTY) && !(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u)).intersection(machine.get_chatcontent().domain())).equals(BSet.EMPTY) && machine.get_chatcontent().range().domain().has(u) && !(BRelation.cross(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u)),BRelation.cross(new BSet<Integer>(u),machine.get_user())).intersection(machine.get_chatcontent())).equals(BSet.EMPTY));
	}

	/*@ public normal_behavior
		requires guard_broadcast(c,u,ul);
		assignable machine.chat, machine.chatcontent, machine.toread, machine.inactive, machine.owner, machine.nextindex, machine.contentorder;
		ensures guard_broadcast(c,u,ul) &&  machine.get_chat().equals(\old((machine.get_chat().union(BRelation.cross(ul,new BSet<Integer>(u)).union(BRelation.cross(new BSet<Integer>(u),ul)))))) &&  machine.get_chatcontent().equals(\old((machine.get_chatcontent().union(BRelation.cross(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u)),(BRelation.cross(new BSet<Integer>(u),ul).union(BRelation.cross(ul,new BSet<Integer>(u))))))))) &&  machine.get_toread().equals(\old((machine.get_toread().union(BRelation.cross(new BSet<Integer>(u),ul).union(BRelation.cross(ul,new BSet<Integer>(u))))).difference(machine.get_active()))) &&  machine.get_inactive().equals(\old((machine.get_inactive().union(BRelation.cross(new BSet<Integer>(u),ul).union(BRelation.cross(ul,new BSet<Integer>(u))))).difference(machine.get_active()))) &&  machine.get_owner().equals(\old((machine.get_owner().union(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u)))))) &&  machine.get_nextindex() == \old(new Integer(machine.get_nextindex() + 1)) &&  machine.get_contentorder().equals(\old((machine.get_contentorder().union(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,machine.get_nextindex())))))); 
	 also
		requires !guard_broadcast(c,u,ul);
		assignable \nothing;
		ensures true; */
	public void run_broadcast( Integer c, Integer u, BSet<Integer> ul){
		if(guard_broadcast(c,u,ul)) {
			BRelation<Integer,Integer> chat_tmp = machine.get_chat();
			BRelation<Pair<Integer,Integer>,Pair<Integer,Integer>> chatcontent_tmp = machine.get_chatcontent();
			BRelation<Integer,Integer> toread_tmp = machine.get_toread();
			BRelation<Integer,Integer> inactive_tmp = machine.get_inactive();
			BRelation<Integer,Integer> owner_tmp = machine.get_owner();
			Integer nextindex_tmp = machine.get_nextindex();
			BRelation<Integer,Integer> contentorder_tmp = machine.get_contentorder();

			machine.set_chat((chat_tmp.union(BRelation.cross(ul,new BSet<Integer>(u)).union(BRelation.cross(new BSet<Integer>(u),ul)))));
			machine.set_chatcontent((chatcontent_tmp.union(BRelation.cross(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u)),(BRelation.cross(new BSet<Integer>(u),ul).union(BRelation.cross(ul,new BSet<Integer>(u))))))));
			machine.set_toread((toread_tmp.union(BRelation.cross(new BSet<Integer>(u),ul).union(BRelation.cross(ul,new BSet<Integer>(u))))).difference(machine.get_active()));
			machine.set_inactive((inactive_tmp.union(BRelation.cross(new BSet<Integer>(u),ul).union(BRelation.cross(ul,new BSet<Integer>(u))))).difference(machine.get_active()));
			machine.set_owner((owner_tmp.union(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u)))));
			machine.set_nextindex(new Integer(nextindex_tmp + 1));
			machine.set_contentorder((contentorder_tmp.union(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,nextindex_tmp)))));

			System.out.println("broadcast executed c: " + c + " u: " + u + " ul: " + ul + " ");
		}
	}

}
