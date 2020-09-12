package view.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.HashMap;

import javares.control.Listener;
import javares.files.config.Config;

import javax.swing.JPanel;

import model.Log;
import model.questions.ChoiceQuestion;
import model.questions.Evaluation;
import model.questions.FormQuestions;
import model.questions.Question;
import model.questions.ScaleQuestion;
import model.questions.TextQuestion;
import controller.DataProvider;

/**
 * @since 18.03.2014
 * @author Julian Schelker
 */
public class FormPanel extends JPanel implements Listener {

	private Config config;
	private DataProvider dataprovider;

	public FormPanel(Config config, DataProvider dataprovider) {
		this.config = config;
		this.dataprovider = dataprovider;
		this.dataprovider.addListener(this);
		initAll();
	}

	// called when shown data is changed
	@Override
	public void actionPerformed(Object arg0, String arg1, Object arg2) {
		this.removeAll();
		initAll();
		this.validate();
		this.repaint();
	}

	private String[] getAnswersForQuestion(FormQuestions fq, Question q) {
		Log[] logs = this.dataprovider.getDisplayedLogs();
		String[] result = new String[logs.length];
		int i = 0;
		for (Log cur : logs) {
			Evaluation e = cur.getEvaluation();
			if (e != null)
				result[i++] = e.getAnswerForQuestion(q);
		}
		if (i != result.length)
			result = Arrays.copyOf(result, i);
		return result;
	}

	private float[] getPercentagePerAnswer(String[] possibleAnswers, String[] answers) {
		HashMap<String, Integer> counted = new HashMap<String, Integer>();
		for (String answer : answers) {
			if (counted.containsKey(answer))
				counted.put(answer, counted.get(answer) + 1);
			else
				counted.put(answer, 1);
		}
		float[] result = new float[possibleAnswers.length];
		int index = 0;
		for (String answer : possibleAnswers) {
			if (counted.containsKey(answer))
				result[index++] = (float) counted.get(answer) / answers.length;
			else
				result[index++] = 0;
		}
		return result;
	}

	private void initAll() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;

		Question hide = FormQuestions.GeneralQuestions.getSub(0);

		for (FormQuestions fq : FormQuestions.values()) {
			for (Question q : fq.getSubQuestions()) {
				if (q.equals(hide))
					continue;
				boolean added = true;
				String[] answers = getAnswersForQuestion(fq, q);
				if (q instanceof ChoiceQuestion) {
					ChoiceQuestion cq = (ChoiceQuestion) q;
					float[] per = getPercentagePerAnswer(cq.getPossibleAnswers(), answers);
					this.add(new ChoiceAnswerPanel(fq, cq, per), c);
				}
				else if (q instanceof ScaleQuestion) {
					ScaleQuestion sq = (ScaleQuestion) q;
					int[] answersNumbers = string2intArr(answers);
					this.add(new ScaledAnswerPanel(fq, sq, answersNumbers), c);
				}
				else if (q instanceof TextQuestion) {
					TextQuestion tq = (TextQuestion) q;
					this.add(new TextAnswerPanel(fq, tq, answers), c);
				}
				else
					added = false;
				if (added) {
					c.gridx++;
					if (c.gridx > 9) {
						c.gridx = 0;
						c.gridy++;
					}
				}
			}
		}
	}

	private int[] string2intArr(String[] answers) {
		int[] result = new int[answers.length];
		int i = 0;
		for (String s : answers) {
			if (s.equals(""))
				continue;
			try {
				result[i++] = Integer.valueOf(s);
			} catch (Exception e) {}
		}
		if (i != result.length)
			result = Arrays.copyOf(result, i);
		return result;
	}

	private static final long serialVersionUID = -8410833488799794621L;

}
