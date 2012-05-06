package tuenti.contest;

import java.math.BigInteger;

import org.apache.log4j.Logger;

import tuenti.contest.commons.TuentiScanner;

public class Challenge2 {
	private static Logger log = Logger.getLogger(Challenge2.class);


	public static void main(String[] args) {
		Challenge2 main = new Challenge2();
		main.process();
	}

	public void process() {
		TuentiScanner in = new TuentiScanner(System.in);
		int lines = in.nextInt();
		in.nextLine();
		for (int i = 0; i < lines; i++) {
			BigInteger number = in.nextBigInt();
			int ones = findOnes(number);
			log.debug(number + ":" + ones);
			System.out.println("Case #" + (i+1) + ": " + findOnes(number));
		}
	}

	private int findOnes(BigInteger number) {
		int length = number.bitLength() -1;
		log.debug("length: "+ length);
		BigInteger rest = number.subtract(fullOnes(length));
		log.debug(rest);
		return rest.bitCount() + length;
	}

	private BigInteger fullOnes(int length) {
		BigInteger result = BigInteger.ZERO;
		for (int i = 0; i < length; i++) {
			result = result.shiftLeft(1);
			result = result.add(BigInteger.ONE);
		}
		return result;
	}
}
