package tuenti.contest.challenge17;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class Challenge17 {
	private static Logger log = Logger.getLogger(Challenge17.class);

	public static void main(String[] args) throws InterruptedException {
		Challenge17 main = new Challenge17();
		try {
			main.process();
		} catch (Exception e) {
			log.error("Exception:", e);
			Thread.sleep(600000);
		}
	}
	//I am just sure this algorithm is wrong but the problem is just how should I choose the tethas to try
	//I can not think on any binary search algorithm that can be applied so even if all the logic is correct
	//This can render false negatives (never false positives) 
	private void process() throws IOException {
		Scanner in = new Scanner(System.in);
		in.useLocale(new Locale("en"));
		int cases = in.nextInt();
		for (int i = 0; i < cases; i++) {
			processProblem(in,i);
		}
	}
	
	private void processProblem(Scanner in, int caseNumber) {
		in.nextLine();
		FloatPoint center = readPoint(in);
		double radius = in.nextDouble();
		in.nextLine();
		int ingNumber = in.nextInt();
		ArrayList<FloatPolygon> ingredients = new ArrayList<FloatPolygon>();
		HashMap<FloatPolygon, Integer> mapTypes = new HashMap<FloatPolygon, Integer>(); 
		for (int i = 0; i < ingNumber; i++) {
			in.nextLine();
			String name = in.next("\\w+");
			int sides = in.nextInt();
			int number = in.nextInt();
			if (number%2 != 0) {
				System.out.println("Case #"+(caseNumber+1)+": FALSE");
				return;
			}
			for (int j = 0; j < number; j++) {
				in.nextLine();
				//Parse all points as a reference to pizza center
				FloatPolygon element = new FloatPolygon(sides, readCorrectedPoint(in, center),
						readCorrectedPoint(in, center));
				ingredients.add(element);
				mapTypes.put(element, i);
			}
		}
		boolean result = processPizza(center, ingredients, ingNumber, mapTypes);
		log.debug("result: " + result);
		System.out.println("Case #"+(caseNumber+1)+": "+(result?"TRUE":"FALSE"));
		
	}

	//The big error, this tries for tethas (seeing everything in polar with pizza center at 0,0)
	//As I could't see any way to find the perfect cut (see perfect tetha) I just try for every ingredient
	//min tetha so to be sure at least itself is not sliced, but for sure there will be cases with every
	//ingredient min tetha returning false while a different tetha can return true, but this passes the test
	//cases I have been working on this for 4 hours and I have less than 1 day to finish :(
	private boolean processPizza(FloatPoint center,
			ArrayList<FloatPolygon> ingredients, int types, HashMap<FloatPolygon, Integer> mapTypes) {
//There is no need of this anymore but I liked it so I just leave this commented		
//		ThetaComparator comparator = new ThetaComparator();
//		Collections.sort(ingredients, comparator);
		for (FloatPolygon poly : ingredients) {
			if (checkEquals(ingredients, poly.getMinTetha(), types, mapTypes)) {
				return true;
			}
		}
		return false;

	
	}

	//Checks if a given cut is perfectly equal or if a given range of [tetha, tetha+ PI[
	//contains just the half of the ingredients and has an equal number of each type inside and outside
	//this can be don easily just checking tetha in polar coordinates for the polygons
	//if any ingredient is sliced for the given tetha value return false inmediatly
	private boolean checkEquals(ArrayList<FloatPolygon> ingredients,
			double theta, int types, HashMap<FloatPolygon, Integer> mapTypes) {
		int inside = 0;
		int[] typeInside = new int[types];
		int[] typeOutside = new int[types];
		for (FloatPolygon poly : ingredients) {
			//The full range is inside -Pi and Pi
			if (theta <= 0) {				
				//The poly is inside
				if (poly.getMaxTetha() < theta + Math.PI 
						&& poly.getMinTetha() >= theta) {
					inside++;
					typeInside[mapTypes.get(poly)]++;
				//The poly is outside
				} else if (poly.getMaxTetha() < theta) {
					typeOutside[mapTypes.get(poly)]++;
				} else {
					//Sliced so return false
					return false;
				}
			// The range is splitted in [tetha,Pi] and [-Pi,tetha-PI[  			
			} else {
				if (poly.getMinTetha() >= theta || poly.getMaxTetha() < theta - Math.PI) {
					inside++;
					typeInside[mapTypes.get(poly)]++;
				} else if (poly.getMinTetha() > theta - Math.PI && poly.getMaxTetha() < theta) {
					typeOutside[mapTypes.get(poly)]++;
				} else {
					return false;
				}
			}
		}
		//Check if the ingredients are evenly splitted
		if (inside != ingredients.size()/2) {
			return false;
		}
		//Check for each ingredient type
		for (int i = 0; i < types; i++) {
			if (typeInside[i] != typeOutside[i]) {
				return false;
			}
		}
		return true;
	}

	//Read a point from input corrected to reference point(0,0) as the pizza center
	private FloatPoint readCorrectedPoint(Scanner in, FloatPoint center) {		
		return new FloatPoint(in.nextFloat() - center.getX(), in.nextFloat() - center.getY());
	}

	//Read a point from input
	private FloatPoint readPoint(Scanner in) {		
		return new FloatPoint(in.nextFloat(), in.nextFloat());
	}
}
