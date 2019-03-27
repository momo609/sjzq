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

public class QuestionUpdateShare extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public QuestionUpdateShare() {
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
		request.setCharacterEncoding("UTF-8");//���öԿͻ�������������±���ı��롣
		response.setCharacterEncoding("UTF-8");//ָ���Է�������Ӧ�������±���ı��롣
		response.addHeader("Access-Control-Allow-Origin", "*");//����
		PrintWriter out = response.getWriter();//��ط������
		
		//��һ����ȡ����
				String QuestionIDStr=request.getParameter("QuestionID");
				
				int QuestionID = 0;
				try {
					QuestionID = Integer.parseInt(QuestionIDStr);
				} catch (Exception e) {
					out.print("{\"errcode\":101,\"errmsg\":\"�Ծ�ID��Ϣ����\"}");
					out.flush();
					out.close();
					return;
				}
				
				
				String ShareStr=request.getParameter("Share");
				
				int Share = 0;
				try {
					Share = Integer.parseInt(ShareStr);
				} catch (Exception e) {
					out.print("{\"errcode\":101,\"errmsg\":\"��Ϣ����\"}");
					out.flush();
					out.close();
					return;
				}
				
				
				String DBupdateQuestionShare_result = DBupdateQuestionShare(QuestionID,Share);
				
				if (!DBupdateQuestionShare_result.equals("ok")) {
					out.print("{\"errcode\":101,\"errmsg\":\""+DBupdateQuestionShare_result+"\"}");
					out.flush();
					out.close();
					return;
					

				}else {
					out.print("{\"errcode\":0,\"errmsg\":\"��ӳɹ���\"}");
					out.flush();
					out.close();
					return;
				}
		
	}
	public String DBupdateQuestionShare(int QuestionID,int Share) {
		System.out.println("ִ��DBupdateUser");
		String sql="UPDATE Question SET Share = ? , UpdateAt = ? WHERE QuestionID=? ";

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
			ps.setInt(1, Share);
			ps.setTimestamp(2, nowTimestamp);
			ps.setInt(3, QuestionID);
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
