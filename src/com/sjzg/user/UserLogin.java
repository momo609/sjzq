package com.sjzg.user;

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


import com.google.gson.JsonObject;
import com.sjzg.database.DBConn;

public class UserLogin extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UserLogin() {
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
		request.setCharacterEncoding("UTF-8");//���öԿͻ�������������±���ı��롣
		response.setCharacterEncoding("UTF-8");//ָ���Է�������Ӧ�������±���ı��롣
		response.addHeader("Access-Control-Allow-Origin", "*");//����
		PrintWriter out = response.getWriter();//��ط������

		
		//��һ����ȡ����
		String UserID=request.getParameter("UserID");
		String Password=request.getParameter("Password");
		


		UserModel userModel=new UserModel();
		userModel.setUserID(UserID);
		userModel.setPassword(Password);
		userModel.setMobilePhone(UserID);
		
		userModel.setRealName("%null%");
		userModel.setSessionToken("%null%");
		userModel.setRole("%null%");
		

		userModel.setSex("null");
		userModel.setAvatar("%null%");
		userModel.setNickName("%null%");
		//�ڶ�������������Ƿ�����
		if (!userModel.validate().equals("ok")){
			out.print("{\"errcode\":201,\"errmsg\":\""+userModel.validate()+"\"}");
			out.flush();
			out.close();
			return;
		}
		
		
		//�����������ݿ����

		
		
		
		UserModel DBfindUser_result = DBfindUser("10593"+userModel.getUserID());
		UserModel DBfindUserByPhone_result = DBfindUserByPhone(userModel.getMobilePhone());
		if (DBfindUser_result == null && DBfindUserByPhone_result == null){
			out.print("{\"errcode\":101,\"errmsg\":\"��½�˻�������\"}");
			out.flush();
			out.close();
			return;
		}
		
		if (DBfindUser_result != null ){
			//��ID�ҵ��û�
			if(DBfindUser_result.getPassword().equals(Password)){
				//�ж�����
				String newSessionToken = DBfindUser_result.createSessionToken(DBfindUser_result.getUserID());
				String DBupdateSessionToken_result = DBupdateSessionToken(newSessionToken, DBfindUser_result.getSessionToken());
				
				if (DBupdateSessionToken_result.equals("ok")){
					//�ɹ�ˢ������
					JsonObject jsonObject = new JsonObject();
					JsonObject userObject = new JsonObject();	
					userObject.addProperty("UserID", DBfindUser_result.getUserID());
					userObject.addProperty("SessionToken", newSessionToken);
					userObject.addProperty("Role", DBfindUser_result.getRole());
					userObject.addProperty("RealName", DBfindUser_result.getRealName());
					jsonObject.addProperty("errcode","0");
					jsonObject.add("data", userObject);
					out.print(jsonObject.toString());
					out.flush();
					out.close();
					return;
					
				}
			}else {

				out.print("{\"errcode\":101,\"errmsg\":\"��½�������\"}");
				out.flush();
				out.close();
				return;
			}
		}
		
		
		if (DBfindUserByPhone_result != null ){
			//��Phone���û�
			if(DBfindUserByPhone_result.getPassword().equals(Password)){
				//�ж�����
				String newSessionToken = DBfindUserByPhone_result.createSessionToken(DBfindUserByPhone_result.getUserID());
				String DBupdateSessionToken_result = DBupdateSessionToken(newSessionToken, DBfindUserByPhone_result.getSessionToken());
				
				if (DBupdateSessionToken_result.equals("ok")){
					//�ɹ�ˢ������
					JsonObject jsonObject = new JsonObject();
					JsonObject userObject = new JsonObject();	
					userObject.addProperty("UserID", DBfindUserByPhone_result.getUserID());
					userObject.addProperty("SessionToken", newSessionToken);
					userObject.addProperty("Role", DBfindUserByPhone_result.getRole());
					userObject.addProperty("RealName", DBfindUserByPhone_result.getRealName());
					jsonObject.addProperty("errcode","0");
					jsonObject.add("data", userObject);
					out.print(jsonObject.toString());
					out.flush();
					out.close();
					return;
					
				}
			}else {

				out.print("{\"errcode\":101,\"errmsg\":\"��½�������\"}");
				out.flush();
				out.close();
				return;
			}
		}
		
		
	
		

		out.print("{\"errcode\":101,\"errmsg\":\"ϵͳ�쳣\"}");
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
	public String DBupdateSessionToken(String newSessionToken,String oldSessionToken) {

		String sql="UPDATE User SET SessionToken = ? , UpdateAt = ?  WHERE SessionToken=?";

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "���ݿ�����쳣";
		int influence=0;//Ӱ�������
		Date nowDate = new Date();
		java.sql.Timestamp nowTimestamp = new java.sql.Timestamp(nowDate.getTime());
		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
			ps.setString(1, newSessionToken);
			ps.setTimestamp(2, nowTimestamp);
			ps.setString(3, oldSessionToken);
			influence=ps.executeUpdate();
			conn.close();
		} catch (SQLException e) {
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
			if(influence<1)
			{
				return errorString;
			}
			else 
			{
				return "ok";
			}
			
	}
	
}
