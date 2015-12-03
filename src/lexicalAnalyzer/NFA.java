package lexicalAnalyzer;

import java.util.ArrayList;
import java.util.HashMap;

public class NFA implements Cloneable{
	private NFAState inputState, outputState;
	private static ArrayList<NFAState> visitedStates; //for deep copying an NFA
	private static HashMap<NFAState, NFAState> originalCloneMapping;
	
	public NFA(NFAState inputState, NFAState outputState){
		this.inputState = inputState;
		this.outputState = outputState;
	}
	
	@Override
    public Object clone() throws CloneNotSupportedException {
		visitedStates = new ArrayList<>();
		originalCloneMapping = new HashMap<>();
		return deepCopy();
    }
	
	// helper for deep cloning an NFA
	private NFA deepCopy(){
		visitedStates.add(this.inputState);
		NFAState clonedNFAState = new NFAState();
		originalCloneMapping.put(this.inputState, clonedNFAState);
		HashMap<Character, ArrayList<NFAState>> tempNext = new HashMap<>();
		clonedNFAState.next = tempNext;
	    for(Character c : this.inputState.next.keySet()){
	    	ArrayList<NFAState> tempNextHash = new ArrayList<>();
	    	for(NFAState nfaState : this.inputState.next.get(c)){
	    		if(visitedStates.contains(nfaState)){
	    			tempNextHash.add(originalCloneMapping.get(nfaState));
	    		} else { //recurse more
	    			NFA subNFA = ((NFA)(new NFA(nfaState, this.outputState)).deepCopy());
	    			tempNextHash.add((NFAState)subNFA.inputState);
	    		}
	    	}
	    	tempNext.put(c, tempNextHash);
	    }
	    if(this.inputState.isLast()){
	    	clonedNFAState.setLast(true);
	    }
	    return new NFA(clonedNFAState, originalCloneMapping.get(this.outputState));
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
