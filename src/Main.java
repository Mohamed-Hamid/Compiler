import java.util.ArrayList;
import java.util.HashMap;

import lexicalAnalyzer.*;
import inputParser.*;

public class Main {

	public static void main(String[] args) throws Exception {

//		String os = System.getProperty("os.name");
//		String filePath = os.startsWith("Windows") ? "C:\\Users\\electric\\Dropbox\\College\\Term 9\\Programming�Languages�Translation\\Project phase 1\\Compiler\\rules.txt"
//				: "/home/hamid/Desktop/rules.txt";
//		NFAState rulesNFAInitialState = InfixEvaluator.getRulesNFA(filePath);
//		DFAState DFAInitialState = DFAState.generateDFA(rulesNFAInitialState);
//		DFAInitialState.print();
		NFA n1 = NFABuilder.concat(NFABuilder.c('1'), NFABuilder.c('2'));
//		NFA n2 = new NFA(n1);
		NFA n2 = (NFA) n1.clone();
		System.out.println(n1.getInputState().next.get('1').get(0).next);
		System.out.println(n2.getInputState().next.get('1').get(0).next);
		System.out.println(n1.getInputState().next.get('1').get(0).next.get(null).get(0).next.get('2').get(0).isLast());
		System.out.println(n2.getInputState().next.get('1').get(0).next.get(null).get(0).next.get('2').get(0).isLast());
	}
}
