package com.raimsoft.spacetrader.scene;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Button;
import bayaba.engine.lib.ButtonObject;
import bayaba.engine.lib.ButtonType;
import bayaba.engine.lib.Font;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;
import bayaba.engine.lib.Sprite;

import com.raimsoft.spacetrader.R;
import com.raimsoft.spacetrader.data.EnumShip;
import com.raimsoft.spacetrader.data.UserInfo;
import com.raimsoft.spacetrader.obj.GameButton;

public class SStation extends SBase
{

	public SStation(Context mContext, GameInfo gInfo)
	{
		super(mContext, gInfo);
		
		for(int i=0; i<btnItems.length; ++i)
		{
			btnItems[i]= new GameButton();
		}
		
	}
	
	private UserInfo uInfo;
	
	private Sprite bg_station= new Sprite();
	private Sprite sprStationButton= new Sprite();
	private Sprite sprHexagon= new Sprite();
	private Sprite sprStationUI_PANEL= new Sprite();
//	private Sprite sprStationUI_NEWS= new Sprite();
//	private Sprite sprStationUI_TRADE= new Sprite();
//	private Sprite sprStationUI_MANAGE= new Sprite();
	private Sprite sprPlanets= new Sprite();
	private Sprite sprShip1= new Sprite();
	private Sprite sprProgress= new Sprite();
	private Sprite sprItem= new Sprite();
	private Sprite sprPower= new Sprite();
	
	private GameButton btnInfo= new GameButton();
	private GameButton btnNews= new GameButton();
	private GameButton btnTrade= new GameButton();
	private GameButton btnManage= new GameButton();
	private GameButton btnExit= new GameButton();
	private GameObject objPlanet= new GameObject();
	private GameObject objShip= new GameObject();

	private GameButton[] btnItems= new GameButton[30];
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
		
		Music = MediaPlayer.create(mContext, R.raw.station2);
		Music.setLooping(true);
		Music.start();
		
		/// 스테이션 정보
		sprPlanets.LoadSprite(gl, mContext, R.drawable.planets, "planets.spr");
		objPlanet= uInfo.GetCurrentPlanet();
		objPlanet.SetObject(sprPlanets, uInfo.GetPlanetType(), 0, 135, 230,  uInfo.GetPlanetType(), 0);//objPlanet.show= true; objPlanet.x= 200; objPlanet.y= 200; objPlanet.scalex= 0.75f; objPlanet.scaley= 0.75f;
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
		
		sprItem.LoadSprite(gl, mContext, R.drawable.buttons_2, "btn_item.spr");
		for(int i=0; i<10; ++i)
		{
			int nRow= (int)(i/5);
			int nCol= i%5;
			btnItems[i].SetButton(mContext, sprItem, 85+nCol*80, 190+nRow*80, 0);
		}
		for(int i=10; i<30; ++i)
		{
			int nRow= (int)(i/5);
			int nCol= i%5;
			btnItems[i].SetButton(mContext, sprItem, 85+nCol*80, 255+nRow*80, 0);
		}
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
			font.DrawFont(gl, 255, 165, 16f, uInfo.GetPlanetName());
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
			for(GameButton BTN : btnItems)
			{
				if(BTN.pattern!=null)
					BTN.DrawSprite(gInfo);
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
			prgBG1.DrawSprite(gl, gInfo, font);
			prgBG2.DrawSprite(gl, gInfo, font);
			prgBG3.DrawSprite(gl, gInfo, font);
			prgHull.DrawSprite(gl, gInfo, font);
			prgFuel.DrawSprite(gl, gInfo, font);
			prgShield.DrawSprite(gl, gInfo, font);
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
			for(GameButton BTN : btnItems)
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

