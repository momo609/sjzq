package com.sjzg.recommend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.sjzg.database.DBConn;

public class getQ {
	private static final String DRIVER="com.mysql.jdbc.Driver";
	private static final String URL="jdbc:mysql://172.16.34.91/db_jizhitest?characterEncoding=UTF-8";
	private static final String USER="root";
	private static final String PASSWORD="a7682318";

	
	private static Connection conn;
	private static Statement st;
	private static ResultSet rs;
	
	public void connetdb()
	{
		open();
	}
	public static Connection getConnection()
	{
		try
		{
			Class.forName(DRIVER);
			conn=DriverManager.getConnection(URL,USER,PASSWORD);			
		}catch(Exception e)
		{
			
		}
		return conn;
	}
	public void open()
	{
		try
		{
			Class.forName(DRIVER);
			conn=DriverManager.getConnection(URL,USER,PASSWORD);			
		}catch(Exception e)
		{
			
		}
	}
	public ResultSet selectInfo(String sql)
	{
		try
		{
			Statement st=conn.createStatement();
			rs=st.executeQuery(sql);
		}catch (Exception e)
		{
			close();
		}
		return rs;
	}
	public int noselectInfo(String sql)
	{
		int result=0;
		try
		{
			st=conn.createStatement();
			
			
			result=st.executeUpdate(sql);
			
		}
		catch (Exception e)
		{
			close();
		}
		return result;
	}

	private void close() {
		try
		{
			if(rs!=null)rs.close();
			if(st!=null)st.close();
			if(conn!=null)conn.close();
			
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	public int[][] getq()
	{
		int[][] Q=new int[56][56];
		String sql="SELECT question.id,question.stem,question.answer,question.answerkey,question.tag,question.type,r_testpaper_question.q_order "+
	               "FROM r_testpaper_question,question where r_testpaper_question.paperid=287 and r_testpaper_question.questionid=question.id ORDER BY r_testpaper_question.q_order";
		Connection conn=getConnection();
		HashMap<String,ArrayList<String>>questions=new HashMap<String,ArrayList<String>>();
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next())
			{
				
	
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Q;
	}
}
