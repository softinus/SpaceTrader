package com.raimsoft.spacetrader.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import com.raimsoft.spacetrader.obj.RainbowMessageBox;
import com.raimsoft.spacetrader.obj.items.BaseItem;
import com.raimsoft.spacetrader.obj.items.EItems;
import com.raimsoft.spacetrader.obj.items.ItemData;
import com.raimsoft.spacetrader.util.GenConst;
import com.raimsoft.spacetrader.util.ParseConnector;
import com.raimsoft.spacetrader.util.PlanetNameMaker;
import com.raimsoft.spacetrader.util.SoundManager;

public class SStation extends SBase {
	private ParseConnector PC = new ParseConnector();

	private final int SHOP_ITEM_COUNT = 10;
	private final int IVEN_ITEM_COUNT = 20;

	private final static int E_NONE = 0;
	private final static int E_INFO = 1;
	private final static int E_NEWS = 2;
	private final static int E_TRADE = 3;
	private final static int E_MANAGE = 4;
	private final static int E_EXIT = 5;
	private final static int E_EVENT1 = 6;

	public SStation(Context mContext, GameInfo gInfo) {
		super(mContext, gInfo);

		for (int i = 0; i < 30; ++i) {
			btnItemsBackground[i] = new GameButton();
		}
	}

	private int nX = (int) (gInfo.ScreenX / 2);
	private int nY = (int) (gInfo.ScreenY / 2);

	// private DBManager DBMgr;

	private boolean bEventOccur = false; // 이벤트 발생 여부
	private Random rnd = new Random();

	private int PLANET_TYPES = 4;
	private int nCurrPlanetType = -1;

	private PlanetNameMaker PNM;
	private UserInfo uInfo;
	private GenConst GC = new GenConst();

	private Sprite bg_station = new Sprite();
	private Sprite sprStationButton = new Sprite();
	private Sprite sprHexagon = new Sprite();
	private Sprite sprStationUI_PANEL = new Sprite();
	// private Sprite sprStationUI_NEWS= new Sprite();
	// private Sprite sprStationUI_TRADE= new Sprite();
	// private Sprite sprStationUI_MANAGE= new Sprite();
	private Sprite sprPlanets = new Sprite();
	private Sprite sprShip1 = new Sprite();
	private Sprite sprProgress = new Sprite(); //
	private Sprite sprItemButton = new Sprite(); // 아이템 버튼
	private Sprite sprPower = new Sprite(); // 파워 버튼
	private Sprite sprItems = new Sprite(); // 아이템 리스트 (png에 있는 순대로)
	private Sprite sprCashPanel = new Sprite(); // 골드 보여주는 패널
	private Sprite sprTradePanel = new Sprite(); // 거래창 패널
	Sprite sprMessage = new Sprite();

	private GameButton btnInfo = new GameButton();
	private GameButton btnNews = new GameButton();
	private GameButton btnTrade = new GameButton();
	private GameButton btnManage = new GameButton();
	private GameButton btnExit = new GameButton();
	private GameButton btnEvent1 = new GameButton(); // 이벤트 패널 버튼

	private GameObject objPlanet = new GameObject();
	private GameObject objShip = new GameObject();
	private BaseItem[] arrShopItems = new BaseItem[SHOP_ITEM_COUNT]; // 샵 아이템
	private BaseItem[] arrInvenItems = new BaseItem[IVEN_ITEM_COUNT]; // 인벤토리

	private GameButton[] btnItemsBackground = new GameButton[SHOP_ITEM_COUNT
			+ IVEN_ITEM_COUNT];
	private GameButton btnPower = new GameButton();

	private GameButton prgBG1 = new GameButton(); // 체력바 배경
	private GameButton prgBG2 = new GameButton(); // 쉴드바 배경
	private GameButton prgBG3 = new GameButton(); // 연료바 배경
	private ButtonObject prgHull = new ButtonObject(); // 체력프로그레스
	private ButtonObject prgShield = new ButtonObject(); // 쉴드프로그레스
	private ButtonObject prgFuel = new ButtonObject(); // 연료프로그레스
	private GameObject objHexagon = new GameObject();
	private GameObject objCashPanel = new GameObject(); // 골드 보여주는 패널

	private GameObject objTradePanelBackground = new GameObject(); // 거래 패널 배경
	private GameObject objArrowBuy = new GameObject(); // 구매 화살표
	private GameObject objArrowSell = new GameObject(); // 판매 화살표
	private GameObject objTradeFieldAmount = new GameObject(); // 거래 양
	private GameObject objTradeFieldPay = new GameObject(); // 지불액수
	private GameButton btnTradeButtonZero = new GameButton(); // 거래 버튼
	private GameButton btnTradeButtonP1 = new GameButton(); // 거래 버튼
	private GameButton btnTradeButtonP10 = new GameButton(); // 거래 버튼
	private GameButton btnTradeButtonMAX = new GameButton(); // 거래 버튼
	private GameButton btnTradeButtonM1 = new GameButton(); // 거래 버튼
	private GameButton btnTradeButtonM10 = new GameButton(); // 거래 버튼
	private GameButton btnTradeButtonTrade = new GameButton(); // 거래 버튼
	private ButtonObject prgTradeWeight = new ButtonObject(); // 무게창
	private GameObject objTradeTarget = new GameObject(); // 거래 대상 함선 그림
	private GameObject objTradeItem = new GameObject(); // 거래 중인 물품

	private RainbowMessageBox msgBox;
	private RainbowMessageBox okBox;

	private MediaPlayer Music;
	private SoundManager soundMgr;

	private Font font = new Font();
	private int m_nMenu = 0;
	private boolean m_bTrading = false; // 거래중인지?
	private boolean m_bBuying = true; // 사는중인지?
	private ItemData m_TradingItemData = null;
	private int m_nTradeAmount = 0;

	@Override
	public void LoadData()
	{
		super.LoadData();

		uInfo = UserInfo.GetInstance();
		PNM = new PlanetNameMaker();

		final int nConst = rnd.nextInt(100);
		if (nConst < 50) // 20% 확률로 이벤트 띄운다.
			bEventOccur = true;

		if (bEventOccur)
		{
			Music = MediaPlayer.create(mContext, R.raw.station1);
			soundMgr = new SoundManager(mContext);
			soundMgr.Create();
			soundMgr.Load(0, R.raw.radar);
			// soundMgr.Play(0);
		} else
			Music = MediaPlayer.create(mContext, R.raw.station2);

		Music.setLooping(true);
		Music.start();

		sprMessage.LoadSprite(gl, mContext, R.drawable.buttons_2,
				"rainbow_messagebox.spr");
		msgBox = new RainbowMessageBox(gl, mContext);
		msgBox.SetMessageBox(1, sprMessage, 0, 0, gInfo.ScreenX / 2,
				gInfo.ScreenY / 2, 0, 0);
		msgBox.scroll = false;

		okBox = new RainbowMessageBox(gl, mContext);
		okBox.SetMessageBox(0, sprMessage, 0, 0, gInfo.ScreenX / 2,
				gInfo.ScreenY / 2, 0, 0);
		okBox.scroll = false;

		// float fConst= GC.GetPositionTimeConstF();
		arrShopItems[0] = new BaseItem(mContext, gl, EItems.E_BOX,
				GC.GetPositionTimeConstF(1));
		arrShopItems[1] = new BaseItem(mContext, gl, EItems.E_MATERIAL,
				GC.GetPositionTimeConstF(2));

		this.InventoryRefresh(); // DB에서 아이템 새로 가져옴

		// / 스테이션 정보
		sprPlanets.LoadSprite(gl, mContext, R.drawable.planets, "planets.spr");
		// objPlanet= uInfo.GetCurrentPlanet();

		nCurrPlanetType = (int) GC.GetPositionConstF(
				uInfo.GetSystemMapPlanet(), PLANET_TYPES);
		objPlanet.SetObject(sprPlanets, nCurrPlanetType, 0, 135, 230,
				nCurrPlanetType, 0);// objPlanet.show= true; objPlanet.x= 200;
									// objPlanet.y= 200; objPlanet.scalex=
									// 0.75f; objPlanet.scaley= 0.75f;
		objPlanet.scalex = 0.50f;
		objPlanet.scaley = 0.50f;

		sprPlanets.LoadSprite(gl, mContext, R.drawable.planets, "planets.spr");
		// /==

		sprShip1.LoadSprite(gl, mContext, "fleets.spr");
		
		// / 함선 정보
		if (uInfo.GetShipType() == EnumShip.E_TRAINING_SHIP_1)
			objShip.SetObject(sprShip1, 0, 0, 140, 275, 0, 0);
		else if (uInfo.GetShipType() == EnumShip.E_TRAINING_SHIP_2)
			objShip.SetObject(sprShip1, 0, 0, 140, 275, 1, 0);
			
		objTradeTarget.SetObject(sprShip1, 0, 0, 65, 460, 0, 0); // 거래창에서 거래 대상
																	// 함선

		
		sprProgress.LoadSprite(gl, mContext, R.drawable.progress, "progress_station.spr");

		prgBG1.SetButton(mContext, sprProgress, 240, 520, 0);
		prgBG2.SetButton(mContext, sprProgress, 240, 590, 0);
		prgBG3.SetButton(mContext, sprProgress, 240, 660, 0);
		prgHull.SetButton(sprProgress, ButtonType.TYPE_PROGRESS, 0, 240, 520, 1);
		prgShield.SetButton(sprProgress, ButtonType.TYPE_PROGRESS, 0, 240, 590,
				2);
		prgFuel.SetButton(sprProgress, ButtonType.TYPE_PROGRESS, 0, 240, 660, 3);

		prgHull.SetText(0, 140, 3, 0.75f, 0.75f, 0.75f, 22f,
				uInfo.GetCurrHull() + " / " + uInfo.GetShipHull());
		prgHull.energy = ((float) uInfo.GetCurrHull() / (float) uInfo
				.GetShipHull()) * 100.0f;

		prgShield.SetText(0, 140, 3, 0.75f, 0.75f, 0.75f, 22f, "100 / 100");
		prgShield.energy = 100;

		prgFuel.SetText(0, 140, 3, 0.75f, 0.75f, 0.75f, 22f,
				uInfo.getCurrFuel() + " / " + uInfo.getShipMaxFuel());
		prgFuel.energy = ((float) uInfo.getCurrFuel() / (float) uInfo
				.getShipMaxFuel()) * 100.0f;

		Log.d("Hull Progress1", Float.toString(uInfo.GetCurrHull()));
		Log.d("Hull Progress2", Float.toString(uInfo.GetShipHull()));
		Log.d("Hull Progress3",
				Float.toString(uInfo.GetCurrHull() / uInfo.GetShipHull()));
		Log.d("Hull Progress4", Float.toString(prgHull.energy));
		// /==

		// / 스테이션 메뉴
		bg_station.LoadSprite(gl, mContext, R.drawable.station_bg,
				"station_ui_bg.spr");
		sprStationButton.LoadSprite(gl, mContext, R.drawable.buttons_2,
				"station_buttons.spr");
		sprStationUI_PANEL.LoadSprite(gl, mContext, R.drawable.station_ui_2,
				"station_ui_2.spr");

		if (bEventOccur)
		{
			btnEvent1.bSound= false;
			btnEvent1.SetButton(mContext, sprStationButton, 240, 200, 0);
			btnEvent1.SetTextColor(0.05f, 0.45f, 0.9f);
			btnEvent1.SetTextCenter(33f, " SOS!");
		}

		// btnInfo.SetTextColor(1.0f, 1.0f, 1.0f);
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
		// /==

		sprHexagon.LoadSprite(gl, mContext, R.drawable.buttons_2,
				"station_hexagon.spr");
		objHexagon.SetObject(sprHexagon, 0, 0, 135, 230, 0, 0);
		// objHexagon.scalex= 1.10f; objHexagon.scaley= 1.10f;

		// / 상점 메뉴
		sprItemButton.LoadSprite(gl, mContext, R.drawable.buttons_2,
				"btn_item.spr");
		for (int i = 0; i < 10; ++i) {
			int nRow = (int) (i / 5);
			int nCol = i % 5;
			btnItemsBackground[i].SetButton(mContext, sprItemButton,
					85 + nCol * 80, 190 + nRow * 80, 0);
			// arrItems[i].SetObject(sprItems, 0, 0, 85+nCol*80, 190+nRow*80, 0,
			// 0);
		}
		for (int i = 10; i < 30; ++i) {
			int nRow = (int) (i / 5);
			int nCol = i % 5;
			btnItemsBackground[i].SetButton(mContext, sprItemButton,
					85 + nCol * 80, 255 + nRow * 80, 0);
			// arrItems[i].SetObject(sprItems, 0, 0, 85+nCol*80, 255+nRow*80, 0,
			// 0);
		}

		sprItems.LoadSprite(gl, mContext, R.drawable.list_items,
				"list_items.spr"); // 아이템들 그림 불러옴.
		for (int i = 0; i < arrInvenItems.length; ++i) // 인벤토리 아이템 리스트 돌면서
		{
			if (arrInvenItems[i] == null)
				break;

			int nRow = (int) (i / 5);
			int nCol = i % 5;
			arrInvenItems[i].SetObject(sprItems, 0, 0, 85 + nCol * 80,
					415 + nRow * 80, 0,
					arrInvenItems[i].itemData.eType.ordinal());
		}

		for (int i = 0; i < arrShopItems.length; ++i) // 샵 아이템 리스트 돌면서
		{
			if (arrShopItems[i] == null)
				break;

			int nRow = (int) (i / 5);
			int nCol = i % 5;
			arrShopItems[i].SetObject(sprItems, 0, 0, 85 + nCol * 80,
					190 + nRow * 80, 0,
					arrShopItems[i].itemData.eType.ordinal());
		}
		// /==

		sprPower.LoadSprite(gl, mContext, R.drawable.buttons_2, "btn_power.spr");
		btnPower.SetButton(mContext, sprPower, 415, 50, 0);

		sprCashPanel.LoadSprite(gl, mContext, R.drawable.buttons_2,
				"station_ui_cash.spr");
		objCashPanel.SetObject(sprCashPanel, 0, 0, 35, 725, 0, 0);

		// /== 거래 메뉴
		sprTradePanel.LoadSprite(gl, mContext, R.drawable.station_ui_shop,
				"station_ui_shop.spr");
		objTradePanelBackground.SetObject(sprTradePanel, 0, 0, nX, nY, 0, 0);
		objArrowBuy.SetObject(sprTradePanel, 0, 0, 60, 350, 1, 0);
		objArrowSell.SetObject(sprTradePanel, 0, 0, 85, 400, 2, 0);
		objTradeFieldAmount.SetObject(sprTradePanel, 0, 0, 235, 400, 5, 0);
		objTradeFieldPay.SetObject(sprTradePanel, 0, 0, 235, 435, 5, 0);

		btnTradeButtonZero.SetButton(mContext, sprTradePanel, 180, 290, 3, 4);
		btnTradeButtonP1.SetButton(mContext, sprTradePanel, 290, 290, 3, 4);
		btnTradeButtonP10.SetButton(mContext, sprTradePanel, 400, 290, 3, 4);
		btnTradeButtonM1.SetButton(mContext, sprTradePanel, 180, 335, 3, 4);
		btnTradeButtonM10.SetButton(mContext, sprTradePanel, 290, 335, 3, 4);
		btnTradeButtonMAX.SetButton(mContext, sprTradePanel, 400, 335, 3, 4);
		btnTradeButtonTrade.SetButton(mContext, sprTradePanel, 400, 420, 3, 4);

		btnTradeButtonTrade.scaley = 1.8f;
		// /==
	}

	@Override
	public void Render() {
		super.Render();

		bg_station.PutImage(gInfo, 0, 0);

		font.BeginFont();
		switch (m_nMenu) {
		case SStation.E_NONE: // 메뉴상태
			if (bEventOccur)
				btnEvent1.DrawButtonWithText(gInfo, gl, font);

			btnInfo.DrawButtonWithText(gInfo, gl, font);
			btnNews.DrawButtonWithText(gInfo, gl, font);
			btnTrade.DrawButtonWithText(gInfo, gl, font);
			btnManage.DrawButtonWithText(gInfo, gl, font);
			btnExit.DrawButtonWithText(gInfo, gl, font);
			break;
		case SStation.E_INFO: // 스테이션 정보
			sprStationUI_PANEL.PutAni(gInfo, nX, nY, 0, 0);
			sprStationUI_PANEL.PutAni(gInfo, 40, 100, 1, 0);
			sprStationUI_PANEL.PutAni(gInfo, 250, 100, 2, 0);
			sprStationUI_PANEL.PutAni(gInfo, 40, 400, 3, 0);
			objHexagon.DrawSprite(gInfo);
			font.DrawFont(gl, 255, 165, 16f,
					PNM.GetCurrPlanetName(uInfo.GetSystemMapPlanet()));
			font.DrawFont(gl, 255, 195, 24f, "(" + uInfo.GetWorldMapX() + ","
					+ uInfo.GetWorldMapY() + "):" + uInfo.GetSystemMapPlanet());
			// font.DrawFont(gl, 380, 400, 28f, uInfo.GetPlanetName());

			objPlanet.DrawSprite(gInfo);
			btnPower.DrawSprite(gInfo);

			break;
		case SStation.E_NEWS: // 월드 뉴스
			sprStationUI_PANEL.PutAni(gInfo, nX, nY, 0, 0);
			sprStationUI_PANEL.PutAni(gInfo, 40, 100, 4, 0);
			sprStationUI_PANEL.PutAni(gInfo, 40, 320, 5, 0);
			btnPower.DrawSprite(gInfo);
			break;
		case SStation.E_TRADE: // 상품 거래

			sprStationUI_PANEL.PutAni(gInfo, nX, nY, 0, 0);
			sprStationUI_PANEL.PutAni(gInfo, 40, 100, 6, 0);
			sprStationUI_PANEL.PutAni(gInfo, 40, 320, 7, 0);
			btnPower.DrawSprite(gInfo);
			for (GameButton BTN : btnItemsBackground) {
				if (BTN.pattern != null)
					BTN.DrawSprite(gInfo);
			}
			for (BaseItem ITEMS : arrInvenItems) {
				if (ITEMS == null || ITEMS.pattern == null)
					continue;

				// if(ITEMS.itemData.nCount==0)
				// continue;

				ITEMS.scalex = 0.5f;
				ITEMS.scaley = 0.5f;
				ITEMS.DrawSprite(gInfo);

				float fXFactor = 0f;

				if (ITEMS.itemData.nCount == 100)
					fXFactor = 0.1f;
				else if (ITEMS.itemData.nCount >= 10)
					fXFactor = 5f;
				else
					fXFactor = 17f;

				font.DrawFont(gl, ITEMS.x + fXFactor, ITEMS.y + 5.5f, 22.5f,
						Integer.toString(ITEMS.itemData.nCount));
			}
			for (BaseItem ITEMS : arrShopItems) {
				if (ITEMS != null) {
					ITEMS.scalex = 0.5f;
					ITEMS.scaley = 0.5f;
					ITEMS.DrawSprite(gInfo);

					float fXFactor = 0f;

					if (ITEMS.itemData.nCurrentPrice >= 100)
						fXFactor = -5f;
					else if (ITEMS.itemData.nCurrentPrice >= 10)
						fXFactor = 0f;
					else
						fXFactor = 5f;

					font.DrawFont(gl, ITEMS.x + fXFactor, ITEMS.y + 5.5f,
							22.5f, "$" + ITEMS.itemData.nCurrentPrice);

				}
			}

			if (m_bTrading) // 거래중이면
			{
				objTradePanelBackground.DrawSprite(gInfo);

				if (m_bBuying)
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

				objTradeTarget.scalex = 0.65f;
				objTradeTarget.scaley = 0.65f;
				objTradeTarget.DrawSprite(gInfo);

				objTradeItem.scalex = 0.6f;
				objTradeItem.scaley = 0.6f;
				objTradeItem.DrawSprite(gInfo);

				font.DrawFont(gl, 135, 208, 30.0f,
						m_TradingItemData.strItemName + " ( $ "
								+ m_TradingItemData.nCurrentPrice + " )");
				font.DrawColorFont(gl, 225, 390, 0.1f, 0.1f, 0.1f, 23.0f, "x"
						+ m_nTradeAmount);
				font.DrawColorFont(gl, 225, 425, 0.1f, 0.1f, 0.1f, 23.0f, "$"
						+ m_TradingItemData.nCurrentPrice * m_nTradeAmount);

			}

			break;
		case SStation.E_MANAGE: // 함선 관리
			sprStationUI_PANEL.PutAni(gInfo, nX, nY, 0, 0);
			sprStationUI_PANEL.PutAni(gInfo, 40, 100, 8, 0);
			sprStationUI_PANEL.PutAni(gInfo, 250, 100, 9, 0);
			sprStationUI_PANEL.PutAni(gInfo, 40, 320, 10, 0);
			// font.DrawFont(gl, 95, 100, 24f, "현재 함선");
			font.DrawFont(gl, 290, 145, 20f, "함선명 : " + uInfo.GetShipName());
			font.DrawFont(gl, 290, 170, 20f, "공격력 : " + uInfo.GetShipAtt());
			font.DrawFont(gl, 290, 195, 20f, "내구도 : " + uInfo.GetShipHull());
			font.DrawFont(gl, 290, 220, 20f, "핸들링 : " + uInfo.GetHandling());
			font.DrawFont(gl, 290, 245, 20f, "스피드 : " + uInfo.GetVelocity());
			// font.DrawFont(gl, 200, 435, 24f, "함선 정비");

			prgBG1.DrawSprite(gInfo);
			prgBG2.DrawSprite(gInfo);
			prgBG3.DrawSprite(gInfo);

			prgHull.DrawSprite(gl, 0, gInfo, font);
			prgFuel.DrawSprite(gl, 0, gInfo, font);
			prgShield.DrawSprite(gl, 0, gInfo, font);
			objShip.DrawSprite(gInfo);
			btnPower.DrawSprite(gInfo);

			font.DrawFont(gl, 65, 510, 14f, "내구도");
			font.DrawFont(gl, 65, 580, 14f, "보호막");
			font.DrawFont(gl, 65, 650, 14f, "연료통");

			break;

		case SStation.E_EVENT1:
			if(Music.isPlaying())
				Music.pause();
			
			font.DrawFont(gl, 55, 125, 24f, "살려주세요!");
			font.DrawFont(gl, 55, 155, 24f, "살려주세요!");
			font.DrawFont(gl, 55, 185, 24f, "살려주세요!");

			break;

		default:
			break;
		}

		if (m_nMenu != 0) {
			objCashPanel.DrawSprite(gInfo);
			font.DrawFont(gl, 265, 735, 34.5f,
					Integer.toString(uInfo.GetGold()));
		}

		font.EndFont();

		if (msgBox.GetShow())
			msgBox.DrawSprite(gInfo); // 메세지 박스

		if (okBox.GetShow())
			okBox.DrawSprite(gInfo); // 메세지 박스

	}

	@Override
	public void Update() {
		super.Update();

		okBox.UpdateObjects(0.0f);
		int nOk = okBox.CheckOverButtons();
		if (nOk == 0) {
			okBox.SetShow(false);
		}
		if (okBox.GetShow()) // 메세지박스 떠있으면 전부 무시
			return;

		msgBox.UpdateObjects(0.0f);
		int nRes = msgBox.CheckOverButtons();
		if (nRes == 0) {
			ParseConnector PC = new ParseConnector();
			int nPay = 0;

			switch (msgBox.type) {
			case 1:
				nPay = this.CalcRepairCost();

				if (uInfo.GetGold() < nPay) // 자산보다 비용이 더 많으면...
				{
					msgBox.SetShow(false);

					okBox.SetButtonTextScr(22f, "골드가 부족합니다.", "확인", "");
					okBox.SetBoxPosition(0);
					okBox.SetShow(true);
				} else {
					uInfo.SetCurrHull(uInfo.GetShipHull());
					prgHull.SetText(0, 140, 3, 0.75f, 0.75f, 0.75f, 22f,
							uInfo.GetCurrHull() + " / " + uInfo.GetShipHull());
					prgHull.energy = ((float) uInfo.GetCurrHull() / (float) uInfo
							.GetShipHull()) * 100.0f;
					uInfo.AddGold(-1 * nPay);
					PC.SetShipHull(uInfo.GetCurrHull());
					PC.PayGold(nPay);
				}
				break;
			case 2:
				break;
			case 3:
				nPay = this.CalcRefuelCost();

				if (uInfo.GetGold() < nPay) // 자산보다 비용이 더 많으면...
				{
					msgBox.SetShow(false);

					okBox.SetButtonTextScr(22f, "골드가 부족합니다.", "확인", "");
					okBox.SetBoxPosition(0);
					okBox.SetShow(true);
				} else {
					uInfo.setCurrFuel(uInfo.getShipMaxFuel());
					prgFuel.SetText(
							0,
							140,
							3,
							0.75f,
							0.75f,
							0.75f,
							22f,
							uInfo.getCurrFuel() + " / "
									+ uInfo.getShipMaxFuel());
					prgFuel.energy = ((float) uInfo.getCurrFuel() / (float) uInfo
							.getShipMaxFuel()) * 100.0f;
					uInfo.AddGold(-1 * nPay);

					PC.SetShipFuel(uInfo.getShipMaxFuel());
					PC.PayGold(nPay);

				}
				break;
			}

			msgBox.SetShow(false);
		} else if (nRes == 1) {
			msgBox.SetShow(false);
		}
		if (msgBox.GetShow()) // 메세지박스 떠있으면 전부 무시
			return;

		if (m_nMenu == SStation.E_MANAGE) // 관리 상태이면
		{
			prgBG1.ButtonUpdate(0.0f);
			prgBG2.ButtonUpdate(0.0f);
			prgBG3.ButtonUpdate(0.0f);

			if (prgBG1.CheckOver()) {
				if (uInfo.GetCurrHull() != uInfo.GetShipHull()) {
					msgBox.SetButtonTextScr(22f, "함선을 수리하시겠습니까?\n수리비용 : $"
							+ this.CalcRepairCost(), "수리", "취소");
					msgBox.SetBoxPosition(0);
					msgBox.SetShow(true);
					msgBox.type = 1;
				}

			} else if (prgBG2.CheckOver()) {
				msgBox.type = 2;
			} else if (prgBG3.CheckOver()) {
				if (uInfo.getCurrFuel() != uInfo.getShipMaxFuel()) {
					msgBox.SetButtonTextScr(22f, "연료를 채우시겠습니까?\n연료비용 : $"
							+ this.CalcRefuelCost(), "만땅", "취소");
					msgBox.SetBoxPosition(0);
					msgBox.SetShow(true);
					msgBox.type = 3;
				}
			}
		} else if (m_nMenu == SStation.E_TRADE) // 트레이드 상태이면
		{
			if (m_bTrading) // 거래중이면
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

				if (m_bBuying)
					btnTradeButtonTrade.SetText(-25, -17, 27.0f, "BUY");
				else
					btnTradeButtonTrade.SetText(-28, -17, 24.0f, "SELL");
				btnTradeButtonP1.SetText(-9, -12, 21.0f, "+1");
				btnTradeButtonP10.SetText(-16, -12, 21.0f, "+10");
				btnTradeButtonM1.SetText(-9, -12, 21.0f, "-1");
				btnTradeButtonM10.SetText(-16, -12, 21.0f, "-10");
				btnTradeButtonMAX.SetText(-21, -12, 21.0f, "MAX");
				btnTradeButtonZero.SetText(-5, -12, 21.0f, "0");

				if (btnTradeButtonTrade.CheckOver() && m_nTradeAmount != 0) {
					if (m_bBuying) {
						uInfo.BuyItems(m_TradingItemData.eType.ordinal(),
								m_nTradeAmount);
						uInfo.SetGold(uInfo.GetGold()
								- (m_nTradeAmount * m_TradingItemData.nCurrentPrice));

						PC.SyncTradeItem(
								true,
								-1
										* (m_nTradeAmount * m_TradingItemData.nCurrentPrice),
								m_TradingItemData.eType.ordinal(),
								m_nTradeAmount);
						m_nTradeAmount = 0;
					} else {
						uInfo.SellItems(m_TradingItemData.eType.ordinal(),
								m_nTradeAmount);
						uInfo.SetGold(uInfo.GetGold()
								+ (m_nTradeAmount * m_TradingItemData.nCurrentPrice));

						PC.SyncTradeItem(
								false,
								(m_nTradeAmount * m_TradingItemData.nCurrentPrice),
								m_TradingItemData.eType.ordinal(), -1
										* m_nTradeAmount);
						m_nTradeAmount = 0;
					}

					m_bTrading = false;
					this.InventoryRefresh();
				}

				if (btnTradeButtonP1.CheckOver()) {
					if (m_bBuying) {
						if (m_TradingItemData.nCurrentPrice
								* (m_nTradeAmount + 1) <= uInfo.GetGold())
							m_nTradeAmount += 1;
					} else {
						if (m_TradingItemData.nCount >= m_nTradeAmount + 1)
							m_nTradeAmount += 1;
					}
				}

				if (btnTradeButtonP10.CheckOver()) {
					if (m_bBuying) {
						if (m_TradingItemData.nCurrentPrice
								* (m_nTradeAmount + 10) <= uInfo.GetGold())
							m_nTradeAmount += 10;
					} else {
						if (m_TradingItemData.nCount >= m_nTradeAmount + 10)
							m_nTradeAmount += 10;
					}
				}

				if (btnTradeButtonM1.CheckOver()) {
					if (m_nTradeAmount - 1 >= 0)
						m_nTradeAmount -= 1;
				}

				if (btnTradeButtonM10.CheckOver()) {
					if (m_nTradeAmount - 10 >= 0)
						m_nTradeAmount -= 10;
				}

				if (btnTradeButtonMAX.CheckOver()) {
					if (m_bBuying) {
						m_nTradeAmount = (int) (uInfo.GetGold() / m_TradingItemData.nCurrentPrice);
					} else {
						m_nTradeAmount = m_TradingItemData.nCount;
					}
				}

				if (btnTradeButtonZero.CheckOver())
					m_nTradeAmount = 0;

				return;
			}

			for (int i = 0; i < SHOP_ITEM_COUNT; ++i) // 샵 아이템들 돌면서
			{
				if (btnItemsBackground[i].CheckOver()) // 아이템 버튼 눌렀으면
				{
					if (arrShopItems[i] == null)
						continue;

					m_TradingItemData = arrShopItems[i].itemData;
					objTradeItem.SetObject(sprItems, 0, 0, 60, 250, 0,
							m_TradingItemData.eType.ordinal());

					m_bTrading = true;
					m_bBuying = true;
				}
			}

			for (int i = SHOP_ITEM_COUNT; i < btnItemsBackground.length; ++i) // 내
																				// 아이템에
																				// 대해서
			{
				if (btnItemsBackground[i].CheckOver()) // 아이템 버튼 눌렀으면
				{
					if (arrInvenItems[i - SHOP_ITEM_COUNT] == null) // 아이템이 없으면
																	// 넘어감
						continue;

					m_TradingItemData = arrInvenItems[i - SHOP_ITEM_COUNT].itemData;
					m_TradingItemData.nCurrentPrice = arrShopItems[m_TradingItemData.eType
							.ordinal()].itemData.nCurrentPrice;
					objTradeItem.SetObject(sprItems, 0, 0, 60, 250, 0,
							m_TradingItemData.eType.ordinal());

					m_bTrading = true;
					m_bBuying = false;
				}
			}

			for (GameButton BTN : btnItemsBackground)
				// 아이템 배경버튼들 업데이트
				BTN.ButtonUpdate(0);
		}

		if (m_nMenu != 0)
			btnPower.ButtonUpdate(0);

		if (btnPower.CheckOver())
			m_nMenu = 0;

		if (m_nMenu != 0) // 메뉴선택중 아니면
			return;

		btnEvent1.ButtonUpdate(0);
		btnInfo.ButtonUpdate(0);
		btnNews.ButtonUpdate(0);
		btnTrade.ButtonUpdate(0);
		btnManage.ButtonUpdate(0);
		btnExit.ButtonUpdate(0);

		if (btnInfo.CheckOver()) {
			m_nMenu = 1;
		} else if (btnNews.CheckOver()) {
			m_nMenu = 2;
		} else if (btnTrade.CheckOver()) {
			m_nMenu = 3;
		} else if (btnManage.CheckOver()) {
			m_nMenu = 4;
		}
		else if (btnEvent1.CheckOver())
		{			
			m_nMenu = 6;
			soundMgr.Play(0);
		}
		else if (btnExit.CheckOver())
		{
			SetScene(EnumScene.E_GAME_SYSTEMMAP);
		}

	}

	protected void InventoryRefresh() {
		for (BaseItem BI : arrInvenItems)
			BI = null;

		arrInvenItems = new BaseItem[IVEN_ITEM_COUNT];

		ArrayList<ItemData> arrItemsData = uInfo.GetItems();
		for (int i = 0; i < arrItemsData.size(); ++i) {
			arrInvenItems[i] = new BaseItem();
			arrInvenItems[i].itemData = arrItemsData.get(i);

			int nRow = (int) (i / 5);
			int nCol = i % 5;
			arrInvenItems[i].SetObject(sprItems, 0, 0, 85 + nCol * 80,
					415 + nRow * 80, 0,
					arrInvenItems[i].itemData.eType.ordinal());
		}
	}

	private int CalcRepairCost() {
		int nRepair = uInfo.GetShipHull() - uInfo.GetCurrHull();
		return (nRepair / 8);
	}

	private int CalcRefuelCost() {
		int nRefuel = uInfo.getShipMaxFuel() - uInfo.getCurrFuel();
		return (nRefuel / 3);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		if (okBox.GetShow()) // 메세지 떠있으면
		{
			okBox.SetShow(false); // 메세지 끈다.
			return;
		}

		if (msgBox.GetShow()) // 메세지 떠있으면
		{
			msgBox.SetShow(false); // 메세지 끈다.
			return;
		}

		if (m_bTrading) {
			m_bTrading = false;
			return;
		}

		if (m_nMenu == 0)
			this.SetScene(EnumScene.E_GAME_SYSTEMMAP);
		else {
			if (!Music.isPlaying())
				Music.start();
			m_nMenu = 0;
		}
	}

}
