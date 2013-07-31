package com.raimsoft.spacetrader.util;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.raimsoft.spacetrader.data.Global;
import com.raimsoft.spacetrader.data.UserInfo;
import com.raimsoft.spacetrader.obj.items.ItemData;

/**
 * 메모리와 서버를 동기화해주는 클래스
 * @author Mark
 *
 */
public class ParseConnector
{
	UserInfo uInfo;
	
	public ParseConnector()
	{
		uInfo= UserInfo.GetInstance();
	}
	
	public void SetShipHull(final int nHull)
	{
		final ParseUser user= ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query= ParseQuery.getQuery(Global.PO_TABLE_USERINFO);	// 유저 데이터를 찾는다.
		query.whereEqualTo(Global.PO_USER_ID, user);
		query.getFirstInBackground(new GetCallback<ParseObject>()
		{				
			@Override
			public void done(ParseObject PO, ParseException e)
			{
				PO.put(Global.PO_SHIP_HULL, nHull);			
				PO.saveInBackground();
			}
		});
	}
	/**
	 * 해당하는 골드를 지불.	
	 * @param nPay
	 */
	public void PayGold(final int nPay)
	{
		if(nPay==0)
			return;
		
		final ParseUser user= ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query= ParseQuery.getQuery(Global.PO_TABLE_USERINFO);	// 유저 데이터를 찾는다.
		query.whereEqualTo(Global.PO_USER_ID, user);
		query.getFirstInBackground(new GetCallback<ParseObject>()
		{				
			@Override
			public void done(ParseObject PO, ParseException e)
			{
				PO.increment(Global.PO_MOENY, -nPay);	// 메모리 -> 서버				
				PO.saveInBackground();
			}
		});
	}
	
	/**
	 * 아이템을 거래한 것을 서버에 알림.
	 * @param nPay : 지불한 총 금액 (샀으면 -, 팔았으면 -)
	 * @param nItemType : 거래한 아이템 타입
	 * @param nItemAmount : 거래한 아이템 양 (샀으면 +, 팔았으면 -)
	 */
	public void SyncTradeItem(final boolean bBuy, final int nPay, final int nItemType, final int nItemAmount)
	{
		if(nItemAmount==0)
			return;
		
		Log.d(":::::::::::ParseConnector:::::::::::", "111111111111");
		final ParseUser user= ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query= ParseQuery.getQuery(Global.PO_TABLE_USERINFO);	// 유저 데이터를 찾는다.
		query.whereEqualTo(Global.PO_USER_ID, user);
		Log.d(":::::::::::ParseConnector:::::::::::", "22222222222");
		query.getFirstInBackground(new GetCallback<ParseObject>()
		{				
			@Override
			public void done(ParseObject PO, ParseException e)
			{
				PO.increment(Global.PO_MOENY, nPay);	// 메모리 -> 서버
				
				PO.saveInBackground();
				Log.d(":::::::::::ParseConnector:::::::::::", "33333333333");
			}
		});
		
		Log.d(":::::::::::ParseConnector:::::::::::", "aaaaaaaaaa");
		ParseQuery<ParseObject> query2= ParseQuery.getQuery(Global.PO_TABLE_OWNITEMS);	// 소유 데이터를 찾는다.
		query2.whereEqualTo(Global.PO_USER_ID, user);
		Log.d(":::::::::::ParseConnector:::::::::::", "bbbbbbbbb");
		query2.findInBackground(new FindCallback<ParseObject>()
		{			
			@Override
			public void done(List<ParseObject> objects, ParseException e)
			{
				boolean bHaveType= false;
				for(ParseObject PO : objects)		// 서버 소유아이템 리스트 돌면서
				{
					int nServerItemKey= PO.getInt(Global.PO_ITEM_KEY);
					int nServerItemCount= -1 * PO.getInt(Global.PO_ITEM_COUNT);
					if(nServerItemKey==nItemType)	// 같은 타입 있으면
					{
						if(( nServerItemCount==nItemAmount) && !bBuy)	// 아이템창 전부 판매했으면 삭제함.
							PO.deleteInBackground();
						else							
							PO.increment(Global.PO_ITEM_COUNT, nItemAmount);	// 추가해줌
						
						PO.saveInBackground();
						bHaveType= true;
						break;
					}
				}
				
				Log.d(":::::::::::ParseConnector:::::::::::", "ccccccccccc");
				
				if(!bHaveType && bBuy)	// 같은 타입 샀는데 없으면 추가.
				{
					ParseObject ownItems = new ParseObject(Global.PO_TABLE_OWNITEMS);
					ownItems.put(Global.PO_ITEM_KEY,   nItemType);
					ownItems.put(Global.PO_ITEM_COUNT, nItemAmount);
					ownItems.put(Global.PO_USER_ID, user);
					try {
						ownItems.save();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				
			}
		});
	}
	/**
	 * 돈을 현재 메모리와 서버 동기화 (메모리->서버)
	 */
	public void SyncPutMoney()
	{
		
		ParseUser user= ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query= ParseQuery.getQuery(Global.PO_TABLE_USERINFO);	// 유저 데이터를 찾는다.
		query.whereEqualTo(Global.PO_USER_ID, user);
		Log.d(":::::::::::ParseConnector:::::::::::", "111111111111");
		
		query.getFirstInBackground(new GetCallback<ParseObject>()
		{				
			@Override
			public void done(ParseObject PO, ParseException e)
			{	
				PO.put(Global.PO_MOENY, uInfo.GetGold());	// 메모리 -> 서버
				PO.saveInBackground();
				Log.d(":::::::::::ParseConnector:::::::::::", "22222222222222222");
			}
		});
	}
	
	/**
	 * 돈 동기화 (서버->메모리)
	 */
	public void SyncGetMoney()
	{
		ParseUser user= ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query= ParseQuery.getQuery(Global.PO_TABLE_USERINFO);	// 유저 데이터를 찾는다.
		query.whereEqualTo(Global.PO_USER_ID, user);
		try
		{
			ParseObject PO= query.getFirst();	
			uInfo.SetGold( PO.getInt(Global.PO_MOENY) );	// 서버 -> 메모리
			
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 아이템 동기화 (메모리->서버)
	 */
	public void SyncPutItems()
	{
		ParseUser user= ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query= ParseQuery.getQuery(Global.PO_TABLE_OWNITEMS);	// 유저 데이터를 찾는다.
		query.whereEqualTo(Global.PO_USER_ID, user);
		Log.d(":::::::::::ParseConnector:::::::::::", "aaaaaaaaaaaaaa");
		try
		{
			List<ParseObject> list= query.find();	// 쿼리를 포그라운드로 떼리고 프로그레스 돌게 한다.
			Log.d(":::::::::::ParseConnector:::::::::::", "bbbbbbbbbbbbbbb");
			for(ParseObject PO : list)
			{
				PO.delete();	// 로그인된 유저에 해당하는 아이템 전부 삭제 후
			}
			Log.d(":::::::::::ParseConnector:::::::::::", "cccccccccccccccc");
			
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		ArrayList<ItemData> items= uInfo.GetItems();	// 아이템 하나씩 돌면서 서버에 put 해준다.
		for(ItemData ITEM : items)
		{
			ParseObject ownItems = new ParseObject(Global.PO_TABLE_OWNITEMS);
			ownItems.put(Global.PO_ITEM_KEY,   ITEM.eType.ordinal());
			ownItems.put(Global.PO_ITEM_COUNT, ITEM.nCount);
			ownItems.put(Global.PO_USER_ID, user);
			ownItems.saveInBackground();
		}	
		
		Log.d(":::::::::::ParseConnector:::::::::::", "ddddddddddddddddd");
		
	}
	
	/**
	 * 아이템 동기화 (서버->메모리)
	 */
	public void SyncGetItems()
	{
		ArrayList<ItemData> items = new ArrayList<ItemData>();
		
		ParseUser user= ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query= ParseQuery.getQuery(Global.PO_TABLE_OWNITEMS);	// 유저 데이터를 찾는다.
		query.whereEqualTo(Global.PO_USER_ID, user);
		try
		{
			List<ParseObject> list= query.find();	// 쿼리를 포그라운드로 떼리고 프로그레스 돌게 한다.
			for(ParseObject PO : list)
			{
				ItemData data= new ItemData();
				data.nCount= PO.getInt(Global.PO_ITEM_COUNT);
				data.SetItemType( PO.getInt(Global.PO_ITEM_KEY) );
				items.add(data);	
			}
			
			
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		uInfo.SetItems(items);
	}
}
