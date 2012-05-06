package tuenti.contest.challenge17;

//Utility class to manage coordinates in double (java Point only allows ints)
//and managing rectangular to polar conversion
public class FloatPoint {
	private double x;
	private double y;
	private double theta;
	private double r;
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public FloatPoint(double x, double y) {
		super();
		this.x = x;
		this.y = y;
		convertToPolar();
	}
	public double getTheta() {
		return theta;
	}
	public double getR() {
		return r;
	}
	private void convertToPolar() {
		theta = Math.atan2(this.y, this.x);
		r = Math.sqrt((this.x*this.x) + (this.y*this.y));
	}
}
