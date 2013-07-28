package com.raimsoft.spacetrader.util;

import java.util.GregorianCalendar;

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
		
		RefreshInfo();
	}
	
	private void RefreshInfo()
	{
		x= uInfo.GetWorldMapX();
		y= uInfo.GetWorldMapY();
		p= uInfo.GetSystemMapPlanet();
	}
	
	public float GetPositionConstF()
	{
		//RefreshInfo();
		fRes= ((x+1)*(y+1)*(p+2) / (( (x+1) % (p+2) )+1)) / 100000.0f;

		while( fRes < 0.09f )	// 0.09보다 작으면
			fRes *= (p+2);

		while( fRes > 1.0f )	// 1이 넘어가면
			fRes= (float) Math.log(fRes);
		
		return fRes;
	}
	
	public float GetPositionConstF(int _p)
	{
		//RefreshInfo();
		fRes= ((x+1)*(y+1)*(_p+2) / (( (x+1) % (_p+2) )+1)) / 100000.0f;

		while( fRes < 0.09f )	// 0.09보다 작으면
			fRes *= (_p+2);

		while( fRes > 1.0f )	// 1이 넘어가면
			fRes= (float) Math.log(fRes);
		
		return fRes;
	}
	
	public float GetPositionConstF(int _p, float fFactor)
	{
		//RefreshInfo();
		fRes= ((x+1)*(y+1)*(_p+2) / (( (x+1) % (_p+2) )+1)) / 100000.0f;

		while( fRes < 0.09f )	// 0.09보다 작으면
			fRes *= (_p+2);

		while( fRes > 1.0f )	// 1이 넘어가면
			fRes= (float) Math.log(fRes);
		
		fRes *= fFactor;
		
		return fRes;
	}
	
	/**
	 * 시간과 장소에 비례하는 상수를 만든다.
	 * @return
	 */
	public float GetPositionTimeConstF(int nItems)
	{
		float fRes;
		
		GregorianCalendar today = new GregorianCalendar ( );

		int nYear = today.get ( GregorianCalendar.YEAR );
		int nMonth = today.get ( GregorianCalendar.MONTH ) + 1;
		int nDay = today.get ( GregorianCalendar.DAY_OF_MONTH ); 
		int nHour= today.get( GregorianCalendar.HOUR_OF_DAY) + 1;
		int nMinute= today.get( GregorianCalendar.MINUTE);
		
		if(nItems==-1)
		{
			float fParam1 = (nHour % nYear) * (nDay + 10 % nHour) + nMonth;
			float fParam2 = (((x % nHour) + (y % nHour)) / (p + 2)) + 1;       
			
			fRes= fParam1 / fParam2;			
		}
		else
		{
			float fParam1 = (nHour % nYear) * (nDay + 10 % nHour) + nMonth;
			float fParam2 = (((x % nHour+nItems) + (y % nHour+nItems)) / (p + nItems + 2)) + 1;       
			
			fRes= fParam1 / fParam2;
		}
        fRes /= 100000.0f;
        fRes = Math.abs(fRes);
        
        while (fRes < 0.09f)	// 0.05보다 작으면
        {
            fRes *= (p + 2);
        }

        while (fRes > 1.0f)	// 1이 넘어가면
        {
            fRes = (float)Math.log(fRes);
        }
        
        float fNextConst= GetNextPositionTimeConstF(nItems);
        
        float fTemp = ((fNextConst - fRes) * ((float)nMinute / 60.0f));   // 다음Hour와 현재Hour의 차이만큼 interpolation함.
        float fFinalConst = fRes + fTemp;
        
        return fFinalConst;
	}
	
	protected float GetNextPositionTimeConstF(int nItems)
	{
		float fRes;
		
		GregorianCalendar today = new GregorianCalendar ( );

		int nYear = today.get ( today.YEAR );
		int nMonth = today.get ( today.MONTH ) + 1;
		int nDay = today.get ( today.DAY_OF_MONTH ); 
		int nHour= today.get(today.HOUR_OF_DAY);
		//int nMinute= today.get(today.MINUTE);
		
		 int nNextHour = nHour + 1;
		if(nItems==-1)
		{
			float fParam1 = (nNextHour % nYear) * (nDay + 10 % nNextHour) + nMonth;
			float fParam2 = (((x % nNextHour) + (y % nNextHour)) / (p + 2)) + 1;       
			
			fRes= fParam1 / fParam2;			
		}
		else
		{
			float fParam1 = (nNextHour % nYear) * (nDay + 10 % nNextHour) + nMonth;
			float fParam2 = (((x % nNextHour+nItems) + (y % nNextHour+nItems)) / (p + nItems + 2)) + 1;       
			
			fRes= fParam1 / fParam2;
		}
		
         fRes /= 100000.0f;
         fRes = Math.abs(fRes);

         while (fRes < 0.09f)	// 0.05보다 작으면
         {
             fRes *= (p + 2);
         }

         while (fRes > 1.0f)	// 1이 넘어가면
         {
             fRes = (float)Math.log(fRes);
         }

         return fRes;
	}
}