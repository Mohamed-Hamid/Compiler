import java.awt.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


public class Main {
	private static HashMap<String, String> symbolTable;
	private Stack<Double> operands = new Stack<>();
	private Stack<Character> operators = new Stack<>();
	private static ArrayList<Character> separators = new ArrayList<Character>(Arrays.asList({' ', '-', '|', '+', '*', '(', ')'})); 
	
	public static void main(String[] args) {
		symbolTable = new HashMap<String, String>();
		try(BufferedReader br = new BufferedReader(new FileReader("/home/hamid/Desktop/rules.txt"))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    
		    sb.append(line);
	        sb.append(System.lineSeparator());
	        line = br.readLine();

	        while (line != null){
		        if(line.charAt(0) == '{'){
		        	for(String token: line.split(" ")){
		        		//NFA concatenation of chars
		        	}
		        } else if(line.charAt(0) == '['){
		        	for(int i = 1; i<line.length()-1; i++){
		        		char parsedChar = line.charAt(i);
		        		if(parsedChar != '\\' && parsedChar != ' '){
		        			symbolTable.put(parsedChar + "", parsedChar + "_PUNCT");
		        		}
		        	}
		        } else { // Expressions OR Definitions
		        	Boolean isDefinition; 
					if(line.indexOf('=') < line.indexOf(':') || line.indexOf(':') == -1){ 
						isDefinition = true;
		        	} else {
		        		isDefinition = false;
		        	}
					
					if(isDefinition) {
						line = line.substring(line.indexOf('='));
					} else {
						line = line.substring(line.indexOf(':'));
					}
		        	for(int i1 = 0, i2 =1; i2 < line.length(); i1++){
//			        	if(separators.){
//			        		
//			        	}
			        }
		        }
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
//	        for(String key: symbolTable.keySet()){
//	        	System.out.println(key + " => " + symbolTable.get(key));
//	        }
		    String everything = sb.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
