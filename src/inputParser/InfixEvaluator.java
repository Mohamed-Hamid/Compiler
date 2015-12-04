package inputParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

import lexicalAnalyzer.*;

public class InfixEvaluator {
	private static HashMap<String, String> tokeNames;
	private static Stack<NFA> operands;
	private static Stack<Character> operators;
	private static Character[] seps = { ' ', '-', '|', '+', '*', '(', ')' };
	private static ArrayList<Character> separators = new ArrayList<Character>(Arrays.asList(seps));
	private static HashMap<String, NFA> definitions = new HashMap<String, NFA>();
	private static HashMap<String, NFA> expressions = new HashMap<String, NFA>();

	public static NFAState getRulesNFA(String filePath) throws Exception {
		// NFA nfa = NFABuilder.concat(NFABuilder.or(NFABuilder.kleeneStar(NFABuilder.c('A')), NFABuilder.c('B')), NFABuilder.c('M'));
		// First path:
		// System.out.println(nfa.getInputState().next.get(null).get(0).next.get('A').get(0).next.get(null).get(1).next.get(null).get(0).next);
		tokeNames = new HashMap<String, String>();
		int lineNumber = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line = br.readLine();
			while (line != null) {
				lineNumber++;
				// Read line, separate on four cases { , [ , expression, definition
				if (line.charAt(0) == '{') {
					String lineWithoutBrackets = line.substring(1, line.length() - 1).trim();
					for (String token : lineWithoutBrackets.split(" ")) {
						NFA stringNFA = NFABuilder.s(token);
						expressions.put(token, stringNFA);
						tokeNames.put(token, token.toUpperCase());
						stringNFA.getOutputState().setAcceptingString(0 + " " + token);
					}
				} else if (line.charAt(0) == '[') {
					for (int i = 1; i < line.length() - 1; i++) {
						char parsedChar = line.charAt(i);
						if (parsedChar != '\\' && parsedChar != ' ') {
							NFA punctuationNFA = NFABuilder.c(parsedChar);
							tokeNames.put(parsedChar + "", parsedChar + "_PUNCT");
							expressions.put(parsedChar + "", punctuationNFA);
							punctuationNFA.getOutputState().setAcceptingString(0 + " " + parsedChar);
						}
					}
				} else { // Expressions OR Definitions
					Boolean isDefinition;
					String LHSName;
					if ((line.indexOf('=') != -1 && line.indexOf('=') < line.indexOf(':')) || line.indexOf(':') == -1) {
						isDefinition = true;
					} else {
						isDefinition = false;
					}
					if (isDefinition) {
						LHSName = line.substring(0, line.indexOf('=')).trim();
						// System.out.println(line.substring(0, line.indexOf('=')).trim() + "=");
						line = line.substring(line.indexOf('=') + 1);
					} else {
						LHSName = line.substring(0, line.indexOf(':')).trim();
						line = line.substring(line.indexOf(':') + 1);
					}

					// Tokenize line, separate on operators or space
					ArrayList<String> lineTokens = tokenize(line);
					System.out.println(lineTokens);

					// Parse tokens with precedence
					String token;
					operands = new Stack<NFA>();
					operators = new Stack<Character>();
					for (int i = 0; i < lineTokens.size(); i++) {
						token = lineTokens.get(i);
						if (isOperator(token)) { // operator
							char operator = token.charAt(0);
							if (operator == ' ') {
								throw new Exception("CANNOT BE A SPACE AS OPERATOR!");
							} else if (operator == '-') {
								// specially treated operator, no precedence rules, may be implemented in a neat way later
								char begin = lineTokens.get(i - 1).charAt(0), end = lineTokens.get(i + 1).charAt(0);
								i++;

								char index = begin;
								// CHANGE: NFA tempNFA = NFA(begin.toString());
								/* NFA tempNFA = new NFA(); */
								ArrayList<Character> rangeChars = new ArrayList<>();
								while (index <= end) {
									// CHANGE: NFA indexNFA = NFA(index.toString());
									// tempNFA = NFAor(tempNFA, indexNFA);
									rangeChars.add(index);
									index = (char) (index + 1);
								}
								System.out.println(" * " + rangeChars + " * ");
								NFA tempNFA = NFABuilder.or(rangeChars.toArray());
								operands.pop(); // remove the first character of the range operator from stack
								operands.push(tempNFA);
							} else {
								// operators: * + | ( ) .
								parseOperator(operator);
							}

							if (i + 1 < lineTokens.size()) {
								String nextToken = lineTokens.get(i + 1);
								// Check for concatenation after + * )
								if (operator == '+' || operator == '*' || operator == ')') {
									if (!isOperator(nextToken) || nextToken.equals("(")) {
										// System.out.println("CONCAT case [+*)].operand OR ).(: " + nextToken);
										parseOperator('.');
									}
								}
							}

							// System.out.println("or: " + token);
						} else { // operand
							// System.out.println("od: " + token);
							NFA operandNFA = getOperandNFA(token);
							operands.push(operandNFA);

							// Check for concatenation operator after the operand
							if (i + 1 < lineTokens.size()) {
								String nextToken = lineTokens.get(i + 1);
								if (!isOperator(nextToken)) {
									// System.out.println("CONCAT case operand.operand: " + nextToken);
									parseOperator('.');
								} else if (nextToken.equals("(")) {
									// System.out.println("CONCAT case operand.(: " + nextToken);
									parseOperator('.');
								}
							}
						}
					}

					// empty the operators stack
					executeStack();

					NFA resultNFA = operands.pop();

					if (isDefinition) {
						definitions.put(LHSName, resultNFA);
					} else {
						tokeNames.put(LHSName, LHSName.toUpperCase());
						expressions.put(LHSName, resultNFA);
						resultNFA.getOutputState().setAcceptingString(lineNumber + " " + LHSName);
					}
				}
				line = br.readLine();
			}

			// Combine each line's NFA in a single NFA
			// NFA resultantNFA = NFABuilder.or(expressions.values().toArray());
			NFAState resultantNFAInitialState = NFABuilder.combine(expressions.values().toArray());

			return resultantNFAInitialState;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private static boolean isOperator(String token) {
		return token.length() == 1 && separators.contains(token.charAt(0));
	}

	private static NFA getOperandNFA(String token) throws CloneNotSupportedException {
		NFA operandNFA;
		if (definitions.containsKey(token)) {
			operandNFA = (NFA) definitions.get(token).clone();
		} else {
			token = token.replace("\\", "");
			operandNFA = NFABuilder.s(token);
		}
		return operandNFA;
	}

	private static void parseOperator(Character currentOperator) {
		// execute operator if its precedence is lower than or equal to stack's peek, else push to stack
		Character stackPeek = operators.empty() ? ' ' : operators.peek();
		HashMap<Character, Integer> operatorsPrecedence = new HashMap<Character, Integer>();
		operatorsPrecedence.put('(', 10);
		operatorsPrecedence.put(')', 10);
		operatorsPrecedence.put('*', 9);
		operatorsPrecedence.put('+', 9);
		operatorsPrecedence.put('.', 8);
		operatorsPrecedence.put('|', 7);

		int currentPrecedence = operatorsPrecedence.get(currentOperator);
		int stackPeekPrecedence = operators.empty() ? 0 : operatorsPrecedence.get(stackPeek); // 0 not needed, it would enter in operators.empty case
		if (currentOperator == ')') {
			// pop and execute from stack till '('
			executeBracket();
		} else if (operators.empty() || operators.peek() == '(' || currentOperator == '(' || currentPrecedence > stackPeekPrecedence) {
			operators.push(currentOperator);
		} else {
			while (true) {

				if (currentPrecedence <= stackPeekPrecedence && stackPeek != '(') {
					NFA secondOperandNFA = operands.pop();
					NFA resultNFA;
					if (stackPeek == '.' | stackPeek == '|') {
						NFA firstOperandNFA = operands.pop();
						resultNFA = generateNFA(stackPeek, firstOperandNFA, secondOperandNFA);
					} else {
						resultNFA = generateNFA(stackPeek, secondOperandNFA, null);
					}
					operands.push(resultNFA);
					operators.pop();
					stackPeek = operators.empty() ? ' ' : operators.peek();
					stackPeekPrecedence = operators.empty() ? 0 : operatorsPrecedence.get(stackPeek);

				} else {
					break;
				}
			}
			operators.push(currentOperator);
		}
	}

	private static void executeBracket() {
		Character poppedOperator = operators.pop();
		while (poppedOperator != '(') {

			NFA secondOperandNFA = operands.pop();
			NFA resultNFA;
			if (poppedOperator == '.' | poppedOperator == '|') {
				NFA firstOperandNFA = operands.pop();
				resultNFA = generateNFA(poppedOperator, firstOperandNFA, secondOperandNFA);
			} else {
				resultNFA = generateNFA(poppedOperator, secondOperandNFA, null);
			}

			operands.push(resultNFA);

			poppedOperator = operators.pop();
		}
	}

	private static NFA generateNFA(Character operator, NFA operand1, NFA operand2) {
		NFA resultantNFA = null;
		switch (operator) {
		// CHANGE:
		case '.':
			resultantNFA = NFABuilder.concat(operand1, operand2);
			break;
		case '|':
			// NFAor(operands1, operand2);
			resultantNFA = NFABuilder.or(operand1, operand2);
			break;
		case '*':
			// NFAkleene(operands1, operand2);
			resultantNFA = NFABuilder.kleeneStar(operand1);
			break;
		case '+':
			try {
				resultantNFA = NFABuilder.kleenePlus(operand1);
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		return resultantNFA;
	}

	private static void executeStack() {
		while (!operators.isEmpty()) {
			Character operator = operators.pop();
			NFA secondOperandNFA = operands.pop();
			if (operator == '.' | operator == '|') {
				NFA firstOperandNFA = operands.pop();
				operands.push(generateNFA(operator, firstOperandNFA, secondOperandNFA));
			} else {
				operands.push(generateNFA(operator, secondOperandNFA, null));
			}
		}
	}

	private static ArrayList<String> tokenize(String line) {
		ArrayList<String> lineTokens = new ArrayList<String>();
		StringBuilder tempChars = new StringBuilder();
		for (int i1 = 0, i2 = 1; i1 < line.length(); i1++, i2++) {
			char currentChar = line.charAt(i1);
			char nextChar = i2 < line.length() ? line.charAt(i2) : ' ';
			if (currentChar == ' ') {
				continue;
			} else {
				tempChars.append(currentChar);
				char prevChar = i1 > 0 ? line.charAt(i1 - 1) : ' ';
				if ((separators.contains(nextChar) && currentChar != '\\') || (separators.contains(currentChar) && prevChar != '\\')) {
					lineTokens.add(tempChars.toString());
					// System.out.println(tempChars);
					tempChars = new StringBuilder();
				}
			}
		}
		return lineTokens;
	}
	
	public static HashMap<String, String> getTokenNames() {
		return tokeNames;
	}
	
}