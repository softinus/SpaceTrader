package com.raimsoft.spacetrader.util;

import com.raimsoft.spacetrader.data.UserInfo;

public class GenConst
{
	UserInfo uInfo;
	int x,y,p;
	float fRes;

	/**
	 * 
	 */
	public GenConst()
	{
		super();
		uInfo= UserInfo.GetInstance();
		
		x= uInfo.GetWorldMapX();
		y= uInfo.GetWorldMapY();
		p= uInfo.GetSystemMapPlanet();
	}
	
	public float GetConstF()
	{
		fRes= ((x+1)*(y+1)*(p+2) / (( (x+1) % (p+2) )+1)) / 100000.0f;

		while( fRes < 0.09f )	// 0.09보다 작으면
			fRes *= (p+2);

		while( fRes > 1.0f )	// 1이 넘어가면
			fRes= (float) Math.log(fRes);
		
		return fRes;
	}
}