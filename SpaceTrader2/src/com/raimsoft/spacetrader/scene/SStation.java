package com.raimsoft.spacetrader.scene;

import java.util.ArrayList;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import bayaba.engine.lib.ButtonObject;
import bayaba.engine.lib.ButtonType;
import bayaba.engine.lib.Font;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;
import bayaba.engine.lib.Sprite;

import com.raimsoft.spacetrader.R;
import com.raimsoft.spacetrader.data.DBManager;
import com.raimsoft.spacetrader.data.EnumShip;
import com.raimsoft.spacetrader.data.UserInfo;
import com.raimsoft.spacetrader.obj.GameButton;
import com.raimsoft.spacetrader.obj.items.BaseItem;
import com.raimsoft.spacetrader.obj.items.EItems;
import com.raimsoft.spacetrader.obj.items.ItemData;
import com.raimsoft.spacetrader.util.GenConst;
import com.raimsoft.spacetrader.util.PlanetNameMaker;

public class SStation extends SBase
{
	private final int SHOP_ITEM_COUNT= 10;
	private final int IVEN_ITEM_COUNT= 20;

	public SStation(Context mContext, GameInfo gInfo)
	{
		super(mContext, gInfo);		
		
		DBMgr= new DBManager(mContext);
		
		
		//arrInvenItems= DBMgr.GetItems().toArray(new BaseItem[IVEN_ITEM_COUNT]);

		//DBMgr.DropItemsTable();
		//DBMgr.AddItems(0, 100, 10);
		//DBMgr.AddItems(1, 100, 45);
		
		//arrItems= DBMgr.GetItems().toArray(new BaseItem[30]);
		
		for(int i=0; i<30; ++i)
		{
			btnItemsBackground[i]= new GameButton();
		}
	}
	
	
	
	private DBManager DBMgr;
	
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
	
	private ButtonObject prgBG1= new ButtonObject();
	private ButtonObject prgBG2= new ButtonObject();
	private ButtonObject prgBG3= new ButtonObject();
	private ButtonObject prgHull= new ButtonObject();
	private ButtonObject prgFuel= new ButtonObject();
	private ButtonObject prgShield= new ButtonObject();
	private GameObject objHexagon= new GameObject();
	
	private MediaPlayer Music;
	
	private Font font = new Font();
	private int nMenu= 0;	
	
	
	@Override
	public void LoadData()
	{
		super.LoadData();
		

		
		uInfo= UserInfo.GetInstance();
		PNM= new PlanetNameMaker();
		
		Music = MediaPlayer.create(mContext, R.raw.station2);
		Music.setLooping(true);
		Music.start();
		
		float fConst= GC.GetPositionTimeConstF();		
		arrShopItems[0]= new BaseItem(mContext, gl, EItems.E_BOX, fConst);
		arrShopItems[1]= new BaseItem(mContext, gl, EItems.E_MATERIAL, fConst);
		
		this.IventoryRefresh();	// DB에서 아이템 새로 가져옴
		
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
		
		objShip.SetObject(sprShip1, 0, 0, 140, 275, 0, 0);
		sprProgress.LoadSprite(gl, mContext, R.drawable.progress, "progress_station.spr");
		prgBG1.SetButton(sprProgress, ButtonType.TYPE_POPUP, 0, 240, 520, 0);
		prgBG2.SetButton(sprProgress, ButtonType.TYPE_POPUP, 0, 240, 590, 0);
		prgBG3.SetButton(sprProgress, ButtonType.TYPE_POPUP, 0, 240, 660, 0);
		prgHull.SetButton(sprProgress, ButtonType.TYPE_PROGRESS, 0, 240, 520, 1);
		prgFuel.SetButton(sprProgress, ButtonType.TYPE_PROGRESS, 0, 240, 590, 2);
		prgShield.SetButton(sprProgress, ButtonType.TYPE_PROGRESS, 0, 240, 660, 3);
		
		prgHull.SetText(0, 140, 3, 0.75f, 0.75f, 0.75f, 22f, uInfo.GetCurrHull()+" / "+uInfo.GetShipHull());
		prgHull.energy= ((float)uInfo.GetCurrHull() / (float)uInfo.GetShipHull()) * 100.0f;
		prgFuel.energy= 100;
		prgShield.energy= 100;
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
		
	}
	@Override
	public void Render()
	{
		super.Render();
		
		
		bg_station.PutImage(gInfo, 0, 0);
		int  nX= (int) (gInfo.ScreenX/2);
		int  nY= (int) (gInfo.ScreenY/2);
		
		font.BeginFont();
		switch (nMenu)
		{
		case 0:	// 메뉴상태
			btnInfo.DrawButtonWithText(gInfo, gl, font);
			btnNews.DrawButtonWithText(gInfo, gl, font);
			btnTrade.DrawButtonWithText(gInfo, gl, font);
			btnManage.DrawButtonWithText(gInfo, gl, font);
			btnExit.DrawButtonWithText(gInfo, gl, font);
			break;
		case 1:	// 스테이션 정보
			sprStationUI_PANEL.PutAni(gInfo, nX, nY, 0, 0);
			sprStationUI_PANEL.PutAni(gInfo, 40, 100, 1, 0);
			sprStationUI_PANEL.PutAni(gInfo, 250, 100, 2, 0);
			sprStationUI_PANEL.PutAni(gInfo, 40, 400, 3, 0);
			objHexagon.DrawSprite(gInfo);
			font.DrawFont(gl, 255, 165, 16f, PNM.GetCurrPlanetName(uInfo.GetSystemMapPlanet()));
			font.DrawFont(gl, 255, 195, 24f, "(423,224):"+uInfo.GetSystemMapPlanet() );
			//font.DrawFont(gl, 380, 400, 28f, uInfo.GetPlanetName());
			
			objPlanet.DrawSprite(gInfo);
			btnPower.DrawSprite(gInfo);
			
			break;
		case 2:	//월드 뉴스	
			sprStationUI_PANEL.PutAni(gInfo, nX, nY, 0, 0);
			sprStationUI_PANEL.PutAni(gInfo, 40, 100, 4, 0);
			sprStationUI_PANEL.PutAni(gInfo, 40, 320, 5, 0);
			btnPower.DrawSprite(gInfo);
			break;
		case 3:	//상품 거래
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
			break;
		case 4:	//함선 관리
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
			prgBG1.DrawSprite(gl, 0, gInfo, font);
			prgBG2.DrawSprite(gl, 0, gInfo, font);
			prgBG3.DrawSprite(gl, 0, gInfo, font);
			prgHull.DrawSprite(gl,0, gInfo, font);
			prgFuel.DrawSprite(gl,0, gInfo, font);
			prgShield.DrawSprite(gl,0, gInfo, font);
			objShip.DrawSprite(gInfo);
			btnPower.DrawSprite(gInfo);
			break;

		default:
			break;
		}
		font.EndFont();
		
		
		
	}
	@Override
	public void Update() 
	{
		super.Update();
		
		if(nMenu==3)
		{
			for(int i=0; i<SHOP_ITEM_COUNT; ++i)	// 샵 아이템들 돌면서
			{
				if(btnItemsBackground[i].CheckOver())	// 눌렀으면
				{
					if(arrShopItems[i]==null)
						continue;
					
					arrShopItems[i].CheckSettingShop(true);
					
					for(int j=0; j<arrShopItems.length; ++j)
					{
						BaseItem currItem= arrShopItems[j]; 
						
						if(currItem==null)	//아이템없으면 빼고 
							continue;
						
						if(currItem.bLastCheck)	// 체크되면.. 해당 아이템 추가
						{
							DBMgr.AddItems(currItem.itemData.eType.ordinal(), 1, currItem.itemData.nCurrentPrice);	// 구매하고
							this.IventoryRefresh();	// 아이템창 갱신
						}
						
						if(i!=j)	// 아까 누른거 아니면 체크세팅
							currItem.CheckSettingShop(false);
					}
				}
			}
			
			for(int i=SHOP_ITEM_COUNT; i<btnItemsBackground.length; ++i)	// 내 아이템에 대해서
			{
				if(btnItemsBackground[i].CheckOver())	// 버튼이 눌렸음
				{
					if(arrInvenItems[i-SHOP_ITEM_COUNT]==null)	// 아이템이 없으면 넘어감
						continue;
					
					arrInvenItems[i-SHOP_ITEM_COUNT].CheckSettingInventory(true);	// 누른거 체크

					
					for(int j=0; j<arrInvenItems.length; ++j)	// 인벤토리 목록 돌면서
					{
						BaseItem currItem= arrInvenItems[j]; 
						
						if(currItem==null)	//아이템없으면 빼고
							continue;
						
						if(currItem.bLastCheck)	// 체크되면.. 해당 아이템 추가
						{
							DBMgr.RemoveItem(j, 1);
							this.IventoryRefresh();	// 아이템창 갱신
						}
						
						if(i-SHOP_ITEM_COUNT==j)	// 아까 누른거 빼고
							continue;
						
						
													
						currItem.CheckSettingInventory(false);							
					}
				}
			}
			
			
			
			for(GameButton BTN : btnItemsBackground)	// 아이템 배경버튼들 업데이트
				BTN.ButtonUpdate(0);
		}
		
		if(nMenu!=0)
			btnPower.ButtonUpdate(0);
		
		if(btnPower.CheckOver())
			nMenu= 0;
		
		
		if(nMenu!=0)	// 메뉴선택중 아니면
			return;
		
		btnInfo.ButtonUpdate(0);
		btnNews.ButtonUpdate(0);
		btnTrade.ButtonUpdate(0); 
		btnManage.ButtonUpdate(0);
		btnExit.ButtonUpdate(0);
		
		
		if(btnInfo.CheckOver())
		{
			nMenu= 1;
		}
		else if(btnNews.CheckOver())
		{
			nMenu= 2;
		}
		else if(btnTrade.CheckOver())
		{
			nMenu= 3;
		}		
		else if(btnManage.CheckOver())
		{
			nMenu= 4;
		}
		else if(btnExit.CheckOver())
		{
			SetScene(EnumScene.E_GAME_SYSTEMMAP);
		}
	}
	
	protected void IventoryRefresh()
	{
		for(BaseItem BI : arrInvenItems)
			BI= null;
		
		arrInvenItems= new BaseItem[IVEN_ITEM_COUNT];
		 
		ArrayList<ItemData> arrItemsData= DBMgr.GetItems();
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
		
		if(nMenu==0)
			this.SetScene(EnumScene.E_MAIN);
		else 
			nMenu= 0;
	}

	
	
}


