package model.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;

/**
 * @since 19.05.2014
 * @author Julian Schelker
 */
public class GraphicsUtil {
	public static void setGraphicsAlpha(Graphics2D g, double alpha) {
		AlphaComposite ac = java.awt.AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
			(float) alpha);
		g.setComposite(ac);
	}
}
