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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.sjzg.database.DBConn;

public class UserForgot extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UserForgot() {
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
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		request.setCharacterEncoding("UTF-8");//设置对客户端请求进行重新编码的编码。
		response.setCharacterEncoding("UTF-8");//指定对服务器响应进行重新编码的编码。
		response.addHeader("Access-Control-Allow-Origin", "*");//跨域
		PrintWriter out = response.getWriter();//务必放在最后

		
		
		
		String MobilePhone=request.getParameter("MobilePhone");
	System.out.println("MobilePhone"+MobilePhone);
		

		UserModel DBfindUserByMobilePhone_result = DBfindUserByMobilePhone(MobilePhone);
		

		

			
			
		if (DBfindUserByMobilePhone_result == null){
			//找不到
			out.print("{\"errcode\":101,\"errmsg\":\"该手机号不存在\"}");
			out.flush();
			out.close();
			return;
		}else {
			String updateString = sendPost("https://api2.bmob.cn/1/requestSmsCode","{\"mobilePhoneNumber\":\""+MobilePhone+"\"}");
			if(updateString.equals("error")){
				out.print("{\"errcode\":101,\"errmsg\":\"请求短信出现错误,请10分钟后重试\"}");
				out.flush();
				out.close();
				return;
			}else{
				out.print("{\"errcode\":0,\"errmsg\":\"成功!\"}");
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
	public UserModel DBfindUserByMobilePhone(String MobilePhone) {
		System.out.println("完成执行DBfindUserByMobilePhone");
		String sql="SELECT * FROM User WHERE MobilePhone=?";
		
		UserModel userModel_temp = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "数据库操作异常";
		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);
			ps.setString(1, MobilePhone);
			 rs=ps.executeQuery();
			if(rs.next())
			{
				userModel_temp = new UserModel();
				userModel_temp.setUserID(rs.getString("UserID"));
				userModel_temp.setRealName(rs.getString("RealName"));
				userModel_temp.setSessionToken(rs.getString("SessionToken"));
				userModel_temp.setPassword(rs.getString("Password"));
				userModel_temp.setMobilePhone(rs.getString("MobilePhone"));
				userModel_temp.setRole(rs.getString("Role"));
				userModel_temp.setSex(rs.getString("Sex"));
				userModel_temp.setAvatar(rs.getString("Avatar"));
				userModel_temp.setNickName(rs.getString("NickName"));
			}
			conn.close();
		} catch (SQLException e) {
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



		return  userModel_temp;
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
//            out = new PrintWriter(conn.getOutputStream());
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
