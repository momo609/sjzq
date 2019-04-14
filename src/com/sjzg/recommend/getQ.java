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
	public static void main(String args[])
	{
		getQ.getq();
	}
	public static int[][] getq()
	{
		HashMap<String,Integer> kg=new HashMap<String,Integer>();
		kg.put("1NF",0);
        kg.put("2NF",1);
        kg.put("3NF",2);
        kg.put("BCNF",3);
        kg.put("主属性",4);
        kg.put("传递函数依赖",5);
        kg.put("决定因素",6);
        kg.put("函数依赖",7);
        kg.put("码",8);
        kg.put("部分函数依赖",9);
        kg.put("非主属性",10);
		int[][] Q=new int[56][11];
//		for(int i=0;i<56;i++)
//		{
//			for(int j=0;j<11;j++)
//			{
//				System.out.print(Q[i][j]);
//			}
//			System.out.println();
//		}
		String sql="SELECT question.id,question.stem,question.answer,question.answerkey,question.tag,question.type,r_testpaper_question.q_order "+
	               "FROM r_testpaper_question,question where r_testpaper_question.paperid=287 and r_testpaper_question.questionid=question.id ORDER BY r_testpaper_question.q_order";
		Connection conn=getConnection();
		HashMap<String,ArrayList<String>>questions=new HashMap<String,ArrayList<String>>();
		int col=0;
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			String s;
			while(rs.next())
			{
		       
		       	s=rs.getString("question.tag");
		         s.replaceAll("\\?", " "); 
				s=s.trim();
				//System.out.println(s+" "+kg.get(s));	
	            Q[col][kg.get(s)]=1;
	            col++;
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		for(int i=0;i<56;i++)
//		{
//			for(int j=0;j<11;j++)
//			{
//				System.out.print(Q[i][j]);
//			}
//			System.out.println();
//		}
		return Q;
	}
}
