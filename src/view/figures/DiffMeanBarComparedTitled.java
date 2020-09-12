
package view.figures;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import model.BoundingBoxIntervalMerger;
import model.Interval;
import model.IntervalProviderI;
import view.FontObjects;


/**
 * @since 18.03.2014
 * @author Julian Schelker
 */
public class DiffMeanBarComparedTitled implements IntervalProviderI {

	private String[] subs;
	private String title;
	private double[] diff;
	private double[] mean;
	private BoundingBoxIntervalMerger interval;
	private DiffMeanBar[] content;
	private JPanel panel;

	public Color[] colors = new Color[] { new Color(138, 100, 100),
		new Color(138, 100, 120) };

	public DiffMeanBarComparedTitled(String title, String[] subs, double[] mean,
		double[] avgDiff) {
		this.panel = new JPanel(true);
		this.title = title;
		this.subs = subs;
		this.mean = mean;
		this.diff = avgDiff;
		initAll();
	}

	/**
	 * @return the panel
	 */
	public JPanel getPanel() {
		return panel;
	}

	public void initAll() {
		this.panel.removeAll();
		this.panel.setPreferredSize(new Dimension(150, 0));
		this.panel.setBorder(new LineBorder(Color.gray));
		this.panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 0, 3);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTHWEST;

		c.gridwidth = this.subs.length;
		JLabel label = new JLabel(title);
		label.setFont(FontObjects.MEDIUM_L_BOLD);
		this.panel.add(label, c);

		c.gridy++;
		c.gridwidth = 2;
		c.insets = new Insets(1, 3, 1, 3);
		for (int i = 0; i < this.subs.length; i++) {
			int align = SwingConstants.LEFT;
			if (i > 0)
				align = SwingConstants.RIGHT;
			JLabel x = new JLabel(subs[i], align);
			x.setForeground(colors[i]);
			this.panel.add(x, c);
			c.gridy++;
		}

		c.insets = new Insets(8, 0, 0, 0);
		c.gridx = 0;
		c.gridy++;
		c.weighty = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 0, 0, 0);

		this.content = new DiffMeanBar[this.subs.length];
		this.interval = new BoundingBoxIntervalMerger();
		for (int i = 0; i < this.subs.length; i++) {
			content[i] = new DiffMeanBar(mean[i], diff[i], colors[i]);
			interval.addData(content[i]);
		}
		for (int i = 0; i < this.subs.length; i++) {
			this.panel.add(content[i].getPanel(), c);
			c.gridx++;
		}

	}

	@Override
	public Interval getInterval() {
		return this.interval.getInterval();
	}

	public void setDisplayInterval(Interval i) {
		for (DiffMeanBar x : this.content) {
			x.setDisplayInterval(i);
		}
	}

	public Interval getLeftInterval() {
		return this.content[0].getInterval();
	}

	public Interval getRightInterval() {
		return this.content[1].getInterval();
	}

	public void setDisplayIntervalLeft(Interval i) {
		this.content[0].setDisplayInterval(i);
	}

	public void setDisplayIntervalRight(Interval i) {
		this.content[1].setDisplayInterval(i);
	}

}
