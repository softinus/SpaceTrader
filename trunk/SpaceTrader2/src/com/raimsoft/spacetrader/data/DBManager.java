package com.raimsoft.spacetrader.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.raimsoft.spacetrader.obj.items.BaseItem;

public class DBManager extends DBCore
{
	private Context context;
	
	public DBManager(Context _context)
	{
		super(_context);
		context= _context;
	}
	
	public void DropItemsTable()
	{
		 DB.execSQL("DROP TABLE IF EXISTS "+Global.DB_TABLE_ITEMSINFO) ;
	}
	
	/**
	 * 
	 * @param nItemType : 아이템 타입
	 * @param nCount : 카운트 (100개 이상넣으면 안됨!)
	 * @param nCurrPrice : 현재 구매 가격
	 * @return 1: 정상동작, -1:아이템꽉참
	 */
	public int AddItems(int nItemType, int nCount, int nCurrPrice)
	{
		ArrayList<BaseItem> arrRes= GetItems();
		
		for(int i=0; i<arrRes.size(); ++i)	// 아이템 리스트 돌면서
		{
			if( arrRes.get(i).eType.ordinal() == nItemType)	// 같은 타입이 있으면
			{
				if( arrRes.get(i).nCount == 100 )
				{
					
				}
				else if( arrRes.get(i).nCount + nCount <= 100 )	// 현재 가지고 있는 개수 + 추가할 개수가 100보다 작으면
				{
					this.UpdateItem(i, arrRes.get(i).nCount+nCount);	// 데이터 갱신
					return 1;
				}
				else
				{
					this.UpdateItem(i, 100);
					nCount= arrRes.get(i).nCount+nCount-100;
					break;
				}
			}
		}
		
		if(arrRes.size() == 20)
			return -1;
		
		ContentValues CV= new ContentValues();
		CV.put("type", nItemType);
		CV.put("count", nCount);
		CV.put("price", nCurrPrice);
		this.DBInsertCV(Global.DB_TABLE_ITEMSINFO, CV);
		
		return 1;
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
	
	public void UpdateItem(int nItemIndex, int nUpdateCount)
	{
		this.DBUpdate(Global.DB_TABLE_ITEMSINFO, "count", nUpdateCount, "_id="+(nItemIndex+1));
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


