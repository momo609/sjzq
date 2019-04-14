package com.sjzg.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.sjzg.database.DBConn;

public class UserResetMobileCheck extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UserResetMobileCheck() {
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
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
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
		request.setCharacterEncoding("UTF-8");//���öԿͻ�������������±���ı��롣
		response.setCharacterEncoding("UTF-8");//ָ���Է�������Ӧ�������±���ı��롣
		response.addHeader("Access-Control-Allow-Origin", "*");//����
		PrintWriter out = response.getWriter();//��ط������

		
		//��һ����ȡ����
		String UserID=request.getParameter("UserID");
		String Password=request.getParameter("Password");
		String MobilePhone=request.getParameter("MobilePhone");
		
		
		System.out.println("ȡ��UserID+"+UserID);
		System.out.println("ȡ��Password+"+Password);


		//�ڶ�������������Ƿ�����

		if(UserID==null||UserID.equals(""))
		{
			out.print("{\"errcode\":201,\"errmsg\":\"���鴫�����\"}");
			out.flush();
			out.close();
			return;
		}
		if(Password==null||Password.equals(""))
		{
			out.print("{\"errcode\":201,\"errmsg\":\"���鴫�����\"}");
			out.flush();
			out.close();
			return;
		}
		if(MobilePhone==null||MobilePhone.equals(""))
		{
			out.print("{\"errcode\":201,\"errmsg\":\"���鴫�����\"}");
			out.flush();
			out.close();
			return;
		}
		//�����������ݿ����

		
		
		
		UserModel DBfindUserByPhone_result = DBfindUserByPhone(MobilePhone);
		
		if (DBfindUserByPhone_result != null ){
			//���ֻ����Ѵ���
			out.print("{\"errcode\":401,\"errmsg\":\"�ֻ����Ѵ���\"}");
			out.flush();
			out.close();
			return;
		}
		
		
		UserModel DBfindUser_result = DBfindUser(UserID);

		if (DBfindUser_result != null ){
			//��ID�ҵ��û�
			if(DBfindUser_result.getPassword().equals(Password)){
				//�ж�����
				String updateString = sendPost("https://api2.bmob.cn/1/requestSmsCode","{\"mobilePhoneNumber\":\""+MobilePhone+"\"}");
				if(updateString.equals("error")){
					out.print("{\"errcode\":101,\"errmsg\":\"������ų��ִ���,��10���Ӻ�����\"}");
					out.flush();
					out.close();
					return;
				}else{
					out.print("{\"errcode\":0,\"errmsg\":\"�ɹ�!\"}");
					out.flush();
					out.close();
					return;
					
				}
			
			}else {
				out.print("{\"errcode\":101,\"errmsg\":\"��½�������\"}");
				out.flush();
				out.close();
				return;
			}
		}
		
		
	
		
		
	
		

		out.print("{\"errcode\":101,\"errmsg\":\"ϵͳ�쳣\"}");
		out.flush();
		out.close();
		return;
		
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

	
	public UserModel DBfindUser(String UserID) {
		System.out.println("���ִ��DBfindUser");
		String sql="SELECT * FROM User WHERE UserID=?";
		UserModel userModel_temp = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "���ݿ�����쳣";
		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
			ps.setString(1, UserID);
			 rs=ps.executeQuery();
			if(rs.next())
			{
				userModel_temp = new UserModel();
				userModel_temp.setUserID(rs.getString("UserID"));
				userModel_temp.setRealName(rs.getString("RealName"));
				userModel_temp.setSessionToken(rs.getString("SessionToken"));
				userModel_temp.setMobilePhone(rs.getString("MobilePhone"));
				userModel_temp.setPassword(rs.getString("Password"));
				userModel_temp.setRole(rs.getString("Role"));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			errorString+=e.getMessage();
		} finally {
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

		return  userModel_temp;
	}

	 public static String sendPost(String url, String param) {
	        PrintWriter out = null;
	        BufferedReader in = null;
	        String result = "";
	        try {
	            URL realUrl = new URL(url);
	            // �򿪺�URL֮�������
	            URLConnection conn = realUrl.openConnection();
	            // ����ͨ�õ���������
	            conn.setRequestProperty("accept", "*/*");
	            conn.setRequestProperty("connection", "Keep-Alive");
	            conn.setRequestProperty("Content-Type",  
	                    "application/json");  
	            
	          //===============��Ҫ�ĵĵط�=========================
	            
	            conn.setRequestProperty("X-Bmob-Application-Id", "8b45e47ae7f91c6e68e86dc355400c2f");
	            conn.setRequestProperty("X-Bmob-REST-API-Key", "22e3e61b87f8818b83a4f1a75420b9b0");
	            
	          //===============��Ҫ�ĵĵط�=========================
	            
	            
	            // ����POST�������������������
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            conn.setUseCaches(false);
	            // ��ȡURLConnection�����Ӧ�������
//	            out = new PrintWriter(conn.getOutputStream());
	            // ��ȡURLConnection�����Ӧ�������������utf-8����
	            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
	            // �����������
	            out.print(param);
	            // flush������Ļ���
	            out.flush();
	            // ����BufferedReader����������ȡURL����Ӧ
	            in = new BufferedReader(
	                    new InputStreamReader(conn.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	            System.out.println("���� POST ��������쳣��"+e);
	            return "error";
	        }
	        //ʹ��finally�����ر��������������
	        finally{
	            try{
	                if(out!=null){
	                    out.close();
	                }
	                if(in!=null){
	                    in.close();
	                }
	            }
	            catch(IOException ex){
	                ex.printStackTrace();
	            }
	        }
	        return result;
	    }    
	    
	 public UserModel DBfindUserByPhone(String UserID) {
			System.out.println("���ִ��DBfindUser");
			String sql="SELECT * FROM User WHERE MobilePhone=?";
			UserModel userModel_temp = null;
			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			String errorString = "���ݿ�����쳣";
			try {
				 conn=DBConn.getConnection();
				 ps=conn.prepareStatement(sql);
				ps.setString(1, UserID);
				 rs=ps.executeQuery();
				if(rs.next())
				{
					userModel_temp = new UserModel();
					userModel_temp.setUserID(rs.getString("UserID"));
					userModel_temp.setRealName(rs.getString("RealName"));
					userModel_temp.setSessionToken(rs.getString("SessionToken"));
					userModel_temp.setMobilePhone(rs.getString("MobilePhone"));
					userModel_temp.setPassword(rs.getString("Password"));
					userModel_temp.setRole(rs.getString("Role"));
				}
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				errorString+=e.getMessage();
			} finally {
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

			return  userModel_temp;
		}
}
