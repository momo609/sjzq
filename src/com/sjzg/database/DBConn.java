package com.sjzg.database;

import java.sql.*;

public class DBConn {
	private static final String DRIVER="com.mysql.jdbc.Driver";
	private static final String URL="jdbc:mysql://172.16.34.91/sjzg?characterEncoding=UTF-8";
	private static final String USER="root";
	private static final String PASSWORD="a7682318";

	
	private static Connection conn;
	private static Statement st;
	private static ResultSet rs;
	
	public DBConn()
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

}
