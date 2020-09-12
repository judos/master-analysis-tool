package model.events;

import model.Time;

/**
 * @since 25.03.2014
 * @author Julian Schelker
 */
public class EnergyEvent extends Event {
	protected int energyDiff;

	/**
	 * @return the energyDiff
	 */
	public int getEnergyDifference() {
		return energyDiff;
	}

	public EnergyEvent(String item, double time, int energy) {
		super(time, item);
		this.energyDiff = energy;
	}

	@Override
	public String toString() {
		return this.name + " taken, time=" + new Time(time).toMMSS() + " energy="
			+ energyDiff;
	}
}
