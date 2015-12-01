package lexicalAnalyzer;

public class NFA {
	public NFAState inputState, outputState;
	
	public NFA(NFAState inputState, NFAState outputState){
		this.inputState = inputState;
		this.outputState = outputState;
	}
	
}
