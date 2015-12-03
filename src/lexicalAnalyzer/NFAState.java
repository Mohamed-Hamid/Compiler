package lexicalAnalyzer;

import java.util.ArrayList;
import java.util.HashMap;

public class NFAState implements Cloneable{
	public static final Character EPSILON = null;
	private static int count = 1;
	public int num;
	private String acceptingString = "";
	private boolean last;
	public HashMap<Character, ArrayList<NFAState>> next;
	
	public NFAState(){
		next = new HashMap<>();
		num = count++;
		last = false;
	}
	
	//Build an NFA state from another
/*	public NFAState(NFAState otherNFAState){
		HashMap<Character, ArrayList<NFAState>> tempNext = new HashMap<>();
	    for(Character c : otherNFAState.next.keySet()){
	    	ArrayList<NFAState> tempNextHash = new ArrayList<>();
	    	for(NFAState nfaState : otherNFAState.next.get(c)){
	    		tempNextHash.add(new NFAState(nfaState));
	    	}
	    	tempNext.put(c, tempNextHash);
	    }
	    num = count++;
	    last = otherNFAState.isLast();
	}
	*/
//	@Override
//    public Object clone() throws CloneNotSupportedException {
//		NFAState clonedNFAState = new NFAState();
//		HashMap<Character, ArrayList<NFAState>> tempNext = new HashMap<>();
//		clonedNFAState.next = tempNext;
//	    for(Character c : this.next.keySet()){
//	    	ArrayList<NFAState> tempNextHash = new ArrayList<>();
//	    	for(NFAState nfaState : this.next.get(c)){
//	    		tempNextHash.add((NFAState) nfaState.clone());
//	    	}
//	    	tempNext.put(c, tempNextHash);
//	    }
////	    clonedNFAState.num = count++;
////	    clonedNFAState.last = this.isLast();
//	    if(last) System.out.println(clonedNFAState.num);
//	    return clonedNFAState;
//    }

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

	@Override 
	public String toString() {
		StringBuilder result = new StringBuilder();
		for(Character c : next.keySet()){
			for(NFAState nfaState : next.get(c)){
				result.append(this.num + " --" + c + "--> " + nfaState.num);
			}
		}
		return result.toString();
	  }

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public String getAcceptingString() {
		return acceptingString;
	}

	public void setAcceptingString(String acceptingString) {
		this.acceptingString = acceptingString;
	}

}
