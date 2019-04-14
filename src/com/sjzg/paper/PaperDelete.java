package com.sjzg.paper;

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

public class PaperDelete extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public PaperDelete() {
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
				String PaperIDStr=request.getParameter("PaperID");
				
				int PaperID = 0;
				try {
					PaperID = Integer.parseInt(PaperIDStr);
				} catch (Exception e) {
					out.print("{\"errcode\":101,\"errmsg\":\"试卷ID信息有误\"}");
					out.flush();
					out.close();
					return;
				}
				
			
				
				
				String DBdeletePaper_result = DBdeletePaper(PaperID);
				
				if (!DBdeletePaper_result.equals("ok")) {
					out.print("{\"errcode\":101,\"errmsg\":\""+DBdeletePaper_result+"\"}");
					out.flush();
					out.close();
					return;
					

				}else {
					out.print("{\"errcode\":0,\"errmsg\":\"修改成功！\"}");
					out.flush();
					out.close();
					return;
				}
	}
	
	
	public String DBdeletePaper(int PaperID) {
		System.out.println("执行DBdeletePaper");
		//注意，由于直接删除题目会导致关联题目的数据出现错乱，因此直接把题目所属的UserID改了
		String sql="UPDATE Paper SET UserID = 'Delete', UpdateAt = ? WHERE PaperID=? ";

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
			ps.setInt(2, PaperID);
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
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
