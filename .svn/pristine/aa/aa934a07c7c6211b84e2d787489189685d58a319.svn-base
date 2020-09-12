package launcher.other;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.questions.FormQuestions;
import model.questions.TextQuestion;

import org.apache.commons.lang3.StringUtils;

import view.form.TextAnswerPanel;

/**
 * @since 18.03.2014
 * @author Julian Schelker
 */
public class TestTextAnswerPanel extends JFrame {
	public TestTextAnswerPanel() {
		JPanel x = new JPanel();
		// Dimension d = new Dimension(150, 400);
		// x.setPreferredSize(d);
		// x.setMinimumSize(d);
		FormQuestions fq = FormQuestions.DidYouSeeTheStatue;
		TextQuestion tq = new TextQuestion("Some text question");
		String[] answers = new String[10];
		for (int i = 0; i < 10; i++)
			answers[i] = i + " " + StringUtils.repeat("long text", 10);
		TextAnswerPanel t = new TextAnswerPanel(fq, tq, answers);

		this.add(t);
		// this.setPreferredSize(d);
		// this.setMinimumSize(d);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	private static final long serialVersionUID = 1573592774303180115L;

	public static void main(String[] args) {
		new TestTextAnswerPanel();
	}
}
