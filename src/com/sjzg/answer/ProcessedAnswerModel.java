package com.sjzg.answer;

public class ProcessedAnswerModel {
	private String UserID;
	private int TestID;
	private String TimeUsed;
	private String Track;
	private float Grade;
	private String UserAnswer;
	private String LookBackTime;
	private int averagelookbacktime;
	
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public int getTestID() {
		return TestID;
	}
	public void setTestID(int testID) {
		TestID = testID;
	}
	public String getTimeUsed() {
		return TimeUsed;
	}
	public void setTimeUsed(String timeUsed) {
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
	public String getUserAnswer() {
		return UserAnswer;
	}
	public void setUserAnswer(String userAnswer) {
		UserAnswer = userAnswer;
	}
	public String getLookBackTime() {
		return LookBackTime;
	}
	public void setLookBackTime(String lookBackTime) {
		LookBackTime = lookBackTime;
	}
	public int getAveragelookbacktime() {
		return averagelookbacktime;
	}
	public void setAveragelookbacktime(int averagelookbacktime) {
		this.averagelookbacktime = averagelookbacktime;
	}
	@Override
	public String toString() {
		return "ProcessedAnswerModel [ UserID=" + UserID
				+ ", TestID=" + TestID + ", TimeUsed=" + TimeUsed + ", Track="
				+ Track + ", Grade=" + Grade + ", UserAnswer=" + UserAnswer
				+ ", LookBackTime=" + LookBackTime + ", averagelookbacktime="
				+ averagelookbacktime + "]";
	}
	
}
