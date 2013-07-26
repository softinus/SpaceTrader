package com.raimsoft.spacetrader.data;


public class Global
{

	public static final String LAUNCH_TARGET="AM"; // TS(티스토어), AM(안드로이드마켓), DEV(개발버전)
	public static final String LOG_TAG="SpaceTrader";
	
	public static final String DB_NAME= "spacetrader.db";			// DB 이름
	//public static final String DB_TABLE_MYINFO= "my_info";		// DB TABLE 이름
	public static final String DB_TABLE_ITEMSINFO= "items_info";		// DB TABLE 이름
	public static final int DB_VERSION= 1;						// DB버전
	
	// 아래는 Shared Preference 예약어
	public static final String SP_LOGIN_SUCCESS="login";
	public static final String SP_LOGIN_ID="login_id";
	public static final String SP_LOGIN_PW="login_pw";
	public static final String SP_MONEY="money";
	
	// 아래는 Parse 통신 예약어
	
	public static final String PO_TABLE_OWNITEMS="Own_Items";
	public static final String PO_ITEM_COUNT= "item_count";
	public static final String PO_ITEM_KEY= "item_key";
	
	
	public static final String PO_TABLE_OWNSKILLS="Own_Skills";
	
	public static final String PO_TABLE_USERINFO="UserInfo";
	public static final String PO_USER_ID="user_id";
	public static final String PO_MOENY="gold";
	public static final String PO_SHIP_TYPE="ship_type";
	public static final String PO_SHIP_HULL="ship_hull";
	public static final String PO_CROOD_WORLD_X="crood_world_x";
	public static final String PO_CROOD_WORLD_Y="crood_world_y";
	public static final String PO_CROOD_SYSTEM_MAP_PLANET="system_map_planet";
	
	
//	public static final String GOOGLE_AD_ID = "a14f34da9ed5c1f";
//	public static final String DAUM_AD_ID = "1d02Z6oT134d197e7da";
//	
//    public static final int CAULY_CHARGEABLE_AD = 0;	    //유료 광고일때
//    public static final int CAULY_NON_CHARGEABLE_AD = 100;	    //무료 광고일때
//    public static final int CAULY_INTERSTITIAL_NO_FILLED_AD = 200;	    //전면광고 없음
//    public static final int CAULY_INVALID_APPCODE = 400;	    //앱코드 불일치
//    public static final int CAULY_SERVER_CONNECTION_ERROR = 500;	    //카울리 서버 에러
//    public static final int CAULY_INTERSTITIAL_NOT_PERMITED = -200;	    //전면광고 최소 요청 주기 미만.
//    public static final int CAULY_SDK_ERROR = -100;	    //SDK 에러.
}
