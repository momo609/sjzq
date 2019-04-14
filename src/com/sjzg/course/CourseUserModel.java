package com.sjzg.course;

public class CourseUserModel {

	//Course的数据模型
	private int CourseID;
	private String SchoolID;
	private String TeacherID;
	private String StartDate;
	private String EndDate;
	private String Position;
	private String CourseName;
	private String Notice;
	
	//User的数据模型
	private String RealName;
	private String Sex;
	private String Avatar;
	private String NickName;
	
	//R_user_course的数据
	private int State;
	private String VerifyInfo;
	
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
	public String getRealName() {
		return RealName;
	}
	public void setRealName(String realName) {
		RealName = realName;
	}
	public String getSex() {
		return Sex;
	}
	public void setSex(String sex) {
		Sex = sex;
	}
	public String getAvatar() {
		return Avatar;
	}
	public void setAvatar(String avatar) {
		Avatar = avatar;
	}
	public String getNickName() {
		return NickName;
	}
	public void setNickName(String nickName) {
		NickName = nickName;
	}
	public int getState() {
		return State;
	}
	public void setState(int state) {
		State = state;
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
	public String getVerifyInfo() {
		return VerifyInfo;
	}
	public void setVerifyInfo(String verifyInfo) {
		VerifyInfo = verifyInfo;
	}
	public String getNotice() {
		return Notice;
	}
	public void setNotice(String notice) {
		Notice = notice;
	}
	
	
	
	
	
}
