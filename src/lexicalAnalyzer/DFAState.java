package lexicalAnalyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class DFAState {

	private static int count = 1;
	private int num;
	public String acceptingString = "";
	public HashMap<Character, DFAState> next;

	public DFAState() {
		this.next = new HashMap<Character, DFAState>();
		this.num = count++;
	}

	public static DFAState generateDFA(NFAState rulesNFAInitialState) {
		DFAState DFAInitialState = new DFAState();
		Queue<DFAState> toExpandState = new LinkedList<DFAState>();
		toExpandState.add(DFAInitialState);

		HashMap<DFAState, HashSet<NFAState>> DFAStateSet = new HashMap<DFAState, HashSet<NFAState>>();

		HashSet<NFAState> epsilonTransitions = new HashSet<NFAState>();
		epsilonTransitions.addAll(getEpsilonTransitions(rulesNFAInitialState));
		epsilonTransitions.add(rulesNFAInitialState);
		DFAStateSet.put(DFAInitialState, epsilonTransitions);

		while (!toExpandState.isEmpty()) {
			DFAState currentDFAState = toExpandState.poll();

			epsilonTransitions = DFAStateSet.get(currentDFAState);
			currentDFAState.checkAcceptance(epsilonTransitions);
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
							// CHECK LATER, Remove duplicates in minimal DFA
							DFAState newDFAState = new DFAState();
							currentDFAState.next.put(nextEdge, newDFAState);

							DFAStateSet.put(newDFAState, inputTransitions);

							toExpandState.add(newDFAState);
						}
					}
				}
			}
		}

		return DFAInitialState;
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

	public void print() {
		Queue<DFAState> toExpandState = new LinkedList<DFAState>();
		toExpandState.add(this);

		while (!toExpandState.isEmpty()) {
			DFAState currentState = toExpandState.poll();
			System.out.print("Num: " + currentState.num + " ");
			System.out.println(currentState.next);
			String stateAcceptingString = currentState.getAcceptingString();
			if (stateAcceptingString.length() != 0) {
				System.out.println(" accepting: " + stateAcceptingString);
			}
			for (Character c : currentState.next.keySet()) {
				toExpandState.add(currentState.next.get(c));
			}

		}
	}

	private String checkAcceptance(HashSet<NFAState> transitions) {
		for (NFAState state : transitions) {
			String NFAStateAcceptingString = state.getAcceptingString();
			if (NFAStateAcceptingString.length() != 0) {
				this.setAcceptingString(NFAStateAcceptingString);
			}
		}
		return "";
	}
	
	public boolean accepting(){
		return acceptingString == "" ? false : true;
	}

	@Override
	public String toString() {
		return num + "";
	}

	public String getAcceptingString() {
		return acceptingString;
	}

	public void setAcceptingString(String acceptingString) {
		this.acceptingString = acceptingString;
	}
}
