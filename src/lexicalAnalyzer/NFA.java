package lexicalAnalyzer;

import java.util.ArrayList;
import java.util.HashMap;

public class NFA implements Cloneable{
	private NFAState inputState, outputState;
	private static ArrayList<NFAState> visitedStates; //for deep copying an NFA
	
	public NFA(NFAState inputState, NFAState outputState){
		this.inputState = inputState;
		this.outputState = outputState;
	}
	
	@Override
    public Object clone() throws CloneNotSupportedException {
		visitedStates = new ArrayList<>();
		return deepCopy();
    }
	
	// helper for deep cloning an NFA
	private NFA deepCopy(){
		visitedStates.add(this.inputState);
		NFAState clonedNFAState = new NFAState();
		HashMap<Character, ArrayList<NFAState>> tempNext = new HashMap<>();
		clonedNFAState.next = tempNext;
	    for(Character c : this.inputState.next.keySet()){
	    	ArrayList<NFAState> tempNextHash = new ArrayList<>();
	    	for(NFAState nfaState : this.inputState.next.get(c)){
	    		if(visitedStates.contains(nfaState)) continue;
	    		tempNextHash.add((NFAState)((NFA)(new NFA(nfaState, this.outputState)).deepCopy()).inputState);
	    	}
	    	tempNext.put(c, tempNextHash);
	    }
	    if(this.inputState.isLast()){
	    	clonedNFAState.setLast(true);
	    }
	    return new NFA(clonedNFAState, this.outputState);
	}
	
	/** Getters and Setters **/
	
	public NFAState getInputState() {
		return inputState;
	}

	public void setInputState(NFAState inputState) {
		this.inputState = inputState;
	}

	public NFAState getOutputState() {
		return outputState;
	}

	public void setOutputState(NFAState outputState) {
		this.outputState = outputState;
	}

}
