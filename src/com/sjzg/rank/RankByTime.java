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
import com.sjzg.database.DBConn;

public class RankByTime extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public RankByTime() {
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
		
		
		ArrayList<AnswerUserModel> DBfindAnswer_result = DBfindAnswer(TestID);
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
				rankModel_temp.setAvatar(DBfindAnswer_result.get(i).getAvatar());
				rankModel_temp.setContent("null");
				rankModel_temp.setID(DBfindAnswer_result.get(i).getUserID());
				rankModel_temp.setIndex(0);
				rankModel_temp.setTitle("null");
				rankModel_temp.setUser(DBfindAnswer_result.get(i).getRealName());
				rankModel_temp.setValue(DBfindAnswer_result.get(i).getTimeUsed());
				RankModel_result.add(rankModel_temp);
			}else{
				int listCount = RankModel_result.size();
				boolean isNew = true;
			
				for (int j=0;j<listCount;j++){
					//遍历排行榜单位
					
					String ID_1 = RankModel_result.get(j).getID();
					String ID_2 = DBfindAnswer_result.get(i).getUserID();

					if(ID_1.equals(ID_2)){
						//已有相同单位,更新内部数据
						RankModel_result.get(j).setValue(RankModel_result.get(j).getValue()+DBfindAnswer_result.get(i).getTimeUsed());
						j=listCount+1;//跳出循环
						isNew=false;
					}

				}
				
				if (isNew) {
					RankModel rankModel_temp = new RankModel();
					rankModel_temp.setAvatar(DBfindAnswer_result.get(i).getAvatar());
					rankModel_temp.setContent("null");
					rankModel_temp.setID(DBfindAnswer_result.get(i).getUserID());
					rankModel_temp.setIndex(0);
					rankModel_temp.setTitle("null");
					rankModel_temp.setUser(DBfindAnswer_result.get(i).getRealName());
					rankModel_temp.setValue(DBfindAnswer_result.get(i).getTimeUsed());
					RankModel_result.add(rankModel_temp);
				}
			}
			
			

		}

		
	
		//冒泡排序,从小到大
		for (int k=0;k<RankModel_result.size();k++){
			//遍历排行榜单位
			for (int l=RankModel_result.size()-1;l>k;l--){
				//遍历排行榜单位
				if(RankModel_result.get(l).getValue()<RankModel_result.get(k).getValue()){
					RankModel rankModel_temp = new RankModel();
					rankModel_temp.setAvatar(RankModel_result.get(l).getAvatar());
					rankModel_temp.setContent(RankModel_result.get(l).getContent());
					rankModel_temp.setID(RankModel_result.get(l).getID());
					rankModel_temp.setIndex(0);
					rankModel_temp.setTitle(RankModel_result.get(l).getTitle());
					rankModel_temp.setUser(RankModel_result.get(l).getUser());
					rankModel_temp.setValue(RankModel_result.get(l).getValue());
					
					RankModel_result.set(l, RankModel_result.get(k));
					RankModel_result.set(k, rankModel_temp);
					
				}

			}

		}

		
		
		//补充总耗时,排名位置等
		float totalTime = 0;
		int current_index = 0;
		int current_index_count = 1;
		float current_value = 0;
		
		for (int m=0;m<RankModel_result.size();m++){
			//遍历排行榜单位
			if(current_value <  RankModel_result.get(m).getValue()){
				//需要新增排名
				current_value = RankModel_result.get(m).getValue();
				current_index += current_index_count;
				RankModel_result.get(m).setIndex(current_index);
				current_index_count = 0;
			}
			current_index_count += 1;
			RankModel_result.get(m).setIndex(current_index);
			RankModel_result.get(m).setContent("耗时:"+(RankModel_result.get(m).getValue()/10)+"秒");
			totalTime += RankModel_result.get(m).getValue();
			
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
		
		
		

		
		jsonObject.addProperty("average",totalTime/RankModel_result.size());
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
	public ArrayList<AnswerUserModel> DBfindAnswer(int TestID) {
		System.out.println("完成执行DBfindAnswer");
		String sql="SELECT Answer.* , User.RealName , User.Sex , User.Avatar , User.NickName FROM Answer , User WHERE Answer.TestID = ? AND User.UserID = Answer.UserID ";

		ArrayList<AnswerUserModel> answerList = new ArrayList<AnswerUserModel>();
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
				
				AnswerUserModel answerModel_temp = new AnswerUserModel();
				
				answerModel_temp.setUserID(rs.getString("UserID"));
				answerModel_temp.setQuestionID(rs.getInt("QuestionID"));
				answerModel_temp.setTestID(rs.getInt("TestID"));
				answerModel_temp.setTimeUsed(rs.getFloat("TimeUsed"));
				answerModel_temp.setTrack(rs.getString("Track"));
				answerModel_temp.setGrade(rs.getFloat("Grade"));
				answerModel_temp.setUnfocus(rs.getString("Unfocus"));
				answerModel_temp.setAppear(rs.getString("Appear"));
				answerModel_temp.setUserAnswer(rs.getString("UserAnswer"));
	
				
				answerModel_temp.setRealName(rs.getString("RealName"));
				answerModel_temp.setSex(rs.getString("Sex"));
				answerModel_temp.setAvatar(rs.getString("Avatar"));
				answerModel_temp.setNickName(rs.getString("NickName"));

				
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
	
}
