package tuenti.contest.challenge5;

import java.util.Date;

public class OldClock {

	public long getLeds (Date start, Date end) {
		int complete = 10 * chg(0, 5) + 6 * chg(0, 9);
		int day = 10 * chg(0, 1) + 4 * Segmentos[2] + 2 * chg(0, 9)
				+ chg(0, 3);
		
		long seconds = (end.getTime() - start.getTime())/1000;
		long m = seconds/60;
		long h = m/60;
		long d = h/24;

		// count the on/off changes for seconds
		long secondsLeds = m*complete;
		long rest = seconds % 60;
		for (int i = 0; i <= rest; i++) {
			secondsLeds += Segmentos[i/10] + Segmentos[i%10];
		}

		//# count the on/off changes for minutes
		long minutesLeds = h*complete*60;
		int rm = (new Long(m % 60)).intValue();
		for (int i = 0; i < rm; i++) {
			minutesLeds += (Segmentos[i/10] + Segmentos[i%10])*60;
		}

		//# last minute was repeated (rs + 1) times instead of 60 (it wasn't completed)
		minutesLeds += (Segmentos[rm/10] + Segmentos[rm % 10])*(rest+1);


		//# count the on/off changes for hours
		long hoursLeds = d*day*3600;
		int rh = (new Long(h % 24)).intValue();
		for (int i = 0; i < rh; i++) {
			hoursLeds += (Segmentos[i/10] + Segmentos[i%10])*3600;
		}

		//# last hour was repeated s % 3600 + 1 times instead of 3600
		hoursLeds += (Segmentos[rh/10] + Segmentos[rh % 10])*(seconds % 3600 + 1);
		return hoursLeds + minutesLeds + secondsLeds;
	}

	private static final int[] Segmentos = new int[] { 6, 2, 5, 5, 4, 5, 6, 3,
			7, 6 };

	private int chg(int start, int end) {
		if (end < start) {
			return 0;
		}
		int leds = 0;
		for (int i = start; i <= end; i++) {
			leds += Segmentos[i];
		}
		return leds;
	}
}
