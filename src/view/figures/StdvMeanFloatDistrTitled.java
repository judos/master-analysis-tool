
package view.figures;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import view.FontObjects;


/**
 * @since 18.03.2014
 * @author Julian Schelker
 */
public class StdvMeanFloatDistrTitled extends JPanel {

	private float[] answers;
	private String title;
	private Color color;
	private ArrayList<String> subs;

	public StdvMeanFloatDistrTitled(String title, float[] answers, Color c) {
		this.title = title;
		this.subs = new ArrayList<String>();
		this.answers = answers;
		this.color = c;
		initAll();
	}

	private void initAll() {
		this.removeAll();
		this.setPreferredSize(new Dimension(150, 0));
		this.setBorder(new LineBorder(Color.gray));
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTHWEST;

		addTextLine(this.title, c, FontObjects.MEDIUM_L_BOLD);
		for (String sub : this.subs)
			addTextLine(sub, c, FontObjects.MEDIUM);

		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(3, 0, 0, 0);
		this.add(new StdvMeanFloatDistribution(this.answers).getPanel(), c);
	}

	private void addTextLine(String text, GridBagConstraints c, Font font) {
		JLabel label = new JLabel("<html>" + text + "</html>");
		label.setFont(font);
		this.add(label, c);
		c.gridy++;
	}

	private static final long serialVersionUID = 1137896287842749349L;

	public void addSubTitle(String string) {
		this.subs.add(string);
		initAll();
	}

}
