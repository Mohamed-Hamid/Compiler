package lexicalAnalyzer;

public class NFA {
	private NFAState inputState, outputState;
	
	public NFA(NFAState inputState, NFAState outputState){
		this.inputState = inputState;
		this.outputState = outputState;
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
