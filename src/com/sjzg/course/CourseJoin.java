package com.sjzg.course;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sjzg.database.DBConn;
import com.sjzg.user.UserModel;

public class CourseJoin extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public CourseJoin() {
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
		
		String VerifyInfo=request.getParameter("VerifyInfo");

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
		
		
		
		//第三步，数据库操作
		String DBjoinCourse_result = DBjoinCourse(UserID,CourseID,VerifyInfo);
		
		if (!DBjoinCourse_result.equals("ok")) {
			out.print("{\"errcode\":101,\"errmsg\":\"加入失败\"}");
			out.flush();
			out.close();
			return;
			

		}else {
			out.print("{\"errcode\":0,\"errmsg\":\"加入成功！\"}");
			out.flush();
			out.close();
			return;
		}
		
		
	}

	
	
	
	
	
	
	
	
	public String DBjoinCourse(String UserID,int CourseID,String VerifyInfo) {
		System.out.println("执行DBjoinCourse");
		String sql="INSERT INTO R_user_course(UserID,CourseID,CreateAt,UpdateAt,State,VerifyInfo) VALUES(?,?,?,?,?,?)";

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "数据库操作异常";
		int influence=0;//影响的行数
		Date nowDate = new Date();
		java.sql.Timestamp nowTimestamp = new java.sql.Timestamp(nowDate.getTime());
		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
			ps.setString(1, UserID);
			ps.setInt(2, CourseID);
			ps.setTimestamp(3, nowTimestamp);
			ps.setTimestamp(4, nowTimestamp);
			ps.setInt(5, 1);
			ps.setString(6, VerifyInfo);
			influence=ps.executeUpdate();
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			errorString+=e.getLocalizedMessage();
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
			if(influence<1)
			{
				return errorString;
			}
			else 
			{
				return "ok";
			}
			
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
