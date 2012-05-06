package tuenti.contest.challenge12;

import java.io.IOException;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class Challenge12 {
	private static Logger log = Logger.getLogger(Challenge12.class);

	//First 2 were easy the third one was found googling after hacking the first 2
	//Then I found the triforce on the image but I hope there were more hints becouse this was not 
	//the kind of thing everyone knows, the hint is unhacked for me maybe in the future or maybe it was
	//just the info to tell us they were hashes we could crack
	public static void main(String[] args) throws InterruptedException {
		Challenge12 main = new Challenge12();
		try {
			main.process();
			//main.process();
		} catch (Exception e) {
			log.error("Exception:", e);
			Thread.sleep(600000);
		}
	}

//	private void magic() throws IOException {
//		File file = new File("D:\\eclipse\\workspace\\tuenti2\\extra\\CANTTF.png");
//		BufferedImage img = ImageIO.read(file);
//		int pixel;
//		Color col;
//		String text = "";
//		for (int i = 0; i < img.getWidth(); i++) {			
//			for (int y = 0; y < img.getHeight(); y++) {
//				pixel = img.getRGB(i, y);
//				col = new Color(pixel);
//				char c = (char)(col.getRed());
//				if ((c >= 'a' && c <= 'e') || c >= '0' && c <= '9')
//					text = text+c;
//			}
//			log.debug(text);
//			text = "";
//		}
//	}
	private void process() throws IOException {
		String key1 	= "a541714a17804ac281e6ddda5b707952"; //wisdom
		String pista 	= "a90365a5c53eb8a9d03b6a248d894c5a";
		String file 	= "482A9A4CD222FC2086C52698893E6B22";
		String filename = "c2e6babf6af5d7d87ad4253e2bdf696e";
		String key2 	= "ed8ce15da9b7b5e2ee70634cc235e363"; //courage
		String serenity = "dce7e0fb11c3cce0898d9529c9fac76b";
		String power	= "62cd275989e78ee56a81f0265a87562e"; //power
		
		Scanner in = new Scanner(System.in);
		String input = in.nextLine();
		String result = "";
		for (int i = 0; i < input.length();i++) {
			int digit = 0;
			digit = addChar(digit, input.charAt(i));
			digit = addChar(digit, key1.charAt(i));
			digit = addChar(digit, key2.charAt(i));
			digit = addChar(digit, power.charAt(i));
			digit = digit % 16;
			result += Integer.toHexString(digit);
		}
		System.out.println(result);
	}
	
	private int addChar (int start, char c) {
		int hv = Character.digit(c,16);
		return start+hv;
	}
	
}
