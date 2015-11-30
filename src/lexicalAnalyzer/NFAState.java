package lexicalAnalyzer;

import java.util.ArrayList;
import java.util.HashMap;

public class NFAState {
	private HashMap<Character, ArrayList<NFAState>> next;

	public void addNext(NFAState nfaState, Character nextChar) {
		ArrayList<NFAState> states;
		if (next.containsKey(nextChar)) {
			states = next.get(nextChar);
		} else {
			states = new ArrayList<NFAState>();
			next.put(nextChar, states);
		}
		states.add(nfaState);
	}

}
