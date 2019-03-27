package com.sjzg.tool;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.sjzg.database.DBConn;
import com.sjzg.user.UserModel;

public class ReadExcel extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ReadExcel() {
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


		doPost(request, response);
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
		PrintWriter out = response.getWriter();
		request.setCharacterEncoding("UTF-8");//设置对客户端请求进行重新编码的编码。
		response.setCharacterEncoding("UTF-8");//指定对服务器响应进行重新编码的编码。
		if(1>0){
			return;
		}
		
	       String excelPath = "D:\\SHARE\\宋玲老师-计算机网络原理最新名单.xls";

	        try {
	            //String encoding = "GBK";
	            File excel = new File(excelPath);
	            if (excel.isFile() && excel.exists()) {   //判断文件是否存在

	                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
	                Workbook wb;
	                //根据文件后缀（xls）进行判断
	                if ( "xls".equals(split[1])){
	                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
	                    wb = new HSSFWorkbook(fis);
	                }else {
	                    System.out.println("文件类型错误!");
	                    return;
	                }

	                //开始解析
	                Sheet sheet = wb.getSheetAt(0);     //读取sheet 0

	                int firstRowIndex = sheet.getFirstRowNum()+1;   //第一行是列名，所以不读
	                int lastRowIndex = sheet.getLastRowNum();
	                System.out.println("firstRowIndex: "+firstRowIndex);
	                System.out.println("lastRowIndex: "+lastRowIndex);

	                for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
	                    System.out.println("rIndex: " + rIndex);
	                    Row row = sheet.getRow(rIndex);
	                    if (row != null) {
	                    	
                    		UserModel userModel=new UserModel();
                    		userModel.setUserID("10593"+row.getCell(0).toString());
                    		userModel.setPassword("12345678");
                    		if(row.getCell(2).toString().equals("男")){
                    			userModel.setSex("man");
                    		}else{
                    			userModel.setSex("woman");
                    		}
                    		
                    		userModel.setRealName(row.getCell(1).toString());
                    		userModel.setMobilePhone("10593"+row.getCell(0).toString());
                    		
                    		
                    		userModel.setRole("student");
                    		
                    		userModel.setAvatar("https://sjzg-1252169987.cos.ap-guangzhou.myqcloud.com/default-avatar.png");
                    		userModel.setNickName("新朋友");
                    		
                    		userModel.setSessionToken(userModel.createSessionToken("10593"+row.getCell(0).toString()));
                    		
                    		
                    		//添加用户表
                    		UserModel DBfindUser_result = DBfindUser(userModel.getUserID());
                    		if (DBfindUser_result == null ){

                        		String DBcreateUser_result = DBcreateUser(userModel);
                        		System.out.println(DBcreateUser_result);
                    		}else{
                    			System.out.println(userModel.getUserID()+"已存在");
                    		}
                    	
                    		
    
                    		//添加课程关系表
                    		String DBjoinCourse_result = DBjoinCourse(userModel.getUserID(),1019);
                    		System.out.println(DBjoinCourse_result);
//                    		
                    		
                    		
                    		Thread.sleep(100);
	                    }
	                }
	            } else {
	                System.out.println("找不到指定的文件");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        out.print("{\"errcode\":0,\"errmsg\":\"5555\"}");
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
	
	
	
	
	
	
	public String DBcreateUser(UserModel userModel) {
		System.out.println("执行DBcreateUser");
		String sql="INSERT INTO User(UserID,RealName,SessionToken,MobilePhone,Password,Role,Sex,Avatar,NickName,CreateAt,UpdateAt) VALUES(?,?,?,?,?,?,?,?,?,?,?)";


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
			ps.setString(1, userModel.getUserID());
			ps.setString(2, userModel.getRealName());
			ps.setString(3, userModel.getSessionToken());
			ps.setString(4, userModel.getMobilePhone());
			ps.setString(5, userModel.getPassword());
			ps.setString(6, userModel.getRole());
			ps.setString(7, userModel.getSex());
			ps.setString(8, userModel.getAvatar());
			ps.setString(9, userModel.getNickName());
			ps.setTimestamp(10, nowTimestamp);
			ps.setTimestamp(11, nowTimestamp);
			influence+=ps.executeUpdate();
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			errorString+=e.getMessage();
		}  finally {
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
				return "error";
			}
			else 
			{
				return userModel.getSessionToken();
			}
			
	}
	
	
	
	
	public String DBjoinCourse(String UserID,int CourseID) {
		System.out.println("执行DBjoinCourse");
		String sql="INSERT INTO R_user_course(UserID,CourseID,CreateAt,UpdateAt,State,VerifyInfo) VALUES(?,?,?,?,?,?)";

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
			ps.setString(1, UserID);
			ps.setInt(2, CourseID);
			ps.setTimestamp(3, nowTimestamp);
			ps.setTimestamp(4, nowTimestamp);
			ps.setInt(5, 2);
			ps.setString(6, "null");
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
				return errorString;
			}
			else 
			{
				return "ok";
			}
			
	}
	
	
}
