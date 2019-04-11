package com.sjzg.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sjzg.database.DBConn;

public class TestGetByUser extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public TestGetByUser() {
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
		String UserID=request.getParameter("UserID");


		
		ArrayList<TestModel> DBfindTest_result = DBfindTest(UserID);
		
		
		if (DBfindTest_result.isEmpty()){
			out.print("{\"errcode\":100,\"errmsg\":\"搜索不到试卷\"}");
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
				tempObject.addProperty("CourseName", DBfindTest_result.get(i).getExtendContent());
				if(DBfindTestAnswer( DBfindTest_result.get(i).getTestID() ,UserID)   ){
					tempObject.addProperty("Finished", true);
				}else{
					tempObject.addProperty("Finished", false);
				}
				
				
				boolean TimeEarly  = false;
				boolean TimeLate  = false;
				
				
				Date cuttentDate = new Date();
				SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				
				long diff = 0;
				try {
					diff = (DateFormat.parse(DBfindTest_result.get(i).getStartTime()).getTime() - cuttentDate.getTime());
				    if (diff>0) {
		            	TimeEarly = true;
		            }
				    diff = (DateFormat.parse(DBfindTest_result.get(i).getEndTime()).getTime() - cuttentDate.getTime());
				    if (diff<0) {
				    	TimeLate = true;
		            }
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        
				tempObject.addProperty("TimeEarly", TimeEarly);
				tempObject.addProperty("TimeLate", TimeLate);
				
				
				
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
	
	
	
	public ArrayList<TestModel> DBfindTest(String UserID) {
		System.out.println("完成执行DBfindTest");
		String sql="SELECT Test.*,R_user_course.* , Course.CourseName FROM  Test , R_user_course ,Course " +
				"WHERE R_user_course.UserID=? " +
				"AND Course.CourseID = Test.CourseID "+
				"AND Test.CourseID = R_user_course.CourseID "+
				"AND R_user_course.State=2 ;";

		ArrayList<TestModel> testList = new ArrayList<TestModel>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "数据库操作异常";

		try {
			
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
			ps.setString(1, UserID);
			 rs=ps.executeQuery();
			
			while(rs.next())
			{
				TestModel testModel_temp = new TestModel();

				testModel_temp.setPaperID(rs.getInt("PaperID"));
				testModel_temp.setCourseID(rs.getInt("CourseID"));
				testModel_temp.setTestTime(rs.getInt("TestTime"));
				testModel_temp.setTestName(rs.getString("TestName"));
				testModel_temp.setTestID(rs.getInt("TestID"));
				testModel_temp.setEndTime(rs.getString("EndTime"));
				testModel_temp.setStartTime(rs.getString("StartTime"));
				testModel_temp.setExtendContent(rs.getString("CourseName"));
				testModel_temp.setCreateAt(rs.getString("CreateAt"));
				
				
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
	
	
	public boolean DBfindTestAnswer(int TestID , String UserID) {
		System.out.println("完成执行DBfindTestAnswer");
		String sql="SELECT Test.*,Answer.* FROM  Answer , Test  " +
				"WHERE Test.TestID = ? " +
				"AND Test.TestID = Answer.TestID "+
				"AND Test.State = 'open' "+
				"AND Answer.UserID = ? ";



		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean testFinished = false;
		String errorString = "数据库操作异常";

		try {
			
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
			ps.setInt(1, TestID);
			ps.setString(2, UserID);
			 rs=ps.executeQuery();
			
			if(rs.next())
			{
				testFinished = true;
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

		return  testFinished;
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
