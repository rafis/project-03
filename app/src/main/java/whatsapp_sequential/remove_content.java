package whatsapp_sequential; 

import eventb_prelude.*;
import Util.Utilities;

public class remove_content{
	/*@ spec_public */ private machine3 machine; // reference to the machine 

	/*@ public normal_behavior
		requires true;
		assignable \everything;
		ensures this.machine == m; */
	public remove_content(machine3 m) {
		this.machine = m;
	}

	/*@ public normal_behavior
		requires true;
 		assignable \nothing;
		ensures \result <==> (machine.get_user().has(u1) && machine.get_user().has(u2) && machine.get_chat().has(new Pair<Integer,Integer>(u1,u2)) && machine.get_content().has(c) && machine.get_chatcontent().domain().has(new Pair<Integer,Integer>(c,u1)) && machine.get_chatcontent().restrictDomainTo(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u1))).range().has(new Pair<Integer,Integer>(u1,u2))); */
	public /*@ pure */ boolean guard_remove_content( Integer c, Integer u1, Integer u2) {
		return (machine.get_user().has(u1) && machine.get_user().has(u2) && machine.get_chat().has(new Pair<Integer,Integer>(u1,u2)) && machine.get_content().has(c) && machine.get_chatcontent().domain().has(new Pair<Integer,Integer>(c,u1)) && machine.get_chatcontent().restrictDomainTo(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u1))).range().has(new Pair<Integer,Integer>(u1,u2)));
	}

	/*@ public normal_behavior
		requires guard_remove_content(c,u1,u2);
		assignable machine.chatcontent, machine.content, machine.owner, machine.contentorder, machine.contenttoread;
		ensures guard_remove_content(c,u1,u2) &&  machine.get_chatcontent().equals(\old((machine.get_chatcontent().restrictDomainTo(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u1))).rangeSubtraction(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))).union(machine.get_chatcontent().restrictDomainTo(machine.get_chatcontent().domain().difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u1)))))))) &&  machine.get_content().equals(\old((machine.get_chatcontent().restrictDomainTo(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u1))).rangeSubtraction(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))).union(machine.get_chatcontent().domainSubtraction(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u1))))).domain().domain())) &&  machine.get_owner().equals(\old((machine.get_chatcontent().restrictDomainTo(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u1))).rangeSubtraction(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))).union(machine.get_chatcontent().restrictDomainTo(machine.get_chatcontent().domain().difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u1)))))).domain())) &&  machine.get_contentorder().equals(\old(machine.get_contentorder().restrictDomainTo((machine.get_chatcontent().restrictDomainTo(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u1))).rangeSubtraction(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))).union(machine.get_chatcontent().restrictDomainTo(machine.get_chatcontent().domain().difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u1)))))).domain().domain()))) &&  machine.get_contenttoread().equals(\old(machine.get_contenttoread().restrictDomainTo(machine.get_chatcontent().restrictDomainTo(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u1))).rangeSubtraction(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))).domain()))); 
	 also
		requires !guard_remove_content(c,u1,u2);
		assignable \nothing;
		ensures true; */
	public void run_remove_content( Integer c, Integer u1, Integer u2){
		if(guard_remove_content(c,u1,u2)) {
			BRelation<Pair<Integer,Integer>,Pair<Integer,Integer>> chatcontent_tmp = machine.get_chatcontent();
			BSet<Integer> content_tmp = machine.get_content();
			BRelation<Integer,Integer> owner_tmp = machine.get_owner();
			BRelation<Integer,Integer> contentorder_tmp = machine.get_contentorder();
			BRelation<Pair<Integer,Integer>,Integer> contenttoread_tmp = machine.get_contenttoread();

			machine.set_chatcontent((chatcontent_tmp.restrictDomainTo(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u1))).rangeSubtraction(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))).union(chatcontent_tmp.restrictDomainTo(chatcontent_tmp.domain().difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u1)))))));
			machine.set_content((chatcontent_tmp.restrictDomainTo(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u1))).rangeSubtraction(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))).union(chatcontent_tmp.domainSubtraction(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u1))))).domain().domain());
			machine.set_owner((chatcontent_tmp.restrictDomainTo(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u1))).rangeSubtraction(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))).union(chatcontent_tmp.restrictDomainTo(chatcontent_tmp.domain().difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u1)))))).domain());
			machine.set_contentorder(contentorder_tmp.restrictDomainTo((chatcontent_tmp.restrictDomainTo(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u1))).rangeSubtraction(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))).union(chatcontent_tmp.restrictDomainTo(chatcontent_tmp.domain().difference(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u1)))))).domain().domain()));
			machine.set_contenttoread(contenttoread_tmp.restrictDomainTo(chatcontent_tmp.restrictDomainTo(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u1))).rangeSubtraction(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(u1,u2))).domain()));

			System.out.println("remove_content executed c: " + c + " u1: " + u1 + " u2: " + u2 + " ");
		}
	}

}
