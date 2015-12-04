package lexicalAnalyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class DFASimulator {
	DFAState inputState;
	String testProgramFilePath;
	
	public DFASimulator(String testProgramFilePath, DFAState inputState) {
		this.inputState = inputState;
		this.testProgramFilePath = testProgramFilePath;
	}
	
	public void simulate(){
		DFAState dfaState;
		try (BufferedReader br = new BufferedReader(new FileReader(testProgramFilePath))) {
			String line = br.readLine();
			while (line != null) {
//				int i3 = -2;
				dfaState = this.inputState;
				System.out.println(dfaState.accepting());
				boolean accept = false;
//				if(dfaState.accepting()){ //trivial accepting state
//					i3 = -1;
//				}
				for (int i1 = 0, i2 = 0, i3 = 0; i2 < line.length();) {
					while(i2 < line.length() && dfaState.next.containsKey(line.charAt(i2))){
						dfaState = dfaState.next.get(line.charAt(i2));
						if(dfaState.accepting()){
							i3 = i2;
							accept = true;
						}
						i2++;
					}
					if(accept){
						System.out.println(i1 + " " + i2 + " " + i3);
						i3 = i2;
						i1 = i2;
						accept = false;
						dfaState = this.inputState;
						System.out.println(dfaState.acceptingString);
					} else {
						i1 = i2;
						i3 = i2;
						continue;
					}
				}
			line = br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	private boolean simulate(String token){
//		DFAState dfaState = this.inputState;
//		for(Character c : token.toCharArray()){
//			if(dfaState.next.containsKey(c)){
//				dfaState = dfaState.next.get(c);
//			} else { //input is longer than the dfa, no match
//				return false;
//			}
//		}
//		return dfaState.accepting() ? true : false;
//	}
}
