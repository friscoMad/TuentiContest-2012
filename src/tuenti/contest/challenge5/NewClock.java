package tuenti.contest.challenge5;

import java.util.Date;

public class NewClock {

	public long getLeds (Date start, Date end) {
		int complete = chg05(0,5) + 6*chg(0,9);
		//Fixed bug in day led count
		int day = chg05(0,2) + 2*chg(0,9) + chg05(0,3);		
		long s = (end.getTime() - start.getTime())/1000;
		long m = s/60;
		long h = m/60;
		long d = h/24;

		//# count the on/off changes for seconds
		long seconds = m*complete;
		int rs = new Long(s % 60).intValue();

		seconds += chg05(0,rs/10) + (rs/10)*chg(0,9) + chg(0,rs%10);


		//# count the on/off changes for minutes
		long minutes = h*complete;
		int rm = new Long(m % 60).intValue();
		minutes += chg05(0,rm/10) + (rm/10)*chg(0,9) + chg(0,rm%10);

		//# count the on/off changes for hours
		long hours = d*day;
		int rh = new Long(h % 24).intValue();
		hours += chg05(0,rh/10) + (rh/10)*chg(0,9) + chg(0,rh%10);		
		return hours + minutes + seconds + 27;
	}
	
	private final int[] leds = new int[] {1,0,4,1,1,2,1,1,4,0};
	private final int[] leds05 = new int[] {2,0,4,1,1,2};

	private int chg(int start, int end) {
		if (end < start) {
			return 0;
		}
		int leds = 0;
		for (int i = start; i <= end; i++) {
			leds += this.leds[i];
		}
		return leds;
	}
	private int chg05(int start, int end) {
		if (end < start) {
			return 0;
		}
		int leds = 0;
		for (int i = start; i <= end; i++) {
			leds += this.leds05[i];
		}
		return leds;
	}
	        
}
