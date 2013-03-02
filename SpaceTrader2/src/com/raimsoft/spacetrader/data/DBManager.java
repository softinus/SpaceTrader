package com.raimsoft.spacetrader.data;

import java.util.ArrayList;

import com.raimsoft.spacetrader.obj.items.BaseItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class DBManager extends DBCore
{
	private Context context;
	
	public DBManager(Context _context)
	{
		super(_context);
		context= _context;
	}
	
	
	public void AddItems(int nItemType, int nCount, int nCurrPrice)
	{
		ContentValues CV= new ContentValues();
		CV.put("type", nItemType);
		CV.put("count", nCount);
		CV.put("price", nCurrPrice);
		this.DBInsertCV(Global.DB_TABLE_ITEMSINFO, CV);
	}
	
	public ArrayList<BaseItem> GetItems()
	{
		ArrayList<BaseItem> arrRes = new ArrayList<BaseItem>();
		String[] strCol= {"type", "count", "price"};
		
		Cursor CS= this.GetCursorFromDB(Global.DB_TABLE_ITEMSINFO, strCol);
		
		CS.moveToFirst();
		
		while( CS.isAfterLast() == false )
		{
			BaseItem item = new BaseItem();
			item.SetItemType( CS.getInt(0) );
			item.nCount= CS.getInt(1);
			item.nCurrentPrice= CS.getInt(2);
			arrRes.add(item);
			
			CS.moveToNext();
		}

		CS.close();	// 이게 오류가 아닐까 추가해봄
		
		return arrRes;
	}
	
//	/**
//	 * 돈값을 세팅한다.
//	 * @param nMoney : 달란트
//	 */
//	public void SetMoneyToDB(int nMoney)
//	{
//		this.DBUpdate(Global.DB_TABLE_ITEMSINFO, "money", nMoney);
//	}	
//	
//	
	

//	/**
//	 * 경험치를 증가시킨다.
//	 * @param nKnowledge : 지식
//	 */
//	public void AddExp(int nKnowledge)
//	{
//		this.DBUpdate(Global.DB_TABLE_MYINFO, "exp", nKnowledge+GetExp());
//	}
//	
	
//	/**
//	 * 기부했는지 여부
//	 * @return
//	 */
//	public boolean GetDonation()
//	{
//		boolean bDonation;
//		if( this.GetStrDataFromDB(Global.DB_TABLE_MYINFO, "do_donation").equals("T"))
//			bDonation= true;
//		else
//			bDonation= false;
//		
//		return bDonation;
//	}
//	
//	public void SetDonation(String _str)
//	{
//		this.DBUpdate(Global.DB_TABLE_MYINFO, "do_donation", _str);
//	}
	

	
	
	

}


