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

import com.google.gson.JsonObject;
import com.sjzg.database.DBConn;


public class CourseGetByID extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public CourseGetByID() {
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
		String CourseIDStr=request.getParameter("CourseID");
		
		int CourseID = -1;
		try {
			CourseID = Integer.parseInt(CourseIDStr);
		} catch (Exception e) {
			out.print("{\"errcode\":101,\"errmsg\":\"传入的课程ID有误\"}");
			out.flush();
			out.close();
			return;
		}
		System.out.println("取的CourseID+"+CourseID);
		
		
		CourseUserModel DBfindCourseUser_result = DBfindCourseUser(CourseID);
		if (DBfindCourseUser_result == null){
			out.print("{\"errcode\":101,\"errmsg\":\"获取不到课程\"}");
			out.flush();
			out.close();
			return;
		}else {
			JsonObject jsonObject = new JsonObject();
			JsonObject tempObject = new JsonObject();
			
			tempObject.addProperty("CourseID", DBfindCourseUser_result.getCourseID());
			tempObject.addProperty("SchoolID", DBfindCourseUser_result.getSchoolID());
			tempObject.addProperty("StartDate", DBfindCourseUser_result.getStartDate());
			tempObject.addProperty("EndDate", DBfindCourseUser_result.getEndDate());
			tempObject.addProperty("Position", DBfindCourseUser_result.getPosition());
			tempObject.addProperty("CourseName", DBfindCourseUser_result.getCourseName());
			tempObject.addProperty("StartDate", DBfindCourseUser_result.getStartDate());
			tempObject.addProperty("EndDate", DBfindCourseUser_result.getEndDate());
			tempObject.addProperty("Notice", DBfindCourseUser_result.getNotice());
			
			tempObject.addProperty("RealName", DBfindCourseUser_result.getRealName());
			tempObject.addProperty("Sex", DBfindCourseUser_result.getSex());
			tempObject.addProperty("Avatar", DBfindCourseUser_result.getAvatar());
			tempObject.addProperty("NickName", DBfindCourseUser_result.getNickName());
			
			
			
			jsonObject.addProperty("errcode","0");
			jsonObject.add("courseTeacherInfo", tempObject);
			out.print(jsonObject.toString());
			out.flush();
			out.close();
			return;
		}
	}

	
	
	public CourseUserModel DBfindCourseUser(int CourseID) {
		System.out.println("完成执行DBfindCourseUser");
		String sql="SELECT Course.*,User.* FROM  Course , User " +
				"WHERE Course.CourseID=? " +
				"AND Course.TeacherID = User.UserID";

		CourseUserModel courseUserModel_temp = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "数据库操作异常";
		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
			ps.setInt(1, CourseID);
			 rs=ps.executeQuery();
			
			while(rs.next())
			{
				courseUserModel_temp = new CourseUserModel();;

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

		return  courseUserModel_temp;
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
