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
public class TextQuestion extends Question implements Serializable {

	private String title;

	public TextQuestion(String title) {
		this.title = title;
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

	@Override
	public String getQuestionText() {
		return this.title;
	}

	private static final long serialVersionUID = 2290720073090334058L;

}
