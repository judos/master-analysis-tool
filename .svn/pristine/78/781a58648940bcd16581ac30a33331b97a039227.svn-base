package model.game;

import javares.math.MathJS;

/**
 * @since 27.03.2014
 * @author Julian Schelker
 */
public class EmissaireMechanics {
	public static double getFreezeTime(double charge, double distance) {
		double m1 = Math.pow(charge * 5, 1.4);
		double m2 = Math.pow(MathJS.clamp(6 - distance, 0, 6) / 5, 1.8);
		return m1 * m2;
	}

	public static double getEnergyUsage(double charge) {
		return charge * 5;
	}

	public static double getChargeScaled(double energy) {
		return energy / 5;
	}
}
