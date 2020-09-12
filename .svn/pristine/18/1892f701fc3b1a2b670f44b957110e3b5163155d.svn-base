package view.form;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import model.questions.FormQuestions;
import model.questions.TextQuestion;

/**
 * @since 19.03.2014
 * @author Julian Schelker
 */
public class TextAnswerPanel extends JPanel {
	public static BufferedWriter out;

	private TextQuestion question;
	private String[] answers;
	private FormQuestions fq;

	public TextAnswerPanel(FormQuestions fq, TextQuestion tq, String[] answers) {
		this.fq = fq;
		this.question = tq;
		this.answers = answers;
		groupAnswers();
		initAll();
	}

	protected void dump() {
		init();
		dumpS(this.question.getOverQuestionTitle());
		dumpS(this.question.getQuestionText());
		for (String s : this.answers)
			dumpS(s);
		dumpS("");
	}

	protected void dumpS(String s) {
		try {
			out.write(s);
			out.newLine();
		} catch (Exception e) {}
	}

	protected void groupAnswers() {
		HashMap<String, Integer> grouped = new HashMap<>();
		for (String answer : this.answers) {
			if (answer.equals(""))
				continue;
			if (grouped.containsKey(answer))
				grouped.put(answer, grouped.get(answer) + 1);
			else
				grouped.put(answer, 1);
		}
		Set<Entry<String, Integer>> set = grouped.entrySet();
		ArrayList<Entry<String, Integer>> list = new ArrayList<>(set);

		Collections.sort(list, new Comparator<Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> arg0, Entry<String, Integer> arg1) {
				return arg1.getValue() - arg0.getValue();
			}
		});
		ArrayList<String> result = new ArrayList<>();
		for (Entry<String, Integer> x : list) {
			if (x.getValue() > 1)
				result.add(x.getValue() + "x " + x.getKey());
			else
				result.add(x.getKey());
		}
		this.answers = result.toArray(new String[] {});

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
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.NORTH;

		this.add(new JLabel("<html>" + this.fq.name() + "?</html>"), c);
		c.gridy++;
		this.add(new JLabel("<html>" + this.question.getQuestionText() + "</html>"), c);
		c.gridy++;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;

		JPanel content = new JPanel();

		content.setLayout(new GridBagLayout());

		GridBagConstraints c2 = c;
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.NORTH;
		c.insets = new Insets(0, 0, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		for (String answer : this.answers) {
			if (answer.trim().equals(""))
				continue;
			JTextArea label = new JTextArea(" " + answer);
			label.setLineWrap(true);
			label.setWrapStyleWord(true);
			label.setEditable(false);

			label.setBackground(new Color(255, 255, 255));
			label.setOpaque(true);
			Border line = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
			Border empty = new EmptyBorder(2, 2, 2, 10);
			CompoundBorder border = new CompoundBorder(line, empty);
			label.setBorder(border);

			content.add(label, c);
			c.gridy++;
		}
		JScrollPane scroll = new JScrollPane(content,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getVerticalScrollBar().setUnitIncrement(5);
		// scroll.add(content);
		this.add(scroll, c2);
	}

	private static final long serialVersionUID = -548251000779742538L;

	private synchronized static void init() {
		if (out != null)
			return;
		try {
			out = new BufferedWriter(new FileWriter(new File("text.txt")));
		} catch (IOException e) {}
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {}
				try {
					out.close();
				} catch (IOException e) {}
			}

		}).start();
	}

}
