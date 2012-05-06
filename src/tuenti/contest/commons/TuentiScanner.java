package tuenti.contest.commons;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TuentiScanner {
	
	protected Scanner scan; 
	
	public TuentiScanner(String input) {
		this.scan = new Scanner(input);
	}

	public TuentiScanner(InputStream input) {
		this.scan = new Scanner(input);
	}
	
	public boolean hasNextLine() {
		return this.scan.hasNextLine();
	}

	public boolean hasNext(String arg0) {
		return this.scan.hasNext(arg0);
	}

	public boolean hasNextBigInt() {
		return this.scan.hasNext("[+-]{0,1}\\d+");
	}
	
	public String nextLine() {
		return this.scan.nextLine();
	}
	
	public int nextInt() {
		return this.scan.nextInt();
	}
	
	public char nextCharacter() {
		return (char)this.scan.nextByte();
	}	

	public String next(String arg0) {
		return this.scan.next(arg0);
	}

	public BigInteger nextBigInt() {
		String sNumber = this.scan.next("[+-]{0,1}\\d+").trim();
		if (sNumber.startsWith("+")) {
			sNumber = sNumber.substring(1);
		}
		return new BigInteger(sNumber);
	}
	
	public void close() {
		this.scan.close();
	}
	
	public void useDelimiter(String arg0) {
		this.scan.useDelimiter(arg0);
	}
	
}
