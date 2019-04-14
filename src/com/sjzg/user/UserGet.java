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

import com.google.gson.JsonObject;
import com.sjzg.database.DBConn;

public class UserGet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UserGet() {
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
		request.setCharacterEncoding("UTF-8");//设置对客户端请求进行重新编码的编码。
		response.setCharacterEncoding("UTF-8");//指定对服务器响应进行重新编码的编码。
		response.addHeader("Access-Control-Allow-Origin", "*");//跨域
		PrintWriter out = response.getWriter();//务必放在最后
		
	
		
		
		
		String Token=request.getParameter("SessionToken");
		String UserID=request.getParameter("UserID");
		

		UserModel DBfindUserByID_result = DBfindUserByID(UserID);
		

		

			
			
		if (DBfindUserByID_result == null){
			//找不到
			out.print("{\"errcode\":101,\"errmsg\":\"找不到用户\"}");
			out.flush();
			out.close();
			return;
		}else {
			String myToken = DBfindUserByID_result.getSessionToken();
			if(myToken.equals(Token)){
			

				JsonObject jsonObject = new JsonObject();
				JsonObject userObject = new JsonObject();
				
				
				userObject.addProperty("UserID", DBfindUserByID_result.getUserID());
				userObject.addProperty("RealName", DBfindUserByID_result.getRealName());
				userObject.addProperty("Role", DBfindUserByID_result.getRole());
				userObject.addProperty("MobilePhone", DBfindUserByID_result.getMobilePhone());		
				userObject.addProperty("Sex", DBfindUserByID_result.getSex());
				userObject.addProperty("Avatar", DBfindUserByID_result.getAvatar());
				userObject.addProperty("NickName", DBfindUserByID_result.getNickName());
				
				jsonObject.addProperty("errcode","0");
				jsonObject.add("data", userObject);
				out.print(jsonObject.toString());
				out.flush();
				out.close();
				return;
			}else{
				out.print("{\"errcode\":101,\"errmsg\":\"TOKEN已过期\"}");
				out.flush();
				out.close();
				return;
			}
			
			
		
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
	public UserModel DBfindUserByID(String UserID) {
	
		String sql="SELECT * FROM User WHERE UserID=?";
		
		UserModel userModel_temp = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "数据库操作异常";
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
				userModel_temp.setPassword(rs.getString("Password"));
				userModel_temp.setMobilePhone(rs.getString("MobilePhone"));
				userModel_temp.setRole(rs.getString("Role"));
				userModel_temp.setSex(rs.getString("Sex"));
				userModel_temp.setAvatar(rs.getString("Avatar"));
				userModel_temp.setNickName(rs.getString("NickName"));
			}
			conn.close();
		} catch (SQLException e) {
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



		return  userModel_temp;
	}

}
