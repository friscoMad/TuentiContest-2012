package tuenti.contest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class Challenge4 {
	private static Logger log = Logger.getLogger(Challenge4.class);

	
	//Private class used for storing race result status
	private class Result {
		private GroupConfig groups;
		private int gas;
		public GroupConfig getGroups() {
			return groups;
		}
		public int getGas() {
			return gas;
		}
		public Result(GroupConfig groups, int gas) {
			super();
			this.groups = groups;
			this.gas = gas;
		}
		public boolean equals (Object result) {
			if (result instanceof Result) {
				return this.getGroups().equals(((Result)result).getGroups());
			} else {
				return false;
			}
		}		
	}
	
	
	//Class for storing group configuration and to be able to hash group configs
	private class GroupConfig {
		private LinkedList<Integer> groups;
		private String hash;
		public LinkedList<Integer> getGroups() {
			return this.groups;
		}
		public String getHash() {
			return this.hash;
		}
		public GroupConfig (LinkedList<Integer> groups) {
			this.groups = groups;
			this.hash = "";
			for (Integer group : groups) {
				this.hash += group + " ";
			}
		}
		public boolean equals (Object config) {
			if (config instanceof GroupConfig) {
				return this.getHash().equals(((GroupConfig)config).getHash());
			} else {
				return false;
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Challenge4 main = new Challenge4();
		main.process();

	}

	public void process() {
		Scanner in = new Scanner(System.in);
		int problems = in.nextInt();
		for (int i = 0; i < problems; i++) {
			int races = in.nextInt();
			int karts = in.nextInt();
			int groupNumber = in.nextInt();
			LinkedList<Integer> groups = new LinkedList<Integer>();
			for (int j = 0; j < groupNumber; j++) {
				groups.addLast(in.nextInt());
			}
			int gas = findGas(races, karts, new GroupConfig(groups));
			log.debug("Gas:" + gas);
			System.out.println(gas);
		}


	}

	private int findGas(int races, int karts, GroupConfig groups) {
		log.debug("Races: "+races + " Karts: "+ karts);
		log.debug("Groups:" + groups.getHash());
		//Initialize loop detector
		ArrayList<Result> history = new ArrayList<Result>(); 
		int gas = 0;
		Result currentRes = new Result(groups, 0);
		
		//Loop for every race
		for (int i = races; i > 0; i--) {
			//Try to find loop with previous group configs
			if (history.indexOf(currentRes) != -1) {
				//Loop found
				int startLoop = history.indexOf(currentRes);
				int loopLength = history.size() - startLoop;
				//If looplenght less than the remainder races then take shortcut
				if (loopLength < i) {
					int loopGas = 0;
					//Find loop gas
					for (int j = startLoop; j < history.size(); j++) {
						loopGas += history.get(j).getGas();
					}
					gas += loopGas * (i / loopLength);
					//Calc the number of loops, the gas used in those loops and the remainder races after
					//those loops
					i = i % loopLength;
					if (i == 0) {
						return gas;
					}
				}
			} 
			//If we have no other method just calculate it
			Result res = getRaceGas(karts, currentRes.getGroups());
			//Store starting config and gas result for loop detection
			history.add(new Result(currentRes.getGroups(), res.gas));
			gas += res.getGas();
			currentRes = res;
		}
		return gas;
	}
	
	@SuppressWarnings("unchecked")
	private Result getRaceGas(int karts, GroupConfig groups) {
		//Otherwise just do it
		int gas = 0;
		int steps = 0;
		//Clone group config avoiding changing previous results
		LinkedList<Integer> groupClone = (LinkedList<Integer>)groups.groups.clone();
		//Limit max number of iterations
		int limit = groupClone.size();
		while (steps < limit) {
			//If the next group is too big avoid poping it
			if (groupClone.peek() > karts) {
				break;
			}
			//Cycle the group
			int group = groupClone.removeFirst();
			groupClone.addLast(group);
			//Update stats
			karts -= group;
			gas += group;
			steps++;
		}		
		Result result = new Result(new GroupConfig(groupClone), gas);
		//Store result
		return result;
	}
}
