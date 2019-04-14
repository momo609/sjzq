package com.sjzg.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sjzg.database.DBConn;

public class UserRegistCheck extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UserRegistCheck() {
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

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
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
		request.setCharacterEncoding("UTF-8");//���öԿͻ�������������±���ı��롣
		response.setCharacterEncoding("UTF-8");//ָ���Է�������Ӧ�������±���ı��롣
		response.addHeader("Access-Control-Allow-Origin", "*");//����
		PrintWriter out = response.getWriter();//��ط������
		
		
		//��һ����ȡ����
		String UserID=request.getParameter("UserID");
		String MobilePhone=request.getParameter("MobilePhone");
		
		
		
		UserModel DBfindUser_result = DBfindUser("10593"+UserID);
		UserModel DBfindUserByPhone_result = DBfindUserByPhone(MobilePhone);
		
		if (DBfindUser_result != null ){
			out.print("{\"errcode\":101,\"errmsg\":\"ѧ���Ѵ���,����ϵ�ͷ���\"}");
			out.flush();
			out.close();
			return;
		}
		
		if (DBfindUserByPhone_result != null ){
			out.print("{\"errcode\":101,\"errmsg\":\"�ֻ����Ѵ���\"}");
			out.flush();
			out.close();
			return;
		}
		
		out.print("{\"errcode\":0,\"errmsg\":\"�ɹ�\"}");
		out.flush();
		out.close();
		return;
		
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
	
	
	public UserModel DBfindUser(String UserID) {
		System.out.println("���ִ��DBfindUser");
		String sql="SELECT * FROM User WHERE UserID=?";
		UserModel userModel_temp = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "���ݿ�����쳣";
		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
			ps.setString(1, UserID);
			 rs=ps.executeQuery();
			if(rs.next())
			{
				userModel_temp = new UserModel();
				userModel_temp.setUserID(rs.getString("UserID"));
				userModel_temp.setRealName(rs.getString("RealName"));
				userModel_temp.setSessionToken(rs.getString("SessionToken"));
				userModel_temp.setMobilePhone(rs.getString("MobilePhone"));
				userModel_temp.setPassword(rs.getString("Password"));
				userModel_temp.setRole(rs.getString("Role"));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			errorString+=e.getMessage();
		} finally {
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

		return  userModel_temp;
	}
	public UserModel DBfindUserByPhone(String UserID) {
		System.out.println("���ִ��DBfindUser");
		String sql="SELECT * FROM User WHERE MobilePhone=?";
		UserModel userModel_temp = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "���ݿ�����쳣";
		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
			ps.setString(1, UserID);
			 rs=ps.executeQuery();
			if(rs.next())
			{
				userModel_temp = new UserModel();
				userModel_temp.setUserID(rs.getString("UserID"));
				userModel_temp.setRealName(rs.getString("RealName"));
				userModel_temp.setSessionToken(rs.getString("SessionToken"));
				userModel_temp.setMobilePhone(rs.getString("MobilePhone"));
				userModel_temp.setPassword(rs.getString("Password"));
				userModel_temp.setRole(rs.getString("Role"));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			errorString+=e.getMessage();
		} finally {
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

		return  userModel_temp;
	}
}
