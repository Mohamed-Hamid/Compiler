package lexicalAnalyzer;

import java.util.ArrayList;
import java.util.HashMap;

public class NFA implements Cloneable{
	private NFAState inputState, outputState;
	
	public NFA(NFAState inputState, NFAState outputState){
		this.inputState = inputState;
		this.outputState = outputState;
	}
	
	public NFA(NFA otherNFA) throws CloneNotSupportedException{
//	    this.inputState = new NFAState(otherNFA.inputState);
//	    this.outputState = new NFAState(otherNFA.outputState);
//		this.inputState = (NFAState) otherNFA.inputState.clone();
//		this.inputState = (NFAState) otherNFA.clone();
	}
	
	@Override
    public Object clone() throws CloneNotSupportedException {
		NFAState clonedNFAState = new NFAState();
		HashMap<Character, ArrayList<NFAState>> tempNext = new HashMap<>();
		clonedNFAState.next = tempNext;
	    for(Character c : this.inputState.next.keySet()){
	    	ArrayList<NFAState> tempNextHash = new ArrayList<>();
	    	for(NFAState nfaState : this.inputState.next.get(c)){
	    		tempNextHash.add((NFAState)((NFA)(new NFA(nfaState, this.outputState)).clone()).inputState);
	    	}
	    	tempNext.put(c, tempNextHash);
	    }
	    if(this.inputState.isLast()){
	    	clonedNFAState.setLast(true);
	    }
	    return new NFA(clonedNFAState, this.outputState);
    }

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
