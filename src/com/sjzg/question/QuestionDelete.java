package com.sjzg.question;

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

public class QuestionDelete extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public QuestionDelete() {
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
				String QuestionIDStr=request.getParameter("QuestionID");
				
				int QuestionID = 0;
				try {
					QuestionID = Integer.parseInt(QuestionIDStr);
				} catch (Exception e) {
					out.print("{\"errcode\":101,\"errmsg\":\"题目ID信息有误\"}");
					out.flush();
					out.close();
					return;
				}
				
			
				
				
				String DBdeleteQuestion_result = DBdeleteQuestion(QuestionID);
				
				if (DBdeleteQuestion_result.equals("ok")) {
					out.print("{\"errcode\":0,\"errmsg\":\"成功\"}");
					out.flush();
					out.close();
					return;
				}else {
					out.print("{\"errcode\":101,\"errmsg\":\""+DBdeleteQuestion_result+"\"}");
					out.flush();
					out.close();
					return;
				}
	}
	public String DBdeleteQuestion(int QuestionID) {
		System.out.println("执行DBdeleteQuestion");
		//注意，由于直接删除题目会导致关联题目的数据出现错乱，因此直接把题目所属的UserID改了
		String sql="UPDATE Question SET Share = 0 , UserID = 'Delete', UpdateAt = ? WHERE QuestionID=? ";
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
			ps.setTimestamp(1, nowTimestamp);
			ps.setInt(2, QuestionID);
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
