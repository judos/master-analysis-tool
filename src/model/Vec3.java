package model;

/**
 * @since 13.02.2014
 * @author Julian Schelker
 */
public class Vec3 {

	private double x;
	private double y;
	private double z;

	public Vec3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * @param str
	 *            split by <b>/</b><br>
	 *            e.g. <b>25.1/10/-5.1</b>
	 */
	public Vec3(String str) {
		String[] parts = str.split("/");
		this.x = Float.valueOf(parts[0]);
		this.y = Float.valueOf(parts[1]);
		this.z = Float.valueOf(parts[2]);
	}

	public float getX() {
		return (float) this.x;
	}

	public float getY() {
		return (float) this.y;
	}

	public float getZ() {
		return (float) this.z;
	}

	@Override
	public String toString() {
		return this.x + "/" + this.y + "/" + this.z;
	}

	public Vec3 add(Vec3 vec) {
		return new Vec3(this.x + vec.x, this.y + vec.y, this.z + vec.z);
	}

	public Vec3 subtract(Vec3 vec) {
		return new Vec3(this.x - vec.x, this.y - vec.y, this.z - vec.z);
	}

	public Vec3 scale(double factor) {
		return new Vec3(this.x * factor, this.y * factor, this.z * factor);
	}

	public float distanceTo(Vec3 p) {
		return (float) Math.sqrt(p2(this.x - p.x) + p2(this.y - p.y) + p2(this.z - p.z));
	}

	public static double p2(double nr) {
		return Math.pow(nr, 2);
	}

}