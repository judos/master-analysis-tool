package model.questions;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;

import javares.control.Listener;

import javax.swing.JPanel;
import javax.swing.JTextField;

import view.formDialog.AnswerPanel;

/**
 * @since 17.03.2014
 * @author Julian Schelker
 */
public class ScaleQuestion extends Question implements Serializable {

	private static final long serialVersionUID = -1894124336522771805L;
	private String questionText;
	private int min;
	private int max;
	private boolean useDynamicIntervall;

	public ScaleQuestion(String questionText) {
		this(questionText, false);
	}

	public ScaleQuestion(String questionText, boolean useDynamicIntervall) {
		this.questionText = questionText;
		this.min = 1;
		this.max = 10;
		this.useDynamicIntervall = useDynamicIntervall;
	}

	@Override
	public AnswerPanel getAnswerPanel() {
		return new AnswerPanel() {

			private JTextField field;

			@Override
			public JPanel getJPanel() {
				JPanel r = new JPanel();
				this.field = new JTextField();
				this.field.setPreferredSize(new Dimension(300, 40));
				this.field.requestFocus();

				this.field.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent arg0) {
						if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
							for (Listener l : listeners)
								l.actionPerformed(this, "enter", null);
					}
				});

				r.add(field);
				return r;
			}

			@Override
			public String getText() {
				return this.field.getText();
			}
		};
	}

	public int getMax() {
		return this.max;
	}

	public int getMin() {
		return this.min;
	}

	@Override
	public String getQuestionText() {
		return this.questionText;
	}

	public boolean isUsingDynamicIntervall() {
		return this.useDynamicIntervall;
	}

	public void setUseDynamicIntervall(boolean v) {
		this.useDynamicIntervall = v;
	}

}
