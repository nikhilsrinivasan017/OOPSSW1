package com.bham.pij.assignments.rps;

import java.util.ArrayList;

/**
 * @author iankenny
 */

import java.util.Random;
import java.util.Scanner;

public class FalloutTerminal {

	/**
	 * This method creates a single line of the terminal display.
	 * @param startAddress The first address of the line.
	 * @param wordProbability If less than or equal to 0.6 the line should contain one word.
	 * @return The line for display.
	 */
	public String buildDisplayLine(int startAddress, float wordProbability) {

		/*
		 *  Add your code here. When you have selected a random word
		 *  to add to the display, you must call addSelectedWord() to 
		 *  save that word. For example, if your variable holding the word is
		 *  called `word' you would call as follows:
		 * 	addSelectedWord(word);
		 * 
		 */
		boolean containsRandomWord = false;
		String asciis = "!\"#$%&'()*+.-/:;>=<?@[\\]^_`{|}~,";
		Random rand = new Random();
		
		String lineL = "0x";
		lineL = lineL.concat(Integer.toHexString(startAddress).toUpperCase());
		lineL = lineL.concat(" ");
		
		String lineR = "0x";
		int halfAddress = startAddress + BYTE_SIZE*NUM_CHARACTERS_PER_ROW/2;
		lineR = lineR.concat(Integer.toHexString(halfAddress).toUpperCase());
		lineR = lineR.concat(" ");
		
		String word = "";
		int pos = -1;
		int leftOrRight = -1;
		
		//line = line.concat(Character.toString((char)startAddress));
		if (wordProbability <= 0.6) {
			containsRandomWord = true;
			word = getRandomWord();
			while(selectedWords.contains(word)) {
				//loop until we get a word that isn't in the selectedWords ArrayList!
				word = getRandomWord();
			}
			addSelectedWord(word);
		}
		
		if(containsRandomWord == true) {
			//1 word MUST exist in the line
			leftOrRight = rand.nextInt(2);
			//word is on the left half of the line
			pos = rand.nextInt(23); //start address of word in L/R half of line
		}
		
		//check if word belongs in left or right side of the line
		if (leftOrRight == 0) {
			//Left Side of Line with Word 
			for (int i = 0; i <= NUM_CHARACTERS_PER_ROW/2 - WORD_LENGTH; i++) {
				if(i!=pos)
					//concatenate a random non-alphanumeric ascii character to LineL
					lineL = lineL.concat(Character.toString(asciis.charAt(rand.nextInt(asciis.length()))));
				else {
					//concatenate the word to LineL
					lineL = lineL.concat(word.toUpperCase());
					//i+=8; //concatenating a word means we increment i by 8
				}
			}
			for (int i = 0; i < NUM_CHARACTERS_PER_ROW/2; i++) {
				//concatenate a random non-alphanumeric ascii character to LineR
				lineR = lineR.concat(Character.toString(asciis.charAt(rand.nextInt(asciis.length()))));
			}
		}else if (leftOrRight == 1) {
			//Right Side of Line with Word 
			for (int i = 0; i <= NUM_CHARACTERS_PER_ROW/2 - WORD_LENGTH; i++) {
				if(i!=pos)
					//concatenate a random non-alphanumeric ascii character to LineR
					lineR = lineR.concat(Character.toString(asciis.charAt(rand.nextInt(asciis.length()))));
				else {
					//concatenate the word to LineR
					lineR = lineR.concat(word.toUpperCase());
					//i+=8; //concatenating a word means we increment i by 8
				}
			}
			for (int i = 0; i < NUM_CHARACTERS_PER_ROW/2; i++) {
				//concatenate a random non-alphanumeric ascii character to LineL
				lineL = lineL.concat(Character.toString(asciis.charAt(rand.nextInt(asciis.length()))));
			}
		}else {
			//No Word in both lines
			for (int i = 0; i < NUM_CHARACTERS_PER_ROW/2 ; i++) {
				//concatenate a random non-alphanumeric ascii character to LineR
				lineR = lineR.concat(Character.toString(asciis.charAt(rand.nextInt(asciis.length()))));
				//concatenate a random non-alphanumeric ascii character to LineL
				lineL = lineL.concat(Character.toString(asciis.charAt(rand.nextInt(asciis.length()))));
			}
		}
		String line = lineL.concat(" " + lineR);
		return line;
	}
/*
 ***************************************************************************************	
 */

	private static final int START_ADDRESS = 0x9380;
	private static final int DISPLAY_HEIGHT = 20;
	private static final int NUM_CHARACTERS_PER_ROW = 60;
	private static final float WORD_PROB_1 = 0.6f;
	private static final int WORD_LENGTH = 8;
	private static final int NUM_GUESSES_ALLOWED = 4;
	private static final int BYTE_SIZE = 8;
	private static Random rand;
	private static final String[] words = {
			"flourish",
			"appendix",
			"separate",
			"unlawful",
			"platform",
			"shoulder",
			"marriage",
			"attitude",
			"reliable",
			"contempt",
			"prestige",
			"evaluate",
			"division",
			"birthday",
			"orthodox",
			"appetite",
			"perceive",
			"pleasant",
			"surprise",
			"elephant",
			"incident",
			"medieval",
			"absolute",
			"dominate",
			"designer",
			"misplace",
			"possible",
			"graduate",
			"solution",
			"governor"
	};
	
	private static String password;

	private static ArrayList<String> selectedWords = new ArrayList<String>();
	
	private void run() {
		
		System.out.println(buildDisplay());
		
		Scanner in = new Scanner(System.in);
		
		int guessCount = NUM_GUESSES_ALLOWED;
		
		boolean done = false;
		
		do {
			System.out.println("> Password required.");
			System.out.println("> Attempts remaining = " + guessCount);
			System.out.println(">");
			String guess = in.nextLine();
			
			if (guess.equalsIgnoreCase(getPassword())) {
				System.out.println("> Access granted.");
				done = true;
			}
			
			else {
				--guessCount;
				System.out.println("> Access denied.");
				System.out.println("> Likeness = " + getCorrectCount(guess, password));
			}	
		} while (guessCount > 0 && !done);
		
		if (guessCount == 0) {
			System.out.println("> Initiating lockout");
		}
		
		in.close();
	}
	
	private int getCorrectCount(String guess, String password) {
		
		int count = 0;
		
		for (int i = 0; i < guess.length(); i++) {			
			if (guess.charAt(i) == password.charAt(i)) {
				count++;
			}
		}
		
		return count;
	}
		
	private String getRandomWord() {
		return words[rand.nextInt(words.length)];
	}
	
	private String buildDisplay() {
		
		String ret = "";
		
		int address = START_ADDRESS;
						
		for (int i = 0; i < DISPLAY_HEIGHT; i++) {
											
			float rf = rand.nextFloat();
			
			String line = buildDisplayLine(address, rf);

			ret += line + "\n";
			
			address += BYTE_SIZE * NUM_CHARACTERS_PER_ROW;
		}
		
		setRandomPassword();
		
		return ret;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String pw) {
		password = pw;
	}

	private void addSelectedWord(String word) {
		selectedWords.add(word);
	}
	
	public void setRandomPassword() {
		password = selectedWords.get(rand.nextInt(selectedWords.size())).toLowerCase();
	}

	public FalloutTerminal() {
		rand = new Random(System.currentTimeMillis());	
	}
	
	public static void main(String[] args) {
		new FalloutTerminal().run();
	}
}
