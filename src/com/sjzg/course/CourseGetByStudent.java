package com.sjzg.course;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sjzg.database.DBConn;
import com.sun.org.apache.regexp.internal.recompile;

public class CourseGetByStudent extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public CourseGetByStudent() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
		
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		request.setCharacterEncoding("UTF-8");//设置对客户端请求进行重新编码的编码。
		response.setCharacterEncoding("UTF-8");//指定对服务器响应进行重新编码的编码。
		response.addHeader("Access-Control-Allow-Origin", "*");//跨域
		PrintWriter out = response.getWriter();//务必放在最后
		
		//第一步，取数据
		String UserID=request.getParameter("UserID");
		System.out.println("取的UserID+"+UserID);
		
		
		ArrayList<CourseUserModel> DBfindCourseUser_result = DBfindCourseUser(UserID);
		
		
		if (DBfindCourseUser_result.isEmpty()){
			out.print("{\"errcode\":100,\"errmsg\":\"查不到课程\"}");
			out.flush();
			out.close();
			return;
		}else {
			JsonObject jsonObject = new JsonObject();
			JsonArray courseJsonArray = new JsonArray();
			int listCount = DBfindCourseUser_result.size();
			for (int i=0;i<listCount;i++){
				JsonObject tempObject = new JsonObject();
				
				tempObject.addProperty("CourseID", DBfindCourseUser_result.get(i).getCourseID());
				tempObject.addProperty("SchoolID", DBfindCourseUser_result.get(i).getSchoolID());
				tempObject.addProperty("StartDate", DBfindCourseUser_result.get(i).getStartDate());
				tempObject.addProperty("EndDate", DBfindCourseUser_result.get(i).getEndDate());
				tempObject.addProperty("Position", DBfindCourseUser_result.get(i).getPosition());
				tempObject.addProperty("CourseName", DBfindCourseUser_result.get(i).getCourseName());
				tempObject.addProperty("Notice", DBfindCourseUser_result.get(i).getNotice());
				
				tempObject.addProperty("RealName", DBfindCourseUser_result.get(i).getRealName());
				tempObject.addProperty("Sex", DBfindCourseUser_result.get(i).getSex());
				tempObject.addProperty("Avatar", DBfindCourseUser_result.get(i).getAvatar());
				tempObject.addProperty("NickName", DBfindCourseUser_result.get(i).getNickName());
				
				
				
				tempObject.addProperty("State", DBfindCourseUser_result.get(i).getState());
				
				
				courseJsonArray.add(tempObject);
			}
			
			
			

			
			
			jsonObject.addProperty("errcode","0");
			jsonObject.add("threads", courseJsonArray);
			out.print(jsonObject.toString());
			out.flush();
			out.close();
			return;
		}
		
		
		
	}

	
	
	
	
	

	public ArrayList<CourseUserModel> DBfindCourseUser(String UserID) {
		System.out.println("完成执行DBfindCourse");
		String sql="SELECT Course.*,User.*,R_user_course.State FROM R_user_course , Course , User " +
				"WHERE R_user_course.UserID=? " +
				"AND R_user_course.CourseID=Course.CourseID " +
				"AND Course.TeacherID = User.UserID";

		ArrayList<CourseUserModel> courseList = new ArrayList<CourseUserModel>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "数据库操作异常";
		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
			ps.setString(1, UserID);
			 rs=ps.executeQuery();
			
			while(rs.next())
			{
				CourseUserModel courseUserModel_temp = new CourseUserModel();

				courseUserModel_temp.setCourseID(rs.getInt("CourseID"));
				courseUserModel_temp.setSchoolID(rs.getString("SchoolID"));
				courseUserModel_temp.setPosition(rs.getString("Position"));
				courseUserModel_temp.setCourseName(rs.getString("CourseName"));
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(rs.getDate("StartDate"));
				courseUserModel_temp.setStartDate((calendar.get(Calendar.YEAR))+"-"+(calendar.get(Calendar.MONDAY)+1) + "-" +(calendar.get(Calendar.DAY_OF_MONTH)));
				
				calendar.setTime(rs.getDate("EndDate"));
				courseUserModel_temp.setEndDate((calendar.get(Calendar.YEAR))+"-"+(calendar.get(Calendar.MONDAY)+1) + "-" +(calendar.get(Calendar.DAY_OF_MONTH)));
				courseUserModel_temp.setNotice(rs.getString("Notice"));
				courseUserModel_temp.setRealName(rs.getString("RealName"));
				courseUserModel_temp.setSex(rs.getString("Sex"));
				courseUserModel_temp.setAvatar(rs.getString("Avatar"));
				courseUserModel_temp.setNickName(rs.getString("NickName"));
				
				courseUserModel_temp.setState(rs.getInt("State"));
				
				
				courseList.add(courseUserModel_temp);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
		    if (rs != null) {
		        try {
		            rs.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		    if (ps != null) {
		        try {
		            ps.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		    if (conn != null) {
		        try {
		            conn.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		}

		return  courseList;
	}
	
	
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
