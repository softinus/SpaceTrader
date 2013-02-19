package com.raimsoft.spacetrader.data;

import java.util.ArrayList;

import com.raimsoft.spacetrader.obj.Planet;


public class UserInfo
{
	private static final UserInfo instance= new UserInfo();
	
	private boolean bLogined= false;				// 로그인 정보
	private EnumShip eShip= EnumShip.E_NULL_INFO;	// 함선 정보
	private String strShipName="UnNamed";
	private int nShipAtt= 0;
	private int nShipHull= 0;
	private float fVelocity= 13.0f;
	private float fHandling= 2.0f;
	
	private int nCurrHull= 0;					// hull
	private int nGold= -1;						// cyber money
	private int nWorldMapX= -1;					// 월드 X
	private int nWorldMapY= -1;					// 월드 Y
	private int nSystemMapPlanet= 1;			// 현재 위치 
	private int nSystemMapPlanet_going= -1;		// 갈 위치
	
	//private ArrayList<Planet> arrPlanet;
	
	
	public int getnSystemMapPlanet_going()
	{
		return nSystemMapPlanet_going;
	}

	public void setSystemMapPlanet_going(int nSystemMapPlanet_going)
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
		return nShipHull;
	}

	public void SetShipHull(int nShipHull) {
		this.nShipHull = nShipHull;
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

