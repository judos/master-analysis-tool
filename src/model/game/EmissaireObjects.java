
package model.game;

/**
 * @since 25.03.2014
 * @author Julian Schelker
 */
public class EmissaireObjects {

	private static String eventOrder = "lamps1,lamps2,lamps3,chest1,lamps4,"
		+ "door2 open:1,door2 open:0,corridorSwitch on:1,corridorSwitch on:0,lamps5,chest3,"
		+ "checkPoint1,roofPanelSwitch,chest5,lamps6,elevator up:1,elevator up:0,lamps7,lamps8,"
		+ "lamps9,lamps10,lamps11,lamps13,checkPoint2,lamps12,chest4,lamps14,"
		+ "cityDoorSwitch on:1,cityDoorSwitch on:0,levelSolved";

	public static String[] getEventOrder() {
		return eventOrder.split(",");
	}

	/**
	 * index 0 is just filler (doesn't exist)
	 */
	public static int[] lamps = new int[] { 0, 6, 12, 12, 12, 14, 12, 12, 12, 12, 12, 12,
		12, 12, 12, 12 };

	/**
	 * index 0 and 2 doesn't exist, chest5 does exist and gives nothing
	 */
	public static int[] chests = new int[] { 0, 10, 0, 10, 10, 0 };

	private static int chestEnergy(String object) {
		int index = Integer.valueOf(object.substring(5));
		return chests[index];
	}

	public static int getEnergyByObject(String object) {
		if (object.startsWith("lamps"))
			return lampEnergy(object);
		else if (object.startsWith("chest"))
			return chestEnergy(object);
		throw new IllegalArgumentException("object not found");
	}

	private static int lampEnergy(String object) {
		int index = Integer.valueOf(object.substring(5));
		return lamps[index];
	}
}
