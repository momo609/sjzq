package com.sjzg.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.sjzg.database.DBConn;




public class UserRegist extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UserRegist() {
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
		String MobilePhone=request.getParameter("MobilePhone");
		String Password=request.getParameter("Password");
		String RealName=request.getParameter("RealName");
		String Role=request.getParameter("Role");
		
		
		
		System.out.println("取的UserID+"+UserID);

		
		UserModel userModel=new UserModel();
		userModel.setUserID(UserID);
		userModel.setPassword(Password);
		userModel.setRealName(RealName);
		userModel.setMobilePhone(MobilePhone);
		
		
		userModel.setRole(Role);
		userModel.setSex("null");
		userModel.setAvatar("https://sjzg-1252169987.cos.ap-guangzhou.myqcloud.com/default-avatar.png");
		userModel.setNickName("新朋友");
		
		

		userModel.setSessionToken(userModel.createSessionToken(UserID));



		
		//第二步，检查数据是否有误
		if (!userModel.validate().equals("ok")){
			out.print("{\"errcode\":201,\"errmsg\":\""+userModel.validate()+"\"}");
			out.flush();
			out.close();
			return;
		}
		
		
		
		//第三步，数据库操作

		UserModel DBfindUser_result = DBfindUser(userModel.getUserID());
		if (DBfindUser_result != null){
			out.print("{\"errcode\":101,\"errmsg\":\"用户已存在\"}");
			out.flush();
			out.close();
			return;
		}
		
		
		
		String DBcreateUser_result = DBcreateUser(userModel);
			if (!DBcreateUser_result.equals("error")) {
				out.print("{\"errcode\":0,\"SessionToken\":\""+DBcreateUser_result+"\"}");
				out.flush();
				out.close();
				return;

			}else {
				out.print("{\"errcode\":301,\"errmsg\":\"系统异常\"}");
				out.flush();
				out.close();
				return;

			}
		
		

	}

	
	
	
	
	public UserModel DBfindUser(String UserID) {
		System.out.println("完成执行DBfindUser");
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
	
	public String DBcreateUser(UserModel userModel) {
		System.out.println("执行DBcreateUser");
		String sql="INSERT INTO User(UserID,RealName,SessionToken,MobilePhone,Password,Role,Sex,Avatar,NickName,CreateAt,UpdateAt) VALUES(?,?,?,?,?,?,?,?,?,?,?)";


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
			ps.setString(1, userModel.getUserID());
			ps.setString(2, userModel.getRealName());
			ps.setString(3, userModel.getSessionToken());
			ps.setString(4, userModel.getMobilePhone());
			ps.setString(5, userModel.getPassword());
			ps.setString(6, userModel.getRole());
			ps.setString(7, userModel.getSex());
			ps.setString(8, userModel.getAvatar());
			ps.setString(9, userModel.getNickName());
			ps.setTimestamp(10, nowTimestamp);
			ps.setTimestamp(11, nowTimestamp);
			influence+=ps.executeUpdate();
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			errorString+=e.getMessage();
		}  finally {
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
				return "error";
			}
			else 
			{
				return userModel.getSessionToken();
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
