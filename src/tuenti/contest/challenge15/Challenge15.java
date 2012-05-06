package tuenti.contest.challenge15;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class Challenge15 {

	private static Logger log = Logger.getLogger(Challenge15.class);

	public static void main(String[] args) throws InterruptedException {
		Challenge15 main = new Challenge15();
		try {
			main.process();
		} catch (Exception e) {
			log.error("Exception:", e);
			Thread.sleep(600000);
		}
	}

	//I just used the same code as the past year, I could use beautier Rosapolis code
	//but I think it was time from some more of my own code
	private void process() {
		ArrayList<Integer> primes = new ArrayList<Integer>();
		int total = 0;
		for (int i = 3; true; i = i + 2) {
			boolean prime = true;
			boolean emirp = true;
			String reverse = new StringBuffer(i + "").reverse().toString();
			int reversei = Integer.parseInt(reverse);
			if (i == reversei || reversei % 2 == 0) {
				emirp = false;
			}
			for (Integer value : primes) {
				if (value > Math.sqrt(i) && value > Math.sqrt(reversei)) {
					break;
				}
				if (i % value == 0) {
					prime = false;
					break;
				}
				if (reversei % value == 0) {
					emirp = false;
					break;
				}

			}
			if (prime) {
				primes.add(new Integer(i));
				if (emirp) {
					log.debug(i);
					total++;
					if (total == 20) {
						System.out.println(i);
						return;
					}
				}
			}
		}
	}
}
