package model.events;

import java.util.ArrayList;

import model.Vec3;

/**
 * @since 26.03.2014
 * @author Julian Schelker
 */
public class FallAnalyzer {
	private ArrayList<FallEvent> fall;
	private boolean falling;
	private Vec3 last;
	private float originZ;
	private ArrayList<Float> intValues;

	public FallAnalyzer() {
		this.fall = new ArrayList<FallEvent>();
		this.falling = false;
		this.intValues = new ArrayList<Float>();
	}

	public void feed(Vec3 pos, double time) {
		if (this.last == null)
			this.last = pos;
		float diffZ = this.last.getZ() - pos.getZ();
		this.last = pos;
		if (!this.falling && diffZ >= 0.5) {
			this.originZ = this.last.getZ();
			this.falling = true;
		}
		float totalFallDistance = this.originZ - pos.getZ();
		if (this.falling && totalFallDistance >= 1 && diffZ < 0.5) {
			this.falling = false;
			if (totalFallDistance >= 1)
				this.fall.add(new FallEvent(time, 0, totalFallDistance));
		}
		// find heightDifference of at least 2m
		// start checking for differences of 1m
		// points need to be consequent
		// find end time of fall
	}

	public ArrayList<FallEvent> getFallEvents() {
		return this.fall;
	}
}
