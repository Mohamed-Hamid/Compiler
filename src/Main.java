import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import lexicalAnalyzer.*;
import inputParser.*;

public class Main {

	public static void main(String[] args) throws Exception {

		String os = System.getProperty("os.name");
		String filePath = os.startsWith("Windows") ? "C:\\Users\\electric\\Dropbox\\College\\Term 9\\Programming Languages Translation\\Project phase 1\\rules.txt"
				: "/home/hamid/Desktop/rules.txt";
		NFA rulesNFA = InfixEvaluator.getRulesNFA(filePath);

		NFAState DFAState = new NFAState();

		int i = 1;

		Queue<NFAState> toExpandState = new LinkedList<NFAState>();
		toExpandState.add(rulesNFA.getInputState());

		HashMap<NFAState, HashSet<NFAState>> DFAStateSet = new HashMap<NFAState, HashSet<NFAState>>();
		while (!toExpandState.isEmpty()) {
			NFAState currentDFAState = toExpandState.poll();
			boolean flag = true;

			HashSet<NFAState> epsilonTransitions = new HashSet<NFAState>();
			if (i == 1 && currentDFAState.next.containsKey(null)) {
				epsilonTransitions.addAll(currentDFAState.next.get(null));
			}
			if (i == 1) {
				epsilonTransitions.add(currentDFAState);
			} else {
				epsilonTransitions = DFAStateSet.get(currentDFAState);
			}

			HashMap<Character, HashSet<NFAState>> DFANext = new HashMap<Character, HashSet<NFAState>>();
			for (NFAState state : epsilonTransitions) { // Each state in current DFA states
				for (Character nextEdge : state.next.keySet()) { // Each input for each state
					if (nextEdge != null) { // DFA table does NOT have a column for epsilon input
						HashSet<NFAState> inputTransitions = new HashSet<NFAState>();

						inputTransitions.addAll(state.next.get(nextEdge));// input transitions without epsilons
						for (NFAState nextState : state.next.get(nextEdge)) {
							if (nextState.next.containsKey(null)) {
								inputTransitions.addAll(nextState.next.get(null)); // input transitions with epsilons
							}
						}

						if (DFANext.containsKey(nextEdge)) { // if a previous state had an input transition on this edge
							HashSet<NFAState> oldInputTransitions = DFANext.get(nextEdge);
							oldInputTransitions.addAll(inputTransitions);
							// DFANext.put(nextEdge, oldInputTransitions); Useless because hashset has the same reference, so it is updated automatically
						} else { // else this is the first state in DFA states to have this input transition
							DFANext.put(nextEdge, inputTransitions);
							// CHECK LATER,
							NFAState newDFAState = new NFAState();
							HashSet<NFAState> shouldntBeAHS = new HashSet<NFAState>();
							shouldntBeAHS.add(newDFAState);
							DFAState.next.put(nextEdge, shouldntBeAHS);

							DFAStateSet.put(newDFAState, inputTransitions);

							toExpandState.add(newDFAState);
						}
					}
				}
			}

			// DFAState.next = DFANext;
			if (flag) {
				// break;
			}
			i++;
		}

		System.out.println("end");
	}
}
