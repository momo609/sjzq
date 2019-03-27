package com.sjzg.jdbctest;
import java.sql.*;


public class JDBCTest {

public static void main(String[] args){

           // ����������
           String driver = "com.mysql.jdbc.Driver";

           // URLָ��Ҫ���ʵ����ݿ���sjzg
           String url = "jdbc:mysql://172.16.34.91:3306/sjzg";

           // MySQL����ʱ���û���
           String user = "root"; 

           // MySQL����ʱ������
           String password = "a7682318";

           try { 
            // ������������
            Class.forName(driver);

            // �������ݿ�
            Connection conn = DriverManager.getConnection(url, user, password);

            if(!conn.isClosed()) 
             System.out.println("Succeeded connecting to the Database!");

            // statement����ִ��SQL���
            Statement statement = conn.createStatement();

            // Ҫִ�е�SQL���
            String sql = "select * from User";

            // �����
            ResultSet rs = statement.executeQuery(sql);

            System.out.println("-----------------");
            System.out.println("ִ�н��������ʾ:");
            System.out.println("-----------------");
            System.out.println(" ѧ��" + "\t" + " ����");
            System.out.println("-----------------");

            String name = null;

            while(rs.next()) {
    
             // ѡ��sname��������
             name = rs.getString("sname");
    
             // ����ʹ��ISO-8859-1�ַ�����name����Ϊ�ֽ����в�������洢�µ��ֽ������С�
             // Ȼ��ʹ��GB2312�ַ�������ָ�����ֽ�����
             name = new String(name.getBytes("ISO-8859-1"),"GB2312");

             // ������
             System.out.println(rs.getString("sno") + "\t" + name);
            }

            rs.close();
            conn.close();

           } catch(ClassNotFoundException e) {


            System.out.println("Sorry,can`t find the Driver!"); 
            e.printStackTrace();


           } catch(SQLException e) {


            e.printStackTrace();


           } catch(Exception e) {


            e.printStackTrace();


           } 
} 





}