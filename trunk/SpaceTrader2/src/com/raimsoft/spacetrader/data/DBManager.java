package com.raimsoft.spacetrader.data;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

public class DBManager extends DBCore
{
	private Context context;
	
	public DBManager(Context _context)
	{
		super(_context);
		context= _context;
	}
	
	
	/**
	 * 돈값을 세팅한다.
	 * @param nMoney : 달란트
	 */
	public void SetMoneyToDB(int nMoney)
	{
		this.DBUpdate(Global.DB_TABLE_MYINFO, "money", nMoney);
	}
	

	

	
	
	
	/**
	 * 학년 정보를 가져온다.
	 * @return 학년
	 */
	public int GetLevel()
	{
		int nGrade = 0;
		
		nGrade= this.GetIntDataFromDB(Global.DB_TABLE_MYINFO, "level", 0);

		return nGrade;
	}

	/**
	 * 경험치 정보를 가져온다.
	 * @return 지식
	 */
	public int GetExp()
	{
		int nEXP = 0;
		
		nEXP= this.GetIntDataFromDB(Global.DB_TABLE_MYINFO, "exp", 0);

		return nEXP;
	}
	

	/**
	 * 경험치를 증가시킨다.
	 * @param nKnowledge : 지식
	 */
	public void AddExp(int nKnowledge)
	{
		this.DBUpdate(Global.DB_TABLE_MYINFO, "exp", nKnowledge+GetExp());
	}
	
	
	/**
	 * 기부했는지 여부
	 * @return
	 */
	public boolean GetDonation()
	{
		boolean bDonation;
		if( this.GetStrDataFromDB(Global.DB_TABLE_MYINFO, "do_donation").equals("T"))
			bDonation= true;
		else
			bDonation= false;
		
		return bDonation;
	}
	
	public void SetDonation(String _str)
	{
		this.DBUpdate(Global.DB_TABLE_MYINFO, "do_donation", _str);
	}
	

	
	
	

}


