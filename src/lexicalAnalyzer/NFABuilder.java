package lexicalAnalyzer;

public class NFABuilder {
	
	/* Base Case 1: Empty String */
	public static final NFA e() {
		NFAState inputState  = new NFAState();
		NFAState outputState = new NFAState();
		inputState.addEpsilonTranisition(outputState);
		outputState.setLast(true);
		return new NFA(inputState ,outputState);
    }
	
	/* Base Case 2: Single Character */
	public static final NFA c(char c) {
		NFAState inputState = new NFAState();
		NFAState outputState = new NFAState();
		outputState.setLast(true);
		inputState.addTransition(outputState, c);
		return new NFA(inputState ,outputState);
    }
	
	/* Base Case 3: Concatenation */
	public static final NFA concat(NFA first, NFA second) {
		first.getOutputState().setLast(false);
		second.getOutputState().setLast(true);
		first.getOutputState().addEpsilonTranisition(second.getInputState());
		return new NFA(first.getInputState(), second.getOutputState());
    }
	
	/* Base Case 4: Orring */
	public static final NFA or(NFA first, NFA second) {
		first.getOutputState().setLast(false);
		second.getOutputState().setLast(false);
		
		NFAState inputState = new NFAState();
		NFAState outputState = new NFAState();
		
		inputState.addEpsilonTranisition(first.getInputState());
		inputState.addEpsilonTranisition(second.getInputState());
		
		first.getOutputState().addEpsilonTranisition(outputState);
		second.getOutputState().addEpsilonTranisition(outputState);
		
		outputState.setLast(true);
		
		NFA newNFA = new NFA(inputState, outputState);
		
		return newNFA;
    }
	
	/* Base Case 5: Kleene Closure */
	public static final NFA kleeneStar(NFA nfa) {
		NFA newNFA = new NFA(nfa.getInputState(), nfa.getOutputState());
		newNFA.getOutputState().addEpsilonTranisition(nfa.getInputState());
        newNFA.getInputState().addEpsilonTranisition(nfa.getOutputState());
		return newNFA;	
    }
	

	public static NFAState combine(Object[] objects) {
		NFAState inputState = new NFAState();
		for(Object NFAinstance: objects){
			inputState.addEpsilonTranisition(((NFA) NFAinstance).getInputState());
		}
		return inputState;
	}

	
	//TODO: Complete this to reuse kleeneStar -> Implement Deep copy of an NFA
	/* Kleene Closure */
	public static final NFA kleenePlus(NFA nfa) throws CloneNotSupportedException {
		NFA clonnedNFA = (NFA) nfa.clone();
		return concat(nfa, kleeneStar(clonnedNFA));
    }
	
	/* String */
	public static final NFA s(String str) {
		if(str.length() == 0){
			return e();
		} else if(str.length() == 1){
			return c(str.charAt(0));
		} else {
			return concat(c(str.charAt(0)), s(str.substring(1)));
		}
    }
	
	/* Orring between multiple NFAs */
	public static final NFA or(Object... regexs) {
		NFA exp = regex(regexs[0]);
		for (int i = 1; i < regexs.length; i++) {
		    exp = or(exp, regex(regexs[i]));
		}
		return exp ;
    }

	/* Concatenation between multiple NFAs */
	public static final NFA concat(Object... regexs) {
		NFA exp = regex(regexs[0]);
		for (int i = 1; i < regexs.length; i++) {
		    exp = concat(exp, regex(regexs[i]));
		}
		return exp;
    }
	
	/* Regex conversion */
    private static final NFA regex(Object o) {
		if (o instanceof NFA)
		    return (NFA)o;
		else if (o instanceof Character)
		    return c((Character)o);
		else if (o instanceof String)
		    return s((String)o);
		else {
		    throw new RuntimeException("bad regexp");
		}
    }

}
