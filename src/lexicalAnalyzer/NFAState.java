package lexicalAnalyzer;

import java.util.HashMap;
import java.util.HashSet;

public class NFAState {
	public static final Character EPSILON = null;
	private static int count = 1;
	private int num;
	private boolean last;
	public HashMap<Character, HashSet<NFAState>> next;
	
	public NFAState(){
		next = new HashMap<>();
		num = count++;
		last = false;
	}
	
	//Build an NFA state from another
	public NFAState(NFAState otherNFAState){
		next = new HashMap<>(otherNFAState.next);
		num = count++;
		last = otherNFAState.isLast();
	}

	/* Adds an edge on input nextChar to state nfaState */
	public void addTransition(NFAState nfaState, Character nextChar) {
		HashSet<NFAState> states;
		if (next.containsKey(nextChar)) {
			states = next.get(nextChar);
		} else {
			states = new HashSet<NFAState>();
			next.put(nextChar, states);
		}
		states.add(nfaState);
	}
	
	/* Adds an edge on empty char to state nfaState */
	public void addEpsilonTranisition(NFAState nfaState) {
		this.addTransition(nfaState, NFAState.EPSILON);
	}

	@Override 
	public String toString() {
		return num + "";
	  }

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

}
