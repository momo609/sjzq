package com.sjzg.answer;

public class AnswerModel {

	private int ID;
	private String UserID;
	private int QuestionID;
	private int TestID;
	private float TimeUsed;
	private String Track;
	private float Grade;
	private String Unfocus;
	private String Appear;
	private String UserAnswer;
	private int LookBackTime;
	private int collection;
	
	public int getCollection() {
		return collection;
	}
	public void setCollection(int collection) {
		this.collection = collection;
	}
	public int getLookBackTime() {
		return LookBackTime;
	}
	public void setLookBackTime(int lookBackTime) {
		LookBackTime = lookBackTime;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public int getQuestionID() {
		return QuestionID;
	}
	public void setQuestionID(int questionID) {
		QuestionID = questionID;
	}
	public int getTestID() {
		return TestID;
	}
	public void setTestID(int testID) {
		TestID = testID;
	}

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

	public String getUnfocus() {
		return Unfocus;
	}
	public void setUnfocus(String unfocus) {
		Unfocus = unfocus;
	}
	public String getAppear() {
		return Appear;
	}
	public void setAppear(String appear) {
		Appear = appear;
	}
	public String getUserAnswer() {
		return UserAnswer;
	}
	public void setUserAnswer(String userAnswer) {
		UserAnswer = userAnswer;
	}

	
	
	
	
	
	
}
