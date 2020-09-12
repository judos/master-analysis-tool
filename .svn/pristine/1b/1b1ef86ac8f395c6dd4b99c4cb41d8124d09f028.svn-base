package model.events;

import javares.data.RandomJS;
import model.game.EmissaireLog;

/**
 * @since 31.03.2014
 * @author Julian Schelker
 */
public class EventFacts {

	private float[] shadowDistances;

	/**
	 * @return the shadowDistances
	 */
	public float[] getShadowDistances() {
		return shadowDistances;
	}

	/**
	 * @return the movementEmissaire
	 */
	public float getMovementEmissaire() {
		return movementEmissaire;
	}

	/**
	 * @return the findEvent
	 */
	public FindEvent getFindEvent() {
		return findEvent;
	}

	/**
	 * @return the deltaZ
	 */
	public float getDeltaZ() {
		return deltaZ;
	}

	private float movementEmissaire;
	private FindEvent findEvent;
	private float deltaZ;
	private float time;

	/**
	 * @return the time
	 */
	public float getTime() {
		return time;
	}

	/**
	 * @return the lifeDiff
	 */
	public int getLifeDiff() {
		return lifeDiff;
	}

	private int lifeDiff;
	private String[] enemies;

	public EventFacts(float[] shadowDistances, float movementEmissaire, float deltaZ,
		FindEvent f, float time, int lifeDiff, String[] entitiesWithoutPlayer) {
		this.shadowDistances = shadowDistances;
		this.movementEmissaire = movementEmissaire;
		this.deltaZ = deltaZ;
		this.findEvent = f;
		this.time = time;
		this.lifeDiff = lifeDiff;
		this.enemies = entitiesWithoutPlayer;
	}

	public String getDamageOrigin() {
		if (this.findEvent.getEvent() == FindEvent.FALL_DAMAGE)
			return "Fall damage";
		if (this.findEvent.getEvent() == FindEvent.EMISSAIRE_ATTACK)
			return EmissaireLog.PLAYER_NAME;

		String origin = "";
		int found = 0;
		for (int i = 0; i < this.shadowDistances.length; i++) {
			if (this.shadowDistances[i] <= 2) {
				origin += this.enemies[i] + " ";
				found++;
			}
		}
		if (found > 1)
			origin += RandomJS.getInt(999999);
		return origin;
	}

	@Override
	public String toString() {
		return "t: " + this.time + ", \tlife: " + this.lifeDiff;
	}
}
