package com.sjzg.question;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sjzg.database.DBConn;
import com.sjzg.paper.PaperModel;

public class QuestionGetByPaper extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public QuestionGetByPaper() {
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
		PaperModel DBfindPaper_result = DBfindPaper(PaperID);
		
		
		if (DBfindPaper_result == null){
			String printString =  "{\"errcode\":101,\"errmsg\":\"找不到试卷\"}";
			out.print(printString);
			out.flush();
			out.close();
			return;
		}
		String questionListStr = DBfindPaper_result.getQuestions();
		System.out.println(questionListStr);

		
		String[] questionList = questionListStr.split("@@");//丢弃空字符串

		if(questionList.length<2){
			String printString =  "{\"errcode\":101,\"errmsg\":\"试题不足\"}";
			out.print(printString);
			out.flush();
			out.close();
			return;
		}
		
		
		ArrayList<QuestionModel> DBfindQuestions_result = DBfindQuestions(questionList);
		
		
		if (DBfindQuestions_result.isEmpty()){
			out.print("{\"errcode\":100,\"errmsg\":\"搜索不到题库\"}");
			out.flush();
			out.close();
			return;
		}else {
			JsonObject jsonObject = new JsonObject();
			JsonArray questionJsonArray = new JsonArray();
			int listCount = DBfindQuestions_result.size();
			for (int i=listCount-1;i>=0;i--){
				JsonObject tempObject = new JsonObject();
				tempObject.addProperty("Answer", DBfindQuestions_result.get(i).getAnswer());
				tempObject.addProperty("Answerkey", DBfindQuestions_result.get(i).getAnswerkey());
				tempObject.addProperty("Choices", DBfindQuestions_result.get(i).getChoices());
				tempObject.addProperty("Content", DBfindQuestions_result.get(i).getContent());
				tempObject.addProperty("Image", DBfindQuestions_result.get(i).getImage());
				tempObject.addProperty("Share", DBfindQuestions_result.get(i).getShare());
				tempObject.addProperty("Tag", DBfindQuestions_result.get(i).getTag());
				tempObject.addProperty("UserID", DBfindQuestions_result.get(i).getUserID());
				tempObject.addProperty("Difficulty", DBfindQuestions_result.get(i).getDifficulty());
				tempObject.addProperty("QuestionID", DBfindQuestions_result.get(i).getQuestionID());
				tempObject.addProperty("Type", DBfindQuestions_result.get(i).getType());
				tempObject.addProperty("CreateAt", DBfindQuestions_result.get(i).getCreateAt());
				questionJsonArray.add(tempObject);
			}

			
			jsonObject.addProperty("errcode","0");
			jsonObject.add("threads", questionJsonArray);
			out.print(jsonObject.toString());
			out.flush();
			out.close();
			return;
		}
	}
	
	public PaperModel DBfindPaper(int PaperID) {
		System.out.println("完成执行DBfindPaper");
		String sql="SELECT * FROM  Paper " +
				"WHERE PaperID=? ";
		Connection conn=DBConn.getConnection();
		PaperModel paperModel_result = null;
					
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setInt(1, PaperID);
			ResultSet rs=ps.executeQuery();
			
			if(rs.next())
			{
				paperModel_result = new PaperModel();
				paperModel_result.setTitle(rs.getString("Title"));
				paperModel_result.setQuestions(rs.getString("Questions"));
				paperModel_result.setDescription(rs.getString("Description"));
				paperModel_result.setTag(rs.getString("Tag"));
		
	
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return  paperModel_result;
	}
	
	
	public ArrayList<QuestionModel> DBfindQuestions(String[] questionList) {
		System.out.println("完成执行DBfindQuestions 长度："+questionList.length);
		int questionCount = questionList.length;
		String tempStr = "";
		  for(int i=0;i< questionCount;i++ ){
			  if(i==0){
				  tempStr = "?";
			  }else {
				  tempStr = tempStr + ",?";
			  }
		  }
		String sql="SELECT Question.QuestionID,Question.Type,Content,Choices,Image,Tag,Share,Answerkey,Difficulty,UserID,Answer,R_question_kp.kpID,Description FROM  Question,Knowledge_point,R_question_kp WHERE Question.QuestionID IN ("+tempStr+") and Question.QuestionID=R_question_kp.QuestionID and R_question_kp.KpID=Knowledge_point.KpID";
	
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "数据库操作异常";


		ArrayList<QuestionModel> resultList = new ArrayList<QuestionModel>();

		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
			 for(int j=0;j< questionCount;j++ ){
					ps.setString(j+1, questionList[j]);
					System.out.println(questionList[j]);
			  }
			 
	
			 rs=ps.executeQuery();
			
			while(rs.next())
			{
				QuestionModel questionModel_temp = new QuestionModel();
				questionModel_temp.setQuestionID(rs.getInt("QuestionID"));
				questionModel_temp.setType(rs.getInt("Type"));
				questionModel_temp.setContent(rs.getString("Content"));
				questionModel_temp.setAnswerkey(rs.getString("Answerkey"));
				questionModel_temp.setChoices(rs.getString("Choices"));
				questionModel_temp.setAnswer(rs.getString("Answer"));
				
				questionModel_temp.setImage(rs.getString("Image"));
				questionModel_temp.setUserID(rs.getString("UserID"));
				questionModel_temp.setTag(rs.getString("Tag"));
				questionModel_temp.setDifficulty(rs.getInt("Difficulty"));
				questionModel_temp.setShare(rs.getInt("Share"));
				questionModel_temp.setCreateAt(rs.getString("CreateAt"));
				questionModel_temp.setKnowledgeid(rs.getInt("KpID"));
				questionModel_temp.setKnowledgepoint(rs.getString("Description"));
				resultList.add(questionModel_temp);
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

		return  resultList;
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
