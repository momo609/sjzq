package com.sjzg.answer;

public class AnswerQuestionModel {

	private int QuestionID;
	
	//Answer 模型
	private float TimeUsed;
	private String Track;
	private float Grade;
	private int UnfocusDuration;
	private int UnfocusCount;
	private String UserAnswer;
	private String Answer;
	
	public String getAnswer() {
		return Answer;
	}
	public void setAnswer(String answer) {
		Answer = answer;
	}
	//question 模型
	private int Type;
	private float Difficulty;
	private int Share;
	private String Content;
	private String Choices;
	private String Answerkey;
	private String Image;
	private String Tag;

	public float getTimeUsed() {
		return TimeUsed;
	}
	public void setTimeUsed(float timeUsed) {
		TimeUsed = timeUsed;
	}
	public String getTrack() {
		return Track;
	}
	public void setTrack(String track) {
		Track = track;
	}
	public float getGrade() {
		return Grade;
	}
	public void setGrade(float grade) {
		Grade = grade;
	}
	public int getUnfocusDuration() {
		return UnfocusDuration;
	}
	public void setUnfocusDuration(int unfocusDuration) {
		UnfocusDuration = unfocusDuration;
	}
	public int getUnfocusCount() {
		return UnfocusCount;
	}
	public void setUnfocusCount(int unfocusCount) {
		UnfocusCount = unfocusCount;
	}

	public String getUserAnswer() {
		return UserAnswer;
	}
	public void setUserAnswer(String userAnswer) {
		UserAnswer = userAnswer;
	}
	
	public int getType() {
		return Type;
	}
	public void setType(int type) {
		Type = type;
	}
	public float getDifficulty() {
		return Difficulty;
	}
	public void setDifficulty(float difficulty) {
		Difficulty = difficulty;
	}
	public int getShare() {
		return Share;
	}
	public void setShare(int share) {
		Share = share;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getChoices() {
		return Choices;
	}
	public void setChoices(String choices) {
		Choices = choices;
	}
	public String getAnswerkey() {
		return Answerkey;
	}
	public void setAnswerkey(String answerkey) {
		Answerkey = answerkey;
	}
	public String getImage() {
		return Image;
	}
	public void setImage(String image) {
		Image = image;
	}
	public String getTag() {
		return Tag;
	}
	public void setTag(String tag) {
		Tag = tag;
	}
	public int getQuestionID() {
		return QuestionID;
	}
	public void setQuestionID(int questionID) {
		QuestionID = questionID;
	}


	
	
	
}
