package model;

/**
 * @since 13.02.2014
 * @author Julian Schelker
 */
public class Time {

	protected double time;

	public Time(double time) {
		this.time = time;
	}

	public double getTime() {
		return this.time;
	}

	public String toMMSS() {
		int m = (int) Math.floor(this.time / 60);
		int s = (int) this.time % 60;

		if (s < 10)
			return m + ":0" + s;
		else
			return m + ":" + s;
	}

}
