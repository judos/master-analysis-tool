
package model;

/**
 * @since 19.05.2014
 * @author Julian Schelker
 */
public class BoundingBoxIntervalMerger implements IntervalProviderI {

	private boolean minSet;
	private boolean maxSet;
	private float min;
	private float max;

	public BoundingBoxIntervalMerger(IntervalProviderI... prov) {
		this.minSet = false;
		this.maxSet = false;
		if (prov != null)
			for (IntervalProviderI p : prov)
				addData(p);
	}

	public void addData(IntervalProviderI p) {
		addData(p.getInterval());
	}

	public void addData(Interval i) {
		if (Float.isNaN(i.getMin()) || Float.isInfinite(i.getMin()))
			throw new RuntimeException("minimum of intervall is: " + i.getMin());
		if (Float.isNaN(i.getMax()) || Float.isInfinite(i.getMax()))
			throw new RuntimeException("max of intervall is: " + i.getMax());
		if (!minSet) {
			this.min = i.getMin();
			this.minSet = true;
		}
		else
			this.min = Math.min(this.min, i.getMin());

		if (!maxSet) {
			this.max = i.getMax();
			this.maxSet = true;
		}
		else
			this.max = Math.max(this.max, i.getMax());
	}

	@Override
	public Interval getInterval() {
		return new Interval(this.min, this.max);
	}

	@Override
	public String toString() {
		String r = "BBIntervalMerger (";
		if (this.minSet)
			r += "min: " + this.min;
		if (this.maxSet) {
			if (this.minSet)
				r += ", ";
			r += "max: " + this.max;
		}
		return r += ")";
	}
}
