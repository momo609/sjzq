package com.sjzg.answer;

import java.util.ArrayList;
import java.util.Comparator;
import com.sjzg.answer.ProcessedAnswerModel;
import com.sjzg.answer.ProcessingData;


public class RankByScore {

	//*************��ѧ���ֳ������ֱ����arraylist,������arraylistװ��arraylist��
	//*************����groupStudent(int testid)
	public ArrayList<ArrayList<ProcessedAnswerModel>> groupStudent(int testid) {
		
		ArrayList<ArrayList<ProcessedAnswerModel>> groupStudent = new ArrayList<ArrayList<ProcessedAnswerModel>>();
		
		//*********��ȡ����������ѧ��
		ArrayList<ProcessedAnswerModel> wholeSortedStudent = new ArrayList<ProcessedAnswerModel>();
		wholeSortedStudent = rankStudent(testid);
		
		//*********��ѧ�����ɼ��ߵ;���Ϊ������
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


	//*************��ȡ����ѧ��answer���� �洢��ArrayList<ProcessedAnswerModel>�з���
	
	public ArrayList<ProcessedAnswerModel> getScore( int testid) {
		
		ProcessingData processingData = new ProcessingData();
		
		ArrayList<ProcessedAnswerModel> result = new ArrayList<ProcessedAnswerModel>();
				
		result = processingData.processingdata(testid);
		
		return result;
			
	}
	
	
	//*************��ѧ��ͨ��grade�������򣬲�����
	public ArrayList<ProcessedAnswerModel> rankStudent(int testid) {
		
		//*********��ȡ����δ����ѧ��
		ArrayList<ProcessedAnswerModel> sortResult = getScore(testid);
		
		//*********������ѧ����������
		java.util.Collections.sort(sortResult,new Comparator<ProcessedAnswerModel>() {
			
			@Override
			public int compare(ProcessedAnswerModel p1, ProcessedAnswerModel p2) {
				// TODO �Զ����ɵķ������
				
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
