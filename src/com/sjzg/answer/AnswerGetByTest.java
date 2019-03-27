package com.sjzg.answer;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sjzg.course.CourseUserModel;
import com.sjzg.database.DBConn;

public class AnswerGetByTest extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public AnswerGetByTest() {
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
				String UserID=request.getParameter("UserID");
				System.out.println("取的UserID+"+UserID);
				
				String TestIDStr=request.getParameter("TestID");
				
				int TestID = -1;
				try {
					TestID = Integer.parseInt(TestIDStr);
				} catch (Exception e) {
					out.print("{\"errcode\":101,\"errmsg\":\"传入的课程ID有误\"}");
					out.flush();
					out.close();
					return;
				}
				System.out.println("取的TestID+"+TestID);
				
				
				ArrayList<AnswerModel> DBfindAnswer_result = DBfindAnswer(UserID,TestID);
				
				
				if (DBfindAnswer_result.isEmpty()){
					out.print("{\"errcode\":100,\"errmsg\":\"查询不到回答\"}");
					out.flush();
					out.close();
					return;
				}else {
					JsonObject jsonObject = new JsonObject();
					JsonArray answerJsonArray = new JsonArray();
					int listCount = DBfindAnswer_result.size();
					for (int i=0;i<listCount;i++){
						JsonObject tempObject = new JsonObject();
						
						tempObject.addProperty("ID", DBfindAnswer_result.get(i).getID());
						tempObject.addProperty("UserID", DBfindAnswer_result.get(i).getUserID());
						tempObject.addProperty("QuestionID", DBfindAnswer_result.get(i).getQuestionID());
						tempObject.addProperty("TestID", DBfindAnswer_result.get(i).getTestID());
						tempObject.addProperty("TimeUsed", DBfindAnswer_result.get(i).getTimeUsed());
						tempObject.addProperty("Track", DBfindAnswer_result.get(i).getTrack());
						
						tempObject.addProperty("Grade", DBfindAnswer_result.get(i).getGrade());
						tempObject.addProperty("Unfocus", DBfindAnswer_result.get(i).getUnfocus());
						tempObject.addProperty("Appear", DBfindAnswer_result.get(i).getAppear());
						tempObject.addProperty("UserAnswer", DBfindAnswer_result.get(i).getUserAnswer());

						
						
				
						
						
						answerJsonArray.add(tempObject);
					}
					
					
					

					
					
					jsonObject.addProperty("errcode","0");
					jsonObject.add("threads", answerJsonArray);
					out.print(jsonObject.toString());
					out.flush();
					out.close();
					return;
				}
	}
	public ArrayList<AnswerModel> DBfindAnswer(String UserID,int TestID) {
		System.out.println("完成执行DBfindCourse");
		String sql="SELECT * FROM sjzg.Answer WHERE TestID = ? AND UserID = ?";

		ArrayList<AnswerModel> answerList = new ArrayList<AnswerModel>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "数据库操作异常";
		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
			ps.setInt(1, TestID);
			ps.setString(2, UserID);
		
			 rs=ps.executeQuery();
			
			while(rs.next())
			{	
				
				AnswerModel answerModel_temp = new AnswerModel();
				answerModel_temp.setID(rs.getInt("ID"));
				answerModel_temp.setUserID(rs.getString("UserID"));
				answerModel_temp.setQuestionID(rs.getInt("QuestionID"));
				answerModel_temp.setTestID(rs.getInt("TestID"));
				answerModel_temp.setTimeUsed(rs.getInt("TimeUsed"));
				answerModel_temp.setTrack(rs.getString("Track"));
				answerModel_temp.setGrade(rs.getFloat("Grade"));
				answerModel_temp.setUnfocus(rs.getString("Unfocus"));
				answerModel_temp.setAppear(rs.getString("Appear"));
				answerModel_temp.setUserAnswer(rs.getString("UserAnswer"));
	
				


				
				answerList.add(answerModel_temp);
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

		return  answerList;
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
