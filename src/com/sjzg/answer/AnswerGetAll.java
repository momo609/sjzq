package com.sjzg.answer;

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

public class AnswerGetAll {

	public ArrayList<AnswerModel> DBfindAnswer(int TestID) {
		System.out.println("完成执行");
		String sql="SELECT * FROM Answer WHERE TestID = ?";

		ArrayList<AnswerModel> answerList = new ArrayList<AnswerModel>();
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
