package tuenti.contest;

import java.math.BigInteger;

import tuenti.contest.commons.TuentiScanner;

public class Test {
	public static void main(String[] args) {
       TuentiScanner in = new TuentiScanner(System.in);
		while (in.hasNextLine()) {
			String line = in.nextLine();
			TuentiScanner lineIn = new TuentiScanner(line);
			BigInteger total = BigInteger.ZERO;
			while (lineIn.hasNextBigInt()) {
				BigInteger number = lineIn.nextBigInt();
				total = total.add(number);
			}
			lineIn.close();
			System.out.println(total);
		}
		in.close();
	}
}
