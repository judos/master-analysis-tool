package model.events;

import javares.math.MathJS;
import model.Time;

/**
 * @since 31.03.2014
 * @author Julian Schelker
 */
public class NamedDamageEvent extends EnergyEvent {

	private int type;
	private String reason;
	private String description;

	/**
	 * @return the type (EmissaireAttack, ShadowAttack, FallDamage, Unclear
	 *         Event) see FindEvent
	 */
	public int getType() {
		return type;
	}

	public NamedDamageEvent(double time, int energyDiff, int type, String reason,
		String description) {
		super(FindEvent.getEventNameByType(type), time, energyDiff);
		this.type = type;
		this.reason = reason;
		this.description = description;
	}

	public boolean isEmissaireAttack() {
		return this.type == FindEvent.EMISSAIRE_ATTACK;
	}

	public boolean isShadowAttack() {
		return this.type == FindEvent.SHADOW_ATTACK;
	}

	@Override
	public String toString() {
		Time t = new Time(time);
		return this.name + "   \t" + t.toMMSS() + "\t(" + MathJS.round(time, 1)
			+ ")   \t" + energyDiff + "\t" + this.reason + "\t" + this.description;
	}

	/**
	 * @return additional description (for shadow attacks this is the name of
	 *         the attacker)
	 */
	public String getDescription() {
		return this.description;
	}

}
