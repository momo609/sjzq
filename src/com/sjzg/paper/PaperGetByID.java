package com.sjzg.paper;

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
import com.sjzg.question.QuestionModel;

public class PaperGetByID extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public PaperGetByID() {
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
			out.print("{\"errcode\":102,\"errmsg\":\"试卷ID信息有误\"}");
			out.flush();
			out.close();
			return;
		}
		

		
		ArrayList<PaperModel> DBfindPaper_result = DBfindPaper(PaperID);
		
		
		if (DBfindPaper_result.isEmpty()){
			out.print("{\"errcode\":100,\"errmsg\":\"搜索不到试卷\"}");
			out.flush();
			out.close();
			return;
		}else {
			JsonObject jsonObject = new JsonObject();
			JsonArray questionJsonArray = new JsonArray();
			int listCount = DBfindPaper_result.size();
			for (int i=listCount-1;i>=0;i--){
				JsonObject tempObject = new JsonObject();
				tempObject.addProperty("Title", DBfindPaper_result.get(i).getTitle());
				tempObject.addProperty("Description", DBfindPaper_result.get(i).getDescription());
				tempObject.addProperty("Tag", DBfindPaper_result.get(i).getTag());
				tempObject.addProperty("UserID", DBfindPaper_result.get(i).getUserID());
				tempObject.addProperty("PaperID", DBfindPaper_result.get(i).getPaperID());
				tempObject.addProperty("CreateAt", DBfindPaper_result.get(i).getCreateAt());
				tempObject.addProperty("Questions", DBfindPaper_result.get(i).getQuestions());
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
	public ArrayList<PaperModel> DBfindPaper(int PaperID) {
		System.out.println("完成执行DBfindPaper");
		String sql="SELECT Paper.*,User.* FROM  Paper , User " +
				"WHERE Paper.PaperID=? " +
				"AND Paper.UserID = User.UserID";

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "数据库操作异常";

		ArrayList<PaperModel> paperList = new ArrayList<PaperModel>();

		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
			ps.setInt(1, PaperID);
			 rs=ps.executeQuery();
			
			while(rs.next())
			{
				PaperModel paperModel_temp = new PaperModel();

				
				paperModel_temp.setTitle(rs.getString("Title"));
				paperModel_temp.setDescription(rs.getString("Description"));
				paperModel_temp.setUserID(rs.getString("NickName"));
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(rs.getTimestamp("CreateAt"));
				paperModel_temp.setCreateAt((calendar.get(Calendar.YEAR))+"-"+(calendar.get(Calendar.MONDAY)+1) + "-" +(calendar.get(Calendar.DAY_OF_MONTH)));
				paperModel_temp.setQuestions(rs.getString("Questions"));
				paperModel_temp.setTag(rs.getString("Tag"));
				paperModel_temp.setPaperID(rs.getInt("PaperID"));
		
			
				paperList.add(paperModel_temp);
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

		return  paperList;
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
