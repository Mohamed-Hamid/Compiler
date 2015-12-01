package lexicalAnalyzer;

import java.util.ArrayList;
import java.util.HashMap;

public class NFAState {
	public static final Character EPSILON = '\0';
	private boolean last;
	private HashMap<Character, ArrayList<NFAState>> next;
	
	public NFAState(){
		next = new HashMap<>();
		last = false;
	}

	/* Adds an edge on input nextChar to state nfaState */
	public void addTransition(NFAState nfaState, Character nextChar) {
		ArrayList<NFAState> states;
		if (next.containsKey(nextChar)) {
			states = next.get(nextChar);
		} else {
			states = new ArrayList<NFAState>();
			next.put(nextChar, states);
		}
		states.add(nfaState);
	}
	
	/* Adds an edge on empty char to state nfaState */
	public void addEpsilonTranisition(NFAState nfaState) {
		this.addTransition(nfaState, NFAState.EPSILON);
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

}
