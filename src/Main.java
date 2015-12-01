import java.awt.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import lexicalAnalyzer.*;

public class Main {
	private static HashMap<String, String> symbolTable;
	private static Stack<NFA> operands;
	private static Stack<Character> operators;
	private static Character[] seps = { ' ', '-', '|', '+', '*', '(', ')' };
	private static ArrayList<Character> separators = new ArrayList<Character>(
			Arrays.asList(seps));
	private static HashMap<String, Double> definitions = new HashMap<String, Double>(); // TC
																						// Double
																						// =>
																						// NFA

	public static void main(String[] args) throws Exception {
		symbolTable = new HashMap<String, String>();
		try (BufferedReader br = new BufferedReader(
				new FileReader(
						"C:\\Users\\electric\\Dropbox\\College\\Term 9\\Programming Languages Translation\\Project phase 1\\rules.txt"))) {
			String line = br.readLine();
			while (line != null) {
				// Read line, separate on four cases { , [ , expression,
				// definition
				if (line.charAt(0) == '{') {
					for (String token : line.substring(1, line.length() - 1)
							.trim().split(" ")) {
						// System.out.println("==="+ token+"===");
						// NFA concatenation of chars
					}
				} else if (line.charAt(0) == '[') {
					for (int i = 1; i < line.length() - 1; i++) {
						char parsedChar = line.charAt(i);
						if (parsedChar != '\\' && parsedChar != ' ') {
							symbolTable.put(parsedChar + "", parsedChar
									+ "_PUNCT");
						}
					}
				} else { // Expressions OR Definitions
					Boolean isDefinition;
					if ((line.indexOf('=') != -1 && line.indexOf('=') < line
							.indexOf(':')) || line.indexOf(':') == -1) {
						isDefinition = true;
					} else {
						isDefinition = false;
					}
					if (isDefinition) {
						// System.out.println(line.substring(0,
						// line.indexOf('=')).trim() + "=");
						line = line.substring(line.indexOf('=') + 1);
					} else {
						line = line.substring(line.indexOf(':') + 1);
					}

					// Tokenize line, separate on operators or space
					ArrayList<String> lineTokens = new ArrayList<String>();
					StringBuilder tempChars = new StringBuilder();
					for (int i1 = 0, i2 = 1; i1 < line.length(); i1++, i2++) {
						char currentChar = line.charAt(i1);
						char nextChar = i2 < line.length() ? line.charAt(i2)
								: ' ';
						if (currentChar == ' ') {
							continue;
						} else {
							tempChars.append(currentChar);
							char prevChar = i1 > 0 ? line.charAt(i1 - 1) : ' ';
							if ((separators.contains(nextChar) && currentChar != '\\')
									|| (separators.contains(currentChar) && prevChar != '\\')) {
								lineTokens.add(tempChars.toString());
								// System.out.println(tempChars);
								tempChars = new StringBuilder();
							}
						}
					}
					System.out.println(lineTokens);

					// Parse tokens with precedence
					String token;
					operands = new Stack<NFA>();
					operators = new Stack<Character>();
					for (int i = 0; i < lineTokens.size(); i++) {
						token = lineTokens.get(i);
						if (isOperator(token)) { // operator
							char operator = token.charAt(0);
							// TC
							if (operator == ' ') {
								throw new Exception(
										"CANNOT BE A SPACE AS OPERATOR!");
							} else if (operator == '-') {
								// specially treated operator, no precedence
								// rules, may be implemented in a neat way later
								char begin = lineTokens.get(i - 1).charAt(0), end = lineTokens
										.get(i + 1).charAt(0);
								i++;
								// operands.pop();
								char index = (char) (begin + 1);
								// NFA tempNFA = NFA(begin.toString());
								while (index <= end) {
									// NFA indexNFA = NFA(index.toString());
									// tempNFA = NFAor(tempNFA, indexNFA);
									index = (char) (index + 1);
								}
								// operands.push(tempNFA);
							} else {
								// operators: * + | ( ) .
								parseOperator(operator);
							}

							if (i + 1 < lineTokens.size()) {
								String nextToken = lineTokens.get(i + 1);
								// Check for concatenation after
								// + * )
								if (operator == '+' || operator == '*'
										|| operator == ')') {
									if (!isOperator(nextToken)
											|| nextToken.equals("(")) {
										System.out
												.println("CONCAT case [+*)].operand OR ).(: "
														+ nextToken);
										// TC
										// nextOperandNFA =
										// getOperandNFA(nextToken)
										// NFA resultNFA = NFAconcat(operandNFA,
										// nextOperandNFA);
										// operands.pop();
										// operands.push(resultNFA);
									}
								}
							}

							// System.out.println("or: " + token);
						} else { // operand
							// System.out.println("od: " + token);
							// TC
							// NFA operandNFA = getOperandNFA(token);
							// operands.push(operandNFA);

							// Check for concatenation operator after the
							// operand
							if (i + 1 < lineTokens.size()) {
								String nextToken = lineTokens.get(i + 1);
								if (!isOperator(nextToken)) {
									System.out
											.println("CONCAT case operand.operand: "
													+ nextToken);
									// TC
									// nextOperandNFA = getOperandNFA(nextToken)
									// NFA resultNFA = NFAconcat(operandNFA,
									// nextOperandNFA);
									// operands.pop();
									// operands.push(resultNFA);
								} else if (nextToken.equals("(")) {
									System.out
											.println("CONCAT case operand.(: "
													+ nextToken);
									// TC
									// parseOperator('.',
									// operators.push('.');

								}
							}
						}
					}

					// empty the operators stack
					while (!operators.isEmpty()) {
						Character operator = operators.pop();
						// NFA firstOperandNFA = operands.pop();
						// NFA secondOperandNFA = operands.pop();
						// if(operator == '|'){
						// NFA resultNFA = NFAor(firstOperandNFA,
						// secondOperandNFA);
						// } else if( operator == '.' ) {
						// NFA resultNFA = NFAconcat(firstOperandNFA,
						// secondOperandNFA);
						// }
						// operands.push(resultNFA);
					}
					// NFA resultNFA = operands.pop();

					// if (isDefinition) {
					// definitions.put(line.substring(0, line.indexOf('='))
					// .trim(), resultNFA);
					// } else {
					// Put in symbol table
					//
					// }
				}
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static boolean isOperator(String token) {
		return token.length() == 1 && separators.contains(token.charAt(0));
	}

	// TC change boolean to NFA
	private static boolean getOperandNFA(String token) {
		// NFA operandNFA;
		if (definitions.containsKey(token)) {
			// operandNFA = definitions.get(token);
		} else {
			// Change operand to a NFA
			// operandNFA = NFA(token);
		}
		// return operandNFA
		return true;

	}

	private static void parseOperator(Character currentOperator) {
		// execute operator if its precedence is lower than or equal to stack's
		// peek, else push to stack
		Character stackPeek = operators.empty() ? ' ' : operators.peek();
		HashMap<Character, Integer> operatorsPrecedence = new HashMap<Character, Integer>();
		operatorsPrecedence.put('(', 10);
		operatorsPrecedence.put('*', 9);
		operatorsPrecedence.put('+', 9);
		operatorsPrecedence.put('.', 8);
		operatorsPrecedence.put('|', 7);

		int currentPrecedence = operatorsPrecedence.get(currentOperator);
		int stackPeekPrecedence = operators.empty() ? 11 : operatorsPrecedence
				.get(stackPeek); // 11 not needed, it would enter in
									// operators.empty case
		if (currentOperator == ')') {
			// pop and execute from stack till '('
			executeBracket();
		} else if (operators.empty() || operators.peek() == '('
				|| currentOperator == '('
				|| currentPrecedence > stackPeekPrecedence) {
			operators.push(currentOperator);
		} else {
			stackPeek = operators.pop();
			while (currentPrecedence <= stackPeekPrecedence && stackPeek != '(') {
				NFA firstOperandNFA = operands.pop();
				NFA secondOperandNFA = operands.pop();
				NFA resultNFA = generateNFA(stackPeek, firstOperandNFA,
						secondOperandNFA);
				operands.push(resultNFA);
				stackPeek = operators.empty() ? ' ' : operators.pop();
				stackPeekPrecedence = operators.empty() ? 11
						: operatorsPrecedence.get(stackPeek);
			}
			operators.push(currentOperator);
		}
	}

	private static void executeBracket() {
		Character poppedOperator = operators.pop();
		while (poppedOperator != '(') {

			NFA firstOperandNFA = operands.pop();
			NFA secondOperandNFA = operands.pop();
			NFA resultNFA = generateNFA(poppedOperator, firstOperandNFA,
					secondOperandNFA);
			operands.push(resultNFA);

			poppedOperator = operators.pop();
		}
	}

	private static NFA generateNFA(Character operator, NFA operand1,
			NFA operand2) {
		switch (operator) {
		case '.':
			// NFAconcat(operands1, operand2);
			break;
		case '|':
			// NFAor(operands1, operand2);
			break;
		case '*':
			// NFAkleene(operands1, operand2);
			break;
		case '+':
			// NFAplus(operands1, operand2);
			break;
		}
		return new NFA();
	}
}
