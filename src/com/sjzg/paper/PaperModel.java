package com.sjzg.paper;

public class PaperModel {
	private int PaperID;
	private String Title;
	private String Description;
	private String Tag;
	private String Questions;
	private String UserID;
	private String CreateAt;
	private String UpdateAt;
	
	
	public String validate() 
	{
		if(Title==null||Title.equals(""))
		{
			return "Title Error";
		}
		if(Description==null||Description.equals(""))
		{
			return "Description Error";
		}

		if(Tag==null||Tag.equals(""))
		{
			return "Tag Error";
		}
		if(Questions==null||Questions.equals(""))
		{
			return "Questions Error";
		}
		if(UserID==null||UserID.equals(""))
		{
			return "UserID Error";
		}

		return "ok";
		
	}
	
	
	public int getPaperID() {
		return PaperID;
	}
	public void setPaperID(int paperID) {
		PaperID = paperID;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getTag() {
		return Tag;
	}
	public void setTag(String tag) {
		Tag = tag;
	}
	public String getQuestions() {
		return Questions;
	}
	public void setQuestions(String questions) {
		Questions = questions;
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
	public String getUpdateAt() {
		return UpdateAt;
	}
	public void setUpdateAt(String updateAt) {
		UpdateAt = updateAt;
	}
	
	
	
	
	
}
