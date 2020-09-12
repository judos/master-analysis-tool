package model.events;

import java.security.InvalidParameterException;

/**
 * @since 27.03.2014
 * @author Julian Schelker
 */
public class FindEvent {
	private double[] poss;
	private String reason;

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	public static final int EMISSAIRE_ATTACK = 0;
	public static final int SHADOW_ATTACK = 1;
	public static final int FALL_DAMAGE = 2;
	public static final int UNCLEAR_EVENT = 10;

	public FindEvent() {
		this.poss = new double[] { 1, 1, 1 };
		this.reason = "";
	}

	public void set(int index, double possibility, String reason) {
		this.poss[index] = possibility;
		this.reason += reason + " ";
	}

	public void setOnly(int index, String reason) {
		this.reason = reason + " ";
		for (int i = 0; i < 2; i++)
			if (i != index)
				this.poss[i] = 0;
		this.poss[index] = 1;
	}

	public boolean isAmbiguous() {
		int possible = 0;
		for (int i = 0; i < 3; i++) {
			if (this.poss[i] > 0)
				possible++;
		}
		return possible != 1;
	}

	public String getEventName() {
		if (isAmbiguous())
			return getEventNameByType(10);
		int i;
		for (i = 0; i < 3; i++)
			if (this.poss[i] > 0)
				break;
		return getEventNameByType(i);
	}

	public static String getEventNameByType(int type) {
		if (type == UNCLEAR_EVENT)
			return "Unclear Event";
		if (type == EMISSAIRE_ATTACK)
			return "Emissaire Attack";
		if (type == SHADOW_ATTACK)
			return "Shadow Attack";
		else if (type == FALL_DAMAGE)
			return "Fall Damage";
		throw new InvalidParameterException("Type unknown: " + type);
	}

	public int getEvent() {
		if (isAmbiguous())
			return 10;
		int i;
		for (i = 0; i < 3; i++)
			if (this.poss[i] > 0)
				break;
		return i;
	}
}
