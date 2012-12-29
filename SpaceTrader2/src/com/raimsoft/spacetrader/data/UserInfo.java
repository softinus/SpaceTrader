package com.raimsoft.spacetrader.data;


public class UserInfo
{
	private static final UserInfo instance= new UserInfo();
	
	private boolean bLogined= false;				// 로그인 정보
	private EnumShip eShip= EnumShip.E_NULL_INFO;	// 함선 정보
	
	
	public static final UserInfo GetInstance()
	{
		return instance;
	}
	
	public boolean GetLogin()
	{
		return bLogined;
	}
	
	public void SetLogin(boolean _b)
	{
		bLogined= _b;
	}
	
	
}
