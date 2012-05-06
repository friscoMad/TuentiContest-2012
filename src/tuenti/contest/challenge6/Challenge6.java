package tuenti.contest.challenge6;

import java.util.Scanner;

import org.apache.log4j.Logger;

public class Challenge6 {

	private static Logger log = Logger.getLogger(Challenge6.class);
	
	public static void main(String[] args) {
		Challenge6 main = new Challenge6();
		main.process();
	}

	public void process() {
		Scanner in = new Scanner(System.in);
		int problems = in.nextInt();
		in.nextLine();
		for (int i = 0; i < problems; i++) {
			int width = in.nextInt();
			int height = in.nextInt();
			int count = in.nextInt();
			in.nextLine();
			String message = in.nextLine();
			log.debug(height +"x"+ width + " Count:" +count);
			log.debug("Message :" +message);
			long result = findThread(height, width, count, message);
			log.debug("Result: "+ result);
			System.out.println("Case #"+(i+1)+": "+result);
		}
	}

	private long findThread(int height, int width, int count, String message) {
		String[] words = message.split(" ");
		//Try to find the perfect font size
		int font = findFont(height, width, count, words);
		log.debug("Font found: " + font);
		//Find the thread used with that font size
		return calcThread(font, count, message);
	}

	private int findFont(int height, int width, int count, String[] message) {
		height = height * count;
		width = width * count;
		//Uses binary search for finding the perfect font size
		//Max font should be equal to the min dimension of the fabric (for 1 letter message)
		int maxFont = Math.min(height, width);
		Integer result = searchFont(height, width, message, 1, maxFont);
		return result;
	}

	private Integer searchFont(int height, int width, String[] message, int minFont,
			int maxFont) {
		if (maxFont < minFont) {
			log.debug("Perfect font not found");
			return null;			
		} 
		int mid = (maxFont + minFont) /2;
		//Check if there are enough space in the fabric with this font size
		if (enoughSpace(height, width, mid, message)) {
			//If there is then try a bigger one
			Integer result = searchFont(height, width, message, mid+1, maxFont);
			//If bigger one is not working then use current
			if (result == null) {
				return mid;
			} else {
				//Else use the bigger one
				return result;
			}
		} else {
			//If there isn't enough space just try with a smoller one
			return searchFont(height, width, message, minFont, mid-1);
		}
	}

	private boolean enoughSpace(int height, int width, int font, String[] message) {
		int currentWidth = width;
		//If fong is higher than space then discard
		if (height < font) {
			return false;
		}
		for (int i = 0; i < message.length; i++) {
			String word = message[i];
			int pixels = word.length()*font;
			//Check if we should start a new line
			if (pixels > currentWidth) {
				height -= font;
				//If so check there is enugh space for actually print the word
				//in the new line
				if (height < font || pixels > width) {
					return false;
				}
				currentWidth = width;
			}
			currentWidth -= pixels;
			//If we can add a new word after this one take into account the white space
			if (currentWidth > font) {
				currentWidth -= font;
			} else {
				//Else force a new line
				currentWidth = 0;
			}
		}
		return true;
	}

	private long calcThread(int font, int count, String message) {
		//Supress white spaces and calc following the provided formula
		message = message.replaceAll("\\s","");
		double length = 1.0/count * font * font /2.0 * message.length();
		return (long)Math.ceil(length);
	}
	
}
