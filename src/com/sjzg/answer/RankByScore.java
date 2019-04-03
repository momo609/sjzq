package com.sjzg.answer;

import java.util.ArrayList;
import java.util.Comparator;
import com.sjzg.answer.ProcessedAnswerModel;
import com.sjzg.answer.ProcessingData;


public class RankByScore {

	//*************将学生分成三组后分别存入arraylist,将三个arraylist装入arraylist中
	//*************调用groupStudent(int testid)
	public ArrayList<ArrayList<ProcessedAnswerModel>> groupStudent(int testid) {
		
		ArrayList<ArrayList<ProcessedAnswerModel>> groupStudent = new ArrayList<ArrayList<ProcessedAnswerModel>>();
		
		//*********获取所有已排序学生
		ArrayList<ProcessedAnswerModel> wholeSortedStudent = new ArrayList<ProcessedAnswerModel>();
		wholeSortedStudent = rankStudent(testid);
		
		//*********将学生按成绩高低均分为三个组
		ArrayList<ProcessedAnswerModel> group1 = new ArrayList<ProcessedAnswerModel>();
		ArrayList<ProcessedAnswerModel> group2 = new ArrayList<ProcessedAnswerModel>();
		ArrayList<ProcessedAnswerModel> group3 = new ArrayList<ProcessedAnswerModel>();
		
		for (int i = 0; i < wholeSortedStudent.size(); i++) {			
			
			if( i%3 == 1 ){
				group1.add(wholeSortedStudent.get(i));
				
			}else if (i%3 == 2) {
				group2.add(wholeSortedStudent.get(i));
				
			}else if (i%3 == 0) {
				group3.add(wholeSortedStudent.get(i));
			}
			
		}
		
		groupStudent.add(group1);
		groupStudent.add(group2);
		groupStudent.add(group3);
		
		return groupStudent;
		
	}	


	//*************获取所有学生answer数据 存储在ArrayList<ProcessedAnswerModel>中返回
	
	public ArrayList<ProcessedAnswerModel> getScore( int testid) {
		
		ProcessingData processingData = new ProcessingData();
		
		ArrayList<ProcessedAnswerModel> result = new ArrayList<ProcessedAnswerModel>();
				
		result = processingData.processingdata(testid);
		
		return result;
			
	}
	
	
	//*************将学生通过grade进行排序，并返回
	public ArrayList<ProcessedAnswerModel> rankStudent(int testid) {
		
		//*********获取所有未排序学生
		ArrayList<ProcessedAnswerModel> sortResult = getScore(testid);
		
		//*********将所有学生进行排序
		java.util.Collections.sort(sortResult,new Comparator<ProcessedAnswerModel>() {
			
			@Override
			public int compare(ProcessedAnswerModel p1, ProcessedAnswerModel p2) {
				// TODO 自动生成的方法存根
				
				float diff = p1.getGrade() - p2.getGrade();
				if(diff > 0){
					
					return 1;
				}
				else if (diff < 0){
					return -1;
				}
				
				return 0;
			}
		});
		
	
		return sortResult;
	}
	
	
	
	

}
