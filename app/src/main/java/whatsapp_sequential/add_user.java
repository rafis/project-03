package whatsapp_sequential; 

import eventb_prelude.*;
import Util.Utilities;

public class add_user{
	/*@ spec_public */ private machine3 machine; // reference to the machine 

	/*@ public normal_behavior
		requires true;
		assignable \everything;
		ensures this.machine == m; */
	public add_user(machine3 m) {
		this.machine = m;
	}

	/*@ public normal_behavior
		requires true;
 		assignable \nothing;
		ensures \result <==> machine.USER.difference(machine.get_user()).has(u); */
	public /*@ pure */ boolean guard_add_user( Integer u) {
		return machine.USER.difference(machine.get_user()).has(u);
	}

	/*@ public normal_behavior
		requires guard_add_user(u);
		assignable machine.user, machine.nextUserIndex;
		ensures guard_add_user(u) &&  machine.get_user().equals(\old((machine.get_user().union(new BSet<Integer>(u))))) &&  machine.get_nextUserIndex() == \old(new Integer(machine.get_nextUserIndex() + 1)); 
	 also
		requires !guard_add_user(u);
		assignable \nothing;
		ensures true; */
	public void run_add_user( Integer u){
		if(guard_add_user(u)) {
			BSet<Integer> user_tmp = machine.get_user();
			Integer nextUserIndex_tmp = machine.get_nextUserIndex();

			machine.set_user((user_tmp.union(new BSet<Integer>(u))));
			machine.set_nextUserIndex(new Integer(nextUserIndex_tmp + 1));

			System.out.println("add_user executed u: " + u + " ");
		}
	}

}
