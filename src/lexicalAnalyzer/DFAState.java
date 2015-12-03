package lexicalAnalyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class DFAState {

	public static NFAState generateDFA(NFA rulesNFA) {
		NFAState DFAState = new NFAState();
		Queue<NFAState> toExpandState = new LinkedList<NFAState>();
		toExpandState.add(DFAState);

		HashMap<NFAState, HashSet<NFAState>> DFAStateSet = new HashMap<NFAState, HashSet<NFAState>>();

		HashSet<NFAState> epsilonTransitions = new HashSet<NFAState>();
		epsilonTransitions.addAll(getEpsilonTransitions(rulesNFA.getInputState()));
		epsilonTransitions.add(rulesNFA.getInputState());
		DFAStateSet.put(DFAState, epsilonTransitions);

		while (!toExpandState.isEmpty()) {
			NFAState currentDFAState = toExpandState.poll();

			epsilonTransitions = DFAStateSet.get(currentDFAState);

			HashMap<Character, HashSet<NFAState>> DFANext = new HashMap<Character, HashSet<NFAState>>();
			for (NFAState state : epsilonTransitions) { // Each state in current DFA states
				for (Character nextEdge : state.next.keySet()) { // Each input for each state
					if (nextEdge != null) { // DFA table does NOT have a column for epsilon input
						HashSet<NFAState> inputTransitions = new HashSet<NFAState>();

						inputTransitions.addAll(state.next.get(nextEdge));// input transitions without epsilons
						for (NFAState nextState : state.next.get(nextEdge)) {
							inputTransitions.addAll(getEpsilonTransitions(nextState));
						}

						if (DFANext.containsKey(nextEdge)) { // if a previous state had an input transition on this edge
							HashSet<NFAState> oldInputTransitions = DFANext.get(nextEdge);
							oldInputTransitions.addAll(inputTransitions);
						} else { // else this is the first state in DFA states to have this input transition
							DFANext.put(nextEdge, inputTransitions);
							// CHECK LATER,
							NFAState newDFAState = new NFAState();
							HashSet<NFAState> shouldntBeAHS = new HashSet<NFAState>();
							shouldntBeAHS.add(newDFAState);
							currentDFAState.next.put(nextEdge, shouldntBeAHS);

							DFAStateSet.put(newDFAState, inputTransitions);

							toExpandState.add(newDFAState);
						}
					}
				}
			}
		}

		return DFAState;
	}

	// gets epsilon transitions on nested levels by BFS
	private static HashSet<NFAState> getEpsilonTransitions(NFAState state) {
		Queue<NFAState> toExpandState = new LinkedList<NFAState>();
		toExpandState.add(state);

		HashSet<NFAState> epsilonTransitions = new HashSet<NFAState>();
		while (!toExpandState.isEmpty()) {
			NFAState currentState = toExpandState.poll();

			if (currentState.next.containsKey(null)) {
				epsilonTransitions.addAll(currentState.next.get(null));
				toExpandState.addAll(currentState.next.get(null));
			}
		}

		return epsilonTransitions;
	}

	public static void print(NFAState state) {
		Queue<NFAState> toExpandState = new LinkedList<NFAState>();
		toExpandState.add(state);

		while (!toExpandState.isEmpty()) {
			NFAState currentState = toExpandState.poll();
			System.out.print("Num: " + currentState.num + " ");
			System.out.println(currentState.next);
			for (Character c : currentState.next.keySet()) {
				toExpandState.addAll( currentState.next.get(c));
			}
			
		}
	}
}
