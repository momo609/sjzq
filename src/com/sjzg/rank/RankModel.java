package com.sjzg.rank;

public class RankModel {
	
	private int Index;//排列的位置
	private String User;//用户真实姓名
	private String Avatar;//用户头像
	private float Value;//具体的值,如多少分,多少秒
	
	private String Title;//标题
	private String Content;//内容
	private String ID;//携带需要跳转的ID,如题目ID
	
	public int getIndex() {
		return Index;
	}
	public void setIndex(int index) {
		this.Index = index;
	}
	public String getUser() {
		return User;
	}
	public void setUser(String user) {
		User = user;
	}
	public float getValue() {
		return Value;
	}
	public void setValue(float value) {
		Value = value;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getAvatar() {
		return Avatar;
	}
	public void setAvatar(String avatar) {
		Avatar = avatar;
	}
	
	
	
	
}
