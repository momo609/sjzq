package com.sjzg.rank;

public class RankModel {
	
	private int Index;//���е�λ��
	private String User;//�û���ʵ����
	private String Avatar;//�û�ͷ��
	private float Value;//�����ֵ,����ٷ�,������
	
	private String Title;//����
	private String Content;//����
	private String ID;//Я����Ҫ��ת��ID,����ĿID
	
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
