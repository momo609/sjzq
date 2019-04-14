package com.sjzg.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.sql.*;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sjzg.database.DBConn;

public class UserUpdateInfo extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UserUpdateInfo() {
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
		
		
		String UserID=request.getParameter("UserID");

		String Sex=request.getParameter("Sex");
		String Avatar=request.getParameter("Avatar");
		String NickName=request.getParameter("NickName");
		NickName = filterOffUtf8Mb4(NickName);
	
		
		UserModel userModel=new UserModel();
		userModel.setUserID(UserID);
		userModel.setSex(Sex);
		userModel.setAvatar(Avatar);
		userModel.setNickName(NickName);

		
		String DBupdateUser_result = DBupdateUser(userModel);
		if (DBupdateUser_result.equals("ok")) {
			out.print("{\"errcode\":0,\"errmsg\":\"成功！\"}");
			out.flush();
			out.close();
			return;

		}else {
			out.print("{\"errcode\":101,\"errmsg\":\""+DBupdateUser_result+"\"}");
			out.flush();
			out.close();
			return;

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
	public String DBupdateUser(UserModel userModel) {
	
		//注意，由于直接删除题目会导致关联题目的数据出现错乱，因此直接把题目所属的UserID改了
		String sql="UPDATE User SET Sex = ? , Avatar = ? , NickName = ?  , UpdateAt = ? WHERE UserID=? ";
		System.out.println("DBupdateUser");
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
	
			 
				ps.setString(1, userModel.getSex());
				ps.setString(2, userModel.getAvatar());
				ps.setString(3, userModel.getNickName());
				
				 ps.setTimestamp(4, nowTimestamp); 
			ps.setString(5, userModel.getUserID());
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
				return 	errorString;
			}
			else 
			{
				return "ok";
			}
			
	}
	

	static public String filterOffUtf8Mb4(String text) throws UnsupportedEncodingException {
		byte[] bytes = text.getBytes("UTF-8");
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);

		int i = 0;
		while (i < bytes.length) {
			short b = bytes[i];
			if (b > 0) {
				buffer.put(bytes[i++]);
				continue;
			}
			b += 256;
			if ((b ^ 0xC0) >> 4 == 0) {
				buffer.put(bytes, i, 2);
				i += 2;
			}
			else if ((b ^ 0xE0) >> 4 == 0) {
				buffer.put(bytes, i, 3);
				i += 3;
			}
			else if ((b ^ 0xF0) >> 4 == 0) {
				i += 4;
			}
		}
		buffer.flip();
		return new String(buffer.array(), "utf-8");
}
}
