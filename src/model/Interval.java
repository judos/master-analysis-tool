package model;

/**
 * @since 19.05.2014
 * @author Julian Schelker
 */
public class Interval {

	private float min;
	private float max;

	/**
	 * @return the min
	 */
	public float getMin() {
		return min;
	}

	/**
	 * @return the max
	 */
	public float getMax() {
		return max;
	}

	public Interval(float min, float max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public String toString() {
		return "Intervall: [" + this.min + ", " + this.max + "]";
	}

}
