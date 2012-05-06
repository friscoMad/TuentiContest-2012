package tuenti.contest.challenge5;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class Challenge5 {

	private static Logger log = Logger.getLogger(Challenge5.class);
	
	public static void main(String[] args) {
		Challenge5 main = new Challenge5();
		try {
			main.process();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public void process() throws ParseException {
		Scanner in = new Scanner(System.in);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		while (in.hasNextLine()) { 
			String line = in.nextLine();
			String[] dates = line.split(" - ");
			Date start = format.parse(dates[0]);
			Date end = format.parse(dates[1]);
			log.debug("Start: " + start + " End: " + end);
			long diff = findDifferences(start, end);
			log.debug("Resultado: "+ diff);
			System.out.println(diff);
		}
	}

	//Using Rosapolis version of challenge 6-13
	//Just ported to java and fixed a bug with day leds in challenge 13
	private long findDifferences(Date start, Date end) {
		OldClock clock1 = new OldClock();
		NewClock clock2 = new NewClock();
		long ledsOld = clock1.getLeds(start, end);
		long ledsNew = clock2.getLeds(start, end);
		log.debug("Viejo: " + ledsOld);
		log.debug("Nuevo: " + ledsNew);
		return ledsOld - ledsNew;
	}
}
