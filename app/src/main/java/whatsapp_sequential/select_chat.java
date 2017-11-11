package whatsapp_sequential; 

import eventb_prelude.*;
import Util.Utilities;

public class select_chat{
	/*@ spec_public */ private machine3 machine; // reference to the machine 

	/*@ public normal_behavior
		requires true;
		assignable \everything;
		ensures this.machine == m; */
	public select_chat(machine3 m) {
		this.machine = m;
	}

	/*@ public normal_behavior
		requires true;
 		assignable \nothing;
		ensures \result <==> (machine.get_user().has(u1) && machine.get_user().has(u2) && machine.get_chat().has(new Pair<Integer,Integer>(u1,u2)) && !machine.get_active().has(new Pair<Integer,Integer>(u1,u2)) && !machine.get_muted().has(new Pair<Integer,Integer>(u1,u2))); */
	public /*@ pure */ boolean guard_select_chat( Integer u1, Integer u2) {
		return (machine.get_user().has(u1) && machine.get_user().has(u2) && machine.get_chat().has(new Pair<Integer,Integer>(u1,u2)) && !machine.get_active().has(new Pair<Integer,Integer>(u1,u2)) && !machine.get_muted().has(new Pair<Integer,Integer>(u1,u2)));
	}

	/*@ public normal_behavior
		requires guard_select_chat(u1,u2);
		assignable machine.active, machine.toread, machine.inactive;
		ensures guard_select_chat(u1,u2) &&  machine.get_active().equals(\old((machine.get_active().override(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2)))))) &&  machine.get_toread().equals(\old(machine.get_toread().difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))))) &&  machine.get_inactive().equals(\old((machine.get_inactive().difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))).union(machine.get_active().restrictDomainTo(new BSet<Integer>(u1)))))); 
	 also
		requires !guard_select_chat(u1,u2);
		assignable \nothing;
		ensures true; */
	public void run_select_chat( Integer u1, Integer u2){
		if(guard_select_chat(u1,u2)) {
			BRelation<Integer,Integer> active_tmp = machine.get_active();
			BRelation<Integer,Integer> toread_tmp = machine.get_toread();
			BRelation<Integer,Integer> inactive_tmp = machine.get_inactive();

			machine.set_active((active_tmp.override(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2)))));
			machine.set_toread(toread_tmp.difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))));
			machine.set_inactive((inactive_tmp.difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))).union(active_tmp.restrictDomainTo(new BSet<Integer>(u1)))));

			System.out.println("select_chat executed u1: " + u1 + " u2: " + u2 + " ");
		}
	}

}
