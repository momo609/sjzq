package com.sjzg.recommend;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.sjzg.answer.ProcessedAnswerModel;
import com.sjzg.answer.RankByScore;
import com.sjzg.database.DBConn;
import com.sjzg.question.QuestionGetByConcepts;
import com.sjzg.question.QuestionModel;
import com.sjzg.tool.KnowledgeGraph;
 
public class finaltest {

	static String results = null;
	static int[] correctcount=new int[11];
//	static double[] correctrate=new double[11];
	static Map<String,Integer>recommend=new HashMap<String,Integer>();
	static Map<String,Double>finalrecommend=new HashMap<String,Double>();
	static Map<String,Integer>recommends=new HashMap<String,Integer>();
	static Map<String,Double>finalrecommends=new HashMap<String,Double>();
	
	static Map<String,Double>c=new HashMap<String,Double>();
	static ArrayList<String>result=new ArrayList<String>();
	static ArrayList<Double>resultrate=new ArrayList<Double>();
	static double[] correctrate=new double[11];
	
	static String filenames[]=new String[12];
	
	static int conceptnumeber=2;
	public static void main(String args[]) throws IOException
	{
		HashMap<String,ArrayList<QuestionModel>> rr=Trainingmodel(12);
	}
	public static List<String> GetStudentidByTestid(int testid)
	{
		  String getpaperidbytest="SELECT UserID FROM Answer WHERE TestID=? group by QuestionID ;";
			Connection conn=DBConn.getConnection();
			ArrayList<String>studentid=new ArrayList<String>();
			try
			{
				PreparedStatement ps=conn.prepareStatement(getpaperidbytest);
				ps.setInt(1, testid);
				ResultSet rs=ps.executeQuery();
				while(rs.next())
				{
					studentid.add(rs.getString(1));
				  // System.out.println(rs.next());
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			//System.out.println("testid.length "+testids.size());
			return studentid;
	}
    public static HashMap<String,ArrayList<QuestionModel>> Trainingmodel(int testid) throws IOException {
    	Map<String,Double>conceptsrate=new HashMap<String,Double>();
        String[] attrNames = new String[] {"time", "lookbacktime","answertrace","collect"};
    	// String[] attrNames = new String[] {"outlook","temperature","humidity","play"};
        // ��ȡ������
        Map<Object, List<Sample>> samples = readSamples(attrNames);
       // System.out.println(samples.toString());
  	  	//���㽨ģʱ��
        long startTime1 = System.currentTimeMillis();    //��ȡ��ʼʱ��
        Object decisionTree = generateDecisionTree(samples, attrNames);
        long endTime1 = System.currentTimeMillis();    //��ȡ����ʱ��
        System.out.println(">>>>>������ģ�ͽ������");
        System.out.println("��ģʱ�䣺" + (endTime1 - startTime1) + "ms");
        // ���ɾ�����
        // ���������
        outputDecisionTree(decisionTree, 0, null);
        RankByScore rank=new RankByScore();
        ArrayList<ArrayList<ProcessedAnswerModel>>studentgroup=rank.groupStudent(testid);
        List<String>studentid=new ArrayList<String>();
        for(int i=0;i<studentgroup.get(0).size();i++)
        {
        	studentid.add(studentgroup.get(0).get(i).getUserID());
        }
        System.out.println("studentid=  "+studentid.toString());
        //List<String>studentid=GetStudentidByTestid(testid);
        HashMap<String,ArrayList<String>>allresult=new HashMap<String,ArrayList<String>>();
        HashMap<String,Map<String,Double>>allstudentmp=new HashMap<String,Map<String,Double>>();
        System.out.println("studentsize "+studentid.size());
        for(int h=0;h<studentid.size();h++)
        {
        	   //�����µ����ݼ�
             Testnewdata(attrNames,decisionTree,studentid.get(h),testid);
             for(int i=0;i<correctrate.length;i++)
             {
          	      System.out.println(correctrate[i]+" "+filenames[i]);
             }
       	     for(int i=0;i<conceptnumeber;i++)
         	 {
          	     recommends(i,correctrate);
        	 }
       	     for(int i=0;i<conceptnumeber;i++)
       	     {
       		     conceptsrate.put(KnowledgeGraph.concepts[i], correctrate[i]);
       	     }
       	     System.out.println("recommend "+recommend);
       	     List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(recommend.entrySet());            
             Collections.sort(list,new Comparator<Map.Entry<String, Integer>>() {  
                    //��������  
                  public int compare(Entry<String, Integer> o1, Entry<String,  Integer> o2) {  
                           return o2.getValue().compareTo(o1.getValue());  
                  }  
             });  
             String[] cc=new String[recommend.size()]; //֪ʶ������
             int i=0;
             for (Entry<String, Integer> e: list) {  
      	          System.out.print("11  "+e.getKey()+"="+e.getValue()+" ");
      	          cc[i]=e.getKey();
      	          i++;
      	          recommends.put(e.getKey(),e.getValue());
                  finalrecommend.put(e.getKey(),conceptsrate.get(e.getKey()));
            }  
            List<Entry<String, Double>> list2 = new ArrayList<Entry<String,Double>>(finalrecommend.entrySet());            
             Collections.sort(list2,new Comparator<Map.Entry<String, Double>>() {  
                    //��������  
                 public int compare(Entry<String, Double> o1, Entry<String,  Double> o2) {  
                     return o1.getValue().compareTo(o2.getValue());  
                }  
             });  
            System.out.println();
             for (Entry<String, Double> e: list2) {  
          	      finalrecommends.put(e.getKey(),e.getValue());
      	          System.out.print("22  "+e.getKey()+"="+e.getValue()+"  ");
                  
             } 
            System.out.println();
            if(cc.length==1)
            {
            	 result.add(cc[0]);
            }
            else
            {
            	 for(int z=0;z<cc.length-1;z++)
                 {
               	     int j=z+1;
               	   //System.out.println("55 "+(z==cc.length-2));
               	    if(recommends.get(cc[z])==recommends.get(cc[j]))
               	    {
               		  System.out.println("11 "+ z+" "+j+" "+recommends.get(cc[z])+" "+recommends.get(cc[j]));
               		  c.put(cc[z], finalrecommends.get(cc[z]));
               		  c.put(cc[j], finalrecommends.get(cc[z]));
               		  /// System.out.println("22 "+c.toString());
               		  if(z==cc.length-2)
               		  {
               			  c= ss(c);
               		  }
               	    }
               	   else 
               	   {
               		   // System.out.println("44 "+z+" "+c.size());
               		    if(c.size()!=0)
               			{
               		   //	 System.out.println("33 "+c.toString());
               			   c= ss(c);
               			   c.clear();
               			}
               		   else
               			{
               			     result.add(cc[z]);
               			     resultrate.add(finalrecommends.get(cc[z]));
               			}
               	     }          	
                   }
            }
           
             
             allresult.put(studentid.get(h), result);
             allstudentmp.put(studentid.get(h),conceptsrate);
             System.out.println("�Ƽ�˳�� "+result.toString()+" "+recommends);//������֪ʶ��
        }
        
        HashMap<String,ArrayList<QuestionModel>> rqlist=new recommend().Recommend(allresult,testid);
//        HashMap<String,ArrayList<QuestionModel>> rqlist=new  HashMap<String,ArrayList<QuestionModel>>();
        
        System.out.println(rqlist);
        return rqlist;
    }
   
    private static Map<String,Double> ss(Map<String,Double>c)
    {
    	  List<Entry<String, Double>> list3 = new ArrayList<Entry<String,Double>>(c.entrySet());
    	    
    	     Collections.sort(list3,new Comparator<Map.Entry<String, Double>>() {  
    	              //��������  
    	     public int compare(Entry<String, Double> o1, Entry<String,  Double> o2) {  
    	               return o1.getValue().compareTo(o2.getValue());  
    	        }  
    	     }); 
    	     for (Entry<String, Double> e: list3) {  
    	    	   result.add(e.getKey());
    	    	  // System.out.println("111 "+result.toString());
    		        //System.out.print("111 "+e.getKey()+"="+e.getValue()+"  ");
    	            
    	     }  
    	     //System.out.println("111 "+result.toString());
    	     return c;
    }
    public static HashMap<String,Integer> GetQuestionnumberByConcepts(int testid)
    {
    	QuestionGetByConcepts q=new QuestionGetByConcepts();
    	 HashMap<String,ArrayList<String>>questions=q.questionnumbergetbyconcepts(testid);
    	
    	HashMap<String,Integer>questionnumber=new HashMap<String,Integer>();
    	List<Entry<String, ArrayList<String>>> list = new ArrayList<Entry<String,ArrayList<String>>>(questions.entrySet());
    	 for (Entry<String, ArrayList<String>> e: list) {  
	    	  questionnumber.put(e.getKey(), e.getValue().size());    
	     }  
    	 return questionnumber;
    }
	private static void Testnewdata(String[] attrNames, Object decisionTree,String stduentid,int testid) throws IOException {
		//������Լ�
		 File directory = new File("");
		 File file = new File(directory.getCanonicalPath()+"/"+testid+"/"+stduentid); 
		 //System.
		 System.out.println(file.getAbsolutePath());
		 String[] flist = file.list();
		 System.out.println(file.getAbsolutePath()+" "+flist[0]);
		int correct=0;
		HashMap<String,Integer> questionnumber=GetQuestionnumberByConcepts(testid);
		 HashMap<String, Map<String, Integer>> allNormalTF = new HashMap<String, Map<String,Integer>>();
		 for(int j=0;j<flist.length;j++)
		 {
			 correct=0;
			  String filename=directory.getCanonicalPath()+"/"+testid+"/"+stduentid+"/" + flist[j];
			  filenames[j]=flist[j];
			  System.out.println(filename);
		      Object[][] TestData=gettestdata(filename);
				int count=1;
//				ArrayList<Object> list=new ArrayList();
				
		        long startTime = System.currentTimeMillis();    //��ȡ��ʼʱ��
				//�õ�ÿ�����������ķ���ֵ��
			    for (Object[] row : TestData) {
		            Sample sample = new Sample();       
		            int i = 0;
		            for (int n = row.length; i < n; i++)
		            {
		            	sample.setAttribute(attrNames[i], row[i]); //�����ݸ�����ֵ�����
		            }
		           // System.out.println(sample);
		            String result=getcategoty(sample,decisionTree, 0);
		            if(result.equals("1"))
		            	correct++;
		            System.out.println(result);
		            count++;
		        }
			    correctcount[j]=correct;
			    System.out.println("correctcount[j]"+correctcount[j]+" "+questionnumber +" "+KnowledgeGraph.concepts[j]);
			    correctrate[j]=(double)correctcount[j]/(double)questionnumber.get(KnowledgeGraph.concepts[j]);  //����׼ȷ��
			    //System.out.println(count);
			    long endTime = System.currentTimeMillis();
			    System.out.println(">>>>>�������");
			    System.out.println("����ʱ�䣺" + (endTime - startTime) + "ms");
		 }
		
		 recommends(0,correctrate);
	}
	
	public static double avgcorrect(double correctrate[])
	{
		double sum=0;
		for (int i=0;i<correctrate.length;i++)
		{
			sum=sum+correctrate[i];
		}
		return sum/correctrate.length;
	}
	public static int recommends(int i,double correctrate[])
	{
		double avg=avgcorrect(correctrate);
		//System.out.println(avg);
			if(correctrate[i]<=avg)
		   {
			 // System.out.println(concepts[i]);
			  if(recommend.get(KnowledgeGraph.concepts[i])==null)
			    {
				  recommend.put(KnowledgeGraph.concepts[i],1);
			    }
			  else
			  {
				  int a=recommend.get(KnowledgeGraph.concepts[i])+1;
				  recommend.put(KnowledgeGraph.concepts[i],a);
			  }
			  for(int j=0;j<conceptnumeber;j++)  
			   {
				  if(KnowledgeGraph.edge[j][i]==1&&j!=i)
				   {
				    	if(correctrate[j]<=avg)
				    		{
				    		 // System.out.println(concepts[i]);
				    		  if(recommend.get(KnowledgeGraph.concepts[j])==null)
						      {
							    recommend.put(KnowledgeGraph.concepts[j],1);
						      }
						      else
						      {
							     int a=recommend.get(KnowledgeGraph.concepts[j])+1;
							     recommend.put(KnowledgeGraph.concepts[j],a);
						      }
				    		    recommends(j,correctrate);
				    		}
				    }
			   }
			 
			  return 1;
		   } 
			else
			  return 0;
		
	}
	public static String getcategoty( Sample sample, Object decisionTree, int level) {	
    		Boolean find=false;
             Tree tree = (Tree) decisionTree;
             String attrName = tree.getAttribute();
            // System.out.println("attName "+attrName+" level "+level);
             Object sampleValue =sample.getAttribute(attrName);
            // System.out.println( sampleValue);
             for (Object attrValue : tree.getAttributeValues()) {
            	 if(sampleValue!=null)
            	 {
            		 if(sampleValue.equals(attrValue)){
            		  //System.out.println(sampleValue+" "+ attrValue);
            		  find=true;
                	 Object child = tree.getChild(attrValue);
                	 if (child instanceof Tree) {
                		 getcategoty(sample,child, level + 1);
                	 }
                	 else{
//                		 System.out.println(sample);
                		 results=child.toString();
                		 break;
                	 }          
                   }    

            	 }
             }
             if(find==false)
             {
            	 results="No Result";
             
             }
             //System.out.println("11"+results);
             return results;
    }


	/**
     * ��ȡ�ѷ����������������Map������ -> ���ڸ÷�����������б�
     * @throws FileNotFoundException 
     */    
//    ����map���͵���
    static Map<Object, List<Sample>> readSamples(String[] attrNames) throws FileNotFoundException {  
        Object[][] rawData=getrawdata();
      //  System.out.println(rawData.length);
        System.out.println("ѵ�����������");
        // ��ȡ�������Լ����������࣬�����ʾ������Sample���󣬲������໮��������
        Map<Object, List<Sample>> ret = new HashMap<Object, List<Sample>>();
        for (Object[] row : rawData) {
        	//System.out.println(row[1]);
            Sample sample = new Sample();
            int i = 0;
           // System.out.println("row.length-1 "+ (row.length-1));
            for (int n = row.length-1; i < n; i++)
            {
            	//System.out.println(i);
            	
            	sample.setAttribute(attrNames[i], row[i]); //�����ݸ�����ֵ�����
            	//System.out.println(" 111  "+   attrNames[i]+"  "+row[i]);
            }
            //System.out.println("111  "+i);
            sample.setCategory(row[i]); //��װ category=row[i]
            List<Sample> samples = ret.get(row[i]);
            if (samples == null) {
                samples = new LinkedList<Sample>();
                ret.put(row[i], samples);
            }
            samples.add(sample); // ���������뵽��Ӧ�����ݽṹ��
        }
 
        return ret;
    }
     //����ѵ����
    private static Object[][] getrawdata() throws FileNotFoundException  {
    	// 1-24863  6-19384  9-66248
    	Object[][] rawData=new Object[672][5];
    	//	Object[][] rawData=new Object[128][5];
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("results.txt")));
		String data = null;
		FileOutputStream o2= new FileOutputStream("results������.txt");
		int i=0;
		String writes ="";
		int z=0;
		try {
			while((data = br.readLine())!=null)
			{	
				String[] dataIn=data.split(",");
				//System.out.println("ss "+ dataIn+" "+i);
				
				for(int j=0;j<5;j++)
				{
					//System.out.println("j="+j+" "+dataIn[j]);
					rawData[i][j]=dataIn[j];
					//System.out.println(rawData[i][j]);
					if(j==4)
					{
						writes=writes+dataIn[j]+"\t";
					}
				}
				if((i+1)%56==0)
				{
					writes=writes+"\r\n";
					z++;
					System.out.println("i="+i+" "+writes+" z="+z);
				}
				o2.write(writes.getBytes("GBK"));
				writes="";
				//System.out.println(writes);
				
					i++;
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println(rawData[1][0]);
		return rawData;
	}
    
    //����û�б�ǩ�Ĳ��Լ�
    
    private static Object[][] gettestdata(String filename) throws FileNotFoundException {
    	//2-23801  5-21648
		Object[][] TestData=new Object[5][4];
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		String data = null;
		int i=0;
		try {
			while((data = br.readLine())!=null)
			{	
				String[] dataIn=data.split(",");
				for(int j=0;j<4;j++)
				{
					System.out.println("j  "+dataIn[j]);
					TestData[i][j]=dataIn[j];
				}
					i++;
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return TestData;
	}
	/**
     * ���������

     */
    static Object generateDecisionTree(
            Map<Object, List<Sample>> categoryToSamples, String[] attrNames) {
      
        // ���ֻ��һ��������������������������Ϊ�������ķ���
//        if (categoryToSamples.size() == 1)
//        {	 System.out.println(categoryToSamples.size());
//        	System.out.println("222");
//            return categoryToSamples.keySet().iterator().next();
//        }
 
        // ���û�й����ߵ����ԣ����������о�����������ķ�����Ϊ�������ķ��࣬��ͶƱѡ�ٳ�����
        if (attrNames.length == 0) {
        	System.out.println("111");
            int max = 0;
            Object maxCategory = null;
            for (Entry<Object, List<Sample>> entry : categoryToSamples
                    .entrySet()) {
                int cur = entry.getValue().size();
                if (cur > max) {
                    max = cur;
                    maxCategory = entry.getKey();
                }
            }
            return maxCategory;
        }
 
        // ѡȡ��������
        Object[] rst = chooseBestTestAttribute(categoryToSamples, attrNames);
 
        // ����������㣬��֧����Ϊѡȡ�Ĳ�������
        Tree tree = new Tree(attrNames[(Integer) rst[0]]);
        // ���ù��Ĳ������Բ�Ӧ�ٴα�ѡΪ��������
        String[] subA = new String[attrNames.length - 1];
        for (int i = 0, j = 0; i < attrNames.length; i++)
            if (i != (Integer) rst[0])
            {
            	System.out.println(attrNames[i]);
            	subA[j++] = attrNames[i];  //������Ҫ��ȥ��
            }
 
        // ���ݷ�֧�������ɷ�֧
        @SuppressWarnings("unchecked")
        
        Map<Object, Map<Object, List<Sample>>> splits =
        /* NEW LINE */(Map<Object, Map<Object, List<Sample>>>) rst[2];
        
        for (Entry<Object, Map<Object, List<Sample>>> entry : splits.entrySet()) {
           // System.out.println(entry);
        	Object attrValue = entry.getKey();//�õ�����Ҫ����������Ӧ��ֵ
            Map<Object, List<Sample>> split = entry.getValue();
            Object child = generateDecisionTree(split, subA);
            tree.setChild(attrValue, child);
        }   
        return tree;
    }
 
    /**
     * ѡȡ���Ų������ԡ�������ָ�������ѡȡ�Ĳ������Է�֧����Ӹ���֧ȷ��������
     * �ķ�����Ҫ����Ϣ��֮����С����ȼ���ȷ���������Ĳ������Ի�õ���Ϣ�������
     * �������飺ѡȡ�������±ꡢ��Ϣ��֮�͡�Map(����ֵ->(����->�����б�))
     */
    static Object[] chooseBestTestAttribute(
            Map<Object, List<Sample>> categoryToSamples, String[] attrNames) {
 
        int minIndex = -1; // ���������±�
        double minValue = Double.MAX_VALUE; // ��С��Ϣ��
        Map<Object, Map<Object, List<Sample>>> minSplits = null; // ���ŷ�֧����
 
        // ��ÿһ�����ԣ����㽫����Ϊ�������Ե�������ڸ���֧ȷ���������ķ�����Ҫ����Ϣ��֮�ͣ�ѡȡ��СΪ����
        for (int attrIndex = 0; attrIndex < attrNames.length; attrIndex++) {
            int allCount = 0; // ͳ�����������ļ�����
 
            // ����ǰ���Թ���Map������ֵ->(����->�����б�)
            Map<Object, Map<Object, List<Sample>>> curSplits =
            /* NEW LINE */new HashMap<Object, Map<Object, List<Sample>>>();  //
            for (Entry<Object, List<Sample>> entry : categoryToSamples  //�������ݽṹ��0��1��
                    .entrySet()) {
                Object category = entry.getKey();  //�õ�0ֵ
                List<Sample> samples = entry.getValue();  //�õ�������
                for (Sample sample : samples) {   //��������ÿ����������
                    Object attrValue = sample
                            .getAttribute(attrNames[attrIndex]);   //������ֵ�ó���
                    Map<Object, List<Sample>> split = curSplits.get(attrValue); //split=�������ݽṹ����
                    if (split == null) {
                        split = new HashMap<Object, List<Sample>>();
                        curSplits.put(attrValue, split);
                    }
                    List<Sample> splitSamples = split.get(category);  //�õ�������ΪXXX��
                    if (splitSamples == null) {
                        splitSamples = new LinkedList<Sample>();
                        split.put(category, splitSamples);
                    }
                    splitSamples.add(sample);
                }
                allCount += samples.size();
            }
 
            // ���㽫��ǰ������Ϊ�������Ե�������ڸ���֧ȷ���������ķ�����Ҫ����Ϣ��֮��
            double curValue = 0.0; // ���������ۼӸ���֧
            for (Map<Object, List<Sample>> splits : curSplits.values()) {
                double perSplitCount = 0;
                for (List<Sample> list : splits.values())
                    perSplitCount += list.size(); // �ۼƵ�ǰ��֧������
                double perSplitValue = 0.0; // ����������ǰ��֧
                for (List<Sample> list : splits.values()) {
                    double p = list.size() / perSplitCount;
                    perSplitValue -= p * (Math.log(p) / Math.log(2));
                }
                curValue += (perSplitCount / allCount) * perSplitValue;
            }
 
            // ѡȡ��СΪ����
            if (minValue > curValue) {
                minIndex = attrIndex;
                minValue = curValue;
                minSplits = curSplits;
            }
        }
        return new Object[] { minIndex, minValue, minSplits };
    }
 
    /**
     * ���������������׼���
     */
    static void outputDecisionTree(Object obj, int level, Object from) {
        for (int i = 0; i < level; i++)
            System.out.print("|-----");
        if (from != null)
        	
            System.out.printf("(%s):", from);
        if (obj instanceof Tree) {
            Tree tree = (Tree) obj;
            String attrName = tree.getAttribute();
            System.out.printf("[%s = ?]\n", attrName);
            for (Object attrValue : tree.getAttributeValues()) {
                Object child = tree.getChild(attrValue);
                outputDecisionTree(child, level + 1, attrName + " ==== "
                        + attrValue);
            }
        } else {
            System.out.printf("[CATEGORY = %s]\n", obj);
        }
    }
 
    /**
     * ����������������Ժ�һ��ָ��������������ķ���ֵ
     */
    static class Sample {
 
        private Map<String, Object> attributes = new HashMap<String, Object>();
 
        private Object category;
 
        public Object getAttribute(String name) {
            return attributes.get(name);
        }
 
        public void setAttribute(String name, Object value) {
            attributes.put(name, value);
        }
 
        public Object getCategory() {
            return category;
        }
 
        public void setCategory(Object category) {
            this.category = category;
        }
 
        public String toString() {
            return attributes.toString();
        }
 
    }
 
    /**
     * ����������Ҷ��㣩���������е�ÿ����Ҷ��㶼������һ�þ�����
     * ÿ����Ҷ������һ����֧���ԺͶ����֧����֧���Ե�ÿ��ֵ��Ӧһ����֧���÷�֧������һ���Ӿ�����
     */
    static class Tree {
 
        private String attribute;
 
        private Map<Object, Object> children = new HashMap<Object, Object>();
 
        public Tree(String attribute) {
            this.attribute = attribute;
        }
 
        public String getAttribute() {
            return attribute;
        }
 
        public Object getChild(Object attrValue) {
            return children.get(attrValue);
        }
 
        public void setChild(Object attrValue, Object child) {
            children.put(attrValue, child);
        }
 
        public Set<Object> getAttributeValues() {
            return children.keySet();
        }
 
    }
    public static void writeexl(Map<String,Double>conceptsrate_o,Map<String,Double>kgrate,HashMap<String,Double>rise)
   	{
       	if(conceptsrate_o == null||kgrate==null){  
               return;  
           }  
           HSSFWorkbook wb = new HSSFWorkbook();  
           HSSFSheet sheet = wb.createSheet("sheet1");  
           HSSFRow row = sheet.createRow(0);
           HSSFRow row2 = sheet.createRow(1);
           HSSFRow row3 = sheet.createRow(2);
           List<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(conceptsrate_o.entrySet());  
           List<Entry<String, Double>> list2 = new ArrayList<Entry<String, Double>>(kgrate.entrySet());  
           List<Entry<String, Double>> list3 = new ArrayList<Entry<String, Double>>(rise.entrySet());  
           int j=0;
           for (Entry<String, Double> e: list) {  
   	        HSSFCell cell = row.createCell(j);
               cell.setCellValue(e.getValue());  
               j++;
               //System.out.print(cell.get);
           }  
           j=0;
           for (Entry<String, Double> e2: list2) {  
   	        HSSFCell cell = row2.createCell(j);
               cell.setCellValue(e2.getValue());  
               j++;
           }  
           j=0;
           for (Entry<String, Double> e3: list3) {  
   	        HSSFCell cell = row3.createCell(j);
               cell.setCellValue(e3.getValue());  
               j++;
           }  
            
           ByteArrayOutputStream os = new ByteArrayOutputStream();  
           try  
           {  
               wb.write(os);  
           } catch (IOException e){  
               e.printStackTrace();  
           }  
           byte[] content = os.toByteArray();  
           File file = new File("E:/֪ʶͼ���Ƽ�/ȫ��ʵ����/����/������1333.xlsx");//Excel�ļ����ɺ�洢��λ�á�  
           OutputStream fos  = null;  
           try  
           {  
               fos = new FileOutputStream(file);  
               wb.write(fos);  
               os.close();  
               fos.close();  
           }catch (Exception e){  
               e.printStackTrace();  
           }             
   	}
 
}