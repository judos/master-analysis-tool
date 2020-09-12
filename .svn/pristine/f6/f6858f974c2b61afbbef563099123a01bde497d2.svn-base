package controller;

import model.Data;
import model.Log;
import model.questions.Evaluation;
import view.formDialog.EvaluationForm;

/**
 * @since 17.03.2014
 * @author Julian Schelker
 */
public class ImportQuestionsController {
	private Data data;

	public ImportQuestionsController(Data data) {
		this.data = data;

		searchForMissingQuestions();
	}

	private void searchForMissingQuestions() {
		for (Log l : this.data.getLogs()) {
			if (!l.hasEvaluation()) {
				try {
					Evaluation e = EvaluationForm.promptEvaluation(l.getName());
					if (e != null)
						l.setEvaluation(e);
					else
						break;
				} catch (Exception e) {
					System.err.println("Evaluation for " + l.getName() + " not finished");
					e.printStackTrace();
					break;
				}
			}
		}
	}
}
