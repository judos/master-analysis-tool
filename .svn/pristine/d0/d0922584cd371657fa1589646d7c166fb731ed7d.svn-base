package model.questions;

/**
 * @since 17.03.2014
 * @author Julian Schelker
 */
public enum FormQuestions {
	GeneralQuestions(new TextQuestion("Name"), new ScaleQuestion("Age", true)),
	WhatDoYouLikeMore(new ChoiceQuestion("exploring", "fighting"), new ChoiceQuestion(
		"playing with others", "playing alone"), new ChoiceQuestion("aestetics",
		"the story"), new ChoiceQuestion("offensive play style", "defensive play style")),
	WhatIsMoreImportant(new ChoiceQuestion("the character", "the game world"),
		new ChoiceQuestion("improving team skills", "improving your own skills"),
		new ChoiceQuestion("acting (player skills)",
			"interaction (between you and others)")), WhatOtherGenres(new TextQuestion(
		"1."), new TextQuestion("2."), new TextQuestion("3.")), HowHardDidYouFind(
		Scale.hardness, new ScaleQuestion("First encounter with shadow"),
		new ScaleQuestion("Second encounter when the shaking door breaks open"),
		new ScaleQuestion("the top floor at the exit of the port")),
	HowInterestingDidYouFind(Scale.interesting, new ScaleQuestion(
		"Collecting lamps in the level to get energy"), new ScaleQuestion(
		"Lantern to stun shadows"), new ScaleQuestion(
		"Mechanical eye to replenish energy"), new ScaleQuestion(
		"Playing with the encaged shadow"), new ScaleQuestion("Other game elements")),
	HowConcentratedWereYou(Scale.concentrate, new ScaleQuestion(
		"How concentrated were you while trying to solve the level")), HowMuchDidYouLike(
		Scale.like, new ScaleQuestion("the graphics of the avatar"), new ScaleQuestion(
			"the graphics of the world"),
		new ScaleQuestion("the brightness of the level")), DidYouSeeTheStatue(
		new TextQuestion("Did you see the statue?")), WhatWasMostExciting(
		new TextQuestion("What was the most exciting part of the level?")),
	WhatsYourRating(Scale.like, new ScaleQuestion("What's your rating for the game?")),
	OtherFeedback(new TextQuestion("Other feedback?"));

	private Question[] subQuestions;
	private String scaleDescription;

	FormQuestions(Question... sub) {
		this("", sub);
	}

	FormQuestions(String scaleDescription, Question... sub) {
		this.subQuestions = sub;
		this.scaleDescription = scaleDescription;
		for (Question q : sub) {
			q.setOverQuestionTitle(this.name());
		}
	}

	public String getQuestionScaleDescription() {
		return this.scaleDescription;
	}

	public Question getSub(int index) {
		return this.subQuestions[index];
	}

	/**
	 * @return the subQuestions
	 */
	public Question[] getSubQuestions() {
		return this.subQuestions;
	}

}
