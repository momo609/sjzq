package com.sjzg.recommend;

import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;


public class Rservel {
	private static String R_EXE_PATH = "D:/InstallSoftware/R-3.5.1/bin/R.exe";
	private static String R_PATH = "D:/InstallSoftware/R-3.5.1/myStartR/Rserve.R";
	
	public static RConnection getRConnection(){
		try{
			RConnection rConnection = new RConnection();
			return rConnection;
		}catch(RserveException e){
			System.out.println("正在启动Rserve服务...");
			try{
				Runtime rn = Runtime.getRuntime();
				String[] commandArgs = {R_EXE_PATH,R_PATH};
				rn.exec(commandArgs);
				Thread.sleep(5000);
			}catch(Exception e2){
				e2.printStackTrace();
			}
			return getRConnection();
		}
	}
}
