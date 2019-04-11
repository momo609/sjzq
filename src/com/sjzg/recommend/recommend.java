package com.sjzg.recommend;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javassist.bytecode.Descriptor.Iterator;

import com.sjzg.answer.analyzeknowledge;
import com.sjzg.paper.PaperModel;
import com.sjzg.question.QuestionGetByConcept;
import com.sjzg.question.QuestionGetByPaper;
import com.sjzg.question.QuestionModel;


public class recommend {
	public HashMap<String,ArrayList<QuestionModel>>  Recommend(HashMap<String,ArrayList<String>> allresult,int testid)
	{
		HashMap<String,Integer>conceptid=new HashMap<String,Integer>();
		conceptid.put("kg", 1);
		conceptid.put("kg2", 2);
		analyzeknowledge dao=new analyzeknowledge();
		int paperid=dao.GetPaperidByTest(testid);
		QuestionGetByPaper questionDaoImpl=new QuestionGetByPaper();
		
		PaperModel DBfindPaper_result = questionDaoImpl.DBfindPaper(paperid);
		
		if (DBfindPaper_result == null){
			System.out.println("Ìâ¿âÎª¿Õ");
		}
		String questionListStr = DBfindPaper_result.getQuestions();
		System.out.println(questionListStr);

		
		String[] questionList = questionListStr.split("@@");//¶ªÆú¿Õ×Ö·û´®

		if(questionList.length<2){
			System.out.println("ÊÔÌâ²»×ã");
		}
		ArrayList<QuestionModel> qlist = questionDaoImpl.DBfindQuestions(questionList);
		HashMap<String,ArrayList<QuestionModel>> rqlist=new HashMap<String,ArrayList<QuestionModel>> ();
		java.util.Iterator<Entry<String, ArrayList<String>>> iter = allresult.entrySet().iterator();
		while (iter.hasNext()) {
		   Map.Entry entry = (Map.Entry) iter.next();
		   Object key = entry.getKey();
		   ArrayList<String> pkp=(ArrayList<String>) entry.getValue();
		   String temp_kp="";
		   for(int i=0;i<pkp.size()-1;i++)
		   {
			   temp_kp=temp_kp+conceptid.get(pkp.get(i))+",";
		   }
		   temp_kp=temp_kp+conceptid.get(pkp.get(pkp.size()-1));
		   System.out.println("pkp "+pkp+" temp_kp "+temp_kp);
		   ArrayList<QuestionModel> prlist=new QuestionGetByConcept().GetQuestionByConcept(temp_kp);
		   rqlist.put((String) entry.getKey(), prlist);
		}
		return rqlist;
	}

}
 