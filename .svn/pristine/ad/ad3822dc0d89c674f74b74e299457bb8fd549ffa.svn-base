package model.questions;

import java.io.Serializable;

import view.formDialog.AnswerPanel;

/**
 * @since 17.03.2014
 * @author Julian Schelker
 */
public abstract class Question implements Serializable {

	private String overQuestionTitle;

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof Question))
			return false;
		Question q = (Question) obj;

		return this.getQuestionText().equals(q.getQuestionText())
			&& this.getOverQuestionTitle().equals(q.getOverQuestionTitle());
	}

	public abstract AnswerPanel getAnswerPanel();

	public String getOverQuestionTitle() {
		return this.overQuestionTitle;
	}

	public abstract String getQuestionText();

	@Override
	public int hashCode() {
		return getQuestionText().hashCode() + this.overQuestionTitle.hashCode();
	}

	public void setOverQuestionTitle(String name) {
		this.overQuestionTitle = name;
	}

	private static final long serialVersionUID = -6467902085332783044L;

}
