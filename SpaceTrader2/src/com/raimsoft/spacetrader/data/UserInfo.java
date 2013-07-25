package com.raimsoft.spacetrader.data;

import java.util.ArrayList;

import android.content.ContentValues;

import com.raimsoft.spacetrader.obj.items.ItemData;



public class UserInfo
{
	/**
	 * @param arrItems
	 */
	private UserInfo()
	{
		this.arrItems = new ArrayList<ItemData>();
	}

	private static final UserInfo instance= new UserInfo();
	
	private boolean bLogined= false;				// 로그인 정보
	private EnumShip eShip= EnumShip.E_NULL_INFO;	// 함선 정보
	private String strShipName="UnNamed";
	private int nShipAtt= 0;
	private int nShipMaxHull= 0;	
	private float fVelocity= 13.0f;
	private float fHandling= 2.0f;
	
	private int nCurrHull= 0;					// hull
	private int nGold= -1;						// cyber money
	private int nWorldMapX= -1;					// 월드 X
	private int nWorldMapY= -1;					// 월드 Y
	private int nSystemMapPlanet= 1;			// 현재 위치 
	private int nSystemMapPlanet_going= -1;		// 갈 위치
	private int nDestinationDistance= -1;		// 목적지까지 거리
	
	private ArrayList<ItemData> arrItems;
	//private ArrayList<Planet> arrPlanet;
	
	
	/**
	 * 
	 * @param nItemType : 아이템 타입
	 * @param nCount : 카운트 (100개 이상넣으면 안됨!)
	 * @param nCurrPrice : 현재 구매 가격
	 * @return 1: 정상동작, -1:아이템꽉참
	 */
	public int BuyItems(int nItemType, int nCount)
	{		
		if(arrItems.size() == 20)	// 20개 이상 안됨.
			return -1;
		
		for(int i=0; i<arrItems.size(); ++i)	// 아이템 리스트 돌면서
		{
			if( arrItems.get(i).eType.ordinal() == nItemType)	// 같은 타입이 있으면
			{
				arrItems.get(i).nCount += nCount;	// 그 칸의 개수를 추가해줌
				return 1;
			}			
		}
		
		// 같은 타입 없으면 그냥 다음 칸에 추가.
		ItemData data= new ItemData();
		data.SetItemType(nItemType);
		data.nCount= nCount;
		arrItems.add(data);
		
		return 1;
	}
	
	public int SellItems(int nItemType, int nCount)
	{		
		for(int i=0; i<arrItems.size(); ++i)	// 아이템 리스트 돌면서
		{
			if( arrItems.get(i).eType.ordinal() == nItemType)	// 같은 타입이 있으면
			{
				arrItems.get(i).nCount -= nCount;	// 그 칸의 개수를 빼줌.
				
				if(arrItems.get(i).nCount == 0)	// 그 칸이 0개의 아이템이면 칸 자체를 지움.
					arrItems.remove(i);
				
				return 1;
			}			
		}
		
		return -1;	// 같은 타입이 없으면 거래 불가
	}
	
	/**
	 * 아이템들 가져옴
	 * @return
	 */
	public ArrayList<ItemData> GetItems()
	{
		return arrItems;
	}
	
	public int GetDestinationDistance()
	{
		return nDestinationDistance;
	}

	public void SetDestinationDistance(int nDestinationDistance)
	{
		this.nDestinationDistance = nDestinationDistance;
	}

	public int GetSystemMapPlanet_going()
	{
		return nSystemMapPlanet_going;
	}

	public void SetSystemMapPlanet_going(int nSystemMapPlanet_going)
	{
		this.nSystemMapPlanet_going = nSystemMapPlanet_going;
	}

	public static final UserInfo GetInstance()
	{
		return instance;
	}
	
	public EnumShip GetShipType()
	{
		return eShip;
	}
	
//	public String GetPlanetName()
//	{
//		return arrPlanet.get(nSystemMapPlanet).strName;
//	}
//	public int GetPlanetType()
//	{
//		return arrPlanet.get(nSystemMapPlanet).nPlanetType;
//	}
//	public Planet GetCurrentPlanet()
//	{
//		return arrPlanet.get(nSystemMapPlanet);
//	}
//	public void SetPlanets(ArrayList<Planet> _arrP)
//	{
//		arrPlanet= _arrP;
//	}
//	public void SetPlanets(Planet _P)
//	{
//		_P.type;
//	}

	public void SetShipType(EnumShip eShip)
	{
		this.eShip = eShip;
		
		switch (eShip.ordinal())
		{
		case 1:
			SetShipName("T-1");
			SetShipAtt(220);
			SetShipHull(3500);
			SetHandling(2.0f);
			SetVelocity(13.0f);
			break;

		case 2:
			SetShipName("T-2");
			SetShipAtt(280);
			SetShipHull(3200);
			SetHandling(2.3f);
			SetVelocity(14.0f);
			break;
		}
	}
	
	public void SetShipType(int nShip)
	{		
		switch (nShip)
		{
		case 1:
			this.eShip= EnumShip.E_TRAINING_SHIP_1;
			SetShipName("T-1");
			SetShipAtt(220);
			SetShipHull(3500);
			SetHandling(2.0f);
			SetVelocity(13.0f);
			break;

		case 2:
			this.eShip= EnumShip.E_TRAINING_SHIP_2;
			SetShipName("T-2");
			SetShipAtt(280);
			SetShipHull(3200);
			SetHandling(2.3f);
			SetVelocity(14.0f);
			break;
		}
	}

	public boolean GetLogin()
	{
		return bLogined;
	}
	
	public void SetLogin(boolean _b)
	{
		bLogined= _b;
	}
	
	public int GetGold()
	{
		return nGold;
	}
	
	public void SetGold(int _n)
	{
		nGold= _n;
	}
	
	public void AddGold(int _n)
	{
		nGold += _n;
	}

	public int GetWorldMapX() {
		return nWorldMapX;
	}

	public void SetWorldMapX(int nWorldMapX) {
		this.nWorldMapX = nWorldMapX;
	}

	public int GetWorldMapY() {
		return nWorldMapY;
	}

	public void SetWorldMapY(int nWorldMapY) {
		this.nWorldMapY = nWorldMapY;
	}

	public int GetSystemMapPlanet() {
		return nSystemMapPlanet;
	}

	public void SetSystemMapPlanet(int nSystemMapPlanet)
	{
		this.nSystemMapPlanet = nSystemMapPlanet;
	}

	public String GetShipName() {
		return strShipName;
	}

	public void SetShipName(String strShipName) {
		this.strShipName = strShipName;
	}

	public int GetShipAtt() {
		return nShipAtt;
	}

	public void SetShipAtt(int nShipAtt) {
		this.nShipAtt = nShipAtt;
	}

	public int GetShipHull() {
		return nShipMaxHull;
	}

	public void SetShipHull(int nShipHull) {
		this.nShipMaxHull = nShipHull;
	}

	public float GetVelocity() {
		return fVelocity;
	}

	public void SetVelocity(float fVelocity) {
		this.fVelocity = fVelocity;
	}

	public float GetHandling() {
		return fHandling;
	}

	public void SetHandling(float fHandling) {
		this.fHandling = fHandling;
	}

	public int GetCurrHull() {
		return nCurrHull;
	}

	public void SetCurrHull(int nCurrHull) {
		this.nCurrHull = nCurrHull;
	}
	
	
}

