package com.sjzg.course;

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


public class CourseMemberGet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public CourseMemberGet() {
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
		String CourseIDStr=request.getParameter("CourseID");
		
		int CourseID = -1;
		try {
			CourseID = Integer.parseInt(CourseIDStr);
		} catch (Exception e) {
			out.print("{\"errcode\":101,\"errmsg\":\"传入的课程ID有误\"}");
			out.flush();
			out.close();
			return;
		}
		System.out.println("取的CourseID+"+CourseID);
		
		
		
		ArrayList<CourseMemberApplyModel> DBfindCourseMember_result = DBfindCourseMember(CourseID);
		
		
		if (DBfindCourseMember_result.isEmpty()){
			out.print("{\"errcode\":301,\"errmsg\":\"查不到\"}");
			out.flush();
			out.close();
			return;
		}else {
			JsonObject jsonObject = new JsonObject();
			JsonArray memberJsonArray = new JsonArray();
			int listCount = DBfindCourseMember_result.size();
			for (int i=0;i<listCount;i++){
				JsonObject tempObject = new JsonObject();
				
				tempObject.addProperty("RealName", DBfindCourseMember_result.get(i).getRealName());
				tempObject.addProperty("Sex", DBfindCourseMember_result.get(i).getSex());
				tempObject.addProperty("Avatar", DBfindCourseMember_result.get(i).getAvatar());
				tempObject.addProperty("NickName", DBfindCourseMember_result.get(i).getNickName());
				tempObject.addProperty("UserID", DBfindCourseMember_result.get(i).getUserID());
				tempObject.addProperty("VerifyInfo", DBfindCourseMember_result.get(i).getVerifyInfo());
				tempObject.addProperty("State", DBfindCourseMember_result.get(i).getState());
				memberJsonArray.add(tempObject);
			}
			
			
			jsonObject.addProperty("errcode","0");
			jsonObject.add("threads", memberJsonArray);
			out.print(jsonObject.toString());
			out.flush();
			out.close();
			return;
		}
		
		
		
	}
	

	public ArrayList<CourseMemberApplyModel> DBfindCourseMember(int CourseID) {
		System.out.println("完成执行DBfindCourse");
		String sql="SELECT User.*,R_user_course.* FROM R_user_course , User " +
				"WHERE R_user_course.CourseID=? " +
				"AND R_user_course.UserID = User.UserID";

		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "数据库操作异常";
		
		ArrayList<CourseMemberApplyModel> memberList = new ArrayList<CourseMemberApplyModel>();

		
		
		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
			ps.setInt(1, CourseID);
			 rs=ps.executeQuery();
			
			while(rs.next())
			{
				CourseMemberApplyModel userInfoModel_temp = new CourseMemberApplyModel();

				
				userInfoModel_temp.setRealName(rs.getString("RealName"));
				userInfoModel_temp.setSex(rs.getString("Sex"));
				userInfoModel_temp.setAvatar(rs.getString("Avatar"));
				userInfoModel_temp.setNickName(rs.getString("NickName"));
				userInfoModel_temp.setUserID(rs.getString("UserID"));
				userInfoModel_temp.setState(rs.getInt("State"));
				userInfoModel_temp.setVerifyInfo(rs.getString("VerifyInfo"));

				
				memberList.add(userInfoModel_temp);
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

		return  memberList;
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
