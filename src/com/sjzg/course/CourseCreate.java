package com.sjzg.course;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sjzg.database.DBConn;
import com.sjzg.user.UserModel;

public class CourseCreate extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public CourseCreate() {
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
		String SchoolID=request.getParameter("SchoolID");
		String TeacherID=request.getParameter("TeacherID");
		String StartDate = request.getParameter("StartDate");
		String EndDate = request.getParameter("EndDate");
		String CourseName = request.getParameter("CourseName");
		String Position = request.getParameter("Position");

		
		

		System.out.println("取的CourseName+"+CourseName);

		
		CourseModel courseModel=new CourseModel();
		courseModel.setCourseID(-1);
		courseModel.setTeacherID(TeacherID);
		courseModel.setSchoolID(SchoolID);
		courseModel.setEndDate(EndDate);
		courseModel.setStartDate(StartDate);
		courseModel.setPosition(Position);
		courseModel.setCourseName(CourseName);
		courseModel.setNotice("暂无");
		
		//第二步，检查数据是否有误
		if (!courseModel.validate().equals("ok")){
			out.print("{\"errcode\":101,\"errmsg\":\""+courseModel.validate()+"\"}");
			out.flush();
			out.close();
			return;
		}
		
		
		String DBcreateCourse_result = DBcreateCourse(courseModel);
		
		if (!DBcreateCourse_result.equals("ok")) {
			out.print("{\"errcode\":101,\"errmsg\":\""+DBcreateCourse_result+"\"}");
			out.flush();
			out.close();
			return;
			

		}else {
			out.print("{\"errcode\":0,\"errmsg\":\"创建成功！\"}");
			out.flush();
			out.close();
			return;
		}
	
		
	}

	
	
	
	
	
	
	
	
	
	public String DBcreateCourse(CourseModel courseModel) {
		System.out.println("执行DBcreateCourse");
		String sql="INSERT INTO Course(SchoolID,TeacherID,StartDate,EndDate,Position,CourseName,Notice,CreateAt,UpdateAt) VALUES(?,?,?,?,?,?,?,?,?)";

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

			ps.setString(1, courseModel.getSchoolID());
			ps.setString(2, courseModel.getTeacherID());
			
			SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");
			java.sql.Date sqlDate;
			sqlDate=new java.sql.Date (DateFormat.parse(courseModel.getStartDate()).getTime());
			ps.setDate(3, sqlDate);
			sqlDate=new java.sql.Date (DateFormat.parse(courseModel.getEndDate()).getTime());
			ps.setDate(4, sqlDate);
			
			
			
			ps.setString(5, courseModel.getPosition());
			ps.setString(6, courseModel.getCourseName());
			ps.setString(7, courseModel.getNotice());
			ps.setTimestamp(8, nowTimestamp);
			ps.setTimestamp(9, nowTimestamp);
			influence+=ps.executeUpdate();
			

			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			errorString+=e.getLocalizedMessage();
		}catch (ParseException e) {
			// TODO Auto-generated catch block
			errorString+=e.getLocalizedMessage();
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
