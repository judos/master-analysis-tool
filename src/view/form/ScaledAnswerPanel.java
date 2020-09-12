package view.form;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import model.questions.FormQuestions;
import model.questions.ScaleQuestion;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import view.figures.StdvMeanIntDistribution;

/**
 * @since 18.03.2014
 * @author Julian Schelker
 */
public class ScaledAnswerPanel extends JPanel {

	private FormQuestions fq;
	private ScaleQuestion sq;
	private int[] answers;

	public ScaledAnswerPanel(FormQuestions fq, ScaleQuestion sq, int[] answers) {
		this.fq = fq;
		this.sq = sq;
		this.answers = answers;
		initAll();
	}

	private void initAll() {
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

		this.add(new JLabel("<html>" + this.fq.name() + "?" + "</html>"), c);
		c.gridy++;
		this.add(new JLabel("<html>" + this.sq.getQuestionText() + "</html>"), c);
		c.gridy++;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(3, 0, 0, 0);

		if (this.sq.isUsingDynamicIntervall()) {
			SummaryStatistics stat = new SummaryStatistics();
			for (int x : answers)
				stat.addValue(x);
			int min = (int) stat.getMin();
			int max = (int) stat.getMax();
			this.add(new StdvMeanIntDistribution(answers, min, max), c);
		}
		else
			this.add(new StdvMeanIntDistribution(answers, this.sq.getMin(), this.sq.getMax()),
				c);

	}

	private static final long serialVersionUID = 1137896287842749349L;

}
