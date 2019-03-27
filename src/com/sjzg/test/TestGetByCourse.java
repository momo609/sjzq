package com.sjzg.test;

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

public class TestGetByCourse extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public TestGetByCourse() {
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
		String CourseIDStr=request.getParameter("CourseID");
		int CourseID = 0;
		
		try {
			CourseID = Integer.parseInt(CourseIDStr);
		} catch (Exception e) {
			out.print("{\"errcode\":101,\"errmsg\":\"课程ID有误\"}");
			out.flush();
			out.close();
			return;
		}
		

		
		ArrayList<TestModel> DBfindTest_result = DBfindTest(CourseID);
		
		
		if (DBfindTest_result.isEmpty()){
			out.print("{\"errcode\":100,\"errmsg\":\"搜索不到测试\"}");
			out.flush();
			out.close();
			return;
		}else {
			JsonObject jsonObject = new JsonObject();
			JsonArray questionJsonArray = new JsonArray();
			int listCount = DBfindTest_result.size();
			for (int i=listCount-1;i>=0;i--){
				JsonObject tempObject = new JsonObject();
				tempObject.addProperty("TestID", DBfindTest_result.get(i).getTestID());
				tempObject.addProperty("CourseID", DBfindTest_result.get(i).getCourseID());
				tempObject.addProperty("PaperID", DBfindTest_result.get(i).getPaperID());
				tempObject.addProperty("TestName", DBfindTest_result.get(i).getTestName());
				tempObject.addProperty("TestTime", DBfindTest_result.get(i).getTestTime());
				tempObject.addProperty("StartTime", DBfindTest_result.get(i).getStartTime());
				tempObject.addProperty("EndTime", DBfindTest_result.get(i).getEndTime());
				tempObject.addProperty("CreateAt", DBfindTest_result.get(i).getCreateAt());
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
	
	
	public ArrayList<TestModel> DBfindTest(int CourseID) {
		System.out.println("完成执行TestModel");
		String sql="SELECT *  FROM  Test " +
				"WHERE CourseID=? ";
		ArrayList<TestModel> testList = new ArrayList<TestModel>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "数据库操作异常";
		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
			ps.setInt(1, CourseID);
			 rs=ps.executeQuery();
			
			while(rs.next())
			{
				TestModel testModel_temp = new TestModel();

				testModel_temp.setTestID(rs.getInt("TestID"));
				testModel_temp.setPaperID(rs.getInt("PaperID"));
				testModel_temp.setCourseID(rs.getInt("CourseID"));
				testModel_temp.setTestTime(rs.getInt("TestTime"));
				testModel_temp.setTestName(rs.getString("TestName"));
				testModel_temp.setEndTime(rs.getString("EndTime"));
				testModel_temp.setStartTime(rs.getString("StartTime"));
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(rs.getTimestamp("CreateAt"));
				testModel_temp.setCreateAt((calendar.get(Calendar.YEAR))+"-"+(calendar.get(Calendar.MONDAY)+1) + "-" +(calendar.get(Calendar.DAY_OF_MONTH)));
				
				
				testList.add(testModel_temp);
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

		return  testList;
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
