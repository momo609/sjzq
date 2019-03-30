package com.sjzg.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestCreateFile {
public static void main(String args[]) throws IOException
{
	 File directory = new File("");
	
			String path=directory.getCanonicalPath()+"/123/456";
			String filename=path+"1.txt";
			  File file=new File(path);
		        if(!file.exists()){
		        	file.mkdirs();
		        }
			//filename=path+s+".txt";
			System.out.println("ÎÄ¼þÃû "+filename);
			FileOutputStream o= new FileOutputStream(filename,true);
//			writes=qlist.get(i).getLevel()+","+judgetime[i]+","+judgelookback[i]+","+countanswer[i]+","+judgecollect[i]+","+finalresults[i]+"\r\n";
			String writes="12244";
			o.write(writes.getBytes("GBK"));
			
}
}
