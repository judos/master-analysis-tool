package model.events;

import java.util.Comparator;

/**
 * @since 26.03.2014
 * @author Julian Schelker
 */
public class TimeComparator implements Comparator<UnidentifiedEvent> {

	@Override
	public int compare(UnidentifiedEvent o1, UnidentifiedEvent o2) {
		return (int) Math.signum(o1.getTime() - o2.getTime());
	}

}
