package com.sjzg.behaviour;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.sjzg.answer.AnswerModel;
import com.sjzg.course.CourseModel;
import com.sjzg.database.DBConn;

public class BehaviourCreate extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public BehaviourCreate() {
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
		String Behaviour = request.getParameter("Behaviour");
		
		
		String TimeUnitStr=request.getParameter("TimeUnit");
		
		int TimeUnit=100;
		
		if(TimeUnitStr!=null ){
			if(TimeUnitStr.equals("10")){
				TimeUnit = 10;
			}
		}
		
		BehaviourModel behaviourModel=new BehaviourModel();
		behaviourModel.setID(-1);
		behaviourModel.setUserID(UserID);
		behaviourModel.setBehaviour(Behaviour);
		behaviourModel.setTimeUnit(TimeUnit);

		
		
		String DBcreateBehaviour_result = DBcreateBehaviour(behaviourModel);
		
		if (!DBcreateBehaviour_result.equals("ok")) {
			out.print("{\"errcode\":101,\"errmsg\":\""+DBcreateBehaviour_result+"\"}");
			out.flush();
			out.close();
			return;
			

		}else {
			out.print("{\"errcode\":0,\"errmsg\":\"�����ɹ���\"}");
			out.flush();
			out.close();
			return;
		}
	
		
	}

	
	
	
	
	
	
	
	
	
	public String DBcreateBehaviour(BehaviourModel behaviourModel) {
		System.out.println("ִ��DBcreateBehaviour");
		String sql="INSERT INTO Behaviour(UserID,Behaviour,TimeUnit,CreateAt,UpdateAt) VALUES(?,?,?,?,?)";

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

			ps.setString(1, behaviourModel.getUserID());

			ps.setString(2, behaviourModel.getBehaviour());
			ps.setInt(3, behaviourModel.getTimeUnit());
			ps.setTimestamp(4, nowTimestamp);
			ps.setTimestamp(5, nowTimestamp);
			influence+=ps.executeUpdate();
			

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
