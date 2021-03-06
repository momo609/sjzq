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

public class QuestionGetByID extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public QuestionGetByID() {
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

		
		ArrayList<QuestionModel> DBfindQuestion_result = DBfindQuestion(QuestionID);
		
		
		if (DBfindQuestion_result.isEmpty()){
			out.print("{\"errcode\":100,\"errmsg\":\"搜索不到题库\"}");
			out.flush();
			out.close();
			return;
		}else {
			JsonObject jsonObject = new JsonObject();
			JsonArray questionJsonArray = new JsonArray();
			int listCount = DBfindQuestion_result.size();
			for (int i=listCount-1;i>=0;i--){
				JsonObject tempObject = new JsonObject();
				tempObject.addProperty("Answer", DBfindQuestion_result.get(i).getAnswer());
				tempObject.addProperty("Answerkey", DBfindQuestion_result.get(i).getAnswerkey());
				tempObject.addProperty("Choices", DBfindQuestion_result.get(i).getChoices());
				tempObject.addProperty("Content", DBfindQuestion_result.get(i).getContent());
				tempObject.addProperty("Image", DBfindQuestion_result.get(i).getImage());
				tempObject.addProperty("Share", DBfindQuestion_result.get(i).getShare());
				tempObject.addProperty("Tag", DBfindQuestion_result.get(i).getTag());
				tempObject.addProperty("UserID", DBfindQuestion_result.get(i).getUserID());
				tempObject.addProperty("Difficulty", DBfindQuestion_result.get(i).getDifficulty());
				tempObject.addProperty("QuestionID", DBfindQuestion_result.get(i).getQuestionID());
				tempObject.addProperty("Type", DBfindQuestion_result.get(i).getType());
				tempObject.addProperty("CreateAt", DBfindQuestion_result.get(i).getCreateAt());
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
	
	
	public ArrayList<QuestionModel> DBfindQuestion(int QuestionID) {
		System.out.println("完成执行DBfindQuestion");
		String sql="SELECT Question.*,User.* FROM  Question , User " +
				"WHERE Question.QuestionID=? " +
				"AND Question.UserID = User.UserID";
		ArrayList<QuestionModel> questionList = new ArrayList<QuestionModel>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "数据库操作异常";
		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
			ps.setInt(1, QuestionID);
			 rs=ps.executeQuery();
			
			while(rs.next())
			{
				QuestionModel questionModel_temp = new QuestionModel();

				questionModel_temp.setContent(rs.getString("Content"));
				questionModel_temp.setAnswerkey(rs.getString("Answerkey"));
				questionModel_temp.setAnswer(rs.getString("Answer"));
				questionModel_temp.setChoices(rs.getString("Choices"));
				questionModel_temp.setUserID(rs.getString("RealName"));
				questionModel_temp.setQuestionID(rs.getInt("QuestionID"));
				questionModel_temp.setImage(rs.getString("Image"));
				questionModel_temp.setType(rs.getInt("Type"));
				questionModel_temp.setTag(rs.getString("Tag"));
				questionModel_temp.setDifficulty(rs.getInt("Difficulty"));
				questionModel_temp.setShare(rs.getInt("Share"));
				questionModel_temp.setCreateAt(rs.getString("CreateAt"));
				questionList.add(questionModel_temp);
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

		return  questionList;
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
