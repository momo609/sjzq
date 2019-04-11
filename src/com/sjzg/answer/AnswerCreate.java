package com.sjzg.answer;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sjzg.database.DBConn;
import com.sjzg.question.QuestionModel;

public class AnswerCreate extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public AnswerCreate() {
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
		String answerListJsonStr=request.getParameter("jsonstr");

		String TimeUnitStr=request.getParameter("TimeUnit");
		
		float TimeUnit=1;
		
		if(TimeUnitStr!=null ){
			if(TimeUnitStr.equals("10")){
				TimeUnit =  0.1f;
			}
		}
		
		System.out.println(answerListJsonStr);
		ArrayList<AnswerModel> answerList = new ArrayList<AnswerModel>();
		try {
			
			JsonParser parser = new JsonParser();
			JsonArray answerArray = new JsonArray();
			answerArray = (JsonArray) parser.parse(answerListJsonStr);
			
			for(int i=0;i<answerArray.size();i++){
				AnswerModel answerModel_temp = new AnswerModel();
				
				answerModel_temp.setUserID(answerArray.get(i).getAsJsonObject().get("UserID").getAsString());
				answerModel_temp.setQuestionID(answerArray.get(i).getAsJsonObject().get("QuestionID").getAsInt());
				answerModel_temp.setTestID(answerArray.get(i).getAsJsonObject().get("TestID").getAsInt());
				answerModel_temp.setTimeUsed(answerArray.get(i).getAsJsonObject().get("TimeUsed").getAsFloat()*TimeUnit);
				answerModel_temp.setGrade(answerArray.get(i).getAsJsonObject().get("Grade").getAsFloat());
				answerModel_temp.setTrack(answerArray.get(i).getAsJsonObject().get("Track").getAsString());
				answerModel_temp.setUserAnswer(answerArray.get(i).getAsJsonObject().get("UserAnswer").getAsString());
				if(answerArray.get(i).getAsJsonObject().get("Unfocus")!=null){
					answerModel_temp.setUnfocus(answerArray.get(i).getAsJsonObject().get("Unfocus").getAsString());
					answerModel_temp.setAppear(answerArray.get(i).getAsJsonObject().get("Appear").getAsString());
				}else{
					answerModel_temp.setUnfocus("%null%");
					answerModel_temp.setAppear("%null%");
				}
			
				try {
					if(answerArray.get(i).getAsJsonObject().get("isCollected")!=null){
						boolean isCollected = answerArray.get(i).getAsJsonObject().get("isCollected").getAsBoolean();
						if(isCollected){
//						 DBcreateQuestionCollection(answerArray.get(i).getAsJsonObject().get("QuestionID").getAsInt(),answerArray.get(i).getAsJsonObject().get("UserID").getAsString(),answerArray.get(i).getAsJsonObject().get("TestID").getAsInt());
							answerModel_temp.setCollection(1);
						}
						else
						{
							answerModel_temp.setCollection(0);
						}
					}
				} catch (Exception e) {
					//添加收藏出错
				}


				
				
				answerList.add(answerModel_temp);
			}
			String DBcreateAnswer_result = DBcreateAnswer(answerList);
			
			if (!DBcreateAnswer_result.equals("ok")) {
				out.print("{\"errcode\":101,\"errmsg\":\"系统异常\"}");
				out.flush();
				out.close();
				return;
				

			}else {
				out.print("{\"errcode\":0,\"errmsg\":\"创建成功！\"}");
				out.flush();
				out.close();
				return;
			}
			
			
			
			
		} catch (Exception e) {
			out.print("{\"errcode\":101,\"errmsg\":\"提交试卷失败\"}");
			out.flush();
			out.close();
			return;
		}
		

	}
	public String DBcreateAnswer(ArrayList<AnswerModel> answerList) {
		System.out.println("执行DBcreateAnswer");
		String sql="INSERT INTO Answer(UserID,QuestionID,TestID,TimeUsed,Track,Grade,Unfocus,Appear,UserAnswer,CreateAt,UpdateAt,Collection) VALUES";
		for(int i=0;i<answerList.size();i++){
			if(i==0){
				sql = sql + "(?,?,?,?,?,?,?,?,?,?,?,?)";
			}
				else {
				sql = sql + ",(?,?,?,?,?,?,?,?,?,?,?,?)";
			}
		
		}
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

			
			for(int i=0;i<answerList.size();i++){
				
				ps.setString(1+(i*11), answerList.get(i).getUserID());
				ps.setInt(2+(i*11), answerList.get(i).getQuestionID());
				ps.setInt(3+(i*11), answerList.get(i).getTestID());
				ps.setFloat(4+(i*11), answerList.get(i).getTimeUsed());
				ps.setString(5+(i*11), answerList.get(i).getTrack());
				ps.setFloat(6+(i*11), answerList.get(i).getGrade());
				ps.setString(7+(i*11), answerList.get(i).getUnfocus());
				ps.setString(8+(i*11), answerList.get(i).getAppear());
				ps.setString(9+(i*11), answerList.get(i).getUserAnswer());
				ps.setTimestamp(10+(i*11), nowTimestamp);
				ps.setTimestamp(11+(i*11), nowTimestamp);
				ps.setInt(12+(i*11), answerList.get(i).getCollection());
			}
			
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
			if(influence<answerList.size())
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
	public String DBcreateQuestionCollection(int QuestionID,  String UserID,int TestID) {
//		System.out.println("执行DBcreateQuestionCollection");
		String sql="INSERT INTO Question_collection(QuestionID,UserID,CreateAt,UpdateAt,TestID) VALUES(?,?,?,?,?)";

		int influence=0;//影响的行数
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "数据库操作异常";
		Date nowDate = new Date();
		java.sql.Timestamp nowTimestamp = new java.sql.Timestamp(nowDate.getTime());
		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);

			ps.setInt(1, QuestionID);
			ps.setString(2, UserID);

			ps.setTimestamp(3, nowTimestamp);
			ps.setTimestamp(4, nowTimestamp);
			ps.setInt(5, TestID);
			influence+=ps.executeUpdate();


			conn.close();
		} catch (SQLException e) {
//			e.printStackTrace();
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
}
