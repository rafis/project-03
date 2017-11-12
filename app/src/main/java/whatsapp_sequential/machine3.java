package whatsapp_sequential;

import eventb_prelude.*;
import Util.*;
//@ model import org.jmlspecs.models.JMLObjectSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class machine3{
	private static final Integer max_integer = Utilities.max_integer;
	private static final Integer min_integer = Utilities.min_integer;

	broadcast evt_broadcast = new broadcast(this);
	unselect_chat evt_unselect_chat = new unselect_chat(this);
	forward evt_forward = new forward(this);
	delete_content evt_delete_content = new delete_content(this);
	create_chat_session evt_create_chat_session = new create_chat_session(this);
	reading_chat evt_reading_chat = new reading_chat(this);
	delete_chat_session evt_delete_chat_session = new delete_chat_session(this);
	unmute_chat evt_unmute_chat = new unmute_chat(this);
	select_chat evt_select_chat = new select_chat(this);
	mute_chat evt_mute_chat = new mute_chat(this);
	chatting evt_chatting = new chatting(this);
	remove_content evt_remove_content = new remove_content(this);
	add_user evt_add_user = new add_user(this);


	/******Set definitions******/
	//@ public static constraint CONTENT.equals(\old(CONTENT)); 
	public static final BSet<Integer> CONTENT = new Enumerated(min_integer,max_integer);

	//@ public static constraint USER.equals(\old(USER)); 
	public static final BSet<Integer> USER = new Enumerated(min_integer,max_integer);


	/******Constant definitions******/


	/******Axiom definitions******/


	/******Variable definitions******/
	/*@ spec_public */ private BRelation<Integer,Integer> active;

	/*@ spec_public */ private BRelation<Integer,Integer> chat;

	/*@ spec_public */ private BRelation<Pair<Integer,Integer>,Pair<Integer,Integer>> chatcontent;

	/*@ spec_public */ private BSet<Integer> content;

	/*@ spec_public */ private BRelation<Integer,Integer> contentorder;

	/*@ spec_public */ private BRelation<Pair<Integer,Integer>,Integer> contenttoread;

	/*@ spec_public */ private BRelation<Integer,Integer> inactive;

	/*@ spec_public */ private BRelation<Integer,Integer> muted;

	/*@ spec_public */ private Integer nextUserIndex;

	/*@ spec_public */ private Integer nextindex;

	/*@ spec_public */ private BRelation<Integer,Integer> owner;

	/*@ spec_public */ private BRelation<Integer,Integer> toread;

	/*@ spec_public */ private BSet<Integer> user;




	/******Invariant definition******/
	/*@ public invariant
		user.isSubset(USER) &&
		content.isSubset(CONTENT) &&
		 chat.domain().isSubset(user) && chat.range().isSubset(user) && BRelation.cross(user,user).has(chat) &&
		 active.domain().isSubset(user) && active.range().isSubset(user) && active.isaFunction() && BRelation.cross(user,user).has(active) &&
		 muted.domain().isSubset(user) && muted.range().isSubset(user) && BRelation.cross(user,user).has(muted) &&
		active.isSubset(chat) &&
		muted.isSubset(chat) &&
		((BRelation.cross(BRelation.cross(content,user),chat)).pow()).has(chatcontent) &&
		 (\forall Integer c;  (\forall Integer u1;  (\forall Integer u2;((content.has(c) && user.has(u1) && user.has(u2) && chatcontent.domain().restrictDomainTo(new BSet<Integer>(c)).range().has(u1) && chatcontent.domain().restrictDomainTo(new BSet<Integer>(c)).range().has(u2)) ==> (u1.equals(u2)))))) &&
		(muted.intersection(active)).equals(BSet.EMPTY) &&
		 (\forall Integer c;  (\forall Integer u;((chatcontent.domain().has(new Pair<Integer,Integer>(c,u))) ==> (!chatcontent.restrictDomainTo(new BRelation<Integer,Integer>(new Pair<Integer,Integer>(c,u))).equals(BSet.EMPTY))))) &&
		NAT1.instance.has(nextUserIndex) &&
		 toread.domain().isSubset(user) && toread.range().isSubset(user) && BRelation.cross(user,user).has(toread) &&
		toread.isSubset(chat) &&
		inactive.isSubset(chat) &&
		(active.union(toread.union(inactive))).equals(chat) &&
		(active.intersection(toread)).equals(BSet.EMPTY) &&
		(active.intersection(inactive)).equals(BSet.EMPTY) &&
		 owner.domain().equals(content) && owner.range().isSubset(user) && owner.isaFunction() && BRelation.cross(content,user).has(owner) &&
		owner.equals(chatcontent.domain()) &&
		NAT1.instance.has(nextindex) &&
		 contentorder.domain().equals(content) && contentorder.range().isSubset(NAT1.instance) && contentorder.isaFunction() && BRelation.cross(content,NAT1.instance).has(contentorder) &&
		chatcontent.domain().domain().equals(contentorder.domain()) &&
		((BRelation.cross(BRelation.cross(content,user),NAT1.instance)).pow()).has(contenttoread); */


	/******Getter and Mutator methods definition******/
	/*@ public normal_behavior
	    requires true;
	    assignable \nothing;
	    ensures \result == this.contentorder;*/
	public /*@ pure */ BRelation<Integer,Integer> get_contentorder(){
		return this.contentorder;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable this.contentorder;
	    ensures this.contentorder == contentorder;*/
	public void set_contentorder(BRelation<Integer,Integer> contentorder){
		this.contentorder = contentorder;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable \nothing;
	    ensures \result == this.nextUserIndex;*/
	public /*@ pure */ Integer get_nextUserIndex(){
		return this.nextUserIndex;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable this.nextUserIndex;
	    ensures this.nextUserIndex == nextUserIndex;*/
	public void set_nextUserIndex(Integer nextUserIndex){
		this.nextUserIndex = nextUserIndex;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable \nothing;
	    ensures \result == this.owner;*/
	public /*@ pure */ BRelation<Integer,Integer> get_owner(){
		return this.owner;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable this.owner;
	    ensures this.owner == owner;*/
	public void set_owner(BSet<Pair<Integer,Integer>> owner){
		set_owner(new BRelation<>(owner));
	}
	public void set_owner(BRelation<Integer,Integer> owner){
		this.owner = owner;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable \nothing;
	    ensures \result == this.nextindex;*/
	public /*@ pure */ Integer get_nextindex(){
		return this.nextindex;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable this.nextindex;
	    ensures this.nextindex == nextindex;*/
	public void set_nextindex(Integer nextindex){
		this.nextindex = nextindex;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable \nothing;
	    ensures \result == this.active;*/
	public /*@ pure */ BRelation<Integer,Integer> get_active(){
		return this.active;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable this.active;
	    ensures this.active == active;*/
	public void set_active(BRelation<Integer,Integer> active){
		this.active = active;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable \nothing;
	    ensures \result == this.content;*/
	public /*@ pure */ BSet<Integer> get_content(){
		return this.content;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable this.content;
	    ensures this.content == content;*/
	public void set_content(BSet<Integer> content){
		this.content = content;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable \nothing;
	    ensures \result == this.toread;*/
	public /*@ pure */ BRelation<Integer,Integer> get_toread(){
		return this.toread;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable this.toread;
	    ensures this.toread == toread;*/
	public void set_toread(BRelation<Integer,Integer> toread){
		this.toread = toread;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable \nothing;
	    ensures \result == this.inactive;*/
	public /*@ pure */ BRelation<Integer,Integer> get_inactive(){
		return this.inactive;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable this.inactive;
	    ensures this.inactive == inactive;*/
	public void set_inactive(BRelation<Integer,Integer> inactive){
		this.inactive = inactive;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable \nothing;
	    ensures \result == this.chatcontent;*/
	public /*@ pure */ BRelation<Pair<Integer,Integer>,Pair<Integer,Integer>> get_chatcontent(){
		return this.chatcontent;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable this.chatcontent;
	    ensures this.chatcontent == chatcontent;*/
	public void set_chatcontent(BRelation<Pair<Integer,Integer>,Pair<Integer,Integer>> chatcontent){
		this.chatcontent = chatcontent;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable \nothing;
	    ensures \result == this.chat;*/
	public /*@ pure */ BRelation<Integer,Integer> get_chat(){
		return this.chat;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable this.chat;
	    ensures this.chat == chat;*/
	public void set_chat(BRelation<Integer,Integer> chat){
		this.chat = chat;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable \nothing;
	    ensures \result == this.muted;*/
	public /*@ pure */ BRelation<Integer,Integer> get_muted(){
		return this.muted;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable this.muted;
	    ensures this.muted == muted;*/
	public void set_muted(BRelation<Integer,Integer> muted){
		this.muted = muted;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable \nothing;
	    ensures \result == this.user;*/
	public /*@ pure */ BSet<Integer> get_user(){
		return this.user;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable this.user;
	    ensures this.user == user;*/
	public void set_user(BSet<Integer> user){
		this.user = user;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable \nothing;
	    ensures \result == this.contenttoread;*/
	public /*@ pure */ BRelation<Pair<Integer,Integer>,Integer> get_contenttoread(){
		return this.contenttoread;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable this.contenttoread;
	    ensures this.contenttoread == contenttoread;*/
	public void set_contenttoread(BRelation<Pair<Integer,Integer>,Integer> contenttoread){
		this.contenttoread = contenttoread;
	}



	/*@ public normal_behavior
	    requires true;
	    assignable \everything;
	    ensures
		user.isEmpty() &&
		content.isEmpty() &&
		chat.isEmpty() &&
		active.isEmpty() &&
		muted.isEmpty() &&
		chatcontent.isEmpty() &&
		nextUserIndex == 1 &&
		toread.isEmpty() &&
		inactive.isEmpty() &&
		owner.isEmpty() &&
		nextindex == 1 &&
		contentorder.isEmpty() &&
		contenttoread.isEmpty();*/
	public machine3(){
		user = new BSet<Integer>();
		content = new BSet<Integer>();
		chat = new BRelation<Integer,Integer>();
		active = new BRelation<Integer,Integer>();
		muted = new BRelation<Integer,Integer>();
		chatcontent = new BRelation<Pair<Integer,Integer>,Pair<Integer,Integer>>();
		nextUserIndex = 1;
		toread = new BRelation<Integer,Integer>();
		inactive = new BRelation<Integer,Integer>();
		owner = new BRelation<Integer,Integer>();
		nextindex = 1;
		contentorder = new BRelation<Integer,Integer>();
		contenttoread = new BRelation<Pair<Integer,Integer>,Integer>();

	}
}