package view.formDialog;

import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javares.control.Listener;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import model.questions.Evaluation;
import model.questions.FormQuestions;
import model.questions.Question;
import model.util.Boolean2;

/**
 * @since 17.03.2014
 * @author Julian Schelker
 */
public class EvaluationForm {

	public static Evaluation promptEvaluation(String name) {
		Evaluation e = new Evaluation();

		for (FormQuestions form : FormQuestions.values()) {
			for (Question q : form.getSubQuestions()) {

				String text = q.getQuestionText();
				String scaleDescr = form.getQuestionScaleDescription();
				AnswerPanel answer = q.getAnswerPanel();

				final JDialog dialog = new JDialog();
				BoxLayout x = new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS);
				dialog.getContentPane().setLayout(x);

				dialog.setTitle(name);
				dialog.setFont(new Font("Arial", 0, 20));
				dialog.add(new JLabel(form.toString()));
				dialog.add(new JLabel(text));
				if (!scaleDescr.equals(""))
					dialog.add(new JLabel("Scale: " + scaleDescr));
				dialog.add(answer.getJPanel());

				dialog.setMinimumSize(new Dimension(300, 150));
				dialog.pack();
				dialog.setModalityType(ModalityType.APPLICATION_MODAL);
				dialog.setLocationRelativeTo(null);

				answer.addListener(new Listener() {
					@Override
					public void actionPerformed(Object arg0, String arg1, Object arg2) {
						dialog.setVisible(false);
					}
				});
				final Boolean2 winClosed = new Boolean2(false);
				dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				dialog.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent arg0) {
						winClosed.setValue(true);
					}

					@Override
					public void windowClosing(WindowEvent arg0) {
						winClosed.setValue(true);
					}
				});
				dialog.setVisible(true);

				if (winClosed.getValue())
					return null;
				e.answerQuestionWith(q, answer.getText());
			}
		}

		return e;
	}
}
