package com.sjzg.question;

public class QuestionModel {

	private int QuestionID;
	private int Type;
	private float Difficulty;
	private int Share;
	private String Content;
	private String Choices;
	private String Answerkey;
	private String Answer;
	private String Image;
	private String Tag;
	private String UserID;
	private String CreateAt;
	private int knowledgeid;
	private String knowledgepoint;
	public String getKnowledgepoint() {
		return knowledgepoint;
	}


	public void setKnowledgepoint(String knowledgepoint) {
		this.knowledgepoint = knowledgepoint;
	}


	public int getKnowledgeid() {
		return knowledgeid;
	}


	public void setKnowledgeid(int knowledgeid) {
		this.knowledgeid = knowledgeid;
	}

	
	public String validate() 
	{
		if(Content==null||Content.equals(""))
		{
			return "Content Error";
		}
		if(Choices==null||Choices.equals(""))
		{
			return "Choices Error";
		}

		if(Answerkey==null||Answerkey.equals(""))
		{
			return "Answerkey Error";
		}
		if(Answer==null||Answer.equals(""))
		{
			return "Answer Error";
		}
		if(Image==null||Image.equals(""))
		{
			return "Image Error";
		}
		if(Tag==null||Tag.equals(""))
		{
			return "Tag Error";
		}
		if(UserID==null||UserID.equals(""))
		{
			return "UserID Error";
		}
		if (Type<1 || Type>5){
			return "Type Error";
		}
		if (Share<0 || Share>1){
			return "Share Error";
		}
		
		return "ok";
		
	}
	
	
	public int getShare() {
		return Share;
	}


	public void setShare(int share) {
		Share = share;
	}


	public int getQuestionID() {
		return QuestionID;
	}
	public void setQuestionID(int questionID) {
		QuestionID = questionID;
	}
	public int getType() {
		return Type;
	}
	public void setType(int type) {
		Type = type;
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
	public String getAnswer() {
		return Answer;
	}
	public void setAnswer(String answer) {
		Answer = answer;
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
	public float getDifficulty() {
		return Difficulty;
	}
	public void setDifficulty(float difficulty) {
		Difficulty = difficulty;
	}
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}


	public String getCreateAt() {
		return CreateAt;
	}


	public void setCreateAt(String createAt) {
		CreateAt = createAt;
	}
	
	
	
	
	
	
	
	
	
	
	
}
