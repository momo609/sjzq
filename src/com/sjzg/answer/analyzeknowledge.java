package com.sjzg.answer;

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
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sjzg.database.DBConn;
import com.sjzg.paper.PaperModel;
import com.sjzg.question.QuestionGetByPaper;
import com.sjzg.question.QuestionModel;
import com.sjzg.recommend.finaltest;
import com.sjzg.tool.KnowledgeGraph;



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
			int paperid = 0;
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
		public float[] getAvgLookbacktimeOfEachQuestion(ArrayList<ProcessedAnswerModel>testresult)
		{
			float avglookbacktime[] = new float[56];
			for (int h=0;h<testresult.size();h++)
			{
//				System.out.println(s);
		        String s=testresult.get(h).getLookBackTime();
				String eachbacktime[]=s.split(",");
				//System.out.println(s);
				for(int i=0;i<eachbacktime.length;i++)
				{
					avglookbacktime[i]=avglookbacktime[i]+(float)Integer.parseInt(eachbacktime[i]);
				}
			}
			for(int i=0;i<avglookbacktime.length;i++)
			{
				float a=avglookbacktime[i];
				//System.out.println(avglookbacktime[i]);
				avglookbacktime[i]=a/testresult.size();
			}
			return avglookbacktime;
			
		}
  public  HashMap<String,ArrayList<QuestionModel>> analyze(int testid) throws IOException
  {
//	  String concepts[]={"函数依赖","部分函数依赖","传递函数依赖","1NF","2NF","3NF","BCNF","决定因素","码","主属性","非主属性"};
	   int question[][]=new int[56][11];
	   //String concepts[]={"识记","理解"};
	   String writes=null;
	   String filename=null;
	   Map<String,Integer>conceptsorder=new HashMap<String,Integer>();
	   for(int i=0;i<KnowledgeGraph.concepts.length;i++)
	   {
		   conceptsorder.put(KnowledgeGraph.concepts[i], i);
	   }
	   analyzeknowledge dao=new analyzeknowledge();
	   ArrayList<ProcessedAnswerModel>testresults=new ProcessingData().processingdata(testid);
	   List <Double> avgTimeList=dao.getAvgTimeOfEachQuestion(testid);
	   
	   float[] avgLookbacktimes=dao.getAvgLookbacktimeOfEachQuestion(testresults); //每道题的平均回看次数
	   
	   int order=0;
	  
	   String resultsss="";
	   String resultssid="";
	   
	   for(int j=0;j<testresults.size();j++)
	   {
		    resultsss="";
			int paperid=dao.GetPaperidByTest(testresults.get(j).getTestID());
			QuestionGetByPaper questionDaoImpl=new QuestionGetByPaper();
			
			PaperModel DBfindPaper_result = questionDaoImpl.DBfindPaper(paperid);
			
			if (DBfindPaper_result == null){
				System.out.println("题库为空");
				return null;
			}
			String questionListStr = DBfindPaper_result.getQuestions();
			//System.out.println(questionListStr);

			
			String[] questionList = questionListStr.split("@@");//丢弃空字符串

			if(questionList.length<2){
				System.out.println("试题不足");
				return null;
			}
			ArrayList<QuestionModel> qlist = questionDaoImpl.DBfindQuestions(questionList);
			
	//		List<QuestionModel>collectlist=questionDaoImpl.getMarkedQuestions(testresults.get(j).getUserID(), 1);
			 
			
			String[] answers=testresults.get(j).getUserAnswer().split("@@");
			String[] timeused=testresults.get(j).getTimeUsed().split(",");
			String[] lookbacktimes=testresults.get(j).getLookBackTime().split(",");
			String[] answertrace=testresults.get(j).getTrack().split(",");
			String[] judgetime=new String[timeused.length];
			String[] judgelookback=new String[lookbacktimes.length];
			int[] countanswer=new int[56];
			for(int i=0;i<timeused.length;i++)
			{
				judgetime[i]=judgetime(timeused[i],avgTimeList.get(i));
				judgelookback[i]=judgelookback(lookbacktimes[i],avgLookbacktimes[i]);
			}
			
			String[] finalresults=new String[qlist.size()];
			String[] judgecollect=testresults.get(j).getCollectionlist().split(",");
			ArrayList <QuestionModel> wrongqlist=new ArrayList<QuestionModel>();
			ArrayList <QuestionModel> rightqlist=new ArrayList<QuestionModel>();
//			for(int i=0;i<qlist.size();i++)
//			{
//				System.out.println(qlist.get(i).toString());
//			}
			
			for(int i=0;i<qlist.size();i++)
			{
				String id=qlist.get(i).getQuestionID()+"";
				String correct = qlist.get(i).getAnswer().trim();
				String stuAnswer = answers[i].trim();
				//如果填空题中有个空需要填两个单词，标准情况下是每个单词以一个空格隔开，但是如果考生用大于一个空格隔开，此时应该先对这种情况进行处理，去掉多余的空格
				stuAnswer=stuAnswer.replaceAll("\\s+"," ");//只去掉中间多余空格 \\s+表示多个空格
				//countanswer[i]=count(stuAnswer,correct);
				//System.out.println(count(stuAnswer,correct));
				if(correct.equalsIgnoreCase(stuAnswer))
				{
					finalresults[i]="1";
					rightqlist.add(qlist.get(i));
					if(count(stuAnswer,correct)!=0)
						countanswer[i]=3;   //答案没有出现
					else
						countanswer[i]=1;   //答对了但是答案出现了很多次
				}else{//不对则添加到错题列表中去
					finalresults[i]="0";
					wrongqlist.add(qlist.get(i));
					if(count(stuAnswer,correct)!=0)
						countanswer[i]=3;   //答案没有出现
					else
						countanswer[i]=2;   //答错了但是答案出现了很多次
				}
				
			}
			
			 FileOutputStream o2;
			 o2 = new FileOutputStream("results.txt",true);
			
			 System.out.println("判断"+qlist.size());
			 File directory = new File("");
			for(int i=0;i<qlist.size();i++)
			{
				String s=qlist.get(i).getKnowledgepoint();
				
				s.replaceAll("\\?", " "); 
				s=s.trim();
				System.out.println(qlist.get(i).getKnowledgepoint());
				String path=directory.getCanonicalPath()+"/"+testid+"/"+testresults.get(j).getUserID()+"/";
				filename=path+s+".txt";
				  File file=new File(path);
			        if(!file.exists()){
			        	file.mkdirs();
			        }
				//filename=path+s+".txt";
				System.out.println("文件名 "+filename);
				FileOutputStream o= new FileOutputStream(filename,true);
//				writes=qlist.get(i).getLevel()+","+judgetime[i]+","+judgelookback[i]+","+countanswer[i]+","+judgecollect[i]+","+finalresults[i]+"\r\n";
				writes=judgetime[i]+","+judgelookback[i]+","+countanswer[i]+","+judgecollect[i]+","+finalresults[i]+"\r\n";
				o.write(writes.getBytes("GBK"));
				o2.write(writes.getBytes("GBK"));
				o.close();o2.close();
			} 
			  
	   }
	   
	   HashMap<String,ArrayList<QuestionModel>> rqlist=new finaltest().Trainingmodel(testid);
	   List<Entry<String, ArrayList<QuestionModel>>> list = new ArrayList<Entry<String,ArrayList<QuestionModel>>>(rqlist.entrySet());
 	   for (Entry<String, ArrayList<QuestionModel>> e: list) {  
	    	    System.out.println(e.getValue().get(0).getContent());
	     }  
 	  return rqlist;
  }
   public static void main(String args[]) throws UnsupportedEncodingException, IOException{
	   String concepts[]={"函数依赖","部分函数依赖","传递函数依赖","1NF","2NF","3NF","BCNF","决定因素","码","主属性","非主属性"};
	   int question[][]=new int[56][11];
	   //String concepts[]={"识记","理解"};
	   String writes=null;
	   String filename=null;
	   Map<String,Integer>conceptsorder=new HashMap<String,Integer>();
	   for(int i=0;i<concepts.length;i++)
	   {
		   conceptsorder.put(concepts[i], i);
	   }
	   analyzeknowledge dao=new analyzeknowledge();
	   int testid=19;
	   ArrayList<ProcessedAnswerModel>testresults=new ProcessingData().processingdata(testid);
	   List <Double> avgTimeList=dao.getAvgTimeOfEachQuestion(testid);
	   
	   float[] avgLookbacktimes=dao.getAvgLookbacktimeOfEachQuestion(testresults); //每道题的平均回看次数
	   
	   int order=0;
	  
	   String resultsss="";
	   String resultssid="";
	   
	   for(int j=0;j<testresults.size();j++)
	   {
		    resultsss="";
			int paperid=dao.GetPaperidByTest(testresults.get(j).getTestID());
			QuestionGetByPaper questionDaoImpl=new QuestionGetByPaper();
			
			PaperModel DBfindPaper_result = questionDaoImpl.DBfindPaper(paperid);
			
			if (DBfindPaper_result == null){
				System.out.println("题库为空");
				return;
			}
			String questionListStr = DBfindPaper_result.getQuestions();
			//System.out.println(questionListStr);

			
			String[] questionList = questionListStr.split("@@");//丢弃空字符串

			if(questionList.length<2){
				System.out.println("试题不足");
				return;
			}
			ArrayList<QuestionModel> qlist = questionDaoImpl.DBfindQuestions(questionList);
			
	//		List<QuestionModel>collectlist=questionDaoImpl.getMarkedQuestions(testresults.get(j).getUserID(), 1);
			 
			
			String[] answers=testresults.get(j).getUserAnswer().split("@@");
			String[] timeused=testresults.get(j).getTimeUsed().split(",");
			String[] lookbacktimes=testresults.get(j).getLookBackTime().split(",");
			String[] answertrace=testresults.get(j).getTrack().split(",");
			String[] judgetime=new String[timeused.length];
			String[] judgelookback=new String[lookbacktimes.length];
			int[] countanswer=new int[56];
			for(int i=0;i<timeused.length;i++)
			{
				judgetime[i]=judgetime(timeused[i],avgTimeList.get(i));
				judgelookback[i]=judgelookback(lookbacktimes[i],avgLookbacktimes[i]);
			}
			
			String[] finalresults=new String[qlist.size()];
			String[] judgecollect=testresults.get(j).getCollectionlist().split(",");
			ArrayList <QuestionModel> wrongqlist=new ArrayList<QuestionModel>();
			ArrayList <QuestionModel> rightqlist=new ArrayList<QuestionModel>();
//			for(int i=0;i<qlist.size();i++)
//			{
//				System.out.println(qlist.get(i).toString());
//			}
			
			for(int i=0;i<qlist.size();i++)
			{
				String id=qlist.get(i).getQuestionID()+"";
				String correct = qlist.get(i).getAnswer().trim();
				String stuAnswer = answers[i].trim();
				//如果填空题中有个空需要填两个单词，标准情况下是每个单词以一个空格隔开，但是如果考生用大于一个空格隔开，此时应该先对这种情况进行处理，去掉多余的空格
				stuAnswer=stuAnswer.replaceAll("\\s+"," ");//只去掉中间多余空格 \\s+表示多个空格
				//countanswer[i]=count(stuAnswer,correct);
				//System.out.println(count(stuAnswer,correct));
				if(correct.equalsIgnoreCase(stuAnswer))
				{
					finalresults[i]="1";
					rightqlist.add(qlist.get(i));
					if(count(stuAnswer,correct)!=0)
						countanswer[i]=3;   //答案没有出现
					else
						countanswer[i]=1;   //答对了但是答案出现了很多次
				}else{//不对则添加到错题列表中去
					finalresults[i]="0";
					wrongqlist.add(qlist.get(i));
					if(count(stuAnswer,correct)!=0)
						countanswer[i]=3;   //答案没有出现
					else
						countanswer[i]=2;   //答错了但是答案出现了很多次
				}
				
			}
			
			 FileOutputStream o2= new FileOutputStream("results.txt",true);
			 System.out.println("判断"+qlist.size());
			 File directory = new File("");
			for(int i=0;i<qlist.size();i++)
			{
				String s=qlist.get(i).getKnowledgepoint();
				
				s.replaceAll("\\?", " "); 
				s=s.trim();
				System.out.println(qlist.get(i).getKnowledgepoint());
				String path=directory.getCanonicalPath()+"/"+testid+"/"+testresults.get(j).getUserID()+"/";
				filename=path+s+".txt";
				  File file=new File(path);
			        if(!file.exists()){
			        	file.mkdirs();
			        }
				//filename=path+s+".txt";
				System.out.println("文件名 "+filename);
				FileOutputStream o= new FileOutputStream(filename,true);
//				writes=qlist.get(i).getLevel()+","+judgetime[i]+","+judgelookback[i]+","+countanswer[i]+","+judgecollect[i]+","+finalresults[i]+"\r\n";
				writes=judgetime[i]+","+judgelookback[i]+","+countanswer[i]+","+judgecollect[i]+","+finalresults[i]+"\r\n";
				o.write(writes.getBytes("GBK"));
				o2.write(writes.getBytes("GBK"));
				
			} 
			  
	   }
	   HashMap<String,ArrayList<QuestionModel>> rqlist=new finaltest().Trainingmodel(testid);
	   List<Entry<String, ArrayList<QuestionModel>>> list = new ArrayList<Entry<String,ArrayList<QuestionModel>>>(rqlist.entrySet());
  	   for (Entry<String, ArrayList<QuestionModel>> e: list) {  
	    	    System.out.println(e.getValue().get(0).getContent());
	     }  

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
       //遍历数组的每个元素    
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
