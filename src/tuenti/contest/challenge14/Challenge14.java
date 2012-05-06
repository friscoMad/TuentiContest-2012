package tuenti.contest.challenge14;

import java.io.IOException;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class Challenge14 {
	private static Logger log = Logger.getLogger(Challenge14.class);

	public static void main(String[] args) throws InterruptedException {
		Challenge14 main = new Challenge14();
		try {
			main.process2();
		} catch (Exception e) {
			log.error("Exception:", e);
			Thread.sleep(600000);
		}
	}

	//This one was rather difficult for me, at the beginning I just skipped the clear Hamming hint
	//So I was just trying to find any sensible data in the stream I even tried with morse
	//And the incorrect lenght of test3 pulled me from seeing that the enconding was 7 bits
	private void process2() throws IOException {
		Scanner in = new Scanner(System.in);
		while (in.hasNextLine()) {
			String line = in.nextLine();
			if (line.length()%7 != 0) {
				log.debug("error");
				System.out.println("Error!");
			} else {
				processLine(line);
			}
		}
	}
	private void processLine(String line) {
		String result = "";
		int temp = 0;
		for (int i = 0; i < line.length(); i +=7) {
			int c = processWord(line.substring(i,i+7));
			if (i%2 == 0) {
				temp = c*16;
			} else {
				temp += c;
				if (temp < 32 || temp > 126) {
					log.debug("error");
					System.out.println("Error!");
					return;
				}
				result += (char)temp;
			}
		}
		log.debug(result);
		System.out.println(result);
	}

	private int processWord(String word) {
		boolean[] values = new boolean[7];
		for (int i = 0; i < 7; i++) {
			values[i] = (word.charAt(i) == '1');
		}
		int error;
		if ((error =hamming74(values)) != 0) {
			values[error-1] = !values[error-1];
		}
		return decode74(values);
	}
	
	private int decode74(boolean d[]) {
		String result = ""+(d[2]?'1':'0') + (d[4]?'1':'0') +(d[5]?'1':'0') +(d[6]?'1':'0');
		return Integer.parseInt(result,2);
	}
	
	//Also the last problem was that I misplaced h2 with h0 so the results were incorrect
	//At least the first letters appeared Just*..garbage so I could find the error and fix it  
	private int hamming74 (boolean d[]) {
		boolean h[]=new boolean[3];
		// Calculate the checksum this row should have
		h[0]=d[0]^d[2]^d[4]^d[6];
		h[1]=d[1]^d[2]^d[5]^d[6];
		h[2]=d[3]^d[4]^d[5]^d[6];
		// Compare these with the actual bits
		String result = ""+(h[2]?'1':'0') + (h[1]?'1':'0') +(h[0]?'1':'0');
		return Integer.parseInt(result,2);
	}

	//Sample image data
	private void process() throws IOException {
		String line1 	= "00001010001101100011011001011101110";
		String line2 	= "01110001101110111110001000100010000";
		String line3 	= "10001101000100010011011101111100111";
		String line4 	= "00100101100010010011101010001101000";
		processLine(line1+line2+line3+line4);
	}
}
