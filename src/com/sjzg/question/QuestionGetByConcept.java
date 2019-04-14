package com.sjzg.question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sjzg.database.DBConn;

public class QuestionGetByConcept {
	public ArrayList<QuestionModel> GetQuestionByConcept(String pkp)
	{
       String sql="SELECT Question.QuestionID,Question.Type,Content,Choices,Image,Tag,Share,Answerkey,Difficulty,UserID,Answer,R_question_kp.kpID,Description,Knowledge_point.CreateAt FROM  Question,Knowledge_point,R_question_kp WHERE R_question_kp.KpID IN ("+pkp+") and R_question_kp.QuestionID=Question.QuestionID and R_question_kp.KpID=Knowledge_point.KpID";
	
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "数据库操作异常";


		ArrayList<QuestionModel> resultList = new ArrayList<QuestionModel>();

		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
			 
	
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
}
