package whatsapp_sequential; 

import eventb_prelude.*;
import Util.Utilities;

public class mute_chat{
	/*@ spec_public */ private machine3 machine; // reference to the machine 

	/*@ public normal_behavior
		requires true;
		assignable \everything;
		ensures this.machine == m; */
	public mute_chat(machine3 m) {
		this.machine = m;
	}

	/*@ public normal_behavior
		requires true;
 		assignable \nothing;
		ensures \result <==> (!machine.get_muted().has(new Pair<Integer,Integer>(u1,u2)) && machine.get_user().has(u1) && machine.get_user().has(u2) && machine.get_chat().has(new Pair<Integer,Integer>(u1,u2))); */
	public /*@ pure */ boolean guard_mute_chat( Integer u1, Integer u2) {
		return (!machine.get_muted().has(new Pair<Integer,Integer>(u1,u2)) && machine.get_user().has(u1) && machine.get_user().has(u2) && machine.get_chat().has(new Pair<Integer,Integer>(u1,u2)));
	}

	/*@ public normal_behavior
		requires guard_mute_chat(u1,u2);
		assignable machine.muted, machine.active, machine.inactive;
		ensures guard_mute_chat(u1,u2) &&  machine.get_muted().equals(\old((machine.get_muted().union(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2)))))) &&  machine.get_active().equals(\old(machine.get_active().difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))))) &&  machine.get_inactive().equals(\old((machine.get_inactive().union(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2)))))); 
	 also
		requires !guard_mute_chat(u1,u2);
		assignable \nothing;
		ensures true; */
	public void run_mute_chat( Integer u1, Integer u2){
		if(guard_mute_chat(u1,u2)) {
			BRelation<Integer,Integer> muted_tmp = machine.get_muted();
			BRelation<Integer,Integer> active_tmp = machine.get_active();
			BRelation<Integer,Integer> inactive_tmp = machine.get_inactive();

			machine.set_muted((muted_tmp.union(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2)))));
			machine.set_active(active_tmp.difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))));
			machine.set_inactive((inactive_tmp.union(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2)))));

			System.out.println("mute_chat executed u1: " + u1 + " u2: " + u2 + " ");
		}
	}

}
