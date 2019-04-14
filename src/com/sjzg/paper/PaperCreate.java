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
import com.sjzg.question.QuestionModel;

public class PaperCreate extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public PaperCreate() {
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
		String Title=request.getParameter("Title");
		String Description = request.getParameter("Description");
		String Tag = request.getParameter("Tag");
		String Questions = request.getParameter("Questions");

		

		PaperModel paperModel = new PaperModel();
		paperModel.setUserID(UserID);
		paperModel.setTitle(Title);
		paperModel.setDescription(Description);
		paperModel.setTag(Tag);
		paperModel.setQuestions(Questions);
		
		//�ڶ�������������Ƿ�����
		if (!paperModel.validate().equals("ok")){
			out.print("{\"errcode\":101,\"errmsg\":\""+paperModel.validate()+"\"}");
			out.flush();
			out.close();
			return;
		}
		
		
		String DBcreatePaper_result = DBcreatePaper(paperModel);
		
		if (DBcreatePaper_result.equals("ok")) {
		
			out.print("{\"errcode\":0,\"errmsg\":\"�����ɹ���\"}");
			out.flush();
			out.close();
			return;

		}else {
			out.print("{\"errcode\":101,\"errmsg\":\""+DBcreatePaper_result+"\"}");
			out.flush();
			out.close();
			return;
		}
	}
	
	
	public String DBcreatePaper(PaperModel paperModel) {
		System.out.println("ִ��DBcreateQuestion");
		String sql="INSERT INTO Paper(UserID,Title,Description,Tag,Questions,CreateAt,UpdateAt) VALUES(?,?,?,?,?,?,?)";

		int influence=0;//Ӱ�������
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "���ݿ�����쳣";
		Date nowDate = new Date();
		java.sql.Timestamp nowTimestamp = new java.sql.Timestamp(nowDate.getTime());
		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
	
			
			ps.setString(1, paperModel.getUserID());
			ps.setString(2, paperModel.getTitle());
			ps.setString(3, paperModel.getDescription());
			ps.setString(4, paperModel.getTag());
			ps.setString(5, paperModel.getQuestions());
			ps.setTimestamp(6, nowTimestamp);
			ps.setTimestamp(7, nowTimestamp);
			
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
