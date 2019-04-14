package com.sjzg.rank;

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
import com.sjzg.answer.AnswerQuestionModel;
import com.sjzg.database.DBConn;

public class RankByMistake extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public RankByMistake() {
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
		String TestIDStr=request.getParameter("TestID");
		int TestID = 0;
		
		try {
			TestID = Integer.parseInt(TestIDStr);
		} catch (Exception e) {
			out.print("{\"errcode\":101,\"errmsg\":\"考试信息有误\"}");
			out.flush();
			out.close();
			return;
		}
		
		
		ArrayList<AnswerQuestionModel> DBfindAnswer_result = DBfindAnswer(TestID);
		if (DBfindAnswer_result.isEmpty()){
			out.print("{\"errcode\":100,\"errmsg\":\"没有数据\"}");
			out.flush();
			out.close();
			return;
		}
		ArrayList<RankModel> RankModel_result =  new ArrayList<RankModel>() ;
		
		
		
		//遍历答案数组,开始组装成排行榜单位模型
		for (int i=0;i<DBfindAnswer_result.size();i++){
			
		
			if(RankModel_result.isEmpty()){
				RankModel rankModel_temp = new RankModel();
				rankModel_temp.setAvatar("null");
				rankModel_temp.setContent(DBfindAnswer_result.get(i).getAnswer());
				rankModel_temp.setID((DBfindAnswer_result.get(i).getQuestionID()+""));
				rankModel_temp.setIndex(DBfindAnswer_result.get(i).getType());
				rankModel_temp.setTitle(DBfindAnswer_result.get(i).getContent());
				rankModel_temp.setUser("null");
				rankModel_temp.setValue(DBfindAnswer_result.get(i).getGrade());
				RankModel_result.add(rankModel_temp);
			}else{
				int listCount = RankModel_result.size();
				boolean isNew = true;
			
				for (int j=0;j<listCount;j++){
					//遍历排行榜单位
					
					String ID_1 = RankModel_result.get(j).getID();
					String ID_2 = DBfindAnswer_result.get(i).getQuestionID()+"";

					if(ID_1.equals(ID_2)){
						//已有相同单位,更新内部数据
						RankModel_result.get(j).setValue(RankModel_result.get(j).getValue()+DBfindAnswer_result.get(i).getGrade());
						j=listCount+1;//跳出循环
						isNew=false;
					}

				}
				
				if (isNew) {
					RankModel rankModel_temp = new RankModel();
					rankModel_temp.setAvatar("null");
					rankModel_temp.setContent(DBfindAnswer_result.get(i).getAnswer());
					rankModel_temp.setID((DBfindAnswer_result.get(i).getQuestionID()+""));
					rankModel_temp.setIndex(DBfindAnswer_result.get(i).getType());
					rankModel_temp.setTitle(DBfindAnswer_result.get(i).getContent());
					rankModel_temp.setUser("null");
					rankModel_temp.setValue(DBfindAnswer_result.get(i).getGrade());
					RankModel_result.add(rankModel_temp);
				}
			}
			
			

		}

		
	
		//冒泡排序,从小到小
		for (int k=0;k<RankModel_result.size();k++){
			//遍历排行榜单位
			for (int l=RankModel_result.size()-1;l>k;l--){
				//遍历排行榜单位
				if(RankModel_result.get(l).getValue()<RankModel_result.get(k).getValue()){
					RankModel rankModel_temp = new RankModel();
					rankModel_temp.setAvatar(RankModel_result.get(l).getAvatar());
					rankModel_temp.setContent(RankModel_result.get(l).getContent());
					rankModel_temp.setID(RankModel_result.get(l).getID());
					rankModel_temp.setIndex(DBfindAnswer_result.get(l).getType());
					rankModel_temp.setTitle(RankModel_result.get(l).getTitle());
					rankModel_temp.setUser(RankModel_result.get(l).getUser());
					rankModel_temp.setValue(RankModel_result.get(l).getValue());
					
					RankModel_result.set(l, RankModel_result.get(k));
					RankModel_result.set(k, rankModel_temp);
					
				}

			}

		}

		
		
		//补充总耗时,排名位置等
		float totalScore = 0;
	

		
		for (int m=0;m<RankModel_result.size();m++){
			//遍历排行榜单位
			


			RankModel_result.get(m).setContent("正确答案："+RankModel_result.get(m).getContent()+"\n正确人数："+((int)RankModel_result.get(m).getValue())+"人");
			totalScore += RankModel_result.get(m).getValue();
			
		}
	
		//输出
		JsonObject jsonObject = new JsonObject();
		JsonArray threadsJsonArray = new JsonArray();

		for (int i=0;i<RankModel_result.size();i++){
			JsonObject tempObject = new JsonObject();
			
			tempObject.addProperty("Index", RankModel_result.get(i).getIndex());
			tempObject.addProperty("User", RankModel_result.get(i).getUser());
			tempObject.addProperty("Avatar", RankModel_result.get(i).getAvatar());
			tempObject.addProperty("Value", RankModel_result.get(i).getValue());
			tempObject.addProperty("Title", RankModel_result.get(i).getTitle());
			tempObject.addProperty("Content", RankModel_result.get(i).getContent());
			tempObject.addProperty("ID", RankModel_result.get(i).getID());
			
			
	
			
			
			threadsJsonArray.add(tempObject);
		}
		
		
		

		
		jsonObject.addProperty("total",totalScore);
		jsonObject.addProperty("errcode","0");
		jsonObject.add("threads", threadsJsonArray);
		out.print(jsonObject.toString());
		out.flush();
		out.close();
		return;
		
		
		
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
	public ArrayList<AnswerQuestionModel> DBfindAnswer(int TestID) {
		System.out.println("完成执行DBfindAnswer");
		String sql="SELECT Answer.* , Question.Type , Question.Content , Question.Choices , Question.Answer FROM  Answer , Question WHERE Answer.TestID=? AND Answer.QuestionID = Question.QuestionID ";

		ArrayList<AnswerQuestionModel> answerQuestionList = new ArrayList<AnswerQuestionModel>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "数据库操作异常";
		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
			ps.setInt(1, TestID);
			 rs=ps.executeQuery();
			
			while(rs.next())
			{	
				
				AnswerQuestionModel answerQuestionModel_temp = new AnswerQuestionModel();
				 answerQuestionModel_temp.setQuestionID(rs.getInt("QuestionID"));
				 answerQuestionModel_temp.setTimeUsed(rs.getFloat("TimeUsed"));
				 answerQuestionModel_temp.setTrack(rs.getString("Track"));
				 answerQuestionModel_temp.setGrade(rs.getFloat("Grade"));
				answerQuestionModel_temp.setUnfocusDuration(rs.getInt("UnfocusDuration"));
				answerQuestionModel_temp.setUnfocusCount(rs.getInt("UnfocusCount"));
				answerQuestionModel_temp.setUserAnswer(rs.getString("UserAnswer"));
				
				answerQuestionModel_temp.setAnswer(rs.getString("Answer"));
				answerQuestionModel_temp.setType(rs.getInt("Type"));
				answerQuestionModel_temp.setContent(rs.getString("Content"));
				answerQuestionModel_temp.setChoices(rs.getString("Choices"));



				
				answerQuestionList.add(answerQuestionModel_temp);
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

		return  answerQuestionList;
	}
	
}
