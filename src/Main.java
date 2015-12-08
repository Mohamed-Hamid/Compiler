import lexicalAnalyzer.*;
import inputParser.*;

public class Main {

	public static void main(String[] args) throws Exception {
		System.out.println(Runtime.getRuntime().maxMemory());
		String os = System.getProperty("os.name");
		String filePath = os.startsWith("Windows") ? "C:\\Users\\electric\\Dropbox\\College\\Term 9\\Programming Languages Translation\\Project phase 1\\Compiler\\rules.txt"
				: "/home/hamid/Desktop/rules.txt";
		NFAState rulesNFAInitialState = InfixEvaluator.getRulesNFA(filePath);
		DFAState DFAInitialState = DFAState.generateDFA(rulesNFAInitialState);
		System.out.println("\n\t\t** DFA Simulation **");
		System.gc();
		DFAState DFAInitialStateMinimized = DFAState.minimizeDFA(DFAInitialState);
		DFASimulator dfaSimulator = new DFASimulator(
				"C:\\Users\\electric\\Dropbox\\College\\Term 9\\Programming Languages Translation\\Project phase 1\\Compiler\\test_program.txt",
				DFAInitialStateMinimized, InfixEvaluator.getTokenNames());
		dfaSimulator.simulate();
		System.out.println("\n\t\t** DFA **");
		DFAInitialStateMinimized.printTable();
	}
}
