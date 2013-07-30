package com.raimsoft.spacetrader.scene;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import bayaba.engine.lib.ButtonObject;
import bayaba.engine.lib.ButtonType;
import bayaba.engine.lib.Font;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;
import bayaba.engine.lib.Sprite;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.raimsoft.spacetrader.R;
import com.raimsoft.spacetrader.data.EnumShip;
import com.raimsoft.spacetrader.data.Global;
import com.raimsoft.spacetrader.data.UserInfo;
import com.raimsoft.spacetrader.obj.GameButton;
import com.raimsoft.spacetrader.obj.items.BaseItem;
import com.raimsoft.spacetrader.obj.items.EItems;
import com.raimsoft.spacetrader.obj.items.ItemData;
import com.raimsoft.spacetrader.util.GenConst;
import com.raimsoft.spacetrader.util.ParseConnector;
import com.raimsoft.spacetrader.util.PlanetNameMaker;

public class SStation extends SBase
{
	private ParseConnector PC= new ParseConnector();
	
	private final int SHOP_ITEM_COUNT= 10;
	private final int IVEN_ITEM_COUNT= 20;
	
	private final static int E_NONE= 0;
	private final static int E_INFO= 1;
	private final static int E_NEWS= 2;
	private final static int E_TRADE=3;
	private final static int E_MANAGE=4;
	private final static int E_EXIT= 5;
	

	public SStation(Context mContext, GameInfo gInfo)
	{
		super(mContext, gInfo);		

		for(int i=0; i<30; ++i)
		{
			btnItemsBackground[i]= new GameButton();
		}
	}
	
	private int  nX= (int) (gInfo.ScreenX/2);
	private int  nY= (int) (gInfo.ScreenY/2);
	
	//private DBManager DBMgr;
	
	private int PLANET_TYPES= 4;
	private int nCurrPlanetType= -1;
	
	private PlanetNameMaker PNM;	
	private UserInfo uInfo;
	private GenConst GC= new GenConst();
	
	private Sprite bg_station= new Sprite();
	private Sprite sprStationButton= new Sprite();
	private Sprite sprHexagon= new Sprite();
	private Sprite sprStationUI_PANEL= new Sprite();
//	private Sprite sprStationUI_NEWS= new Sprite();
//	private Sprite sprStationUI_TRADE= new Sprite();
//	private Sprite sprStationUI_MANAGE= new Sprite();
	private Sprite sprPlanets= new Sprite();
	private Sprite sprShip1= new Sprite();
	private Sprite sprProgress= new Sprite();			// 
	private Sprite sprItemButton= new Sprite();			// 아이템 버튼
	private Sprite sprPower= new Sprite();				// 파워 버튼
	private Sprite sprItems= new Sprite();				// 아이템 리스트 (png에 있는 순대로)
	private Sprite sprCashPanel= new Sprite();			// 골드 보여주는 패널 	
	private Sprite sprTradePanel= new Sprite();			// 거래창 패널	
	
	
	private GameButton btnInfo= new GameButton();
	private GameButton btnNews= new GameButton();
	private GameButton btnTrade= new GameButton();
	private GameButton btnManage= new GameButton();
	private GameButton btnExit= new GameButton();
	private GameObject objPlanet= new GameObject();
	private GameObject objShip= new GameObject();
	private BaseItem[] arrShopItems= new BaseItem[SHOP_ITEM_COUNT];	// 샵 아이템
	private BaseItem[] arrInvenItems= new BaseItem[IVEN_ITEM_COUNT];	// 인벤토리	

	private GameButton[] btnItemsBackground= new GameButton[SHOP_ITEM_COUNT+IVEN_ITEM_COUNT];
	private GameButton btnPower= new GameButton();
	
	private GameButton prgBG1= new GameButton();	// 체력바 배경
	private GameButton prgBG2= new GameButton();	// 쉴드바 배경
	private GameButton prgBG3= new GameButton();	// 연료바 배경
	private ButtonObject prgHull= new ButtonObject();	// 체력프로그레스
	private ButtonObject prgShield= new ButtonObject();	// 쉴드프로그레스
	private ButtonObject prgFuel= new ButtonObject();	// 연료프로그레스
	private GameObject objHexagon= new GameObject();
	private GameObject objCashPanel= new GameObject();	// 골드 보여주는 패널

	private GameObject objTradePanelBackground= new GameObject();	// 거래 패널 배경
	private GameObject objArrowBuy= new GameObject();				// 구매 화살표
	private GameObject objArrowSell= new GameObject();				// 판매 화살표
	private GameObject objTradeFieldAmount= new GameObject();		// 거래 양
	private GameObject objTradeFieldPay= new GameObject();			// 지불액수
	private GameButton btnTradeButtonZero= new GameButton();			// 거래 버튼
	private GameButton btnTradeButtonP1= new GameButton();			// 거래 버튼
	private GameButton btnTradeButtonP10= new GameButton();			// 거래 버튼
	private GameButton btnTradeButtonMAX= new GameButton();			// 거래 버튼
	private GameButton btnTradeButtonM1= new GameButton();			// 거래 버튼
	private GameButton btnTradeButtonM10= new GameButton();			// 거래 버튼
	private GameButton btnTradeButtonTrade= new GameButton();			// 거래 버튼
	private ButtonObject prgTradeWeight= new ButtonObject();		// 무게창
	private GameObject objTradeTarget= new GameObject();			// 거래 대상 함선 그림
	private GameObject objTradeItem= new GameObject();					// 거래 중인 물품
	
	
	private MediaPlayer Music;
	
	private Font font = new Font();
	private int m_nMenu= 0;	
	private boolean m_bTrading= false;	// 거래중인지?
	private boolean m_bBuying= true;	// 사는중인지?
	private ItemData m_TradingItemData= null;
	private int m_nTradeAmount= 0;
	
	
	@Override
	public void LoadData()
	{
		super.LoadData();
		

		
		uInfo= UserInfo.GetInstance();
		PNM= new PlanetNameMaker();
		
		Music = MediaPlayer.create(mContext, R.raw.station2);
		Music.setLooping(true);
		Music.start();
		
		//float fConst= GC.GetPositionTimeConstF();		
		arrShopItems[0]= new BaseItem(mContext, gl, EItems.E_BOX, GC.GetPositionTimeConstF(0));
		arrShopItems[1]= new BaseItem(mContext, gl, EItems.E_MATERIAL, GC.GetPositionTimeConstF(1));
		
		this.InventoryRefresh();	// DB에서 아이템 새로 가져옴
		
		/// 스테이션 정보
		sprPlanets.LoadSprite(gl, mContext, R.drawable.planets, "planets.spr");
		//objPlanet= uInfo.GetCurrentPlanet();
		
		nCurrPlanetType= (int) GC.GetPositionConstF(uInfo.GetSystemMapPlanet(), PLANET_TYPES);
		objPlanet.SetObject(sprPlanets, nCurrPlanetType, 0, 135, 230,  nCurrPlanetType, 0);//objPlanet.show= true; objPlanet.x= 200; objPlanet.y= 200; objPlanet.scalex= 0.75f; objPlanet.scaley= 0.75f;
		objPlanet.scalex= 0.50f;	objPlanet.scaley= 0.50f;
		
		sprPlanets.LoadSprite(gl, mContext, R.drawable.planets, "planets.spr");
		///==
		
		/// 함선 정보
		if(uInfo.GetShipType() == EnumShip.E_TRAINING_SHIP_1)
			sprShip1.LoadSprite(gl, mContext, R.drawable.resource_2, "ship_1.spr");
		else if(uInfo.GetShipType() == EnumShip.E_TRAINING_SHIP_2)
			sprShip1.LoadSprite(gl, mContext, R.drawable.resource_2, "ship_2.spr");
		
		objTradeTarget.SetObject(sprShip1, 0, 0, 65, 460, 0, 0);	// 거래창에서 거래 대상 함선
		
		objShip.SetObject(sprShip1, 0, 0, 140, 275, 0, 0);
		sprProgress.LoadSprite(gl, mContext, R.drawable.progress, "progress_station.spr");
		
		prgBG1.SetButton(mContext, sprProgress, 240, 520, 0);
		prgBG2.SetButton(mContext, sprProgress, 240, 590, 0);
		prgBG3.SetButton(mContext, sprProgress, 240, 660, 0);
		prgHull.SetButton(sprProgress, ButtonType.TYPE_PROGRESS, 0, 240, 520, 1);
		prgShield.SetButton(sprProgress, ButtonType.TYPE_PROGRESS, 0, 240, 590, 2);
		prgFuel.SetButton(sprProgress, ButtonType.TYPE_PROGRESS, 0, 240, 660, 3);
		
		prgHull.SetText(0, 140, 3, 0.75f, 0.75f, 0.75f, 22f, uInfo.GetCurrHull()+" / "+uInfo.GetShipHull());
		prgHull.energy= ((float)uInfo.GetCurrHull() / (float)uInfo.GetShipHull()) * 100.0f;
		
		prgShield.SetText(0, 140, 3, 0.75f, 0.75f, 0.75f, 22f, "100 / 100");
		prgShield.energy= 100;
		
		prgFuel.SetText(0, 140, 3, 0.75f, 0.75f, 0.75f, 22f, "100 / 100");
		prgFuel.energy= 100;
		
		Log.d("Hull Progress1", Float.toString(uInfo.GetCurrHull()));
		Log.d("Hull Progress2", Float.toString(uInfo.GetShipHull()));
		Log.d("Hull Progress3", Float.toString(uInfo.GetCurrHull() / uInfo.GetShipHull()));
		Log.d("Hull Progress4", Float.toString(prgHull.energy));
		///==
		
		
		/// 스테이션 메뉴
		bg_station.LoadSprite(gl, mContext, R.drawable.station_bg, "station_ui_bg.spr");
		sprStationButton.LoadSprite(gl, mContext, R.drawable.buttons_2, "station_buttons.spr");
		sprStationUI_PANEL.LoadSprite(gl, mContext, R.drawable.station_ui_2, "station_ui_2.spr");
		
		btnInfo.SetButton(mContext, sprStationButton, 240, 300, 0);
		btnInfo.SetTextCenter(32f, "스테이션 정보");
		
		btnNews.SetButton(mContext, sprStationButton, 240, 400, 0);
		btnNews.SetTextCenter(32f, "월드 뉴스");
		
		btnTrade.SetButton(mContext, sprStationButton, 240, 500, 0);
		btnTrade.SetTextCenter(32f, "상품 거래");
		
		btnManage.SetButton(mContext, sprStationButton, 240, 600, 0);
		btnManage.SetTextCenter(32f, "함선 관리");
		
		btnExit.SetButton(mContext, sprStationButton, 240, 700, 0);
		btnExit.SetTextCenter(32f, "스테이션 나가기");	
		///==
		
		sprHexagon.LoadSprite(gl, mContext, R.drawable.buttons_2, "station_hexagon.spr");
		objHexagon.SetObject(sprHexagon, 0, 0, 135, 230, 0, 0);
		//objHexagon.scalex= 1.10f; objHexagon.scaley= 1.10f;
		
		
		/// 상점 메뉴
		sprItemButton.LoadSprite(gl, mContext, R.drawable.buttons_2, "btn_item.spr");
		for(int i=0; i<10; ++i)
		{
			int nRow= (int)(i/5);
			int nCol= i%5;
			btnItemsBackground[i].SetButton(mContext, sprItemButton, 85+nCol*80, 190+nRow*80, 0);
			//arrItems[i].SetObject(sprItems, 0, 0, 85+nCol*80, 190+nRow*80, 0, 0);
		}
		for(int i=10; i<30; ++i)
		{
			int nRow= (int)(i/5);
			int nCol= i%5;
			btnItemsBackground[i].SetButton(mContext, sprItemButton, 85+nCol*80, 255+nRow*80, 0);
			//arrItems[i].SetObject(sprItems, 0, 0, 85+nCol*80, 255+nRow*80, 0, 0);
		}
		
		sprItems.LoadSprite(gl, mContext, R.drawable.list_items, "list_items.spr");		// 아이템들 그림 불러옴.
		for(int i=0; i<arrInvenItems.length; ++i)	// 인벤토리 아이템 리스트 돌면서
		{
			if(arrInvenItems[i]==null)
				break;
			
			int nRow= (int)(i/5);
			int nCol= i%5;
			arrInvenItems[i].SetObject(sprItems, 0, 0, 85+nCol*80, 415+nRow*80, 0, arrInvenItems[i].itemData.eType.ordinal());
		}
		
		for(int i=0; i<arrShopItems.length; ++i)	// 샵 아이템 리스트 돌면서
		{
			if(arrShopItems[i]==null)
				break;
			
			int nRow= (int)(i/5);
			int nCol= i%5;
			arrShopItems[i].SetObject(sprItems, 0, 0, 85+nCol*80, 190+nRow*80, 0, arrShopItems[i].itemData.eType.ordinal());
		}
		///==
		
		sprPower.LoadSprite(gl, mContext, R.drawable.buttons_2, "btn_power.spr");
		btnPower.SetButton(mContext, sprPower, 415, 50, 0);
		
		sprCashPanel.LoadSprite(gl, mContext, R.drawable.buttons_2, "station_ui_cash.spr");
		objCashPanel.SetObject(sprCashPanel, 0, 0, 35, 725, 0, 0);

		///== 거래 메뉴
		sprTradePanel.LoadSprite(gl, mContext, R.drawable.station_ui_shop, "station_ui_shop.spr");
		objTradePanelBackground.SetObject(sprTradePanel, 0, 0, nX, nY, 0, 0);
		objArrowBuy.SetObject(sprTradePanel, 0, 0, 60, 350, 1, 0);
		objArrowSell.SetObject(sprTradePanel, 0, 0, 85, 400, 2, 0);
		objTradeFieldAmount.SetObject(sprTradePanel, 0, 0, 235, 400, 5, 0);
		objTradeFieldPay.SetObject(sprTradePanel, 0, 0, 235, 435, 5, 0);
		
		btnTradeButtonZero.SetButton(mContext, sprTradePanel, 180, 290, 3,4);
		btnTradeButtonP1.SetButton(mContext, sprTradePanel, 290, 290, 3,4);
		btnTradeButtonP10.SetButton(mContext, sprTradePanel, 400, 290, 3,4);
		btnTradeButtonM1.SetButton(mContext, sprTradePanel, 180, 335, 3,4);
		btnTradeButtonM10.SetButton(mContext, sprTradePanel, 290, 335, 3,4);
		btnTradeButtonMAX.SetButton(mContext, sprTradePanel, 400, 335, 3,4);
		btnTradeButtonTrade.SetButton(mContext, sprTradePanel, 400, 420, 3,4);
		
		btnTradeButtonTrade.scaley= 1.8f;
		///==
	}
	@Override
	public void Render()
	{
		super.Render();
		
		bg_station.PutImage(gInfo, 0, 0);

		
		font.BeginFont();
		switch (m_nMenu)
		{
		case SStation.E_NONE:	// 메뉴상태
			btnInfo.DrawButtonWithText(gInfo, gl, font);
			btnNews.DrawButtonWithText(gInfo, gl, font);
			btnTrade.DrawButtonWithText(gInfo, gl, font);
			btnManage.DrawButtonWithText(gInfo, gl, font);
			btnExit.DrawButtonWithText(gInfo, gl, font);
			break;
		case SStation.E_INFO:	// 스테이션 정보
			sprStationUI_PANEL.PutAni(gInfo, nX, nY, 0, 0);
			sprStationUI_PANEL.PutAni(gInfo, 40, 100, 1, 0);
			sprStationUI_PANEL.PutAni(gInfo, 250, 100, 2, 0);
			sprStationUI_PANEL.PutAni(gInfo, 40, 400, 3, 0);
			objHexagon.DrawSprite(gInfo);
			font.DrawFont(gl, 255, 165, 16f, PNM.GetCurrPlanetName(uInfo.GetSystemMapPlanet()));
			font.DrawFont(gl, 255, 195, 24f, "("+uInfo.GetWorldMapX()+","+uInfo.GetWorldMapY()+"):"+uInfo.GetSystemMapPlanet() );
			//font.DrawFont(gl, 380, 400, 28f, uInfo.GetPlanetName());
			
			objPlanet.DrawSprite(gInfo);
			btnPower.DrawSprite(gInfo);
			
			break;
		case SStation.E_NEWS:	//월드 뉴스	
			sprStationUI_PANEL.PutAni(gInfo, nX, nY, 0, 0);
			sprStationUI_PANEL.PutAni(gInfo, 40, 100, 4, 0);
			sprStationUI_PANEL.PutAni(gInfo, 40, 320, 5, 0);
			btnPower.DrawSprite(gInfo);
			break;
		case SStation.E_TRADE:	//상품 거래
			

			
			sprStationUI_PANEL.PutAni(gInfo, nX, nY, 0, 0); 
			sprStationUI_PANEL.PutAni(gInfo, 40, 100, 6, 0);
			sprStationUI_PANEL.PutAni(gInfo, 40, 320, 7, 0);
			btnPower.DrawSprite(gInfo);
			for(GameButton BTN : btnItemsBackground)
			{
				if(BTN.pattern!=null)
					BTN.DrawSprite(gInfo);
			}
			for(BaseItem ITEMS : arrInvenItems)
			{
				if(ITEMS == null || ITEMS.pattern==null) 
					continue;
				
//				if(ITEMS.itemData.nCount==0)
//					continue;
				
				ITEMS.scalex= 0.5f;
				ITEMS.scaley= 0.5f;
				ITEMS.DrawSprite(gInfo);
				
				float fXFactor=0f;
				
				if(ITEMS.itemData.nCount==100)
					fXFactor= 0.1f;
				else if(ITEMS.itemData.nCount >= 10)	
					fXFactor= 5f;
				else
					fXFactor= 17f;
				
				font.DrawFont(gl, ITEMS.x+fXFactor, ITEMS.y+5.5f, 22.5f, Integer.toString(ITEMS.itemData.nCount) );
			}
			for(BaseItem ITEMS : arrShopItems)
			{
				if(ITEMS != null)
				{
					ITEMS.scalex= 0.5f;
					ITEMS.scaley= 0.5f;
					ITEMS.DrawSprite(gInfo);
					
					float fXFactor=0f;
					
					if(ITEMS.itemData.nCurrentPrice>=100)
						fXFactor= -5f;
					else if(ITEMS.itemData.nCurrentPrice >= 10)	
						fXFactor= 0f;
					else
						fXFactor= 5f;
					
					font.DrawFont(gl, ITEMS.x+fXFactor, ITEMS.y+5.5f, 22.5f, "$"+ITEMS.itemData.nCurrentPrice );
					
				}
			}
			
			if(m_bTrading)	// 거래중이면
			{
				objTradePanelBackground.DrawSprite(gInfo);
				
				if(m_bBuying)
					objArrowBuy.DrawSprite(gInfo);
				else
					objArrowSell.DrawSprite(gInfo);
				
				objTradeFieldAmount.DrawSprite(gInfo);
				objTradeFieldPay.DrawSprite(gInfo);
				
				btnTradeButtonTrade.DrawButtonWithText(gInfo, gl, font);
				btnTradeButtonP1.DrawButtonWithText(gInfo, gl, font);
				btnTradeButtonP10.DrawButtonWithText(gInfo, gl, font);
				btnTradeButtonM1.DrawButtonWithText(gInfo, gl, font);
				btnTradeButtonM10.DrawButtonWithText(gInfo, gl, font);
				btnTradeButtonMAX.DrawButtonWithText(gInfo, gl, font);
				btnTradeButtonZero.DrawButtonWithText(gInfo, gl, font);
				
				objTradeTarget.scalex= 0.65f;
				objTradeTarget.scaley= 0.65f;
				objTradeTarget.DrawSprite(gInfo);
				
				objTradeItem.scalex= 0.6f;
				objTradeItem.scaley= 0.6f;
				objTradeItem.DrawSprite(gInfo);
				
				font.DrawFont(gl, 135, 208, 30.0f, m_TradingItemData.strItemName + " ( $ "+ m_TradingItemData.nCurrentPrice +" )");
				font.DrawColorFont(gl, 225, 390, 0.1f, 0.1f, 0.1f, 23.0f, "x"+m_nTradeAmount);
				font.DrawColorFont(gl, 225, 425, 0.1f, 0.1f, 0.1f, 23.0f, "$"+m_TradingItemData.nCurrentPrice * m_nTradeAmount);
				
			}
			
			break;
		case SStation.E_MANAGE:	//함선 관리
			sprStationUI_PANEL.PutAni(gInfo, nX, nY, 0, 0);
			sprStationUI_PANEL.PutAni(gInfo, 40, 100, 8, 0);
			sprStationUI_PANEL.PutAni(gInfo, 250, 100, 9, 0);
			sprStationUI_PANEL.PutAni(gInfo, 40, 320, 10, 0);
			//font.DrawFont(gl, 95, 100, 24f, "현재 함선");			
			font.DrawFont(gl, 290, 145, 20f, "함선명 : "+uInfo.GetShipName());
			font.DrawFont(gl, 290, 170, 20f, "공격력 : "+uInfo.GetShipAtt());
			font.DrawFont(gl, 290, 195, 20f, "내구도 : "+uInfo.GetShipHull());
			font.DrawFont(gl, 290, 220, 20f, "핸들링 : "+uInfo.GetHandling());
			font.DrawFont(gl, 290, 245, 20f, "스피드 : "+uInfo.GetVelocity());
			//font.DrawFont(gl, 200, 435, 24f, "함선 정비");
			
			prgBG1.DrawSprite(gInfo);
			prgBG2.DrawSprite(gInfo);
			prgBG3.DrawSprite(gInfo);
			
			prgHull.DrawSprite(gl,0, gInfo, font);
			prgFuel.DrawSprite(gl,0, gInfo, font);
			prgShield.DrawSprite(gl,0, gInfo, font);
			objShip.DrawSprite(gInfo);
			btnPower.DrawSprite(gInfo);
			
			font.DrawFont(gl, 65, 510, 14f, "내구도");
			font.DrawFont(gl, 65, 580, 14f, "보호막");
			font.DrawFont(gl, 65, 650, 14f, "연료통");
			
			break;

		default:
			break;
		}
		
		if(m_nMenu!=0)
		{
			objCashPanel.DrawSprite(gInfo);
			font.DrawFont(gl, 265, 735, 34.5f, Integer.toString(uInfo.GetGold()) );
		}
		
		font.EndFont();
		
		
		
		
	}
	@Override
	public void Update() 
	{
		super.Update();
		
		if(m_nMenu==SStation.E_MANAGE)	// 관리 상태이면
		{
			prgBG1.ButtonUpdate(0.0f);
			prgBG2.ButtonUpdate(0.0f);
			prgBG3.ButtonUpdate(0.0f);
			
			if(prgBG1.CheckOver())
			{
				uInfo.SetCurrHull(uInfo.GetShipHull());
				prgHull.SetText(0, 140, 3, 0.75f, 0.75f, 0.75f, 22f, uInfo.GetCurrHull()+" / "+uInfo.GetShipHull());
				prgHull.energy= ((float)uInfo.GetCurrHull() / (float)uInfo.GetShipHull()) * 100.0f;
			}
			else if(prgBG2.CheckOver())
			{
			}
			else if(prgBG3.CheckOver())
			{
			}
		}		
		else if(m_nMenu==SStation.E_TRADE)	// 트레이드 상태이면
		{
			if(m_bTrading)	// 거래중이면
			{
				btnTradeButtonTrade.ButtonUpdate(0.0f);
				btnTradeButtonP1.ButtonUpdate(0.0f);
				btnTradeButtonP10.ButtonUpdate(0.0f);
				btnTradeButtonM1.ButtonUpdate(0.0f);
				btnTradeButtonM10.ButtonUpdate(0.0f);
				btnTradeButtonMAX.ButtonUpdate(0.0f);
				btnTradeButtonZero.ButtonUpdate(0.0f);
				
				btnTradeButtonTrade.SetTextColor(0.1f, 0.1f, 0.1f);
				btnTradeButtonP1.SetTextColor(0.1f, 0.1f, 0.1f);
				btnTradeButtonP10.SetTextColor(0.1f, 0.1f, 0.1f);
				btnTradeButtonM1.SetTextColor(0.1f, 0.1f, 0.1f);
				btnTradeButtonM10.SetTextColor(0.1f, 0.1f, 0.1f);
				btnTradeButtonMAX.SetTextColor(0.1f, 0.1f, 0.1f);
				btnTradeButtonZero.SetTextColor(0.1f, 0.1f, 0.1f);
				
				if(m_bBuying)
					btnTradeButtonTrade.SetText(-25, -17, 27.0f, "BUY");
				else
					btnTradeButtonTrade.SetText(-28, -17, 24.0f, "SELL");
				btnTradeButtonP1.SetText(-9, -12, 21.0f, "+1");
				btnTradeButtonP10.SetText(-16, -12, 21.0f, "+10");
				btnTradeButtonM1.SetText(-9, -12, 21.0f, "-1");
				btnTradeButtonM10.SetText(-16, -12, 21.0f, "-10");
				btnTradeButtonMAX.SetText(-21, -12, 21.0f, "MAX");
				btnTradeButtonZero.SetText(-5, -12, 21.0f, "0");

				
				
				if(btnTradeButtonTrade.CheckOver())
				{
					m_bTrading= false;
					
					if(m_bBuying)					
					{
						uInfo.BuyItems(m_TradingItemData.eType.ordinal(), m_nTradeAmount);
						uInfo.SetGold( uInfo.GetGold() - (m_nTradeAmount * m_TradingItemData.nCurrentPrice) );						
						
						PC.SyncTradeItem(true, -1*(m_nTradeAmount * m_TradingItemData.nCurrentPrice), m_TradingItemData.eType.ordinal(), m_nTradeAmount);
						m_nTradeAmount= 0;
					}
					else
					{
						uInfo.SellItems(m_TradingItemData.eType.ordinal(), m_nTradeAmount);
						uInfo.SetGold( uInfo.GetGold() + (m_nTradeAmount * m_TradingItemData.nCurrentPrice) );
						
						PC.SyncTradeItem(false, (m_nTradeAmount * m_TradingItemData.nCurrentPrice), m_TradingItemData.eType.ordinal(), -1*m_nTradeAmount);
						m_nTradeAmount= 0;
					}
					
					this.InventoryRefresh();
				}

				if(btnTradeButtonP1.CheckOver())
				{
					if(m_bBuying)					
					{
						if(m_TradingItemData.nCurrentPrice * (m_nTradeAmount+1) <= uInfo.GetGold())
							m_nTradeAmount += 1;
					}
					else
					{
						if( m_TradingItemData.nCount >= m_nTradeAmount+1 )
							m_nTradeAmount += 1;
					}
				}
				
				if(btnTradeButtonP10.CheckOver())
				{					
					if(m_bBuying)					
					{
						if(m_TradingItemData.nCurrentPrice * (m_nTradeAmount+10) <= uInfo.GetGold())
							m_nTradeAmount += 10;
					}
					else
					{
						if( m_TradingItemData.nCount >= m_nTradeAmount+10 )
							m_nTradeAmount += 10;
					}
				}
				
				if(btnTradeButtonM1.CheckOver())
				{
					if(m_nTradeAmount-1 >= 0)
						m_nTradeAmount -= 1;
				}
				
				if(btnTradeButtonM10.CheckOver())
				{
					if(m_nTradeAmount-1 >= 0)
						m_nTradeAmount -= 10;
				}
				
				if(btnTradeButtonMAX.CheckOver())
				{
					if(m_bBuying)					
					{
						m_nTradeAmount= (int) (uInfo.GetGold() / m_TradingItemData.nCurrentPrice);
					}
					else
					{
						m_nTradeAmount= m_TradingItemData.nCount;
					}
				}
				
				if(btnTradeButtonZero.CheckOver())
					m_nTradeAmount= 0;
				
				return;
			}
				
			for(int i=0; i<SHOP_ITEM_COUNT; ++i)	// 샵 아이템들 돌면서
			{
				if(btnItemsBackground[i].CheckOver())	// 아이템 버튼 눌렀으면
				{
					if(arrShopItems[i]==null)
						continue;
					
					m_TradingItemData= arrShopItems[i].itemData;
					objTradeItem.SetObject(sprItems, 0, 0, 60, 250, 0, m_TradingItemData.eType.ordinal());
					
					
					m_bTrading= true;
					m_bBuying= true;
				}
			}
			
			for(int i=SHOP_ITEM_COUNT; i<btnItemsBackground.length; ++i)	// 내 아이템에 대해서
			{
				if(btnItemsBackground[i].CheckOver())	// 아이템 버튼 눌렀으면
				{
					if(arrInvenItems[i-SHOP_ITEM_COUNT]==null)	// 아이템이 없으면 넘어감
						continue;
					
					m_TradingItemData= arrInvenItems[i-SHOP_ITEM_COUNT].itemData;
					m_TradingItemData.nCurrentPrice= arrShopItems[m_TradingItemData.eType.ordinal()].itemData.nCurrentPrice;
					objTradeItem.SetObject(sprItems, 0, 0, 60, 250, 0, m_TradingItemData.eType.ordinal());
					
					m_bTrading= true;
					m_bBuying= false;
				}
			}
			
//			for(int i=SHOP_ITEM_COUNT; i<btnItemsBackground.length; ++i)	// 내 아이템에 대해서
//			{
//				if(btnItemsBackground[i].CheckOver())	// 버튼이 눌렸음
//				{
//					if(arrInvenItems[i-SHOP_ITEM_COUNT]==null)	// 아이템이 없으면 넘어감
//						continue;
//					
//					arrInvenItems[i-SHOP_ITEM_COUNT].CheckSettingInventory(true);	// 누른거 체크
//
//					
//					for(int j=0; j<arrInvenItems.length; ++j)	// 인벤토리 목록 돌면서
//					{
//						BaseItem currItem= arrInvenItems[j]; 
//						
//						if(currItem==null)	//아이템없으면 빼고
//							continue;
//						
//						if(currItem.bLastCheck)	// 체크되면.. 해당 아이템 추가
//						{
//							//DBMgr.RemoveItem(j, 1);
//							this.InventoryRefresh();	// 아이템창 갱신
//						}
//						
//						if(i-SHOP_ITEM_COUNT==j)	// 아까 누른거 빼고
//							continue;
//						
//						
//													
//						currItem.CheckSettingInventory(false);							
//					}
//				}
//			}
			
			
			
			for(GameButton BTN : btnItemsBackground)	// 아이템 배경버튼들 업데이트
				BTN.ButtonUpdate(0);
		}
		
		if(m_nMenu!=0)
			btnPower.ButtonUpdate(0);
		
		if(btnPower.CheckOver())
			m_nMenu= 0;
		
		
		if(m_nMenu!=0)	// 메뉴선택중 아니면
			return;
		
		btnInfo.ButtonUpdate(0);
		btnNews.ButtonUpdate(0);
		btnTrade.ButtonUpdate(0); 
		btnManage.ButtonUpdate(0);
		btnExit.ButtonUpdate(0);
		
		
		if(btnInfo.CheckOver())
		{
			m_nMenu= 1;
		}
		else if(btnNews.CheckOver())
		{
			m_nMenu= 2;
		}
		else if(btnTrade.CheckOver())
		{
			m_nMenu= 3;
		}		
		else if(btnManage.CheckOver())
		{
			m_nMenu= 4;
		}
		else if(btnExit.CheckOver())
		{
			SetScene(EnumScene.E_GAME_SYSTEMMAP);
		}
	}
	
	protected void InventoryRefresh()
	{
		for(BaseItem BI : arrInvenItems)
			BI= null;
		
		arrInvenItems= new BaseItem[IVEN_ITEM_COUNT];
		 
		ArrayList<ItemData> arrItemsData= uInfo.GetItems();
		for(int i=0; i<arrItemsData.size(); ++i)
		{
			arrInvenItems[i]= new BaseItem();
			arrInvenItems[i].itemData= arrItemsData.get(i);
			
			int nRow= (int)(i/5);
			int nCol= i%5;
			arrInvenItems[i].SetObject(sprItems, 0, 0, 85+nCol*80, 415+nRow*80, 0, arrInvenItems[i].itemData.eType.ordinal());
		}
	}
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		
		if(m_bTrading)
		{
			m_bTrading= false;
			return;
		}
		
		if(m_nMenu==0)
			this.SetScene(EnumScene.E_MAIN);
		else 
			m_nMenu= 0;
	}

	
	
}


