package com.sjzg.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sjzg.database.DBConn;

public class TestCreate extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public TestCreate() {
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
		request.setCharacterEncoding("UTF-8");//���öԿͻ�������������±���ı��롣
		response.setCharacterEncoding("UTF-8");//ָ���Է�������Ӧ�������±���ı��롣
		response.addHeader("Access-Control-Allow-Origin", "*");//����
		PrintWriter out = response.getWriter();//��ط������
		
		
		//��һ����ȡ����
		String CourseIDStr=request.getParameter("CourseID");
		String PaperIDStr=request.getParameter("PaperID");
		String TestTimeStr=request.getParameter("TestTime");
		
		int CourseID = 0;
		try {
			CourseID = Integer.parseInt(CourseIDStr);
		} catch (Exception e) {
			out.print("{\"errcode\":101,\"errmsg\":\"�γ�ID��Ϣ����\"}");
			out.flush();
			out.close();
			return;
		}
		
		int PaperID = 0;
		try {
			PaperID = Integer.parseInt(PaperIDStr);
		} catch (Exception e) {
			out.print("{\"errcode\":101,\"errmsg\":\"�Ծ�ID��Ϣ����\"}");
			out.flush();
			out.close();
			return;
		}
		
		int TestTime = 0;
		try {
			TestTime = Integer.parseInt(TestTimeStr);
		} catch (Exception e) {
			out.print("{\"errcode\":101,\"errmsg\":\"ʱ������\"}");
			out.flush();
			out.close();
			return;
		}
		
		String TestName = request.getParameter("TestName");
		String StartTime = request.getParameter("StartTime");
		String EndTime = request.getParameter("EndTime");
		

		TestModel testModel = new TestModel();
		testModel.setPaperID(PaperID);
		testModel.setCourseID(CourseID);
		testModel.setStartTime(StartTime);
		testModel.setEndTime(EndTime);
		testModel.setTestName(TestName);
		testModel.setTestTime(TestTime);
		testModel.setState("open");
		
		//�ڿ��Խ��������ģ��ѵ��
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				//�����ǵ��÷���
			}
		}, Long.parseLong(EndTime)-Long.parseLong(StartTime));
		
		
		
		
		//�ڶ�������������Ƿ�����
		if (!testModel.validate().equals("ok")){
			out.print("{\"errcode\":101,\"errmsg\":\""+testModel.validate()+"\"}");
			out.flush();
			out.close();
			return;
		}
		
		
		String DBcreateTest_result = DBcreateTest(testModel);
		
		if (!DBcreateTest_result.equals("ok")) {
			out.print("{\"errcode\":101,\"errmsg\":\""+DBcreateTest_result+"\"}");
			out.flush();
			out.close();
			return;
			

		}else {
			out.print("{\"errcode\":0,\"errmsg\":\"�����ɹ���\"}");
			out.flush();
			out.close();
			return;
		}
	}

	
	public String DBcreateTest(TestModel testModel) {
		System.out.println("ִ��DBcreateTest");
		String sql="INSERT INTO Test(PaperID,CourseID,TestName,TestTime,StartTime,EndTime,State,CreateAt,UpdateAt) VALUES(?,?,?,?,?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "���ݿ�����쳣";

		int influence=0;//Ӱ�������
		Date nowDate = new Date();
		java.sql.Timestamp nowTimestamp = new java.sql.Timestamp(nowDate.getTime());
		
		try {
			 conn=DBConn.getConnection();
			 ps=conn.prepareStatement(sql);

			
			
			ps.setInt(1, testModel.getPaperID());
			ps.setInt(2, testModel.getCourseID());
			ps.setString(3, testModel.getTestName());
			ps.setInt(4, testModel.getTestTime());
			SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Timestamp sqlTimestamp;
			sqlTimestamp=new java.sql.Timestamp (DateFormat.parse(testModel.getStartTime()).getTime());
			ps.setTimestamp(5, sqlTimestamp);
			sqlTimestamp=new java.sql.Timestamp (DateFormat.parse(testModel.getEndTime()).getTime());
			ps.setTimestamp(6, sqlTimestamp);

			ps.setString(7, testModel.getState());
			
			ps.setTimestamp(8, nowTimestamp);
			ps.setTimestamp(9, nowTimestamp);
			
			influence+=ps.executeUpdate();


			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			errorString+=e.getLocalizedMessage();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			errorString+=e.getLocalizedMessage();
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
			if(influence<1)
			{
				return errorString;
			}
			else 
			{
				return "ok";
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

}
