package model.util;

import java.awt.Point;
import java.awt.Polygon;

import javares.math.MathJS;

/**
 * @since 18.04.2014
 * @author Julian Schelker
 */
public class PolygonUtil {

	public static Polygon createSector(Point screenPos, double directionAngle,
		double sectorAngle, double radius) {
		final double pointPerAngle = 10. / 360 * (2 * Math.PI);
		int points = MathJS.ceil(sectorAngle / pointPerAngle);
		points = Math.max(points, 2);
		double anglePerPoint = sectorAngle / (points - 1);
		int nrPoints = points + 1;
		int[] x = new int[nrPoints];
		int[] y = new int[nrPoints];
		x[0] = screenPos.x;
		y[0] = screenPos.y;
		for (int i = 0; i < points; i++) {
			double angle = directionAngle - sectorAngle / 2 + anglePerPoint * i;
			x[i + 1] = (int) (radius * Math.cos(angle)) + x[0];
			y[i + 1] = (int) (radius * Math.sin(angle)) + y[0];
		}
		return new Polygon(x, y, nrPoints);
	}

}
