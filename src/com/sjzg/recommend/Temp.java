package com.sjzg.recommend;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;


public class Temp {
	public static void main(String[] args) throws RserveException, REXPMismatchException {
		RConnection connection = Rservel.getRConnection();
		System.out.println("ƽ��ֵ");
		String vetor = "c(1,2,3,4)";
		connection.eval("meanVal<-mean("+vetor+")");
		double mean = connection.eval("meanVal").asDouble();
		System.out.println("the mean of given vector is = "+mean);
		
		System.out.println("ִ�нű�");
		connection.eval("source('D:/InstallSoftware/R-3.5.1/myStartR/myAdd.R')");
		int num1 = 20;
		int num2 = 10;
		connection.eval("myAdd()");
		System.out.println("done!");
		connection.close();
	}
}
