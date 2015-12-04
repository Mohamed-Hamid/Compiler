package lexicalAnalyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import sharedResources.SymbolTableEntry;

public class DFASimulator {
  private DFAState inputState;
  private String testProgramFilePath;
  private HashMap<String, String> tokenNames;
  private HashMap<String, SymbolTableEntry> symbolTable;
  private ArrayList<String> simulationResult;
  
  public DFASimulator(String testProgramFilePath, DFAState inputState, HashMap<String, String> tokenNames) {
    this.inputState = inputState;
    this.testProgramFilePath = testProgramFilePath;
    this.tokenNames = tokenNames;
    this.symbolTable = new HashMap<>();
    this.simulationResult = new ArrayList<>();
  }
  
  public HashMap<String, SymbolTableEntry> simulate(){
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
        	simulationResult.add(acceptingDFAState.getAcceptingString());
            System.out.println(i1 + " " + i2 + " " + i3 + " => \t\t" + acceptingDFAState.getAcceptingString());
            String symbol = line.substring(i1, i3 + 1);
            String token = acceptingDFAState.getAcceptingString();
            symbolTable.put(symbol, new SymbolTableEntry(tokenNames.get(token)));
//            System.out.println(symbol  + "\t\t\t" + symbolTable.get(symbol).getToken());
            i3 = i2;
            i1 = i2;
            accept = false;
            dfaState = this.inputState;
          } else {
        	if(!accept && (line.charAt(i2) != ' ')){
        		simulationResult.add("Error");
        		System.out.println("Error");
        	}
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
    return symbolTable;
  }

  public void outputSimulationResult() throws IOException{
	  simulate();
	  FileWriter writer = new FileWriter("output.txt"); 
	  for(String str: this.simulationResult) {
	    writer.write(str + "\n");
	  }
	  writer.close();
  }
}
