package com.sjzg.recommend;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class pmfcd {
	  static double P[][]=new double[12][2];
	  static double Q[][]=new double[56][2];
	  static double QT[][]=new double[2][56];
  public static void main(String args[]) throws UnsupportedEncodingException, IOException
  {
	    int q[][]=new getQ().getq();  //Q矩阵
	    double a[][]=new double[12][56];
	    double sa[][]=new double[12][56];   
	    BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream("C:/Users/admin/Desktop/result1.txt")));
	    
//	    BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream("E:/知识图谱推荐/结果/全部实验结果/DINAmp.txt")));
	    BufferedReader br3 = new BufferedReader(new InputStreamReader(new FileInputStream("E:/知识图谱推荐/结果/全部实验结果/sg.txt")));
	    double mp[][]=new double[12][11];  //DINA算法算出的掌握程度
	    double s[]=new double[56];
	    double g[]=new double[56];
	    double Buv[][]=new double[12][56];
	    double Bu[]=new double[12];
	    double Bv[]=new double[56];
	    double la[][]=new double[12][56];
	    double cp[][]=new double[12][56];
		String data = null;
		int i=0;
		try {
			while((data = br2.readLine())!=null)
			{	
				String[] dataIn=data.split(",");
				System.out.println("ss "+ data);
				for(int j=0;j<dataIn.length/11;j++)
				{
					
					for(int h=0;h<11;h++)
					{
						mp[j][h]=Double.parseDouble(dataIn[j*11+h]);
						System.out.print(mp[j][h]+" ");
					}
					System.out.println();
				}
				i++;
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
		i=0;
		System.out.println();
		try {
			while((data = br3.readLine())!=null)
			{	
				String[] dataIn=data.split(" ");
				//System.out.println("ss "+ dataIn+" "+i);
			    g[i]=Double.parseDouble(dataIn[0]);
			    s[i]=Double.parseDouble(dataIn[1]);
				i++;
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int z=0;z<12;z++)
		{
			for(int j=0;j<56;j++)
			{
				for(int h=0;h<11;h++)
				{
					if(q[j][h]==1)
						sa[z][j]=Math.sqrt(mp[z][h]);
				}
					
			}
		}
		for(int z=0;z<12;z++)
		{
			for(int j=0;j<56;j++)
			{
				if(sa[z][j]!=0)
				{
					a[z][j]=((1-s[j])*sa[z][j])/((1-s[j])*sa[z][j]+g[j]*(1-sa[z][j]));
				}
				else
				{
					a[z][j]=(s[j]*sa[z][j])/(s[j]*sa[z][j]+(1-g[j])*(1-sa[z][j]));
				}
				System.out.print(a[z][j]+" ");
			}
			System.out.println();
		}
		double sumu=0;
		for(int z=0;z<12;z++)
		{
			sumu=0;
			for(int j=0;j<56;j++)
			{
				sumu=sumu+a[z][j];
			}
			Bu[z]=sumu/56;
		}
		double sumv=0;
		for(int j=0;j<56;j++)
		{
			sumv=0;
			for(int z=0;z<12;z++)
			{
				sumv=sumv+a[z][j];
			}
			Bv[j]=sumv/12;
		}
		matrix_factorization(a,5000,0.0002,0.02);
		double aa[][]=new double[12][56];
		aa=matrixmutiple(P,QT);
		System.out.println("aa");
		for(int z=0;z<12;z++)
		{
			for(int j=0;j<56;j++)
			{
				System.out.print(aa[z][j]+" ");
			}
			System.out.println("\n");
		}
		double sumrow=0;
		double sumcol=0;
		double[] averagepro=new double[56];
		double[] averagepeo=new double[12];
		for(int z=0;z<12;z++)
		{
			sumrow=0;
			for(int j=0;j<56;j++)
			{
				sumrow=sumrow+aa[z][j];
			}
//			sum=sum+sumrow/56;
			averagepeo[z]=sumrow/56;
		}
		for(int j=0;j<56;j++)
		{
			for(int z=0;z<12;z++)
			{
				sumcol=sumcol+aa[z][j];
			}
			averagepro[j]=sumcol/12;
		}
//		double averagetotal=sum/12;
//		System.out.println("avaer "+averagetotal);
		for(int z=0;z<12;z++)
		{
			for(int j=0;j<56;j++)
			{
				la[z][j]=averagepeo[z]+0.4*(Bu[z]+Bv[j])+0.6*aa[z][j];
//				la[z][j]=0.3*(Bu[z]+Bv[j])+0.7*aa[z][j];
     			System.out.println("Bu[z]+Bv[j] "+(Bu[z]+Bv[j]));
			}
		}
		System.out.println("la");
		for(int z=0;z<12;z++)
		{
			for(int j=0;j<56;j++)
			{
				System.out.print(la[z][j]+" ");
			}
			System.out.println("\n");
		}
//		FileOutputStream o2= new FileOutputStream("E:/知识图谱推荐/pmfcd_cp.txt");
//		String writes="";
		for(int z=0;z<12;z++)
		{
			for(int j=0;j<56;j++)
			{
				cp[z][j]=Math.pow(g[j], (1-la[z][j]))*Math.pow((1-s[j]), la[z][j]);
				//System.out.println(Math.pow(g[j], (1-la[z][j]))+" "+Math.pow((1-s[j]), la[z][j]));
				
//				if(j!=55)
//				{
//					writes=writes+String.format("%.0f", cp[z][j])+",";
//				}
//				else
//				{
//					writes=writes+String.format("%.0f", cp[z][j]);
//				}
			}
//			writes=writes+"\r\n";
//			o2.write(writes.getBytes("GBK"));
		}
//		for(int z=0;z<12;z++)
//		{
//			for(int j=0;j<56;j++)
//			{
//				System.out.print(cp[z][j]+" ");
//			}
//			System.out.println("\n");
//		}
	
  }

  public static void matrix_factorization(double a[][], int steps, double alpha, double beta)
  {
	  int k=2;
	  Random r = new Random();
	  for(int l=0;l<12;l++)
	  {
		  for(int z=0;z<2;z++)
		  { 
			  P[l][z]= r.nextGaussian();
		  }
	  }
	  Random r2 = new Random();
	  for(int l=0;l<56;l++)
	  {
		  for(int z=0;z<2;z++)
		  {
			  Q[l][z]=r.nextGaussian();
		  }
	  }
	  QT=Transpose(Q,56,2);
	  double eij;
	  double e;
	  for (int step=0;step<steps;step++)
	  {
		      for (int z=0;z<a.length ;z++)
		      {
		    	  for (int j=0;j<a[0].length;j++) 
		    	  {
		    		  if (a[z][j]>0)
		    		  {
		    			   eij=a[z][j]-mutiple(P[z],QT,j);  // .dot(P,Q) 表示矩阵内积
		                    for (int h=0;h<k;h++)
		                    {
		                    	P[z][h]=P[z][h]+alpha*(2*eij*QT[h][j]-beta*P[z][h]);
		                    	QT[h][j]=QT[h][j]+alpha*(2*eij*P[z][h]-beta*QT[h][j]);
		                    }
		    		  }
		    	  }
		      }
		                        
		      //eR=numpy.dot(P,Q)
		      e=0;
		      for (int z=0;z<a.length ;z++)
		      {
		    	  for (int j=0;j<a[0].length;j++)
		    	  {
		    		  if (a[z][j]>0)
		    		  {
		    			  e=e+Math.pow((a[z][j]-mutiple(P[z],QT,j)),2);
		                  for (int h=0;h<k;h++)
		                  {
		                	  e=e+(beta/2)*(Math.pow(P[z][h],2)+Math.pow(QT[h][j],2));
		                  }
		    		  }           
		        	}
		        }
		    if(e<0.001)
		       break;
	  }
			            
  }
  public static double [][] Transpose(double [][] Matrix, int Line, int List) 
  {
	  double [][] MatrixC = new double [List] [Line] ;
	  for (int i = 0; i < Line; i++) 
	  {
		  for (int j = 0; j < List; j++) 
		  {
			  MatrixC[j][i] = Matrix[i][j] ;
          }
	  }
	  return MatrixC ;
   }
  public static double mutiple(double a[], double b[][],int j) 
  {
      //当a的列数与矩阵b的行数不相等时，不能进行点乘，返回null
      if (a.length != b.length)
          return 0.0;
      //c矩阵的行数y，与列数x
      double sum=0;
      for (int i = 0; i < a.length; i++)
      {
    	  sum=sum+a[i]*b[i][j];
      }
                  
      return sum;
  }
  public static double[][] matrixmutiple(double a[][], double b[][]) {
      //当a的列数与矩阵b的行数不相等时，不能进行点乘，返回null
      if (a[0].length != b.length)
          return null;
      //c矩阵的行数y，与列数x
      int y = a.length;
      int x = b[0].length;
      double c[][] = new double[y][x];
      for (int i = 0; i < y; i++)
          for (int j = 0; j < x; j++)
              //c矩阵的第i行第j列所对应的数值，等于a矩阵的第i行分别乘以b矩阵的第j列之和
              for (int k = 0; k < b.length; k++)
                  c[i][j] += a[i][k] * b[k][j];
      return c;
  }


}
