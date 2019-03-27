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

import com.sjzg.database.DBConn;

public class UserResetMobile extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UserResetMobile() {
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
		request.setCharacterEncoding("UTF-8");//设置对客户端请求进行重新编码的编码。
		response.setCharacterEncoding("UTF-8");//指定对服务器响应进行重新编码的编码。
		response.addHeader("Access-Control-Allow-Origin", "*");//跨域
		PrintWriter out = response.getWriter();//务必放在最后
		

		String MobilePhone=request.getParameter("MobilePhone");
		String Password=request.getParameter("Password");
		String SmsCode=request.getParameter("SmsCode");
		String UserID=request.getParameter("UserID");

		
		//第二步，检查数据是否有误

		if(UserID==null||UserID.equals(""))
		{
			out.print("{\"errcode\":201,\"errmsg\":\"请检查传入参数\"}");
			out.flush();
			out.close();
			return;
		}
		if(Password==null||Password.equals(""))
		{
			out.print("{\"errcode\":201,\"errmsg\":\"请检查传入参数\"}");
			out.flush();
			out.close();
			return;
		}
		if(MobilePhone==null||MobilePhone.equals(""))
		{
			out.print("{\"errcode\":201,\"errmsg\":\"请检查传入参数\"}");
			out.flush();
			out.close();
			return;
		}
		if(SmsCode==null||SmsCode.equals(""))
		{
			out.print("{\"errcode\":201,\"errmsg\":\"请检查传入参数\"}");
			out.flush();
			out.close();
			return;
		}
		System.out.println("取的MobilePhone+"+MobilePhone);
		
		
		
		
		
		//第三步，数据库操作
		
		UserModel DBfindUser_result = DBfindUser(UserID);

		if (DBfindUser_result != null ){
			//从ID找到用户
			if(DBfindUser_result.getPassword().equals(Password)){
				//判断密码
				
				String updateString = sendPost("https://api2.bmob.cn/1/verifySmsCode/"+SmsCode,"{\"mobilePhoneNumber\":\""+MobilePhone+"\"}");
				if(updateString.equals("error")){
					out.print("{\"errcode\":101,\"errmsg\":\"短信验证码错误!\"}");
					out.flush();
					out.close();
					return;
				}else{
					String DBupdateUserPhone_result = DBupdateUserPhone(UserID,MobilePhone);
					if (DBupdateUserPhone_result.equals("ok")) {
						out.print("{\"errcode\":0,\"errmsg\":\"修改成功！\"}");
						out.flush();
						out.close();
						return;

					}else {
						out.print("{\"errcode\":101,\"errmsg\":\""+DBupdateUserPhone_result+"\"}");
						out.flush();
						out.close();
						return;

					}
				}
				
				
			}else {
				out.print("{\"errcode\":101,\"errmsg\":\"登陆密码错误\"}");
				out.flush();
				out.close();
				return;
			}
		}
		
		
		
		
		
		

		

		

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
		System.out.println("完成执行DBfindUser");
		String sql="SELECT * FROM User WHERE UserID=?";
		UserModel userModel_temp = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "数据库操作异常";
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

	
	public String DBupdateUserPhone(String UserID,String MobilePhone) {
		System.out.println("执行DBupdateUser");

		String sql="UPDATE User SET MobilePhone = ? , UpdateAt = ? WHERE UserID=? ";

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "数据库操作异常";
		int influence=0;//影响的行数
		Date nowDate = new Date();
		java.sql.Timestamp nowTimestamp = new java.sql.Timestamp(nowDate.getTime());
		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
	
			 
				ps.setString(1, MobilePhone);
				 ps.setTimestamp(2, nowTimestamp); 

				ps.setString(3, UserID);
			
		
			influence=ps.executeUpdate();
			conn.close();
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
			if(influence<1)
			{
				return 	errorString;
			}
			else 
			{
				return "ok";
			}
			
	}
	
	 public static String sendPost(String url, String param) {
	        PrintWriter out = null;
	        BufferedReader in = null;
	        String result = "";
	        try {
	            URL realUrl = new URL(url);
	            // 打开和URL之间的连接
	            URLConnection conn = realUrl.openConnection();
	            // 设置通用的请求属性
	            conn.setRequestProperty("accept", "*/*");
	            conn.setRequestProperty("connection", "Keep-Alive");
	            conn.setRequestProperty("Content-Type",  
	                    "application/json");  
	            
	          //===============需要改的地方=========================
	            
	            conn.setRequestProperty("X-Bmob-Application-Id", "8b45e47ae7f91c6e68e86dc355400c2f");
	            conn.setRequestProperty("X-Bmob-REST-API-Key", "22e3e61b87f8818b83a4f1a75420b9b0");
	            
	          //===============需要改的地方=========================
	            conn.setReadTimeout(8000);
	            conn.setConnectTimeout(8000);
	            
	            // 发送POST请求必须设置如下两行
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            conn.setUseCaches(false);
	            // 获取URLConnection对象对应的输出流
//	            out = new PrintWriter(conn.getOutputStream());
	            // 获取URLConnection对象对应的输出流，设置utf-8编码
	            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
	            // 发送请求参数
	            out.print(param);
	            // flush输出流的缓冲
	            out.flush();
	            // 定义BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(
	                    new InputStreamReader(conn.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	            System.out.println("发送 POST 请求出现异常！"+e);
	            return "error";
	        }
	        //使用finally块来关闭输出流、输入流
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
	    
	
}
