package view.figures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashMap;

import javares.math.MathJS;

import javax.swing.JPanel;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import view.FontObjects;

/**
 * @since 18.03.2014
 * @author Julian Schelker
 */
public class StdvMeanIntDistribution extends JPanel {

	private double mean;
	private double diff;
	private HashMap<Integer, Integer> grouped;
	private int min;
	private int max;
	private int count;

	public StdvMeanIntDistribution(int[] answers, int min, int max) {
		this.min = min;
		this.max = max;
		this.count = answers.length;
		initMath(answers, min, max);
	}

	protected int barNrToY(float barNr, Rectangle clip) {
		if (Float.isNaN(barNr))
			barNr = ((float) max + min) / 2;
		int sideBars = this.max - this.min + 1; // min is inclusive
		int barHeight = clip.height / sideBars;

		return (int) (((float) this.max - barNr) * barHeight + barHeight / 2);
	}

	private void initMath(int[] answers, int min, int max) {
		SummaryStatistics stat = new SummaryStatistics();
		this.grouped = new HashMap<Integer, Integer>();
		for (int x : answers) {
			stat.addValue(x);
			if (this.grouped.containsKey(x))
				this.grouped.put(x, this.grouped.get(x) + 1);
			else
				this.grouped.put(x, 1);
		}
		this.mean = stat.getMean();
		this.diff = stat.getStandardDeviation();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Rectangle r = g.getClipBounds();
		g2.clearRect(r.x, r.y, r.width, r.height);

		int sideBars = this.max - this.min + 1; // min is inclusive
		int barHeight = r.height / sideBars;

		// draw background bars

		for (int curBar = min; curBar <= max; curBar++) {
			g2.setColor(Color.LIGHT_GRAY);
			int barNr = curBar - min;
			int amount = 0;
			if (this.grouped.containsKey(curBar))
				amount = this.grouped.get(curBar);

			float per = (float) amount / Math.max(1, this.count);
			int width = (int) (per * r.width);
			int y = r.y + r.height - (barNr + 1) * barHeight;
			g2.fillRect(r.x + r.width / 2 - width / 2, y, width, barHeight);

			g2.setColor(Color.gray);
			int fh = g2.getFontMetrics().getAscent();
			g2.drawString(curBar + ":", r.x + 5, y + barHeight / 2 + fh / 2);
		}

		// draw mean
		g2.setColor(Color.black);
		int y = barNrToY((float) this.mean, r);
		g2.drawLine(r.width / 2 - 10, y, r.width / 2 + 10, y);
		String avg = "Avg";
		String avgNr = "" + MathJS.round(this.mean, 1);
		g2.setFont(FontObjects.MEDIUM_L_BOLD);
		int fh = g2.getFontMetrics().getAscent();
		int w = g2.getFontMetrics().stringWidth(avg);
		g2.drawString(avg, r.width / 2 - w - 12, y + fh / 2);
		g2.drawString(avgNr, r.width / 2 + 12, y + fh / 2);

		// draw standard deviation
		String std = "Stdv";
		String stdNr = "" + MathJS.round(this.diff, 1);
		w = g2.getFontMetrics().stringWidth(std);
		int y1 = barNrToY((float) (this.mean - this.diff), r);
		int y2 = barNrToY((float) (this.mean + this.diff), r);
		int stdY1 = (int) (y + 1.5f * fh); // under the avg
		int stdY2 = y1 + fh / 2; // at the lowest line of the stdv bar
		int stdY = Math.max(stdY1, stdY2);
		stdY = Math.min(stdY, r.y + r.height - fh / 2);
		g2.drawString(std, r.width / 2 - w - 12, stdY);
		g2.drawString(stdNr, r.width / 2 + 12, stdY);

		g2.drawLine(r.width / 2 - 10, y1, r.width / 2 + 10, y1);
		g2.drawLine(r.width / 2 - 10, y2, r.width / 2 + 10, y2);
		g2.drawLine(r.width / 2, y1, r.width / 2, y2);
	}

	private static final long serialVersionUID = -8515757841420131292L;

}
