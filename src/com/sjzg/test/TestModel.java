package com.sjzg.test;

public class TestModel {
	private int TestID;
	private int CourseID;
	private int PaperID;
	private int TestTime;
	private String State;
	private String TestName;
	private String StartTime;
	private String EndTime;
	private String CreateAt;
	private String UpdateAt;
	
	private String ExtendContent;//方便传输的扩展内容
	
	public String validate() 
	{
		if(TestName==null||TestName.equals(""))
		{
			return "TestName Error";
		}


		if(StartTime==null||StartTime.equals(""))
		{
			return "StartTime Error";
		}
		if(EndTime==null||EndTime.equals(""))
		{
			return "Questions Error";
		}


		return "ok";
		
	}
	
	
	public int getTestID() {
		return TestID;
	}


	public void setTestID(int testID) {
		TestID = testID;
	}


	public int getCourseID() {
		return CourseID;
	}
	public void setCourseID(int courseID) {
		CourseID = courseID;
	}
	public int getPaperID() {
		return PaperID;
	}
	public void setPaperID(int paperID) {
		PaperID = paperID;
	}
	public int getTestTime() {
		return TestTime;
	}
	public void setTestTime(int testTime) {
		TestTime = testTime;
	}
	public String getTestName() {
		return TestName;
	}
	public void setTestName(String testName) {
		TestName = testName;
	}
	public String getStartTime() {
		return StartTime;
	}
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}
	public String getEndTime() {
		return EndTime;
	}
	public void setEndTime(String endTime) {
		EndTime = endTime;
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


	public String getState() {
		return State;
	}


	public void setState(String state) {
		State = state;
	}


	public String getExtendContent() {
		return ExtendContent;
	}


	public void setExtendContent(String extendContent) {
		ExtendContent = extendContent;
	}



	
	
	
}
