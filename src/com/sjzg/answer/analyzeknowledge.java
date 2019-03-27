

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.sjzg.database.DBConn;
import com.sjzg.paper.PaperModel;
import com.sjzg.question.QuestionGetByPaper;
import com.sjzg.question.QuestionModel;
import com.sjzg.recommend.finaltest;



public class analyzeknowledge {
	  public List<Double> getAvgTimeOfEachQuestion(int testid){
		  List <Double> avgTimeList=new ArrayList <Double>();
		  String getavgtime="SELECT avg(TimeUsed),QuestionID FROM Answer WHERE TestID=? GROUP BY QuestionID";
			Connection conn=DBConn.getConnection();
			try
			{
				PreparedStatement ps=conn.prepareStatement(getavgtime);
				ps.setInt(1, testid);
				ResultSet rs=ps.executeQuery();
				while(rs.next())
				{
					avgTimeList.add(rs.getDouble(1));
				  // System.out.println(rs.next());
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			//System.out.println("testid.length "+testids.size());
			return avgTimeList;
	    }
	  public int GetPaperidByTest(int testid)
	  {
		  
		  String getpaperidbytest="SELECT PaperID FROM Test WHERE TestID=?";
			Connection conn=DBConn.getConnection();
			int paperid;
			try
			{
				PreparedStatement ps=conn.prepareStatement(getpaperidbytest);
				ps.setInt(1, testid);
				ResultSet rs=ps.executeQuery();
				while(rs.next())
				{
					paperid=rs.getInt(1);
				  // System.out.println(rs.next());
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			//System.out.println("testid.length "+testids.size());
			return paperid;
	  }
   public static void main(String args[]) throws UnsupportedEncodingException, IOException{
	   String concepts[]={"��������","���ֺ�������","���ݺ�������","1NF","2NF","3NF","BCNF","��������","��","������","��������"};
	   int question[][]=new int[56][11];
	   //String concepts[]={"ʶ��","����"};
	   String writes=null;
	   String filename=null;
	   Map<String,Integer>conceptsorder=new HashMap<String,Integer>();
	   for(int i=0;i<concepts.length;i++)
	   {
		   conceptsorder.put(concepts[i], i);
	   }
	   analyzeknowledge dao=new analyzeknowledge();
	   
	   int testid;
	   List <Double> avgTimeList=dao.getAvgTimeOfEachQuestion(testid);
	   
	   float[] avgLookbacktimes=dao.getAvgLookbacktimeOfEachQuestion(testid); //ÿ�����ƽ���ؿ�����
	   
	   int order=0;
	  
	   String resultsss="";
	   String resultssid="";
	   
	   for(int j=0;j<testresults.size();j++)
	   {
		    resultsss="";
			int paperid=dao.GetPaperidByTest(testresults.get(j).gettestid());
			QuestionGetByPaper questionDaoImpl=new QuestionGetByPaper();
			
			PaperModel DBfindPaper_result = questionDaoImpl.DBfindPaper(paperid);
			
			if (DBfindPaper_result == null){
				System.out.println("���Ϊ��");
				return;
			}
			String questionListStr = DBfindPaper_result.getQuestions();
			System.out.println(questionListStr);

			
			String[] questionList = questionListStr.split("@@");//�������ַ���

			if(questionList.length<2){
				System.out.println("���ⲻ��");
				return;
			}
			ArrayList<QuestionModel> qlist = questionDaoImpl.DBfindQuestions(questionList);
			
			List<QuestionModel>collectlist=questionDaoImpl.getMarkedQuestions(testresults.get(j).getStudentid(), 1);
			 
			
			String[] answers=testresults.get(j).getAnswers().split("@@");
			String[] timeused=testresults.get(j).getTime_used().split(",");
			String[] lookbacktimes=testresults.get(j).getLook_back_times().split(",");
			String[] answertrace=testresults.get(j).getAnswer_trace().split(",");
			String[] judgetime=new String[timeused.length];
			String[] judgelookback=new String[lookbacktimes.length];
			int[] countanswer=new int[56];
			for(int i=0;i<timeused.length;i++)
			{
				judgetime[i]=judgetime(timeused[i],avgTimeList.get(i));
				judgelookback[i]=judgelookback(lookbacktimes[i],avgLookbacktimes[i]);
			}
			
			String[] finalresults=new String[qlist.size()];
			int[] judgecollect=new int[qlist.size()];
			ArrayList <QuestionModel> wrongqlist=new ArrayList<QuestionModel>();
			ArrayList <QuestionModel> rightqlist=new ArrayList<QuestionModel>();
//			for(int i=0;i<qlist.size();i++)
//			{
//				System.out.println(qlist.get(i).toString());
//			}
			
			for(int i=0;i<qlist.size();i++)
			{
				String id=qlist.get(i).getQuestionID()+"";
				if(collectlist.toString().indexOf(id)>=0)
				{
					judgecollect[i]=1;
				}
				else
				{
					judgecollect[i]=0;
				}
				String correct = qlist.get(i).getAnswer().trim();
				String stuAnswer = answers[i].trim();
				//�����������и�����Ҫ���������ʣ���׼�������ÿ��������һ���ո������������������ô���һ���ո��������ʱӦ���ȶ�����������д�����ȥ������Ŀո�
				stuAnswer=stuAnswer.replaceAll("\\s+"," ");//ֻȥ���м����ո� \\s+��ʾ����ո�
				//countanswer[i]=count(stuAnswer,correct);
				System.out.println(count(stuAnswer,correct));
				if(correct.equalsIgnoreCase(stuAnswer))
				{
					finalresults[i]="1";
					rightqlist.add(qlist.get(i));
					if(count(stuAnswer,correct)!=0)
						countanswer[i]=3;   //��û�г���
					else
						countanswer[i]=1;   //����˵��Ǵ𰸳����˺ܶ��
				}else{//���������ӵ������б���ȥ
					finalresults[i]="0";
					wrongqlist.add(qlist.get(i));
					if(count(stuAnswer,correct)!=0)
						countanswer[i]=3;   //��û�г���
					else
						countanswer[i]=2;   //����˵��Ǵ𰸳����˺ܶ��
				}
				
			}
			
			 FileOutputStream o2= new FileOutputStream("results.txt");
			for(int i=0;i<qlist.size();i++)
			{
				String s=qlist.get(i).getKnowledgepoint();
				
				s.replaceAll("\\?", " "); 
				s=s.trim();
				System.out.println(qlist.get(i).getKnowledgepoint());
				String path="/"+testresults.get(j).getStudentid()+"/";
				  File file=new File(path);
			        if(!file.exists()){
			        	file.mkdirs();
			        }
				filename=path+s+".txt";
				FileOutputStream o= new FileOutputStream(filename,true);
//				writes=qlist.get(i).getLevel()+","+judgetime[i]+","+judgelookback[i]+","+countanswer[i]+","+judgecollect[i]+","+finalresults[i]+"\r\n";
				writes=judgetime[i]+","+judgelookback[i]+","+countanswer[i]+","+judgecollect[i]+","+finalresults[i]+"\r\n";
				o.write(writes.getBytes("GBK"));
				o2.write(writes.getBytes("GBK"));
				
			} 
			  
	   }
	   HashMap<String,ArrayList<QuestionModel>> rqlist=new finaltest().Trainingmodel(testid);
	   
   }
   public static String judgetime(String p_time,Double avgtime){
	   if(Double.parseDouble(p_time)>avgtime)
		   return "0";
	   else
		   return "1";
   }
   public static String judgelookback(String p_lookback,float avglookback){
	   if(Integer.parseInt(p_lookback)>(int)avglookback)
		   return "0";
	   else
		   return "1";
   }
   public static int count(String answertrace,String rightanswer)
   {
	   int count=0;  
       //���������ÿ��Ԫ��    
       for(int i=0;i<=answertrace.length()-1;i++) {  
    	   if((i+rightanswer.length())<=answertrace.length())
           {
    		   String getstr=answertrace.substring(i,i+1);
    		   //System.out.println(getstr.equals(rightanswer));
                if(getstr.equals(rightanswer)){  
                    count++;  
               }  
           }
    	   else
    		   break;
       }  
       return count;
   }
}