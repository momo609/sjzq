package com.sjzg.answer;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.sjzg.course.CourseUserModel;
import com.sjzg.database.DBConn;

public class AnswerGetByID extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public AnswerGetByID() {
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
		String AnswerIDStr=request.getParameter("AnswerID");
		
		int AnswerID = -1;
		try {
			AnswerID = Integer.parseInt(AnswerIDStr);
		} catch (Exception e) {
			out.print("{\"errcode\":101,\"errmsg\":\"传入的答案ID有误\"}");
			out.flush();
			out.close();
			return;
		}
		System.out.println("取的AnswerID+"+AnswerID);
		
		
		AnswerQuestionModel DBfindAnswer_result = DBfindAnswer(AnswerID);
		if (DBfindAnswer_result == null){
			out.print("{\"errcode\":101,\"errmsg\":\"获取不到答案\"}");
			out.flush();
			out.close();
			return;
		}else {
			JsonObject jsonObject = new JsonObject();
			JsonObject tempObject = new JsonObject();
			
			

			tempObject.addProperty("TimeUsed", DBfindAnswer_result.getTimeUsed());
			tempObject.addProperty("Track", DBfindAnswer_result.getTrack());	
			tempObject.addProperty("Grade", DBfindAnswer_result.getGrade());
			tempObject.addProperty("UnfocusDuration", DBfindAnswer_result.getUnfocusDuration());
			tempObject.addProperty("UnfocusCount", DBfindAnswer_result.getUnfocusCount());
			tempObject.addProperty("UserAnswer", DBfindAnswer_result.getUserAnswer());
			tempObject.addProperty("Answer", DBfindAnswer_result.getAnswer());
			
			tempObject.addProperty("Type", DBfindAnswer_result.getType());
			tempObject.addProperty("Difficulty", DBfindAnswer_result.getDifficulty());	
			tempObject.addProperty("Share", DBfindAnswer_result.getShare());
			tempObject.addProperty("Content", DBfindAnswer_result.getContent());
			tempObject.addProperty("Choices", DBfindAnswer_result.getChoices());
			tempObject.addProperty("Answerkey", DBfindAnswer_result.getAnswerkey());
			tempObject.addProperty("Image", DBfindAnswer_result.getImage());
			tempObject.addProperty("Tag", DBfindAnswer_result.getTag());
			


			
			jsonObject.addProperty("errcode","0");
			jsonObject.add("data", tempObject);
			out.print(jsonObject.toString());
			out.flush();
			out.close();
			return;
		}
	}

	
	
	public AnswerQuestionModel DBfindAnswer(int ID) {
		System.out.println("完成执行DBfindAnswer");
		String sql="SELECT Answer.* , Question.* FROM  Answer , Question WHERE Answer.ID=? AND Answer.QuestionID = Question.QuestionID";


		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "数据库操作异常";
		AnswerQuestionModel answerQuestionModel_temp = null;
		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
			ps.setInt(1, ID);
			 rs=ps.executeQuery();
			
			if(rs.next())
			{
				answerQuestionModel_temp = new AnswerQuestionModel();



				 answerQuestionModel_temp.setTimeUsed(rs.getInt("TimeUsed"));
				 answerQuestionModel_temp.setTrack(rs.getString("Track"));
				 answerQuestionModel_temp.setGrade(rs.getFloat("Grade"));
				answerQuestionModel_temp.setUnfocusDuration(rs.getInt("UnfocusDuration"));
				answerQuestionModel_temp.setUnfocusCount(rs.getInt("UnfocusCount"));
				answerQuestionModel_temp.setUserAnswer(rs.getString("UserAnswer"));
				answerQuestionModel_temp.setAnswer(rs.getString("Answer"));
				
				answerQuestionModel_temp.setType(rs.getInt("Type"));
				answerQuestionModel_temp.setDifficulty(rs.getFloat("Difficulty"));
				answerQuestionModel_temp.setShare(rs.getInt("Share"));
				answerQuestionModel_temp.setContent(rs.getString("Content"));
				answerQuestionModel_temp.setChoices(rs.getString("Choices"));
				answerQuestionModel_temp.setAnswerkey(rs.getString("Answerkey"));
				answerQuestionModel_temp.setImage(rs.getString("Image"));
				answerQuestionModel_temp.setTag(rs.getString("Tag"));

				
				
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
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

		return  answerQuestionModel_temp;
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
