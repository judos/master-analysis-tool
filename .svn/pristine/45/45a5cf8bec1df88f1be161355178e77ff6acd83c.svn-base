package view.form;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import model.questions.ChoiceQuestion;
import model.questions.FormQuestions;
import view.figures.PercentageBar;

/**
 * @since 18.03.2014
 * @author Julian Schelker
 */
public class ChoiceAnswerPanel extends JPanel {

	private FormQuestions fq;
	private ChoiceQuestion q;
	private float[] per;

	public ChoiceAnswerPanel(FormQuestions fq, ChoiceQuestion q, float[] per) {
		this.fq = fq;
		this.q = q;
		this.per = per;
		initAll();
	}

	private void initAll() {
		this.setPreferredSize(new Dimension(150, 0));
		this.setBorder(new LineBorder(Color.gray));
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 0, 3);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTHWEST;

		c.gridwidth = this.per.length;
		this.add(new JLabel(this.fq.name() + "?"), c);

		c.gridy++;
		c.gridwidth = 2;
		int i = 0;
		c.insets = new Insets(1, 3, 1, 3);
		String[] answers = this.q.getPossibleAnswers();
		for (String answer : answers) {
			int align = SwingConstants.LEFT;
			if (i > 0)
				align = SwingConstants.RIGHT;
			JLabel x = new JLabel(answer, align);
			x.setForeground(colors[i]);
			this.add(x, c);
			c.gridy++;
			i++;
		}

		c.insets = new Insets(8, 0, 0, 0);
		c.gridx = 0;
		c.gridy++;
		c.weighty = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 0, 0, 0);
		i = 0;
		for (float per : this.per) {
			this.add(new PercentageBar(per, colors[i++]), c);
			c.gridx++;
		}

	}

	public static final Color[] colors = new Color[] { new Color(100, 128, 100),
		new Color(128, 100, 100) };

	private static final long serialVersionUID = -48643465099326586L;

}
