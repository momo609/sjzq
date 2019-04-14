package com.sjzg.question;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//import net.sf.json.JSONArray;
//import net.sf.json.JSONException;
//import net.sf.json.JSONObject;



import java.util.Map.Entry;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Picture;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.mysql.jdbc.Statement;
import com.sjzg.database.DBConn;
import com.sjzg.paper.PaperCreate;
import com.sjzg.paper.PaperModel;

public class QuestionAnalysisByword {
	private ArrayList<File> uploadImageList=new ArrayList<File>();
    public static final int SINGLE_CHOICE=1;
    public static final int MULTI_CHOICE=4;
    public static final int JUDGE=2;
    public static final int FILL_BLANK=3;
//    private  String[] cIndex={"A.","B.","C.","D.","E.","F.","G.","H.","I.","J.","K.",
//            "L.","M.","N.","O.","P.","Q.","R.","S.","T.","U.","V.","W.","X.","Y.","Z."};
    private  String[] cIndex={"A.","B.","C.","D","N.","Y."};
	public String insertquestion(Map<Integer,Object> map)
	{
		String sql="INSERT INTO Question(Type,Content,Choices,Answer,Answerkey,Image,Tag,Difficulty,Share,UserID,CreateAt,UpdateAt) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String questionidList="";
		String errorString = "数据库操作异常";
		Date nowDate = new Date();
		java.sql.Timestamp nowTimestamp = new java.sql.Timestamp(nowDate.getTime());
		List<Entry<Integer, Object>> list = new ArrayList<Entry<Integer,Object>>(map.entrySet());
	    for (Entry<Integer, Object> e: list) {  
          
			try {
				QuestionModel q=(QuestionModel) e.getValue();
				 conn=DBConn.getConnection();
				 ps=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

				ps.setInt(1, q.getType());
				ps.setString(2, q.getContent());
				ps.setString(3, q.getChoices());
				ps.setString(4, q.getAnswer());
				ps.setString(5, q.getAnswerkey());
				ps.setString(6, q.getImage());
				ps.setString(7, q.getTag());
				ps.setFloat(8, q.getDifficulty());
				ps.setInt(9, q.getShare());
				ps.setString(10, q.getUserID());
				ps.setTimestamp(11, nowTimestamp);
				ps.setTimestamp(12, nowTimestamp);
				ps.executeUpdate();
			    rs = ps.getGeneratedKeys(); //获取结果   
				if (rs.next()) {
				    q.setQuestionID(rs.getInt(1));//取得ID
				    questionidList=questionidList+q.getQuestionID()+"@@";
				}
				
			} catch (SQLException h) {
				h.printStackTrace();
				errorString+=h.getLocalizedMessage();
			}finally {
			    if (rs != null) {
			        try {
			            rs.close();
			        } catch (SQLException h) { /* ignored */}
			    }
			    if (ps != null) {
			        try {
			            ps.close();
			        } catch (SQLException h) { /* ignored */}
			    }
			    if (conn != null) {
			        try {
			            conn.close();
			        } catch (SQLException h) { /* ignored */}
			    }
			}
	            
	     }  
	     return questionidList;
	}
    public static void main(String args[]) throws Exception{
    	QuestionAnalysisByword aw = new QuestionAnalysisByword();
    	String s = aw.importFromWord(new File("C:/Users/admin/Desktop/大四重启动/一些文档/题库.doc"));
//    	System.out.println("aw:"+s);
    	Map<Integer,Object> map = aw.string2QuestionMap(s);
    	System.out.println("mao:"+map.toString());
    	String questionidlist=aw.insertquestion(map);
		PaperModel paperModel = new PaperModel();
		paperModel.setUserID("1234");
		paperModel.setTitle("规范化第一次测试");
		paperModel.setDescription("第一次测试了解初始掌握程度");
		paperModel.setQuestions(questionidlist);
		PaperCreate papercreate=new PaperCreate();
		papercreate.DBcreatePaper(paperModel);
    }
	public String importFromWord(File file) throws Exception{
        String text="";
        //读入word到document对象
        HWPFDocument document=null;
        FileInputStream fis = new FileInputStream(file);
        document = new HWPFDocument(fis);
        //获取word中的图片表和连续相同格式的段
        PicturesTable pTable = document.getPicturesTable();
        int numCharacterRuns = document.getRange().numCharacterRuns();

        //遍历所有段
        for (int i = 0; i < numCharacterRuns; i++) {
            CharacterRun characterRun = document.getRange().getCharacterRun(i);
            //如果该段为图片，则将图片缓存到存储中，并加入到图片上传列表
            //等待上传，并用★标记图片所在位置
            if (pTable.hasPicture(characterRun)) {
                Picture pic = pTable.extractPicture(characterRun, false);
                String fileName = pic.suggestFullFileName();
                OutputStream out;
                try {
                    String BOUNDARY = UUID.randomUUID().toString();
                    File f=new File(BOUNDARY+fileName);
                    out = new FileOutputStream(f);
                    pic.writeImageContent(out);
                    uploadImageList.add(f);
                } catch (Exception e)
                {
                            e.printStackTrace();
                }
                text = text + "★";
            }else{
                //不是图片则把文字附加到末尾
                text = text + characterRun.text();
            }
        }
        text=text.trim();
        return text.replace("\r", "").replace("\n","");
	}
	
	public  Map<Integer,Object> string2QuestionMap(String arg) throws Exception
    {
        Map<Integer,Object> map=new HashMap<Integer,Object>();
//        System.out.println("绘声绘色："+arg);
        String[] t=arg.split("@@@");
        int i=1;
        for(String s:t)
        {
            int type=getQuestionType(s);
            if(type==SINGLE_CHOICE)//单选
                addSCQ(s,map,i++);
//            else if(type==JUDGE)
//                addJQ(s,map,i++);
//            else if(type==FILL_BLANK)
//                addFBQ(s,map,i++);
//            else if(type==MULTI_CHOICE)//判断
//                addMCQ(s,map,i++);

        }
        return map;
    }
	
	 private static int getQuestionType(String q)
	    {
	        int type=0;
	        String t=q.substring(1,q.indexOf("]"));
	        if(t.equals("单选"))
	            type=SINGLE_CHOICE;
	        else if(t.equals("判断"))
	            type=JUDGE;
	        else if(t.equals("填空"))
	            type=FILL_BLANK;
	        else if(t.equals("多选"))
	            type=MULTI_CHOICE;

	        return type;
	    }
	 
//	 private void addMCQ(String s, Map<Integer, Object> list, int order) {
//	        // TODO Auto-generated method stub
//	        QuestionModel mcq=new QuestionModel();
//	        mcq.setContent(s.substring(4,s.indexOf("A.")));
//	        System.out.println(s.lastIndexOf("答案:"));
//	        String choices=s.substring(s.indexOf("A."),s.lastIndexOf("答案:"));
//	        System.out.println("aaa:"+choices);
//	        StringBuilder sbChoices=new StringBuilder();
//	        int i=0;
//	        for(;i<cIndex.length;i++)
//	        {
//	            if(choices.indexOf(cIndex[i+1])==-1)
//	            break;
//	        	if(i==2){
//	        		sbChoices.append(choices.substring(choices.indexOf(cIndex[i])+2,choices.indexOf(cIndex[i+1]+".")));
//	        	}else{
//	        		sbChoices.append(choices.substring(choices.indexOf(cIndex[i])+2,choices.indexOf(cIndex[i+1])));
//	        	}
//	            sbChoices.append("@@");
////	            System.out.println("bbb:"+cIndex[i]);
//	        }
//	        System.out.println("sss:"+choices);
////	        sbChoices.append(choices.split(cIndex[i])[1]);
//	        sbChoices.append(choices.substring(choices.indexOf(cIndex[i]+".")+2,choices.length()));
////	        System.out.println("sss:"+cIndex[i]);
//	        
//	        mcq.setChoices(sbChoices.toString());
//	        String sAnswer=s.split("答案:")[1].split("分数:")[0].trim();
//	        StringBuilder sbAnwser=new StringBuilder();
//	        for(i=0;i<sAnswer.length();i++)
//	        {
//	            sbAnwser.append(sAnswer.charAt(i));
//	        }
//	        mcq.setAnswer(sbAnwser.toString());
////	        mcq.setPoint(Integer.parseInt(s.split("分数:")[1].split("解析:")[0]));
//	        mcq.setAnswerkey(s.substring(s.indexOf("解析:")+3,s.indexOf("知识点:")));
//	        mcq.setTag(s.substring(s.indexOf("知识点:") + 4, s.indexOf("能力水平:")));
////	        mcq.setLevel(Integer.parseInt(s.split("能力水平:")[1].trim()));
//	        mcq.setQ_order(order);
//	        mcq.setType(4);
//	        list.put(order, mcq);
//	    }
//	    private static void addFBQ(String s,Map<Integer,Object> list,int order) {
//	        FillBlankQuestion fbq=new FillBlankQuestion();
//	        fbq.setStem(s.substring(4,s.indexOf("填空数:")));
//	        int blankCount=Integer.parseInt(s.substring(s.indexOf("填空数:")+4,s.indexOf("答案1")));
//	        StringBuilder sbAnswer=new StringBuilder();
//	        int i=1;
//	        for(;i<blankCount;i++)
//	        {
//	        	if(s.substring(s.indexOf("答案" + i) + 4, s.indexOf("答案" + (i + 1))).trim().indexOf("/")!=-1){
//	        		String[] ss = s.substring(s.indexOf("答案" + i) + 4, s.indexOf("答案" + (i + 1))).trim().split("/");
//	        		 StringBuilder last=new StringBuilder();
//	        		for (int j = 0; j < ss.length-1; j++) {
//						last.append(ss[j].replaceAll("\\s+"," ")).append( "**");
//					}
//	        		last.append(ss[ss.length-1]);
//	        		sbAnswer.append(last);
//	        	}
//	        	else{
//	        		sbAnswer.append(s.substring(s.indexOf("答案" + i) + 4, s.indexOf("答案" + (i + 1))).trim().replaceAll("\\s+"," "));
//	        	}
//	            sbAnswer.append("##");
//	        }
//	        if(s.substring(s.indexOf("答案"+i)+4,s.indexOf("分数")).trim().indexOf("/")!=-1){
//        		String[] ss = s.substring(s.indexOf("答案"+i)+4,s.indexOf("分数")).trim().split("/");
//        		 StringBuilder last=new StringBuilder();
//        		for (int j = 0; j < ss.length-1; j++) {
//        			last.append(ss[j].replaceAll("\\s+"," ")).append( "**");
//				}
//        		last.append(ss[ss.length-1]);
//        		sbAnswer.append(last);
//        	}
//        	else{
//        		sbAnswer.append(s.substring(s.indexOf("答案"+i)+4,s.indexOf("分数")).trim());
//        	}
//	        fbq.setAnswer(sbAnswer.toString());
//	        fbq.setPoint(Integer.parseInt(s.substring(s.indexOf("分数:")+3, s.indexOf("解析:"))));
//	        fbq.setAnswerkey(s.substring(s.indexOf("解析:")+3,s.indexOf("知识点:")));
//	        fbq.setTag(s.substring(s.indexOf("知识点:")+4,s.indexOf("能力水平:")));
//	        fbq.setLevel(Integer.parseInt(s.split("能力水平:")[1].trim()));
//	        fbq.setQ_order(order);
//	        fbq.setType(3);
//	        list.put(order,fbq);
//	    }
//	    private static void addJQ(String s,Map<Integer,Object> list,int order) {
//	        JudgeQuestion jq=new JudgeQuestion();
//	        jq.setStem(s.substring(4,s.indexOf("答案")));
//
//	        jq.setAnswer(s.substring(s.indexOf("答案") + 3, s.indexOf("分数")).trim());
//	        jq.setPoint(Integer.parseInt(s.substring(s.indexOf("分数:")+3, s.indexOf("解析:"))));
//	        jq.setAnswerkey(s.substring(s.indexOf("解析:")+3,s.indexOf("知识点:")));
//	        jq.setTag(s.substring(s.indexOf("知识点:")+4,s.indexOf("能力水平:")));
//	        jq.setLevel(Integer.parseInt(s.split("能力水平:")[1].trim()));
//	        jq.setQ_order(order);
//	        jq.setType(2);
//	        list.put(order,jq);
//
//	    }
	    private  void addSCQ(String s,Map<Integer,Object> list,int order) {
	    	QuestionModel sc=new QuestionModel();
	    	sc.setContent(s.substring(4,s.indexOf("A.")));
	        String choices=s.substring(s.indexOf("A."),s.lastIndexOf("答案:"));
//	        System.out.println("choices："+choices);
	        StringBuilder sbChoices=new StringBuilder();
	        int i=0;
	        for(;i<cIndex.length;i++)
	        {
	            if(choices.indexOf(cIndex[i+1])==-1)
	                break;
	            sbChoices.append(choices.substring(choices.indexOf(cIndex[i])+2,choices.indexOf(cIndex[i+1])));
	            sbChoices.append("@@");
	        }
	        
	        sbChoices.append(choices.split(cIndex[i]+"\\.")[1]);
//	        System.out.println(cIndex[i]+"||"+choices.split(cIndex[i])[1]);
	        sc.setChoices(sbChoices.toString());
	        System.out.println(sbChoices.toString());
	        sc.setAnswer(s.split("答案:")[1].split("分数:")[0].trim());
//	        sc.setPoint(Integer.parseInt(s.split("分数:")[1].split("解析:")[0]));
	        sc.setAnswerkey(s.split("解析:")[1].split("知识点:")[0]);
//	        System.out.println(0);
	        sc.setTag(s.split("知识点:")[1].split("能力水平:")[0]);
//	        System.out.println(1);
//	        sc.setLevel(Integer.parseInt(s.split("能力水平:")[1].trim()));
//	        sc.setLevel(Integer.valueOf(s.split("能力水平:")[1]).intValue());
//	        System.out.println("Dddd:");
//	        sc.setQ_order(order);
	        sc.setType(1);
	        list.put(order, sc);

	    }
	    
	    
//	    public JSONObject qMap2JSON(Map<Integer,Object> questionMap)
//	    {
//
//	        Map<String, List<?>> map=new HashMap<String, List<?>>();
//	        List<SingleChoiceQuestion> scqList=new ArrayList<SingleChoiceQuestion>();
//	        List<FillBlankQuestion> fbqList=new ArrayList<FillBlankQuestion>();
//	        List<JudgeQuestion> jqList=new ArrayList<JudgeQuestion>();
//	        List<MultipleChoiceQuestion> mcqList=new ArrayList<MultipleChoiceQuestion>();
//	        for(int i=1;i<=questionMap.size();i++)
//	        {
//	            Object o=questionMap.get(i);
//	            if(o instanceof SingleChoiceQuestion)
//	            {
//	                scqList.add((SingleChoiceQuestion)o);
//	            }else if(o instanceof  MultipleChoiceQuestion)
//	            {
//	                mcqList.add((MultipleChoiceQuestion)o);
//	            }
//	            else if(o instanceof  FillBlankQuestion)
//	            {
//	                fbqList.add((FillBlankQuestion)o);
//	            }
//	            else if(o instanceof  JudgeQuestion)
//	            {
//	                jqList.add((JudgeQuestion)o);
//	            }
//	        }
//	        Gson gson=new Gson();
//	        JSONArray scqJa= null;JSONArray mcqJa= null;
//	        JSONArray jqJa= null;JSONArray fbqJa= null;
//	        JSONObject jo=new JSONObject();
//	        try {
//	            scqJa = new JSONArray(gson.toJson(scqList,new TypeToken<List<SingleChoiceQuestion>>(){}.getType()));
//	            mcqJa = new JSONArray(gson.toJson(mcqList,new TypeToken<List<SingleChoiceQuestion>>(){}.getType()));
//	            jqJa = new JSONArray(gson.toJson(jqList,new TypeToken<List<SingleChoiceQuestion>>(){}.getType()));
//	            fbqJa = new JSONArray(gson.toJson(fbqList,new TypeToken<List<SingleChoiceQuestion>>(){}.getType()));
//
//	            jo.put("SingleChoiceQuestion",scqJa);
//	            jo.put("MultipleChoiceQuestion",mcqJa);
//	            jo.put("FillBlankQuestion",fbqJa);
//	            jo.put("JudgeQuestion",jqJa);
//	        } catch (Exception e) {
//	        	// TODO Auto-generated catch block
//	        	e.printStackTrace();
//	        }
//
//	        return jo ;
//	    }
}
