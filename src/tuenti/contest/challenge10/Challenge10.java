package tuenti.contest.challenge10;

import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

import org.apache.log4j.Logger;

public class Challenge10 {
	private static Logger log = Logger.getLogger(Challenge10.class);

	public static void main(String[] args) throws InterruptedException {
		Challenge10 main = new Challenge10();
		try {
			main.process();
		} catch (Exception e) {
			log.error("Exception:",e );
			Thread.sleep(600000);
		}
	}

	private void process() throws IOException {
		Scanner in = new Scanner(System.in);
		while (in.hasNextLine()) {
			String line = in.nextLine();
			log.debug(line);
			Long lineResult = processLine(line);
			log.debug("result: " + lineResult);
			System.out.println(lineResult);
		}
		
	}

	//Simple scan and stack operators and operations
	private Long processLine(String line) {
		Stack <Long> stack = new Stack<Long>();
		Scanner scan = new Scanner(line);
		while (true) {
			if (scan.hasNextLong()) {
				stack.push(scan.nextLong());
			} else {
				String operator = scan.next("(\\w+)|\\.|#|&|@|\\$");
				if (operator.equals(".")) {
					return stack.pop();
				} else {
					processOperator(stack, operator);
				}
			}
		}
	}

	//Handle operations over the stack
	private void processOperator(Stack<Long> stack, String operator) {
		if (operator.equals("$")) {
			Long x = stack.pop();
			stack.push(stack.pop() - x);
		} else if (operator.equals("@")) {
			Long x = stack.pop();
			stack.push(stack.pop() + x); 
		} else if (operator.equals("&")) {
			Long x = stack.pop();
			stack.push(stack.pop() / x); 
		} else if (operator.equals("@")) {
			Long x = stack.pop();
			stack.push(stack.pop() + x); 
		} else if (operator.equals("#")) {
			Long x = stack.pop();
			stack.push(stack.pop() * x); 
		} else if (operator.equals("mirror")) {
			stack.push(stack.pop() * -1); 
		} else if (operator.equals("breadandfish")) {
			stack.push(stack.peek()); 
		} else if (operator.equals("fire")) {
			stack.pop(); 
		} else if (operator.equals("dance")) {
			Long x = stack.pop();
			Long y = stack.pop();
			stack.push(x);
			stack.push(y);
//This one was the last one I found but it should have been easier
//than dance and breadandfish
		} else if (operator.equals("conquer")) {
			Long x = stack.pop();
			stack.push(stack.pop() % x); 
		}
	}

}
