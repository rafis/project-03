package whatsapp_sequential; 

import eventb_prelude.*;
import Util.Utilities;

public class unmute_chat{
	/*@ spec_public */ private machine3 machine; // reference to the machine 

	/*@ public normal_behavior
		requires true;
		assignable \everything;
		ensures this.machine == m; */
	public unmute_chat(machine3 m) {
		this.machine = m;
	}

	/*@ public normal_behavior
		requires true;
 		assignable \nothing;
		ensures \result <==> (machine.get_muted().has(new Pair<Integer,Integer>(u1,u2)) && machine.get_user().has(u1) && machine.get_user().has(u2) && machine.get_chat().has(new Pair<Integer,Integer>(u1,u2))); */
	public /*@ pure */ boolean guard_unmute_chat( Integer u1, Integer u2) {
		return (machine.get_muted().has(new Pair<Integer,Integer>(u1,u2)) && machine.get_user().has(u1) && machine.get_user().has(u2) && machine.get_chat().has(new Pair<Integer,Integer>(u1,u2)));
	}

	/*@ public normal_behavior
		requires guard_unmute_chat(u1,u2);
		assignable machine.muted;
		ensures guard_unmute_chat(u1,u2) &&  machine.get_muted().equals(\old(machine.get_muted().difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))))); 
	 also
		requires !guard_unmute_chat(u1,u2);
		assignable \nothing;
		ensures true; */
	public void run_unmute_chat( Integer u1, Integer u2){
		if(guard_unmute_chat(u1,u2)) {
			BRelation<Integer,Integer> muted_tmp = machine.get_muted();

			machine.set_muted(muted_tmp.difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))));

			System.out.println("unmute_chat executed u1: " + u1 + " u2: " + u2 + " ");
		}
	}

}
