
package view.figures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import javares.math.MathJS;

import javax.swing.JPanel;

import view.FontObjects;


/**
 * @since 13.03.2014
 * @author Julian Schelker
 */
public class PercentageBar extends JPanel {

	private float per;
	private Color barColor;
	private ArrayList<String> notes;

	public PercentageBar(float per) {
		this(per, new Color(128, 128, 128));
	}

	public PercentageBar(float per, Color c) {
		this.per = per;
		this.barColor = c;
		this.notes = new ArrayList<String>();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Rectangle r = g.getClipBounds();
		int barHeight = (int) (r.getHeight() * this.per);
		g2.setBackground(new Color(200, 200, 200));
		g2.clearRect(r.x, r.y, r.width, r.height);
		g2.setColor(barColor);
		g2.fillRect(r.x, r.y + r.height - barHeight, r.width, barHeight);
		double percentage = MathJS.round(this.per * 100, 0);
		Label per = new Label((int) percentage + " %");
		g2.setFont(FontObjects.MEDIUM_L_BOLD);

		int lineHeight = g2.getFontMetrics().getHeight();
		int totalHeight = this.notes.size() * lineHeight;

		g2.setColor(Color.black);

		int y = r.y + r.height / 2 - totalHeight / 2;
		per.drawCentered(g2, r.x + r.width / 2, y);
		g2.setFont(FontObjects.MEDIUM);
		for (String note : this.notes) {
			y += lineHeight;
			new Label(note).drawCentered(g2, r.x + r.width / 2, y);
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -265646008408027474L;

	public void addText(String string) {
		this.notes.add(string);
	}

}
