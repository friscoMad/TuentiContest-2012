package tuenti.contest;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class Challenge1 {
	private static Logger log = Logger.getLogger(Challenge1.class);
	
	private class Key {
		private Point position;
		private int presses;
		public Point getPosition() {
			return position;
		}
		public void setPosition(Point position) {
			this.position = position;
		}
		public int getPresses() {
			return presses;
		}
		public void setPresses(int presses) {
			this.presses = presses;
		}
		public Key(Point position, int presses) {
			super();
			this.position = position;
			this.presses = presses;
		}
		public Key(int x, int y, int presses) {
			super();
			this.position = new Point(x,y);
			this.presses = presses;
		}
	}
	
	protected static String positions = " 1;abc2;def3;ghi4;jkl5;mno6;pqrs7;tuv8;wxyz9;;0";
	protected static Point upper = new Point(2,3);
	protected static HashMap<Character, Key> letters; 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Challenge1 main = new Challenge1();
		main.process();
	}
	
	public void process() {
		Scanner in = new Scanner(System.in);
		createPostionMap();
		int lines = in.nextInt();
		in.nextLine();
		while (in.hasNextLine()) {
			String line = in.nextLine();
			log.debug(line);
			ArrayList<Character> lineChars = new ArrayList<Character>();
			for (int i = 0; i < line.length(); i++) {
				lineChars.add(line.charAt(i));
			}
			System.out.println(processLine(lineChars)*10);
		}
		in.close();
	}
	
	private void createPostionMap() {
		String[] buttons = positions.split(";");
		letters = new HashMap<Character, Key>();
		int x = 0;
		int y = 0;		
		for (String button : buttons) {
			for (int presses = 0; presses < button.length(); presses++) {
				letters.put(button.charAt(presses), new Key(x,y,presses+1));
			}
			x++;
			if (x==3) {
				y++;
				x=0;
			}
		}
	}
	private int processLine(ArrayList<Character> line) {
		boolean isUpper = false;
		Point current = new Point(1,3);
		Key key;
		int time = 0;
		for (Character car : line) {
			log.debug("car: " +car);
			if (!Character.isDigit(car) && !Character.isWhitespace(car)
					&& Character.isUpperCase(car) != isUpper) {
				time += travel(current, upper) + 10;
				current = upper;
				isUpper = !isUpper;
				log.debug("pressed upper "+ time);
				log.debug("upper state"+ isUpper);
			}
			if (Character.isUpperCase(car)) {
				car = Character.toLowerCase(car);
			}
			key = letters.get(car); 
			if (key.getPosition().equals(current)) {
				time += 50;
				log.debug("same position "+ time);
			} else {
				time += travel(current, key.getPosition());
				log.debug("travel to "+ key.getPosition() + ":" + time);
			}
			time += 10*key.getPresses();
			log.debug("pressed "+ time);
			current = key.getPosition();
			log.debug("finish at "+ current);
		}
		return time;
	}
	
	private int travel (Point from, Point to) {
		int xDist = Math.abs(from.x - to.x);
		int yDist = Math.abs(from.y - to.y);
		int min = Math.min(xDist, yDist);
		int max = Math.max(xDist, yDist);
		if (min == 0) {
			if (max == xDist) {
				return xDist * 20; 
			} else {
				return yDist * 30;
			}
		} else {
			if (max == xDist) {
				return (xDist-min) * 20 + min*35; 
			} else {
				return (yDist-min) * 30 + min*35;
			}
		}
	}
}
