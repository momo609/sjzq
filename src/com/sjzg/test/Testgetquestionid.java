package com.sjzg.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.mysql.jdbc.Statement;
import com.sjzg.database.DBConn;
import com.sjzg.question.QuestionModel;

public class Testgetquestionid {
	public static void main(String args[]){
		String sql="INSERT INTO Question(Type,Content,Choices,Answer,Answerkey,Difficulty,UserID) VALUES(?,?,?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "数据库操作异常";
		Date nowDate = new Date();
		java.sql.Timestamp nowTimestamp = new java.sql.Timestamp(nowDate.getTime());
		QuestionModel questionModel=new QuestionModel();
		questionModel.setType(1);
		questionModel.setAnswer("1234");
		questionModel.setAnswerkey("12234");
		questionModel.setChoices("1234");
		questionModel.setContent("1234");
		questionModel.setDifficulty(1);
		questionModel.setUserID("1234");
		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

			ps.setInt(1, questionModel.getType());
			ps.setString(2, questionModel.getContent());
			ps.setString(3, questionModel.getChoices());
			ps.setString(4, questionModel.getAnswer());
			ps.setString(5, questionModel.getAnswerkey());
			ps.setFloat(6, questionModel.getDifficulty());
			ps.setString(7, questionModel.getUserID());
			ps.executeUpdate();
		    rs = ps.getGeneratedKeys(); //获取结果   
			if (rs.next()) {
			    questionModel.setQuestionID(rs.getInt(1));//取得ID
			}
			
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
	    System.out.println(questionModel.toString());
			
	}
	
}
