package com.sjzg.course;

public class CourseModel {

	//Course的数据模型
	private int CourseID;
	private String SchoolID;
	private String TeacherID;
	private String StartDate;
	private String EndDate;
	private String Position;
	private String CourseName;
	private String Notice;
	
	public String validate() 
	{
		
		if(SchoolID==null||SchoolID.equals(""))
		{
			return "SchoolID Error";
		}

		if(TeacherID==null||TeacherID.equals(""))
		{
			return "TeacherID Error";
		}
		if(Position==null||Position.equals(""))
		{
			return "Position Error";
		}
		if(StartDate==null||StartDate.equals(""))
		{
			return "StartDate Error";
		}
		if(EndDate==null||EndDate.equals(""))
		{
			return "EndDate Error";
		}
		if(CourseName==null||CourseName.equals(""))
		{
			return "CourseName Error";
		}
		if(Notice==null||Notice.equals(""))
		{
			return "Notice Error";
		}
		return "ok";
		
	}
	
	
	public int getCourseID() {
		return CourseID;
	}
	public void setCourseID(int courseID) {
		CourseID = courseID;
	}
	public String getSchoolID() {
		return SchoolID;
	}
	public void setSchoolID(String schoolID) {
		SchoolID = schoolID;
	}
	public String getTeacherID() {
		return TeacherID;
	}
	public void setTeacherID(String teacherID) {
		TeacherID = teacherID;
	}

	public String getStartDate() {
		return StartDate;
	}


	public void setStartDate(String startDate) {
		StartDate = startDate;
	}


	public String getEndDate() {
		return EndDate;
	}


	public void setEndDate(String endDate) {
		EndDate = endDate;
	}


	public String getPosition() {
		return Position;
	}
	public void setPosition(String position) {
		Position = position;
	}
	public String getCourseName() {
		return CourseName;
	}
	public void setCourseName(String courseName) {
		CourseName = courseName;
	}


	public String getNotice() {
		return Notice;
	}


	public void setNotice(String notice) {
		Notice = notice;
	}
	
	
	
	
	
	
	
}
