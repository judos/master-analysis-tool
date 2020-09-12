package model.events;

import java.security.InvalidParameterException;

import javares.data.ArraysJS;
import model.game.EmissaireMechanics;

/**
 * @since 31.03.2014
 * @author Julian Schelker
 */
public class EmissaireAttack extends NamedDamageEvent {

	private float[] shadowDistances;
	private String[] enemies;

	public EmissaireAttack(double time, int energyDiff, String reason) {
		super(time, energyDiff, 0, reason, "");

	}

	public EmissaireAttack(EventFacts fact, String[] enemies) {
		super(fact.getTime(), fact.getLifeDiff(), 0, fact.getFindEvent().getReason(), "");

		this.shadowDistances = fact.getShadowDistances();
		this.enemies = enemies;
	}

	public double getStunTimeForShadow(String enemy) {
		int index = ArraysJS.indexOf(this.enemies, enemy);
		if (index == -1)
			throw new InvalidParameterException("This enemy doesn't exist");
		return EmissaireMechanics.getFreezeTime(this.energyDiff,
			this.shadowDistances[index]);
	}
}
