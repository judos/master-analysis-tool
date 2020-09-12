package model.events;

import model.Time;

/**
 * @since 26.03.2014
 * @author Julian Schelker
 */
public class FallEvent extends EnergyEvent {

	protected float heightFallen;

	public FallEvent(double time, int energyDiff, float heightFallen) {
		super("FallEvent", time, energyDiff);
		this.heightFallen = heightFallen;
	}

	@Override
	public String toString() {
		Time t = new Time(time);
		return "FallEvent height: " + this.heightFallen + ", time: " + t.toMMSS();
	}

}
