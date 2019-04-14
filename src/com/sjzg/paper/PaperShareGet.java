package com.sjzg.paper;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sjzg.database.DBConn;

public class PaperShareGet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public PaperShareGet() {
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

		ArrayList<String> DBfindShare_result = DBfindShare(PaperID);
		
		if (DBfindShare_result.isEmpty()){
			out.print("{\"errcode\":100,\"errmsg\":\"该题目没有指定分享\"}");
			out.flush();
			out.close();
			return;
		}else {
			JsonObject jsonObject = new JsonObject();
			JsonArray userJsonArray = new JsonArray();
			int listCount = DBfindShare_result.size();
			for (int i=listCount-1;i>=0;i--){
				JsonObject tempObject = new JsonObject();

				tempObject.addProperty("UserID", DBfindShare_result.get(i));

				userJsonArray.add(tempObject);
			}
			

			jsonObject.addProperty("errcode","0");
			jsonObject.add("threads", userJsonArray);
	
			out.print(jsonObject.toString());
			out.flush();
			out.close();
			return;
		}
		
	}
	public ArrayList<String> DBfindShare(int PaperID) {
		System.out.println("完成执行DBfindShare");
		String sql="SELECT * FROM  Paper_share WHERE PaperID=?";
		ArrayList<String> userList = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "数据库操作异常";
		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
			ps.setInt(1, PaperID);
			 rs=ps.executeQuery();
			
			while(rs.next())
			{
				userList.add(rs.getString("UserID"));
			}
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

		return  userList;
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


