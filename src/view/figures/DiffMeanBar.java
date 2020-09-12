
package view.figures;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javares.math.MathJS;

import javax.swing.JPanel;

import model.Interval;
import model.IntervalProviderI;
import model.util.GraphicsUtil;
import view.FontObjects;


/**
 * @since 19.05.2014
 * @author Julian Schelker
 */
public class DiffMeanBar implements IntervalProviderI {

	private double mean;
	private double maxDiff;
	private double min;
	private double max;

	private double displayMin; // value
	private double displayMax; // value
	private final int displayBorder = 5; // pixel

	private Rectangle clip;

	private Color color;
	private JPanel panel;

	public DiffMeanBar(double mean, double diff) {
		this(mean, diff, new Color(200, 120, 100));
	}

	public DiffMeanBar(double mean, double diff, Color color) {
		this.panel = new JPanel(true) {

			private static final long serialVersionUID = -5716544760545425517L;

			@Override
			protected void paintComponent(Graphics g) {
				DiffMeanBar.this.paintComponent(g);
			}
		};
		this.mean = mean;
		this.maxDiff = diff;
		this.color = color;
		initMath();
	}

	private void initMath() {
		this.min = Math.min(0, this.mean);
		this.max = Math.max(0, this.mean);

		this.displayMin = this.min;
		this.displayMax = this.max;
		if (this.mean < 0)
			this.displayMin -= this.maxDiff;
		if (this.mean > 0)
			this.displayMax += this.maxDiff;
	}

	@Override
	public Interval getInterval() {
		return new Interval((float) this.displayMin, (float) this.displayMax);
	}

	public void setDisplayInterval(Interval i) {
		this.displayMin = i.getMin();
		this.displayMax = i.getMax();
	}

	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		this.clip = g.getClipBounds();
		g2.clearRect(clip.x, clip.y, clip.width, clip.height);

		g2.setColor(this.color);
		g2.fillRect(5, getYByValue(this.max), clip.width - 10, heightByValue(this.max
			- this.min));
		GraphicsUtil.setGraphicsAlpha(g2, 0.3f);
		g2.setColor(this.color.brighter());
		if (this.mean > 0)
			g2.fillRect(5, getYByValue(this.max + this.maxDiff), clip.width - 10,
				heightByValue(2 * this.maxDiff));
		if (this.mean < 0)
			g2.fillRect(5, getYByValue(this.min + this.maxDiff), clip.width - 10,
				heightByValue(2 * this.maxDiff));

		// draw mean
		GraphicsUtil.setGraphicsAlpha(g2, 1);
		g2.setColor(Color.black);
		int y = getYByValue(this.mean);
		g2.drawLine(clip.width / 2 - 7, y, clip.width / 2 + 7, y);

		// draw max difference
		int y1 = getYByValue(this.mean - this.maxDiff);
		int y2 = getYByValue(this.mean + this.maxDiff);
		g2.drawLine(clip.width / 2 - 7, y1, clip.width / 2 + 7, y1);
		g2.drawLine(clip.width / 2 - 7, y2, clip.width / 2 + 7, y2);
		g2.drawLine(clip.width / 2, y1, clip.width / 2, y2);

		// draw text below or above
		int roundDec = 1;
		if (this.mean < 2)
			roundDec = 2;
		Label2 t1 = new Label2("Avg:", MathJS.roundToStr(this.mean, roundDec));
		Label2 t2 = new Label2("AvgDiff:", MathJS.roundToStr(this.maxDiff, roundDec));

		FontMetrics m = g2.getFontMetrics();

		int yText = y + m.getAscent() + 5;
		int spaceBelow = clip.height - yText;
		if (spaceBelow < m.getHeight() * 2 - 5) {
			yText = y - m.getHeight() * 2 + 5;
			if (yText < clip.y)
				yText = clip.height - m.getHeight() * 2 - 5;
		}

		GraphicsUtil.setGraphicsAlpha(g2, 0.8);
		g2.setFont(FontObjects.MEDIUM_L_BOLD);
		t1.drawAround(g2, clip.width / 2, yText);
		yText += m.getHeight() + 5;
		g2.setFont(FontObjects.MEDIUM);
		t2.drawAround(g2, clip.width / 2, yText);
	}

	public int getYByValue(double value) {
		double y = this.clip.height - this.displayBorder - heightByValue(value);
		return (int) y;
	}

	public int heightByValue(double value) {
		double per = (value - this.displayMin) / (this.displayMax - this.displayMin);
		double effectiveHeight = this.clip.height - 2 * this.displayBorder;
		double y = per * effectiveHeight;
		return (int) y;
	}

	public JPanel getPanel() {
		return this.panel;
	}

}
