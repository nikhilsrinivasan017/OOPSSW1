package com.bham.pij.assignments.calculator;
/* Name: Nikhil Srinivasan 
 * Student ID: 2198267
 * Developing the required functionality for Assignment 2's Calculator
 */
import java.util.ArrayList;

public class Calculator {
	
	static final char DECIMAL_SEPARATOR = '.';
	static final char MINUS_SIGN = '-';
	static final char PLUS_OP = '+';
	static final char MULT_OP = '*';
	static final char DIV_OP = '/';
	String expression;
	float memVal;
	float result;
	ArrayList<Float> history;
	
	public Calculator(){ //default constructor
		expression = null;
		memVal = 0;
		result = 0;
		history = new ArrayList<Float>();
	}
	
	public Calculator(String expression){ //default constructor
		this.expression = expression;
		memVal = 0;
		result = 0;
		history = new ArrayList<Float>();
	}
	
	/**
	 * This method receives a String containing an expression
	 * (in the format described above), evaluates the expression and returns the result as a float.
	 * If the String expression does not contain a valid expression (as described above) or contains a divide-by-
	 * zero attempt then this method should return the value Float.MIN VALUE.
	 * @param expression - String of the format: operand[space]operator[space]operand
	 * @return calculator's result of the string expression
	 */
	public float evaluate(String expression) {
		this.expression = expression;
		ArrayList<String> expr = new ArrayList<String>();
		boolean isValidExpr = false, divByZero = false;
		String exp[] = expression.split(" ");
		
		//split the expression into parts divided by the spaces -- op1 operator op2
		for (int i = 0; i<exp.length; i++) {
			expr.add(exp[i]);
		}
		
		float result = Float.MIN_VALUE;
		
		//check if expression isn't empty or invalid
		if (expr.size() > 1) {
			//check if expression is operating on memoryValue i.e. (* 19.2 || / 10.45 || + -1.2)
			if (expr.size() == 2) {
				//check if expression is operating on memoryValue
				//modify to operate on memory variable if stored and non-zero (operator[space]operand)
				if(this.getMemoryValue() != 0) {
					//we have a memory variable for the Calculator Class
					//which becomes our first operator i.e. add it to the beginning of the list
					expr.add(0, Float.toString(this.getMemoryValue()));
					//problem here as if we have a memVal then we arent evaluating regular expressions!!!???
				}
				
			}
			
			//Bracket Functionality -> (oper1[sp][op1][sp]oper2)[sp]op2[sp](oper3[sp][op3][sp]oper4)
			//Recursion for each expr in brackets???
			// (oper1 [space] op1 [space] oper2) [space] op2 [space] (oper3 [space] op3 [space] oper4)
			if (expr.get(0).charAt(0) == '(' && expr.get(4).charAt(0) == '(') {
				//operating on 2 bracketed expressions
				String expression1, expression2;
				//expression1
				String operand1 = expr.get(0).substring(1);
				String operator1 = expr.get(1);
				String operand2 = expr.get(2);
				
				String operator2 = expr.get(3); //operator on both bracketed expression
				
				//expression2
				String operand3 = expr.get(4).substring(1);
				String operator3 = expr.get(5);
				String operand4 = expr.get(6);
				
				if (operand2.charAt(operand2.length()-1) == ')' && operand4.charAt(operand4.length()-1) == ')' ) {
					//valid bracketed expression
					operand2 = operand2.substring(0,operand2.length()-1); //remove closing bracket
					operand4 = operand4.substring(0,operand4.length()-1); //remove closing bracket
				}
				//String builder for expression 1 and Recursively call evaluate on it
				expression1 = operand1 + " " + operator1 + " " + operand2;
				expression2 = operand3 + " " + operator3 + " " + operand4;
				Calculator b = new Calculator();
				float result1 = b.evaluate(expression1);
				float result2 = b.evaluate(expression2);
				
				result = processOperation(result1, result2, operator2);
	 		}
			
			
			//check if expr is an INDEPENDENT SIMPLE expression i.e. "operand1 [sp] operator [sp] operand2"
			if(expr.size() == 3) {
				String op1 = expr.get(0);
				String operator = expr.get(1); //operator length should be one and evaluate to either +,-,/,*
				String op2 = expr.get(2);
				float opp1 = 0, opp2 = 0;
				try {
					opp1 = Float.parseFloat(op1); //throw exception that we need to catch if not a valid float?
					opp2 = Float.parseFloat(op2);
				}catch (NumberFormatException nfe) {
					//invalid floats keep result as min_val
					System.out.println("Invalid Input.");
					return Float.MIN_VALUE; //exit the method and return result as Float.MIN_VALUE
				}
				result = processOperation(opp1, opp2, operator);
				
			}
			//Arbitrary Length expression - Order of Precedence without brackets!
			if (expr.size() > 3 && !(expr.get(0).charAt(0) == '(') && !(expr.get(4).charAt(0) == '(')) { 
				//Order of Precedence *,/ > +,- need to fix this for mult & div
				for (int pos = 0; pos < expr.size(); pos++) {
					if(expr.get(pos).equals("*")) {
						float temp;
						//MULTIPLICATION
						try {
							temp = processOperation(Float.parseFloat(expr.get(pos-1)), Float.parseFloat(expr.get(pos+1)), expr.get(pos));
						}catch (NumberFormatException nfe) {
							//invalid floats keep result as min_val
							System.out.println("Invalid Input.");
							temp = Float.MIN_VALUE; //exit the method and return result as Float.MIN_VALUE
						}
						if(temp != Float.MIN_VALUE) {
							//valid expression has been evaluated so we can replace the operands and operators in the expr w temp
							expr.add(pos-1, Float.toString(temp));
							expr.remove(pos); expr.remove(pos); expr.remove(pos);
						}else {
							return Float.MIN_VALUE;
						}
					}
				}
				for (int pos = 0; pos < expr.size(); pos++) {
					if(expr.get(pos).equals("/")) {
						float temp;
						//DIVISION
						try {
							temp = processOperation(Float.parseFloat(expr.get(pos-1)), Float.parseFloat(expr.get(pos+1)), expr.get(pos));
						}catch (NumberFormatException nfe) {
							//invalid floats keep result as min_val
							System.out.println("Invalid Input.");
							temp = Float.MIN_VALUE; //exit the method and return result as Float.MIN_VALUE
						}
						if(temp != Float.MIN_VALUE) {
							//valid expression has been evaluated so we can replace the operands and operators in the expr w temp
							expr.add(pos-1, Float.toString(temp));
							expr.remove(pos); expr.remove(pos); expr.remove(pos);
						}else {
							return Float.MIN_VALUE;
						}
					}
				}
				if (!expr.contains("*") && !expr.contains("/")) {
					for(int pos = 0; pos < expr.size(); pos++) {
						//ADDITION & SUBTRACTION
						if(expr.get(pos).equals("+") || expr.get(pos).equals("-")) {
							float temp;
							try {
								temp = processOperation(Float.parseFloat(expr.get(pos-1)), Float.parseFloat(expr.get(pos+1)), expr.get(pos));
							}catch (NumberFormatException nfe) {
								//invalid floats keep result as min_val
								System.out.println("Invalid Input.");
								temp = Float.MIN_VALUE; //exit the method and return result as Float.MIN_VALUE
							}
							if(temp != Float.MIN_VALUE) {
								//valid expression has been evaluated so we can replace the operands and operators in the expr w temp
								expr.add(pos-1, Float.toString(temp));
								expr.remove(pos); expr.remove(pos); expr.remove(pos);
							}else {
								return Float.MIN_VALUE;
							}
						}
					}
				}
				
				//finally we get either A SIMPLE INDEPENDENT EXPRESSION of the format: "operand1 operator operand2"...
				System.out.println(expr.toString());
				Calculator arb = new Calculator();
				if (expr.size() == 3)
					result = arb.evaluate(expr.get(0) + " " + expr.get(1) + " " + expr.get(2));
				//OR just a floating pt value to store as result
				else if(expr.size() == 1)
					result = Float.parseFloat(expr.get(0));
			}
		}else {
			//expr.size() < 1 which deems it as invalid.
			System.out.println("Invalid Input.");
		}
		
		
		this.result = result;
		
		if(result != Float.MIN_VALUE) //valid result
			this.history.add(result);
		//else invalid expression and we do not add it to history 
		
		return result;
		
	}
	
	/**
	 *  
	 * @param opp1 i.e. operand 1
	 * @param opp2 i.e. operand 2
	 * @param operator which operates on the 2 operands
	 * @return float result of the operation between 2 operands (Float.Min_Value if invalid expression)
	 */
	public static float processOperation (float opp1, float opp2, String operator) {
		float result = Float.MIN_VALUE;
		if(opp1 == Float.MIN_VALUE || opp2 == Float.MIN_VALUE) {
			//invalid expression
			return result;
		}
		
		if(operator.length() == 1) {
			//potentially valid operators
			if(operator.charAt(0) == PLUS_OP)
				result = opp1 + opp2;
			else if(operator.charAt(0) == MINUS_SIGN)
				result = opp1 - opp2;
			else if(operator.charAt(0) == MULT_OP)
				result = opp1*opp2;
			else if(operator.charAt(0) == DIV_OP) {
				if(opp2 == 0) {
					//divide by zero error keep result as min_val
					System.out.println("Invalid Input. (Divide by Zero)");
				}else {
					result = opp1/opp2;
				}
			}
			else {
				//invalid operator
				System.out.println("Invalid Input.");
			}
		}else {
			//invalid operator length
			System.out.println("Invalid Input.");
		}
		return result;
	}
	
	/**
	 * This method returns the current value (result) that the calculator has evaluated. For example, 
	 * if getCurrentValue() is called after the user has entered the expression 3 + 4, it would return the value 7. 
	 * If the user has not entered a valid expression, as defined above, this method should return zero.
	 * @return float result if a valid expression, else zero for an invalid expression
	 */
	public float getCurrentValue() {
		float currentVal = this.result;
		if (currentVal == Float.MIN_VALUE) //invalid expression
			return 0;
		else
			return currentVal;
	}
	
	/**
	 * 
	 * @return the current memory value of the calculator
	 */
	public float getMemoryValue() {
		return this.memVal;
	}
	
	/**
	 * If the user presses presses the 'm' key the program should store
	 * the most recent calculator result (if it was valid, zero otherwise).
	 * If the user types "mr", the program should print to the console the stored memory value (or zero if no
	 * value has been stored).
	 * @param memval
	 */
	public void setMemoryValue(float memval) {
		this.memVal = memval;
	}
	
	/**
	 * If the user presses the 'c' key, the program should clear the memory by setting it to zero.
	 * set this.memVal to 0
	 */
	public void clearMemory() {
		this.setMemoryValue(0);
	}
	
	/**
	 * This method returns the history value at the index in the parameter. If the parameter is zero, 
	 * the method should return the first value that was added to the history. 
	 * If the parameter is 1, the method should return the second value that was added to the history, and so on.
	 * @param index
	 * @return (index+1)th value added to history 
	 */
	public float getHistoryValue(int index) {
		return (this.history.get(index));
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
