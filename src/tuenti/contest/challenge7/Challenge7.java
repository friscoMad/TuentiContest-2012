package tuenti.contest.challenge7;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class Challenge7 {

	
	//For sure this problem can be solved with an easier algorithm
	//But the first part (mapping the letters to a graph) was so simple and clean that I just 
	//do not want to change it, even when traversing the graph to get the full set of solutions is a nightmare 
	private static Logger log = Logger.getLogger(Challenge7.class);
	private Letter start = null;
	private HashMap<Character,Letter> letters = new HashMap<Character,Letter>(); 
	
	public static void main(String[] args) {
		Challenge7 main = new Challenge7();
		main.process();
	}

	public void process() {
		Scanner in = new Scanner(System.in);
		start = new Letter(' ');
		while (in.hasNextLine()) {
			String line = in.nextLine();
			processLine(line);
		}
		//logGraph(start);
		printSolutions();
	}

	//Main graph sorting and process
	private void processLine(String line) {
		Letter lastFound = this.start;
		log.debug(line);
		Letter newLetter;
		for (int i = 0; i < line.length();i++) {
			char car = line.charAt(i);
			//If the letter is not in the graph the add it as a sub node from the last found letter
			if (!letters.containsKey(car)) {
				newLetter = new Letter(car);
				lastFound.getNext().add(newLetter);
				newLetter.getPrev().add(lastFound);
				letters.put(car, newLetter);
			} else {
				newLetter = letters.get(car);
				//If the letter is found then if it is not correctly placed then change the vectors
				if (!canReach(lastFound, car)) {
					removeFromPrevious(lastFound, newLetter);
					removeFromNexts(newLetter, lastFound);
					lastFound.getNext().add(newLetter);
					newLetter.getPrev().add(lastFound);
				} 
			}
			//Move the pointer to the last found letter
			lastFound = newLetter;
		}
	}

	//Finds if a letter can be reached forward from another letter
	private boolean canReach(Letter lastFound, char car) {
		if (lastFound.getCar() == car) {
			return true;
		}
		for (Letter letter : lastFound.getNext()) {
			if (canReach(letter, car)) {
				return true;
			}
		}
		return false;
	}

	//Walks backguards the node removing all forward links to the wanted letter
	//This is used when we need to rearrange the letter order
	private void removeFromPrevious(Letter parent, Letter wanted) {
		parent.getNext().remove(wanted);
		wanted.getPrev().remove(parent);
		for (Letter letter : parent.getPrev()) {
			removeFromPrevious(letter, wanted);
		}
	}

	//Walks backguards the node removing all forward links to the wanted letter
	//This is used when we need to rearrange the letter order
	private void removeFromNexts(Letter parent, Letter wanted) {
		parent.getPrev().remove(wanted);
		wanted.getNext().remove(parent);
		for (Letter letter : parent.getNext()) {
			removeFromNexts(letter, wanted);
		}
	}
	
	//The culprit I will have nightmares for the next month (till I try lvl9 :D)
	private void printSolutions() {
		ArrayList<String> solutions = findSolutions(this.start);
		//As of the combinatorial problem of unknown orders maybe there are duplicated results
		//so we have to remove them
		removeDuplicate(solutions);
		//As we could not use the partial solution to find real unreachables so we could use DP,
		//now we have to remove solutions with doubled letters
		filterSolutions(solutions);
		//It seems that the challenge does not accept the solutions in any order so we have to sort them
		Collections.sort(solutions);
		for(String solution : solutions) {
			log.debug(solution);
			System.out.println(solution);
		}
	}
	
	private void filterSolutions(ArrayList<String> solutions) {
		Pattern p = Pattern.compile("(\\w).*\\1");

		for (int i = 0; i < solutions.size(); ) {
			String solution = solutions.get(i);
			//Due to the combinatory effect not all solutions have the correct size
			//Discard the invalid ones.
			if (solution.length() != letters.size()) {
				solutions.remove(i);
				continue;
			}
			//Remove solutions with duplicate characters
			Matcher m = p.matcher(solution);
			if (m.find()) {
				solutions.remove(i);
				continue;
			}
			i++;
		}
	}

	@SuppressWarnings("unchecked")
	public static void removeDuplicate(ArrayList arlList){
		HashSet h = new HashSet(arlList);
		arlList.clear();
		arlList.addAll(h);
	}
	//Improve performance for validity checks using DP
	private HashMap<Letter, ArrayList<String>> solutionsFound = new HashMap<Letter, ArrayList<String>>(); 
	private ArrayList<String> findSolutions(Letter current) {
		ArrayList<String> results = new ArrayList<String>();
		if (current.getNext().size() == 0) {
			results.add("");
			return results;
		}
		if (solutionsFound.containsKey(current)) {
			return solutionsFound.get(current);
		}
		for (Letter letter : current.getNext()) {
			//Find unreachable nodes from this letter, this must be combined with some magic
			List<Character> unreachable = unreachable(letter);
			//If there are no unreachables then we are lucky and just add the letter and continue
			if (unreachable.size() == 0) {
				for (String sol : findSolutions(letter)) {
					results.add(letter.getCar()+sol);
				}
			} else {
				unreachable.add(letter.getCar());
				//Otherwise calculate all combinations between the current letter and the unreachable ones
				List<String> combinations = combine(unreachable);
				for (String part : combinations) {
					//As this can render not valid parts check for validity before continuing
					if (isValid(part)) {
						for (String sol : findSolutions(letter)) {
							results.add(part+sol);
						}
					}
				}
			}
		}
		solutionsFound.put(current, results);
		return results;
	}
	
	//Improve performance for validity checks using DP
	private HashMap<String, Boolean> previousValids = new HashMap<String, Boolean>(); 
	private boolean isValid(String part) {
		//Any 1 letter part is valid
		if (part.length() < 2) {
			return true;
		}
		if (previousValids.containsKey(part)) {
			return previousValids.get(part);
		}
		//Check if there are any letter misplaced from the first one
		for (int i = 1; i < part.length(); i++) {
			if (canReachBackguards(this.letters.get(part.charAt(0)), part.charAt(i))) {
				previousValids.put(part, false);
				log.debug("checking validity :" + part + " false");
				return false;
			}
		}
		
		//Be recursive my friend!
		boolean result = isValid(part.substring(1));
		log.debug("checking validity :" + part + " " + result);
		previousValids.put(part, result);
		return result;
	}

	//Creates all permutations with 0-N length and the N letters 
	private List<String> combine(List<Character> bag) {
		ArrayList<String> results = new ArrayList<String>();
		if (bag.size() == 1) {
			results.add("");
			results.add(bag.get(0).toString());
		}
		for (Character car : bag) {
			ArrayList<Character> chars = new ArrayList<Character>(bag);
			chars.remove(car);
			//I said be recursive my friend!
			List<String> combinations = combine(chars);
			for (String com : combinations) {
				results.add(com);
				results.add(car+com);
			}
		}
		return results;
	}

	//Simple function to check if we can reach from one letter to other backguards
	private boolean canReachBackguards(Letter lastFound, char car) {
		if (lastFound.getCar() == car) {
			return true;
		}
		for (Letter letter : lastFound.getPrev()) {
			if (canReachBackguards(letter, car)) {
				return true;
			}
		}
		return false;
	}
	
	//Find all unreachable nodes from one
	private List<Character> unreachable(Letter start) {
		ArrayList<Character> result = new ArrayList<Character>();
		for (char letter : this.letters.keySet()) {			
			if (!canReachBackguards(start, letter) && !canReach(start, letter)) {
				result.add(letter);
			}
		}
		return result;
	}
	
	private void logGraph(Letter node) {
		String line = node.getCar()+"";
		while (node.getNext().size() == 1 && node.getNext().get(0).getPrev().size() < 2) {
			node = node.getNext().get(0);
			line += node.getCar();
		}
		if (node.getNext().size() == 0) {
			log.debug(line);
			return;
		}
		line += "-> [";
		for(Letter letter : node.getNext()) {
			line +=letter.getCar()+",";
		}
		line += "]";
		log.debug(line);
		for(Letter letter : node.getNext()) {
			logGraph(letter);
		}
	}
}
