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
		DFAState dfaState, acceptingDFAState = null;
		try (BufferedReader br = new BufferedReader(new FileReader(testProgramFilePath))) {
			String line = br.readLine();
			while (line != null) {
				System.out.println("\n" + line);
				dfaState = this.inputState;
				boolean accept = false;
				for (int i1 = 0, i2 = 0, i3 = 0; i2 < line.length();) {
					while((i2 < line.length() && line.charAt(i2) != ' ') && (dfaState.next.containsKey(line.charAt(i2))) ){
						dfaState = dfaState.next.get(line.charAt(i2));
						if(dfaState.accepting()){
							i3 = i2;
							accept = true;
							acceptingDFAState = dfaState;
						}
						i2++;
					}
					if(accept){
						System.out.println(i1 + " " + i2 + " " + i3 + " => \t\t" + acceptingDFAState.getAcceptingString());
						i3 = i2;
						i1 = i2;
						accept = false;
						dfaState = this.inputState;
					} else {
						i2++;
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
}
