package model.events;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * @since 17.02.2014
 * @author Julian Schelker
 */
public class UnidentifiedEvent {

	private HashMap<String, String> values;

	/**
	 * @param lineData
	 *            csv value<br>
	 *            e.g. type=healthChange;life=13;time=10
	 * @param initialTime
	 */
	public UnidentifiedEvent(String lineData, double initialTime) {
		String[] atts = lineData.split(";");
		this.values = new HashMap<String, String>();
		for (String att : atts) {
			String[] value = att.split("=");
			if (value[0].equals("time"))
				value[1] = "" + (Double.valueOf(value[1]) - initialTime);
			this.values.put(value[0], value[1]);
		}
		if (!this.values.containsKey("type"))
			throw new RuntimeException("no type set");
	}

	public float getTime() {
		return Float.valueOf(this.values.get("time"));
	}

	public String getValue(String key) {
		return this.values.get(key);
	}

	public boolean isOfType(Type t) {
		String[] atts = t.filter.split(";");
		for (String att : atts) {
			String[] values = att.split("=");
			if (!this.values.containsKey(values[0]))
				return false;
			if (!this.values.get(values[0]).equals(values[1]))
				return false;
		}
		return true;
	}

	public String toStringTimeDiff(double addTime) {
		StringBuffer s = new StringBuffer("<event>");
		s.append("type=" + this.values.get("type") + ";");
		for (Entry<String, String> e : this.values.entrySet()) {
			if (!e.getKey().equals("time") && !e.getKey().equals("type"))
				s.append(e.getKey() + "=" + e.getValue() + ";");
		}
		s.append("time=" + (getTime() + addTime));
		s.append("</event>");
		return s.toString();
	}

	public enum Type {
		DEATH_POINT("type=event;event=playerDied"), ITEM("type=itemUsed"), HEALTH_CHANGE(
			"type=healthChange"), EYE_RETURNED("type=mechEyeUse;available=1"),
		LEVEL_SOLVED("type=event;event=levelSolved");

		private String filter;

		private Type(String filter) {
			this.filter = filter;
		}
	}
}
