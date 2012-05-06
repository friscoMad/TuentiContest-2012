package tuenti.contest.challenge16;

import java.util.Scanner;

import org.apache.log4j.Logger;

public class Challenge16 {

	private static Logger log = Logger.getLogger(Challenge16.class);

	public static void main(String[] args) throws InterruptedException {
		Challenge16 main = new Challenge16();
		try {
			main.process();
		} catch (Exception e) {
			log.error("Exception:", e);
			Thread.sleep(600000);
		}
	}

	double[] safe;
	double[] malware;
	double safeT = 0;
	double malwareT = 0;

	// Here I used a naive bayesian filter 
	// Based on http://bionicspirit.com/blog/2012/02/09/howto-build-naive-bayes-classifier.html
	// I read about it on Barrapunto some time ago and seems to be the perfect time to implement it.
	private void process() {
		Scanner in = new Scanner(System.in);
		int training = in.nextInt();
		in.nextLine();
		int test = in.nextInt();
		in.nextLine();
		int functions = in.nextInt();
		this.safe = new double[functions];
		this.malware = new double[functions];
		in.nextLine();
		int total = 0;
		train(in, functions, training);
		preprocess();
		for (int i = 0; i < test; i++) {
			int[] values = getValues(in, functions);
			if (isMalware(values)) {
				for (int j : values) {
					total += j;
				}
			}
		}
		System.out.println(total);
	}

	//This uses the logaritmic implementation to avoid underflows
	private boolean isMalware(int[] values) {
		double probSafe = 0;
		double probMalware = 0;
		for (int i = 0; i < values.length; i++) {
			probSafe += safe[i]*values[i];
			probMalware += malware[i]*values[i];
		}
		probSafe += safeT;
		probMalware += malwareT;
		return (probMalware > probSafe);
	}
	
	//This was added by me as in this case there was no need to continue training after the 
	//first batch so everything goes faster if all the probabilities are preprocessed 
	private void preprocess() {
		for (int i = 0; i < safe.length; i++) {
			double total = safe[i] + malware[i];
			safe[i] = Math.log(safe[i]/total);
			malware[i] = Math.log(malware[i]/total);
		}
		double total = safeT + malwareT;
		safeT = Math.log(safeT/total);
		malwareT = Math.log(malwareT/total);
	}

	private void train(Scanner in, int functions, int testCases) {

		for (int i = 0; i < testCases; i++) {
			String type = in.next("S|M");
			if (type.equals("S")) {
				this.safeT++;
			} else {
				this.malwareT++;
			}
			for (int j = 0; j < functions; j++) {
				if (type.equals("S")) {
					this.safe[j] += in.nextInt();
				} else {
					this.malware[j] += in.nextInt();
				}
			}
		}
	}

	private int[] getValues(Scanner in, int numValues) {
		int[] result = new int[numValues];
		for (int i = 0; i < numValues; i++) {
			result[i] = in.nextInt();
		}
		return result;
	}
}
