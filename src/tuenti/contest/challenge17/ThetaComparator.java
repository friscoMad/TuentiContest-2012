package tuenti.contest.challenge17;

import java.util.Comparator;

public class ThetaComparator implements Comparator<FloatPolygon> {
	public int compare(FloatPolygon p1, FloatPolygon p2) {
		return ((int)Math.ceil(p2.getCenter().getTheta() - p1.getCenter().getTheta()));
	}
}
