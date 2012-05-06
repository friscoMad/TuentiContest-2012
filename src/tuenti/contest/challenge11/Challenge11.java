package tuenti.contest.challenge11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class Challenge11 {
	private static Logger log = Logger.getLogger(Challenge11.class);

	//This class contains the result of 2 different methods just don't like to delete all of the hard
	//work (the first method is a lot faster for small bag sizes (just 5-6msec for bags with 6-7 letters vs 150)
	public static void main(String[] args) throws InterruptedException {
		Challenge11 main = new Challenge11();
		try {
			main.process();
		} catch (Exception e) {
			log.error("Exception:", e);
			Thread.sleep(600000);
		}
	}

	private void process() throws IOException {
		Scanner in = new Scanner(System.in);
		log.debug("Start processing dict");
		dictProcesser();
		log.debug("End processing dict");
		int problems = in.nextInt();
		in.nextLine();
		for (int i = 0; i < problems; i++) {
			String line = in.nextLine();
			log.debug(line);
			String[] words = line.split(" ");
			processProblem(words[0], words[1]);
		}

	}

	HashMap<String, Integer> visited;
	ArrayList<String> results;
	int best;
	//Processes one problem, getting one letter at a time from the horizontal and adding to the pool
	//to find words
	//We use word value and vowel/consonant count as stop for recursivity so also initialize that
	//Visited bag of letters are also skipped (due to the constant order of bags, different bags will map to
	//the same hash).
	private void processProblem(String letters, String word) {
		best = 0;
		results = new ArrayList<String>();
		visited = new HashMap<String, Integer>();
		String originalSortedLetters = sortLetters2(letters);
		for (int i = 0; i < word.length(); i++) {
			String newWord = letters + word.charAt(i);
			//int[] vc = vowels(newWord);
			//findWord(sortLetters(newWord), vc[0], vc[1], getStringValue(newWord));
			findWord2(sortLetters2(newWord), originalSortedLetters, getStringValue(newWord));
		}
		Collections.sort(results);
		log.debug("best:" + best);
//		for (String word2 : results) {
//			log.debug("restuls:" + word2);
//		}
		System.out.println(results.get(0) + " " + best);
	}

//	private void findWord(String letters, int vowels, int consonants, int value) {
//		//Avoid redo work for the same letter combination
//		if (visited.containsKey(letters)) {
//			return;
//		//check if word exists			
//		} else if (words.containsKey(letters)) {
//			visited.put(letters, value);
//			//if it is better then clear and start from an empty list
//			if (value > best) {
//				results.clear();
//				best = value;
//			}
//			//if just equal or better add it
//			if (value >= best) {
//				results.add(words.get(letters));
//			}
//			return;
//		}
//		int length = letters.length();
//		//Remove one letter from the bag
//		//check vowel and consonant stops and new value before doing the recursivity
//		//This avoid slow string operations
//		if (value == best) {
//			return;
//		}
//		for (int i = 0; i < length; i++) {
//			char c = letters.charAt(i);
//			int newValue = value-POINTS[c-'A'];
//			if (newValue < best) {
//				continue;
//			}
//			int newV = vowels, newC = consonants;
//			if (isVowel(c)) {
//				newV--;
//			}
//			if (isConsonant(c)){
//				newC--;
//			}
//			if (newV == 0 || newC == 0) {
//				continue;
//			}
//			findWord(letters.substring(0, i) + letters.substring(i + 1, length), newV, newC, value-POINTS[c-'A']);
//		}
//		return;
//	}

	private void findWord2(String letters, String origLetters, int maxValue) {
		if (visited.containsKey(letters)) {
			return;			
		}
		visited.put(letters, 0);
		int minLength = letters.length();
		for(String key : keys) {
			if (key.length() > minLength) {
				continue;
			}
			int value = values.get(key);
			if (value < best) {
				break;
			}
			if (value > maxValue) {
				continue;
			}
			if (contains(letters, key) && !contains(origLetters, key)) {
				if (value > best) {
					results.clear();
				}
				log.debug("Found:"+key);
				best = value;
				results.add(words.get(key));
			}
		}
	}
	
	private boolean contains(String haystack, String pin) {
		int offset = 0;
		for (int i = 0; i < pin.length(); i++) {
			if ((offset = haystack.indexOf(pin.charAt(i),offset)) == -1) {
				return false;
			}
			offset++;
		}
		return true;
	}
	
	// Preprocess dictionary as sortedLetters-> words
	HashMap<String, String> words = new HashMap<String, String>();
	HashMap<String, Integer> values = new HashMap<String, Integer>();
	ArrayList<String> keys;
	//Preprocess dictionary finds the best word for every letter bag and
	//hashes the same combination of letters equal due to the special sorting magic
	//this takes almost 2 seconds on my computer
	private void dictProcesser() throws IOException {
		Scanner scan = new Scanner(
				readFileAsString("D:\\eclipse\\workspace\\tuenti2\\extra\\descrambler_wordlist.txt"));
		String word, sortedWord;
		String prevWord;
		while (scan.hasNextLine()) {
			word = scan.nextLine().trim();
			sortedWord = sortLetters2(word.trim());
			prevWord = words.get(sortedWord);
			if (prevWord == null || prevWord.compareTo(word) > 0) {
				words.put(sortedWord, word);
				values.put(sortedWord, getStringValue(word));
			}
		}
		keys = new ArrayList<String>(words.keySet());
		Collections.sort(keys,new WordCompare());
	}

	// Read file to string, if files were bigger we must change this to another
	// implementation
	private static String readFileAsString(String filePath)
			throws java.io.IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}

	private static final int[] POINTS = new int[] { 1, 3, 3, 2, 1, 4, 2, 4, 1,
			8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10 };

//	//Special sorting magic, first sort alphabetically
//	//then by value using java merge sort
//	//this results on a string order by value and alphabet so the same bag of letters will end
//	//to the same string in all permutations while ordered by points
//	private String sortLetters(String word) {
//		char[] content2 = word.toCharArray();
//		Arrays.sort(content2);
//		List<Character> content = asList(content2);
//		LetterCompare comparator = new LetterCompare();
//		Collections.sort(content, comparator);
//		return content.toString();
//	}
	
	private String sortLetters2(String word) {
		char[] content2 = word.toCharArray();
		Arrays.sort(content2);
		return new String(content2);
	}

//	private static class LetterCompare implements Comparator<Character> {
//		public int compare(Character c1, Character c2) {
//			return (POINTS[c2.charValue() - 'A'] - POINTS[c1.charValue() - 'A']);
//		}
//	}

	private static class WordCompare implements Comparator<String> {
		public int compare(String s1, String s2) {
			return (getStringValue(s2) - getStringValue(s1));
		}
	}
	
	//String <-> List<Character> util function 
	public List<Character> asList(final char[] string) {
		return new AbstractList<Character>() {
			public int size() {
				return string.length;
			}

			public Character get(int index) {
				return string[index];
			}

			public Character set(int index, Character newVal) {
				char old = string[index];
				string[index] = newVal;
				return old;
			}

			public String toString() {
				StringBuilder builder = new StringBuilder(string.length);
				for (Character ch : string) {
					builder.append(ch);
				}
				return builder.toString();
			}
		};
	}

	//Calculate string value
	private static int getStringValue(String word) {
		char[] letters = word.toCharArray();
		int result = 0;
		for (int i = 0; i < letters.length; i++) {
			result += POINTS[letters[i] - 'A'];
		}
		return result;
	}

//	//Count vowels and consonants
//	private int[] vowels(String word) {
//		int[] vc = new int[] {0,0};
//		for (int i = 0; i < word.length(); i++) {
//			char c = word.charAt(i); // first character of String
//			if  (isVowel(c)){
//				vc[0]++;
//			}
//			if (isConsonant(c)) {
//				vc[1]++;
//			}
//		}
//		return vc;
//	}
//	
//	private boolean isVowel(char c) {
//		return (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U' || c == 'Y');
//	}
//	private boolean isConsonant(char c) {
//		return (c != 'A' && c != 'E' && c != 'I' && c != 'O' && c != 'U');
//	}

}
