package whatsapp_sequential; 

import eventb_prelude.*;
import Util.Utilities;

public class reading_chat{
	/*@ spec_public */ private machine3 machine; // reference to the machine 

	/*@ public normal_behavior
		requires true;
		assignable \everything;
		ensures this.machine == m; */
	public reading_chat(machine3 m) {
		this.machine = m;
	}

	/*@ public normal_behavior
		requires true;
 		assignable \nothing;
		ensures \result <==> (machine.get_user().has(u1) && machine.get_user().has(u2) && machine.get_chat().has(new Pair<Integer,Integer>(u1,u2))); */
	public /*@ pure */ boolean guard_reading_chat( Integer u1, Integer u2) {
		return (machine.get_user().has(u1) && machine.get_user().has(u2) && machine.get_chat().has(new Pair<Integer,Integer>(u1,u2)));
	}

	/*@ public normal_behavior
		requires guard_reading_chat(u1,u2);
		assignable machine.contenttoread;
		ensures guard_reading_chat(u1,u2) &&  machine.get_contenttoread().equals(\old(new Best<Integer>(new JMLObjectSet {Integer cInteger u | (\exists Integer e; (machine.get_chatcontent().restrictRangeTo(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))).domain().has(new Pair<ERROR,ERROR>(null,u))); e.equals(new Pair<Pair<Integer,ERROR>,ERROR>(new Pair<ERROR,ERROR>(null,u),machine.get_contentorder().apply(null))))}))); 
	 also
		requires !guard_reading_chat(u1,u2);
		assignable \nothing;
		ensures true; */
	public void run_reading_chat( Integer u1, Integer u2){
		if(guard_reading_chat(u1,u2)) {
			BRelation<Pair<Integer,Integer>,Integer> contenttoread_tmp = machine.get_contenttoread();

			machine.set_contenttoread(null); // Set Comprehension: feature not supported by EventB2Java

			System.out.println("reading_chat executed u1: " + u1 + " u2: " + u2 + " ");
		}
	}

}
