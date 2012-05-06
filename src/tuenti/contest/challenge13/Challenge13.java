package tuenti.contest.challenge13;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class Challenge13 {
	private static Logger log = Logger.getLogger(Challenge13.class);

	public static void main(String[] args) throws InterruptedException {
		Challenge13 main = new Challenge13();
		try {
			main.process();
		} catch (Exception e) {
			log.error("Exception:", e);
			Thread.sleep(600000);
		}
	}

	private void process() throws IOException {
		Scanner in = new Scanner(System.in);
		int problems = in.nextInt();
		for (int i = 0; i < problems; i++) {
			in.nextLine();
			int total = in.nextInt();
			int split = in.nextInt();
			log.debug("total: " + total + " split: " + split);
			long solution = 0;
			solution = resolve(total, split);
			log.debug("solution: " + solution);
			System.out.println("Case #" + (i+1) + ": " + solution);
		}
	}

	//Find all loops sizes in the requested configuration
	//Then just calc LCM between them
	private long resolve(int total, int split) {
		int rest = total - split;
		int tail = Math.abs(rest - split);
		int width = Math.min(rest, split);
		ArrayList<Integer> loopSizes = new ArrayList<Integer>();
		//Avoid revisiting loops
		//Take advantage of the false initialization of bools to avoid an extra loop of N
		boolean[] visited = new boolean[total+1];
		for (int i = 1; i <= total; i++) {
			if (visited[i]) {
				continue;
			}
			int loop = 0;
			int pos = i;
			do {
				visited[pos] = true;
				pos = findEndPostion(pos, total, split, rest, tail, width);
				loop++;
			} while (!visited[pos]);
			if (loop != 1) {
				loopSizes.add(loop);
			}
		}
		long result = loopSizes.get(0);
		if (loopSizes.size() > 1) {
			for (int i = 1; i < loopSizes.size(); i++) {
				int loop = loopSizes.get(i);
				if (loop == result) {
					continue;
				}
				result = LCM(result, loop);
			}
		}
		return result;
	}
	
	
	//Typical math functions (still unsure why they are not implemented in Math library)
	public long GCD(long a, long b)
	{
	   if (b==0) return a;
	   return GCD(b,a%b);
	}	

	public long LCM(long a, long b)
	{
	   return b*a/GCD(a,b);
	}
	
	
	//Avoid any extra calculation during the loops only ifs and adds or subtracts
	private int findEndPostion(int i, int total, int split, int rest, int tail,
			int width) {
		if (i <= split) {
			if (split > rest) {
				if (i <= tail) {
					return total - i+1;
				}
				i = i - tail;
			}
			return (width - i) * 2+1;
		} else {
			i = i - split;
			if (split < rest) {
				if (i <= tail) {
					return total - i+1;
				}
				i = i - tail;
			}
			return ((width - i) * 2) + 2;
		}
	}

}
