
package view.figures;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashMap;

import javares.math.MathJS;

import javax.swing.JPanel;

import model.util.Map;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import view.FontObjects;


/**
 * @since 09.04.2014
 * @author Julian Schelker
 */
public class StdvMeanFloatDistribution {

	public static final int BORDER = 10;

	private int groups;

	private double mean;
	private double diff;
	private HashMap<Integer, Integer> grouped;
	private double min;
	private double max;
	private int count;
	private double groupSize;

	private boolean groupsSet;

	private float[] values;

	private double panelHeight;

	private JPanel panel;

	/**
	 * has 50 different groups to display the values
	 * 
	 * @param values
	 */
	public StdvMeanFloatDistribution(float[] values) {
		this.count = values.length;
		this.values = values;
		this.groupsSet = false;
		this.panel = new JPanel() {

			private static final long serialVersionUID = -1749481971041868270L;

			@Override
			protected void paintComponent(Graphics g) {
				StdvMeanFloatDistribution.this.paintComponent(g);
			}
		};
	}

	protected int valueToY(double value, Rectangle clip) {
		if (Double.isNaN(value))
			value = (float) (max + min) / 2;
		return (int) (clip.y + clip.height - BORDER - valueToHeight(value - this.min,
			clip));
	}

	protected double getBarHeight(Rectangle clip) {
		return ((double) clip.height - 2 * BORDER) / this.groups;
	}

	protected int getBarHeightI(Rectangle clip) {
		return (clip.height - 2 * BORDER) / this.groups;
	}

	protected int valueToHeight(double value, Rectangle clip) {
		if (Double.isNaN(value))
			return 1;
		return (int) (value / groupSize * getBarHeight(clip));
	}

	private void initGroups(double height) {
		if (Math.abs(this.panelHeight - height) > 100)
			this.groupsSet = false;
		if (!this.groupsSet) {
			this.panelHeight = height;
			this.groups = (int) Math.round(height / 50);
			initMath(this.values);
		}
		this.groups = Math.max(1, this.groups);
	}

	private void initMath(float[] answers) {
		SummaryStatistics stat = new SummaryStatistics();
		for (float x : answers)
			stat.addValue(x);
		this.mean = stat.getMean();
		this.diff = stat.getStandardDeviation();
		this.min = stat.getMin();
		this.max = stat.getMax();
		if (this.min == this.max) {
			if (this.min == 0) {
				min = -1;
				max = 1;
			}
			else {
				max *= 1.1;
				min *= 0.9;
			}
		}
		this.groupSize = (this.max - this.min) / (this.groups - 1);
		this.max += this.groupSize / 2;
		this.min -= this.groupSize / 2;

		this.grouped = new HashMap<Integer, Integer>();
		for (float x : answers) {
			int groupNr = (int) Math.floor((x - this.min) / this.groupSize);
			Map.add(this.grouped, groupNr, 1);
		}
	}

	protected void paintComponent(Graphics g) {
		initGroups(g.getClipBounds().getHeight());

		Graphics2D g2 = (Graphics2D) g;
		Rectangle clip = g.getClipBounds();
		g2.clearRect(clip.x, clip.y, clip.width, clip.height);

		// draw background bars
		// and value labels on the left
		double curBarValueUpper = 0;
		int fh = g2.getFontMetrics().getAscent();
		for (int curBar = 0; curBar < this.groups; curBar++) {
			g2.setColor(Color.LIGHT_GRAY);
			int amount = 0;
			if (this.grouped.containsKey(curBar))
				amount = this.grouped.get(curBar);

			float per = (float) amount / Math.max(1, this.count);
			int barWidth = (int) (per * clip.width);
			double curBarValueLower = this.min + curBar * this.groupSize;
			curBarValueUpper = this.min + (curBar + 1) * this.groupSize;

			// fill background bar
			g2.fillRect(clip.x + clip.width / 2 - barWidth / 2,
				valueToY(curBarValueUpper, clip), barWidth, getBarHeightI(clip) + 1);

			g2.setColor(Color.gray);

			String curValue = MathJS.roundToStr(curBarValueLower, 1);
			g2.drawString(curValue + "  -", clip.x + 5, valueToY(curBarValueLower, clip)
				+ fh / 2 - 3);

			String showPer = (int) MathJS.round(per * 100, 0) + "%";
			if (per > 0) {
				int w = g2.getFontMetrics().stringWidth(showPer);
				g2.drawString(showPer, clip.x + clip.width - w - 5,
					valueToY(curBarValueLower + this.groupSize / 2, clip) + fh / 2);
			}
		}
		// draw highest label (top,left)
		String curValue = MathJS.roundToStr(curBarValueUpper, 1);
		g2.drawString(curValue + "  -", clip.x + 5, valueToY(curBarValueUpper, clip) + fh
			/ 2 - 3);

		// draw mean
		g2.setColor(Color.black);
		int y = valueToY((float) this.mean, clip);
		g2.drawLine(clip.width / 2 - 10, y, clip.width / 2 + 10, y);
		String avg = "Avg";
		String avgNr = "" + MathJS.round(this.mean, 1);
		g2.setFont(FontObjects.MEDIUM_L_BOLD);
		int w = g2.getFontMetrics().stringWidth(avg);
		g2.drawString(avg, clip.width / 2 - w - 12, y + fh / 2);
		g2.drawString(avgNr, clip.width / 2 + 12, y + fh / 2);

		// draw standard deviation
		String std = "Stdv";
		String stdNr = "" + MathJS.round(this.diff, 1);
		w = g2.getFontMetrics().stringWidth(std);
		int y1 = valueToY((float) (this.mean - this.diff), clip);
		int y2 = valueToY((float) (this.mean + this.diff), clip);
		int stdY1 = (int) (y + 1.5f * fh); // under the avg
		int stdY2 = y1 + fh / 2; // at the lowest line of the stdv bar
		int stdY = Math.max(stdY1, stdY2);
		stdY = Math.min(stdY, clip.y + clip.height - fh / 2);
		g2.drawString(std, clip.width / 2 - w - 12, stdY);
		g2.drawString(stdNr, clip.width / 2 + 12, stdY);

		g2.drawLine(clip.width / 2 - 10, y1, clip.width / 2 + 10, y1);
		g2.drawLine(clip.width / 2 - 10, y2, clip.width / 2 + 10, y2);
		g2.drawLine(clip.width / 2, y1, clip.width / 2, y2);
	}

	public Component getPanel() {
		return this.panel;
	}

}
