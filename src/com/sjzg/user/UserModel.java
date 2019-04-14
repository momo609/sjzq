package com.sjzg.user;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class UserModel {

	//User������ģ��
	private String UserID;
	private String RealName;
	private String SessionToken;
	private String MobilePhone;
	private String Password;
	private String Role;
	private String Sex;
	private String Avatar;
	private String NickName;
	
	
	//������֤
	public String validate() 
	{
		if(UserID==null||UserID.equals(""))
		{
			return "UserID Error";
		}
		if(RealName==null||RealName.equals(""))
		{
			return "RealName Error";
		}

		if(Password==null||Password.equals(""))
		{
			return "Password Error";
		}
		if(Role==null||(!Role.equals("teacher")))
		{
			Role = "student";
		}
		if(MobilePhone==null||MobilePhone.equals(""))
		{
			return "Role Error";
		}
		if(NickName==null||NickName.equals(""))
		{
			return "NickName Error";
		}
		if(Sex==null||Sex.equals(""))
		{
			return "Sex Error";
		}
		if(Avatar==null||Avatar.equals(""))
		{
			return "Avatar Error";
		}
		return "ok";
		
	}
	
	//��ȡSessionToken
	
	
	public String createSessionToken(String plainString) {
		String md5_String = "null";
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		String dateString = dateFormat.format(new Date());
		plainString = plainString + dateString;
		  try {
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				md5.update(plainString.getBytes());
	            // digest()���ȷ������md5 hashֵ������ֵΪ8Ϊ�ַ�������Ϊmd5 hashֵ��16λ��hexֵ��ʵ���Ͼ���8λ���ַ�
	            // BigInteger������8λ���ַ���ת����16λhexֵ�����ַ�������ʾ���õ��ַ�����ʽ��hashֵ
				md5_String = new BigInteger(1, md5.digest()).toString(16);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return md5_String;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public String getMobilePhone() {
		return MobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		MobilePhone = mobilePhone;
	}
	public String getSessionToken() {
		return SessionToken;
	}
	public void setSessionToken(String sessionToken) {
		SessionToken = sessionToken;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getRole() {
		return Role;
	}
	public void setRole(String role) {
		Role = role;
	}

	public String getRealName() {
		return RealName;
	}

	public void setRealName(String realName) {
		RealName = realName;
	}

	public String getSex() {
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
	}

	public String getAvatar() {
		return Avatar;
	}

	public void setAvatar(String avatar) {
		Avatar = avatar;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}



}
