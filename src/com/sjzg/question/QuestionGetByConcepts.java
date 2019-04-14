package com.sjzg.question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.sjzg.database.DBConn;
import com.sjzg.paper.PaperModel;

public class QuestionGetByConcepts {
public HashMap<String,ArrayList<String>>questionnumbergetbyconcepts(int testid)
{
		System.out.println("ÕÍ≥…÷¥––DBfindPaper");
		String sql="SELECT R_question_kp.QuestionID,Knowledge_point.KpID,Knowledge_point.Description,Test.PaperID FROM  R_question_kp,Test,Knowledge_point "+
		          "where Test.TestID=? and  R_question_kp.PaperID=Test.PaperID and Knowledge_point.KpID=R_question_kp.KpID ";
		Connection conn=DBConn.getConnection();
		HashMap<String,ArrayList<String>>questions=new HashMap<String,ArrayList<String>>();
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setInt(1, testid);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next())
			{
				if(questions.get(rs.getString("Description"))!=null)
				{
					String questionid=rs.getInt("QuestionID")+"";
					questions.get(rs.getString("Description")).add(questionid);
				}
				else
				{
					ArrayList<String> questionlist=new ArrayList<String>();
					String questionid=rs.getInt("QuestionID")+"";
					questionlist.add(questionid);
					questions.put(rs.getString("Description"), questionlist);
				}
	
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
      
        
		return  questions;
	
}
}
