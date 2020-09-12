package model.questions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import javares.control.Listener;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import view.formDialog.AnswerPanel;

/**
 * @since 17.03.2014
 * @author Julian Schelker
 */
public class ChoiceQuestion extends Question implements Serializable {

	private static final long serialVersionUID = -3853976867650312962L;
	private String[] possibleAnswers;

	public ChoiceQuestion(String... answers) {
		this.possibleAnswers = answers;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ChoiceQuestion))
			return false;
		ChoiceQuestion o = (ChoiceQuestion) obj;
		return super.equals(obj)
			&& Arrays.equals(this.possibleAnswers, o.possibleAnswers);
	}

	@Override
	public AnswerPanel getAnswerPanel() {
		return new AnswerPanel() {

			private ButtonGroup group;

			@Override
			public JPanel getJPanel() {
				final JPanel r = new JPanel();
				r.setFocusable(true);
				r.requestFocus();
				this.group = new ButtonGroup();
				final ArrayList<AbstractButton> b = new ArrayList<AbstractButton>();

				ActionListener l = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						r.requestFocus();
					}
				};

				for (String choice : possibleAnswers) {
					JRadioButton button = new JRadioButton(choice);
					b.add(button);
					button.setActionCommand(choice);
					button.addActionListener(l);
					r.add(button);
					group.add(button);
				}
				JRadioButton button = new JRadioButton("not answered");
				b.add(button);
				button.setActionCommand("");
				button.addActionListener(l);
				r.add(button);
				group.add(button);

				r.addKeyListener(new KeyListener() {
					@Override
					public void keyPressed(KeyEvent arg0) {
						int index = arg0.getKeyCode() - KeyEvent.VK_0;
						if (index >= 1 && index <= b.size()) {
							b.get(index - 1).setSelected(true);
						}
						r.requestFocus();

						if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
							for (Listener l : listeners)
								l.actionPerformed(this, "enter", null);
						}
					}

					@Override
					public void keyReleased(KeyEvent arg0) {}

					@Override
					public void keyTyped(KeyEvent arg0) {}

				});
				return r;
			}

			@Override
			public String getText() {
				return this.group.getSelection().getActionCommand();
			}
		};
	}

	public String[] getPossibleAnswers() {
		return this.possibleAnswers;
	}

	@Override
	public String getQuestionText() {
		return "";
	}

	@Override
	public int hashCode() {
		return super.hashCode() + Arrays.hashCode(this.possibleAnswers);
	}
}
