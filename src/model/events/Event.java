package model.events;

/**
 * @since 25.03.2014
 * @author Julian Schelker
 */
public class Event {

	protected double time;
	protected String name;

	/**
	 * @return the name of this event
	 */
	public String getName() {
		return name;
	}

	public Event(double time, String name) {
		this.time = time;
		this.name = name;
	}

	public Event(String name, double time) {
		this(time, name);
	}

	/**
	 * @return the time
	 */
	public double getTime() {
		return time;
	}

}