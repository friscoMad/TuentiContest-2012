package tuenti.contest.challenge18;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.math.BigInteger;

import org.apache.log4j.Logger;

import tuenti.contest.commons.TuentiScanner;

public class Challenge18 {
	private static Logger log = Logger.getLogger(Challenge18.class);

	//Bee transcoded to BF then run with Brainfuck Developer
	public static void main(String[] args) throws InterruptedException {
		Challenge18 main = new Challenge18();
		try {
//			main.beeToBF(
//							"D:\\eclipse\\workspace\\tuenti2\\extra\\program.bee",
//							"D:\\eclipse\\workspace\\tuenti2\\src\\tuenti\\contest\\challenge18\\program.bf");
			main.process();
		} catch (Exception e) {
			log.error("Exception:", e);
			Thread.sleep(600000);
		}
	}

	/*
	 * Input is the number of straight cuts made through a round chocolate cake
	 * and output is the maximum number of cake pieces that can be produced.
	 */
	private void process() {
		TuentiScanner in = new TuentiScanner(System.in);
		int cases = in.nextInt();
		for (int i = 0; i < cases; i++) {
			in.nextLine();
			BigInteger cuts = in.nextBigInt();
			log.debug(cuts);
			BigInteger result = cutNumber(cuts);
			log.debug("Result: " + result);
			System.out.println("Case #"+(i+1)+": "+result);
		}
	}
	
	//Simple function that returns the maximun number of pieces for a given number of cuts
	//Using BigInteger as it seems the submit will overflow
	private BigInteger cutNumber(BigInteger cuts) {
		BigInteger square = cuts.multiply(cuts);
		BigInteger result = square.add(cuts).add(BigInteger.valueOf(2l)).divide(BigInteger.valueOf(2l));
		return result;
	}

	private void beeToBF(String pathInput, String pathOutput)
			throws IOException {
		FileReader fileIn = new FileReader(pathInput);
		StreamTokenizer in = new StreamTokenizer(fileIn);
		in.ordinaryChar('.');
		in.ordinaryChar('+');
		in.ordinaryChar('-');
		FileWriter fileOut = new FileWriter(pathOutput);
		BufferedWriter out = new BufferedWriter(fileOut);

		System.out.println("Converting from Bee! to BrainF***");
		in.pushBack();
		int tok;
		int count = 0;
		String code = "";
		boolean beeExpected = true;
		while ((tok = in.nextToken()) != StreamTokenizer.TT_EOF) {
			if (beeExpected) {
				if (tok != StreamTokenizer.TT_WORD || !in.sval.equals("Bee"))
					throw new IOException("Syntax error in Bee! input.");
				beeExpected = false;
			} else {
				code += (char) tok;
				if (code.length() == 2) {
					if (code.equals(".?"))
						out.write(">");
					else if (code.equals("?."))
						out.write("<");
					else if (code.equals(".."))
						out.write("+");
					else if (code.equals("!!"))
						out.write("-");
					else if (code.equals(".!"))
						out.write(",");
					else if (code.equals("!."))
						out.write(".");
					else if (code.equals("!?"))
						out.write("[");
					else if (code.equals("?!"))
						out.write("]");
					else
						throw new IOException("Syntax error in Bee! input.");
					code = "";
					count += 2;
					if (count >= 72) {
						out.newLine();
						count = 0;
					}
				}
				beeExpected = true;
			}
		}
		fileIn.close();
		out.close();
	}

}
