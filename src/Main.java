import lexicalAnalyzer.*;
import inputParser.*;

public class Main {

	public static void main(String[] args) throws Exception {

		String os = System.getProperty("os.name");
		String filePath = os.startsWith("Windows") ? "C:\\Users\\electric\\Dropbox\\College\\Term 9\\Programming Languages Translation\\Project phase 1\\Compiler\\rules.txt"
				: "/home/hamid/Desktop/rules.txt";
		NFAState rulesNFAInitialState = InfixEvaluator.getRulesNFA(filePath);
		DFAState DFAInitialState = DFAState.generateDFA(rulesNFAInitialState);
		DFAInitialState.print();
	}
}
