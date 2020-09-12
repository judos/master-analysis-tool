
package view.statistics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javares.math.MathJS;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import view.FontObjects;
import view.figures.PercentageBar;


/**
 * @since 13.03.2014
 * @author Julian Schelker
 */
public class ItemGraphPanel extends JPanel {

	private static final long serialVersionUID = -398208752739509430L;
	private String item;
	private int used;
	private float per;
	private int total;

	public ItemGraphPanel(String item, int used, int total) {
		this.item = item;
		this.used = used;
		this.total = total;
		this.per = (float) used / total;
		initAll();
	}

	private void initAll() {
		// this.setPreferredSize(new Dimension(100, 0));
		this.setBorder(new LineBorder(Color.gray));
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTHWEST;
		JLabel label = new JLabel(this.item + ":");
		label.setFont(FontObjects.MEDIUM_L_BOLD);
		Dimension min = label.getMinimumSize();
		min = new Dimension((int) MathJS.max(min.getWidth(), 160), (int) min.getHeight());
		this.setMinimumSize(min);
		this.setPreferredSize(min);
		this.add(label, c);
		c.gridy++;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 0, 0, 0);

		PercentageBar bar = new PercentageBar(per, new Color(100, 150, 100));
		bar.addText("used: " + this.used + " / " + this.total);
		this.add(bar, c);

	}
}
