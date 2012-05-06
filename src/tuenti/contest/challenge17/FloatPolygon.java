package tuenti.contest.challenge17;

//Regular polygon utility class
//Given a center, vertex position, and number of sides it can return the enclosing radius
// and the min and max tethas of all the vertex coordinates in polar system
public class FloatPolygon {
	private int sides;
	private FloatPoint center;
	private FloatPoint vertex;
	private Double enclosingRadius = null;
	private Double vertexAngle = null;
	private Double minTetha = null;
	private Double maxTetha = null;
	
	public int getSides() {
		return sides;
	}
	public FloatPoint getCenter() {
		return center;
	}
	public FloatPoint getVertex() {
		return vertex;
	}
	public FloatPolygon(int sides, FloatPoint center, FloatPoint vertex) {
		super();
		this.sides = sides;
		this.center = center;
		this.vertex = vertex;
		double vertexX = vertex.getX()-center.getX();
		double vertexY = vertex.getY()-center.getY();
		enclosingRadius = Math.abs(Math.sqrt((vertexX*vertexX) + (vertexY*vertexY)));
		FloatPoint vertex2 = new FloatPoint(this.vertex.getX()-center.getX(), this.vertex.getY()-center.getY());
		this.vertexAngle = vertex2.getTheta();
		findMinMaxTetha();
	}
	
	public double getMinTetha() {
		return this.minTetha;
	}
	
	public double getMaxTetha() {
		return this.maxTetha;
	}
	public double getEnclosingRadius() {
		return this.enclosingRadius;
	}
	private void findMinMaxTetha() {
		this.minTetha = 2*Math.PI;
		this.maxTetha = -2*Math.PI;
		FloatPoint point;
		for (int i = 0; i < this.sides; i++) {
			double vertexX = this.center.getX() + this.enclosingRadius * Math.cos(2 * Math.PI * i / this.sides + this.vertexAngle);
			double vertexY = this.center.getY() + this.enclosingRadius * Math.sin(2 * Math.PI * i / this.sides + this.vertexAngle);
			point = new FloatPoint(vertexX, vertexY);
			if (point.getTheta() < this.minTetha) {
				this.minTetha = point.getTheta();
			}
			if (point.getTheta() > this.maxTetha) {
				this.maxTetha = point.getTheta();
			}
		}
	}
}
