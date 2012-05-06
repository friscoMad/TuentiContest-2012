package tuenti.contest;

import java.awt.Point;
import java.util.Scanner;

public class Challenge3 {

	private class StockRange {
		private Point min;
		private Point max;
		public Point getMin() {
			return min;
		}
		public void setMin(Point min) {
			this.min = min;
		}
		public Point getMax() {
			return max;
		}
		public void setMax(Point max) {
			this.max = max;
		}
		public int value() {
			return max.y - min.y;
		}
		public String toString() {
			return (min.x*100) + " " +(max.x*100) + " " + this.value();
		}
		public StockRange(int minTime, int minValue, int maxTime, int maxValue) {
			min = new Point(minTime, minValue);
			max = new Point(maxTime, maxValue);
		}
		public StockRange(Point min, Point max) {
			this.min = min;
			this.max = max;
		}
	}
	public static void main(String[] args) {
		Challenge3 main = new Challenge3();
		main.process();
	}

	public void process() {
		Scanner in = new Scanner(System.in);
		int first = in.nextInt();
		StockRange currentBest = new StockRange(0, first, 0, first);
		Point lastMin = new Point(0, first);
		Point lastMax = new Point(0, first);
		int lastValue = first;
		boolean descendingTrend = true;
		int time = 0;
		while(in.hasNextInt()) {
			int number = in.nextInt();
			if (descendingTrend && number > lastValue) {
				if (lastValue < lastMin.y) {
					lastMin = new Point(time, lastValue);
					lastMax = lastMin;
				}
				descendingTrend = false;
			} else if (!descendingTrend && number < lastValue) {
				if (lastValue > lastMax.y) {
					lastMax = new Point(time, lastValue);
					if (currentBest.value() < lastMax.y - lastMin.y) {
						currentBest = new StockRange(lastMin, lastMax);
					}
				}
				descendingTrend = true;
			}
			time++;
			lastValue = number;
		}
		lastMax = new Point(time, lastValue);
		if (currentBest.value() < lastMax.y - lastMin.y) {
			currentBest = new StockRange(lastMin, lastMax);
		}
		System.out.println(currentBest);
	}

}
