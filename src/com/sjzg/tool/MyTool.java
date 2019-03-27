package com.sjzg.tool;

import java.sql.SQLException;

import java.sql.*;


import com.sjzg.database.DBConn;

public class MyTool {
	public static boolean DBcheckSessionToken(String SessionToken) {
	
		if(SessionToken != null && SessionToken.length()>0){
		//符合要求
		}else{
		
			return false;
		}
		String sql="SELECT * FROM User WHERE SessionToken=?";	
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isAllow = false;
		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
			ps.setString(1, SessionToken);
			 rs=ps.executeQuery();
			if(rs.next())
			{
				isAllow = true;
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}   finally {
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

		return  isAllow;
	}
	
	
	public static String DBcheckUserRole(String SessionToken) {
	
		
		if(SessionToken != null && SessionToken.length()>0){
		//符合要求
		}else{
		
			return "student";
		}
		String sql="SELECT * FROM User WHERE SessionToken=?";	
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String role = "student";
		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
			ps.setString(1, SessionToken);
			 rs=ps.executeQuery();
			if(rs.next())
			{
				role = rs.getString("Role");
			}
			conn.close();
		} catch (SQLException e) {
			role = "student";
			e.printStackTrace();
		}   finally {
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

		return  role;
	}
}
