package com.sjzg.question;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sjzg.course.CourseModel;
import com.sjzg.database.DBConn;

public class QuestionCreate extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public QuestionCreate() {
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
		request.setCharacterEncoding("UTF-8");//设置对客户端请求进行重新编码的编码。
		response.setCharacterEncoding("UTF-8");//指定对服务器响应进行重新编码的编码。
		response.addHeader("Access-Control-Allow-Origin", "*");//跨域
		PrintWriter out = response.getWriter();//务必放在最后
		
		
		//第一步，取数据
		String TypeStr=request.getParameter("Type");
		String Content=request.getParameter("Content");
		String Choices = request.getParameter("Choices");
		String Answerkey = request.getParameter("Answerkey");
		String Answer = request.getParameter("Answer");
		String Image = request.getParameter("Image");
		String Tag = request.getParameter("Tag");
		String UserID = request.getParameter("UserID");
		String ShareStr = request.getParameter("Share");
		String knowledgepoint=request.getParameter("knowledgepoint");
		
		int Type = 0;
		try {
			Type = Integer.parseInt(TypeStr);
		} catch (Exception e) {
			out.print("{\"errcode\":201,\"errmsg\":\"选择类型有误\"}");
			out.flush();
			out.close();
			return;
		}
		
		
		int Share = 0;
		try {
			Share = Integer.parseInt(ShareStr);
		} catch (Exception e) {
			out.print("{\"errcode\":101,\"errmsg\":\"是否公开有误\"}");
			out.flush();
			out.close();
			return;
		}
		System.out.println("取的Content+"+Content);

		
		QuestionModel questionModel=new QuestionModel();
		questionModel.setQuestionID(0);
		questionModel.setType(Type);
		questionModel.setContent(Content);
		questionModel.setChoices(Choices);
		questionModel.setAnswerkey(Answerkey);
		questionModel.setAnswer(Answer);
		questionModel.setImage(Image);
		questionModel.setTag(Tag);
		questionModel.setDifficulty(0);
		questionModel.setUserID(UserID);
		questionModel.setShare(Share);
		questionModel.setKnowledgepoint(knowledgepoint);
		//第二步，检查数据是否有误
		if (!questionModel.validate().equals("ok")){
			out.print("{\"errcode\":101,\"errmsg\":\""+questionModel.validate()+"\"}");
			out.flush();
			out.close();
			return;
		}
		
		
		String DBcreateQuestion_result = null;
		try {
			DBcreateQuestion_result = DBcreateQuestion(questionModel);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (!DBcreateQuestion_result.equals("ok")) {
			out.print("{\"errcode\":101,\"errmsg\":\"系统异常\"}");
			out.flush();
			out.close();
			return;
			

		}else {
			out.print("{\"errcode\":0,\"errmsg\":\"创建成功！\"}");
			out.flush();
			out.close();
			return;
		}
	
		
	}
    public String InsertkpTOq(QuestionModel questionModel) throws SQLException
    {
    	String errorString = "数据库操作异常";
    	Date nowDate = new Date();
		java.sql.Timestamp nowTimestamp = new java.sql.Timestamp(nowDate.getTime());
    	String findkp="SELECT * FROM Knowledge_point WHERE Description=?";
    	Connection conn = DBConn.getConnection();;
		PreparedStatement ps = conn.prepareStatement(findkp);
		ps.setString(1, questionModel.getKnowledgepoint());
		ResultSet rs =ps.executeQuery();
		int influence=0;//影响的行数
		if(rs.next())
		{
			questionModel.setKnowledgeid(rs.getInt("KpID"));
		}
		try
		{
			String insertkptoq="INSERT INTO R_question_kp(QuestionID,KpID,CreateAt,UpdateAt) VALUES(?,?,?,?)";
		    conn = DBConn.getConnection();;
		    ps = conn.prepareStatement(insertkptoq);
		    ps.setInt(1, questionModel.getQuestionID());
		    ps.setInt(2, questionModel.getKnowledgeid());
		    ps.setTimestamp(3, nowTimestamp);
		    ps.setTimestamp(4, nowTimestamp);
		    ps.executeUpdate();

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
	
	
	public String DBcreateQuestion(QuestionModel questionModel) throws SQLException {
		System.out.println("执行DBcreateQuestion");
		String sql="INSERT INTO Question(Type,Content,Choices,Answer,Answerkey,Image,Tag,Difficulty,Share,UserID,CreateAt,UpdateAt) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
		String findkp="SELECT * FROM Knowledge_point WHERE Description=?";
		String errorString = "数据库操作异常";
		Date nowDate = new Date();
		java.sql.Timestamp nowTimestamp = new java.sql.Timestamp(nowDate.getTime());
		int influence=0;//影响的行数
		Connection conn = DBConn.getConnection();;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ps=conn.prepareStatement(findkp);
		ps.setString(1, questionModel.getKnowledgepoint());
		rs=ps.executeQuery();
		if(!rs.next())
		{
			String createkp="INSERT INTO Knowledge_point(Description,CreateAt,UpdateAt) VALUES(?,?,?)";
			ps=conn.prepareStatement(createkp);
			ps.setString(1, questionModel.getKnowledgepoint());
			ps.setTimestamp(2, nowTimestamp);
			ps.setTimestamp(3, nowTimestamp);
		}
		InsertkpTOq(questionModel);
		try {
			 ps=conn.prepareStatement(sql);

			ps.setInt(1, questionModel.getType());
			ps.setString(2, questionModel.getContent());
			ps.setString(3, questionModel.getChoices());
			ps.setString(4, questionModel.getAnswer());
			ps.setString(5, questionModel.getAnswerkey());
			ps.setString(6, questionModel.getImage());
			ps.setString(7, questionModel.getTag());
			ps.setFloat(8, questionModel.getDifficulty());
			ps.setInt(9, questionModel.getShare());
			ps.setString(10, questionModel.getUserID());
			ps.setTimestamp(11, nowTimestamp);
			ps.setTimestamp(12, nowTimestamp);
			
			influence+=ps.executeUpdate();


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
	

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
