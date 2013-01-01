package com.raimsoft.spacetrader.data;


public class UserInfo
{
	private static final UserInfo instance= new UserInfo();
	
	private boolean bLogined= false;				// 로그인 정보
	private EnumShip eShip= EnumShip.E_NULL_INFO;	// 함선 정보
	private int nGold= -1;
	private int nWorldMapX= -1;
	private int nWorldMapY= -1;
	private int nSystemMapPlanet= 1;
	private int nSystemMapPlanet_going= -1;
	
	
	public int getnSystemMapPlanet_going()
	{
		return nSystemMapPlanet_going;
	}

	public void setnSystemMapPlanet_going(int nSystemMapPlanet_going)
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

	public void SetShipType(EnumShip eShip)
	{
		this.eShip = eShip;
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

	public void SetSystemMapPlanet(int nSystemMapPlanet) {
		this.nSystemMapPlanet = nSystemMapPlanet;
	}
	
	
}
