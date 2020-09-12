
package view.figures;

import java.awt.FontMetrics;
import java.awt.Graphics;


/**
 * @since 23.05.2014
 * @author Julian Schelker
 */
public class Label {

	public static final int SPACING = 8;

	private String text;

	public Label(String text) {
		this.text = text;
	}

	public void drawCentered(Graphics g, int x, int y) {
		FontMetrics f = g.getFontMetrics();
		int width = f.stringWidth(this.text);
		g.drawString(this.text, x - width / 2, y);
	}

	public void drawLeftFrom(Graphics g, int x, int y) {
		FontMetrics f = g.getFontMetrics();
		int width = f.stringWidth(this.text);
		g.drawString(this.text, x - width - SPACING, y);
	}

	public void drawRightFrom(Graphics g, int x, int y) {
		g.drawString(this.text, x + SPACING, y);
	}
}
