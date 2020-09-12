package model.questions;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @since 17.03.2014
 * @author Julian Schelker
 */
public class Evaluation implements Serializable {

	private HashMap<Question, String> values;

	public Evaluation() {
		this.values = new HashMap<Question, String>();
	}

	public void answerQuestionWith(Question question, String answer) {
		this.values.put(question, answer);

	}

	public void dump() {
		for (FormQuestions q : FormQuestions.values()) {
			System.out.println(q.toString());
			for (Question qq : q.getSubQuestions()) {
				System.out.println(qq.getQuestionText());
				System.out.println(this.values.get(qq));
			}
		}
	}

	public String getAnswerForQuestion(Question q) {
		return this.values.get(q);
	}

	private static final long serialVersionUID = -2917944580171297941L;

}
