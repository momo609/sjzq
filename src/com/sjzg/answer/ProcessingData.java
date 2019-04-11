package com.sjzg.answer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.google.gson.JsonArray;
import com.sjzg.database.DBConn;
import com.sjzg.paper.PaperModel;
import com.sjzg.question.QuestionGetByPaper;
import com.sjzg.question.QuestionModel;

public class ProcessingData {
	public static void main(String args[])
	{
		new ProcessingData().processingdata(19);
	}
public ArrayList<String>getuserbytest(int testid)
{
	String sql="SELECT distinct UserID FROM Answer WHERE TestID = ?";
	ArrayList<String>Alluser=new ArrayList<String>();
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	String errorString = "数据库操作异常";
	
	try {
		 conn=DBConn.getConnection();
		 ps=conn.prepareStatement(sql);
		ps.setInt(1, testid);
	
		 rs=ps.executeQuery();
		
		while(rs.next())
		{	
			Alluser.add(rs.getString("UserID"));
		     
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
	return Alluser;
}
public ArrayList<ProcessedAnswerModel> processingdata(int testid)
{
	
	String sql="SELECT * FROM Answer WHERE TestID = ? AND UserID = ?";
	ArrayList<String>Alluser=getuserbytest(testid);
	
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	String errorString = "数据库操作异常";
	ArrayList<ProcessedAnswerModel>testresult=new ArrayList<ProcessedAnswerModel>();
	for(int h=0;h<Alluser.size();h++)
	{
		ArrayList<AnswerModel> answerList = new ArrayList<AnswerModel>();
		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
			 ps.setInt(1, testid);
		     ps.setString(2, Alluser.get(h));
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
				answerModel_temp.setCollection(rs.getInt("Collection"));
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
		for(int i=0;i<answerList.size();i++)
		{

			//System.out.println(answerList.get(i).getTrack());
			String patt1 ="do:(\\w*)";
			Pattern r = Pattern.compile(patt1);
			Matcher m = r.matcher(answerList.get(i).getTrack());
			String track="";
			// System.out.println("Found value: " + m.groupCount() );
		
			 while(m.find( )) {
				 if(!m.group(1).equals("OK")&&!m.group(1).equals("UNLOCK")&&!m.group(1).equals("CLEAN"))
				 {
					 track=track+m.group(1);
				 }
				// System.out.println("Found value: " + m.group(1));
				 
		      } 
	       String[] s=answerList.get(i).getAppear().split("@@");
		   answerList.get(i).setTrack(track);
		   answerList.get(i).setLookBackTime(s.length);
		   //System.out.println(track+" "+s.length);
		}
		String track="";
		String timeuse="";
		String lookbacktimes="";
		String useranswer="";
		float grade=0;
		String collection="";
		for(int i=0;i<answerList.size()-1;i++)
		{
			track=track+answerList.get(i).getTrack()+",";
			timeuse=timeuse+answerList.get(i).getTimeUsed()+",";
			lookbacktimes=lookbacktimes+answerList.get(i).getLookBackTime()+",";
			useranswer=useranswer+answerList.get(i).getUserAnswer()+"@@";
			grade=grade+answerList.get(i).getGrade();
			collection=collection+answerList.get(i).getCollection()+",";
		}
		track=track+answerList.get(answerList.size()-1).getTrack();
		timeuse=timeuse+answerList.get(answerList.size()-1).getTimeUsed();
		lookbacktimes=lookbacktimes+answerList.get(answerList.size()-1).getLookBackTime();
		grade=grade+answerList.get(answerList.size()-1).getGrade();
		useranswer=useranswer+answerList.get(answerList.size()-1).getUserAnswer();
		collection=collection+answerList.get(answerList.size()-1).getCollection();
		
		ProcessedAnswerModel processedanswerModel_temp=new ProcessedAnswerModel();
		processedanswerModel_temp.setLookBackTime(lookbacktimes);
		processedanswerModel_temp.setTestID(testid);
		processedanswerModel_temp.setTimeUsed(timeuse);
		processedanswerModel_temp.setTrack(track);	
		processedanswerModel_temp.setUserAnswer(useranswer);
		processedanswerModel_temp.setUserID(Alluser.get(h));
		processedanswerModel_temp.setGrade(grade);
		processedanswerModel_temp.setCollectionlist(collection);
		//System.out.println(processedanswerModel_temp.toString());
		testresult.add(processedanswerModel_temp);
	}
	System.out.println(testresult);
	return testresult;
	
	

	
}
}
