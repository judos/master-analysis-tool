package model;

import java.util.HashMap;

import javares.math.MathJS;
import model.events.NamedDamageEvent;
import model.game.EmissaireMechanics;
import model.util.Map;

/**
 * @since 20.05.2014
 * @author Julian Schelker
 */
public class CombatAnalyzer {

	private Log log;
	private HashMap<String, Integer> damageTaken;
	private HashMap<String, Integer> damageDone;
	private HashMap<String, Integer> stunCount;

	private HashMap<String, Integer> attackCount;
	private HashMap<String, Float> stunTime;

	public static final double RANGE_DANGER_ZONE = 7.5;

	public CombatAnalyzer() {
		this.log = null;
	}

	public HashMap<String, Integer> getDamageTakenFrom() {
		return this.damageTaken;
	}

	public HashMap<String, Integer> getDamageDoneTo() {
		return this.damageDone;
	}

	/**
	 * @return the stunCount
	 */
	public HashMap<String, Integer> getStunCount() {
		return stunCount;
	}

	/**
	 * @return the attackCount
	 */
	public HashMap<String, Integer> getAttackCount() {
		return attackCount;
	}

	/**
	 * @return the stunTime
	 */
	public HashMap<String, Float> getStunTime() {
		return stunTime;
	}

	public void initialize(Log log) {
		this.log = log;
		initializeDamageTaken();
		initializeDamageDone();
	}

	private void initializeDamageDone() {
		// set damage to 0 for every enemy
		this.damageDone = new HashMap<String, Integer>();
		// count of stuns (counting when the shadow is stunned)
		this.stunCount = new HashMap<String, Integer>();
		this.stunTime = new HashMap<String, Float>();
		// count of attacks (counting for the nearest shadow)
		this.attackCount = new HashMap<String, Integer>();
		for (String ent : log.getEntitiesWithoutPlayer()) {
			this.damageDone.put(ent, 0);
			this.stunCount.put(ent, 0);
			this.stunTime.put(ent, 0f);
			this.attackCount.put(ent, 0);
		}

		//
		// add all damage from the different shadow attacks
		String[] entities = log.getEntitiesWithoutPlayer();
		for (NamedDamageEvent e : log.getEventsDamage()) {
			if (e.isEmissaireAttack()) {

				float[] shadowDistances = log.getShadowDistance((float) e.getTime());
				int nearest = MathJS.minIndex(shadowDistances);
				String attacked = entities[nearest];
				Map.add(this.damageDone, attacked, Math.abs(e.getEnergyDifference()));

				Map.add(attackCount, attacked, 1);

				double charge = EmissaireMechanics.getChargeScaled(Math.abs(e
					.getEnergyDifference()));
				for (int i = 0; i < entities.length; i++) {
					float distance = shadowDistances[i];
					double freezeTime = EmissaireMechanics
						.getFreezeTime(charge, distance);

					if (freezeTime > 0.01) {
						if (nearest != i)
							Map.add(attackCount, entities[i], 1);
						Map.add(stunCount, entities[i], 1);
						Map.add(stunTime, entities[i], (float) freezeTime);
					}
				}

			}
		}
	}

	private void initializeDamageTaken() {
		// set damage to 0 for every enemy
		this.damageTaken = new HashMap<String, Integer>();
		for (String ent : log.getEntitiesWithoutPlayer())
			this.damageTaken.put(ent, 0);

		// add all damage from the different shadow attacks
		for (NamedDamageEvent e : log.getEventsDamage()) {
			if (e.isShadowAttack()) {
				String attacker = e.getDescription();
				Map.add(this.damageTaken, attacker, Math.abs(e.getEnergyDifference()));
			}
		}
	}

}
