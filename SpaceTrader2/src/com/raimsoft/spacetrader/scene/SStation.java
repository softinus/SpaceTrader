package com.raimsoft.spacetrader.scene;

import com.raimsoft.spacetrader.R;
import com.raimsoft.spacetrader.obj.GameButton;

import android.content.Context;
import android.media.MediaPlayer;
import bayaba.engine.lib.ButtonObject;
import bayaba.engine.lib.ButtonType;
import bayaba.engine.lib.Font;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.Sprite;

public class SStation extends SBase
{

	public SStation(Context mContext, GameInfo gInfo)
	{
		super(mContext, gInfo);
		
	}
	
	private Sprite bg_station= new Sprite();
	private Sprite sprStationButton= new Sprite();
	private Sprite sprStationUI= new Sprite();
	
	private GameButton btnInfo= new GameButton();
	private GameButton btnNews= new GameButton();
	private GameButton btnTrade= new GameButton();
	private GameButton btnManage= new GameButton();
	private GameButton btnExit= new GameButton();
	
	private MediaPlayer Music;
	
	private Font font = new Font();
	private int nMenu= 0;
	
	@Override
	public void LoadData()
	{
		super.LoadData();
		
		Music = MediaPlayer.create(mContext, R.raw.station_1);
		Music.setLooping(true);
		Music.start();
		
		bg_station.LoadSprite(gl, mContext, R.drawable.station, "bg_station.spr");
		sprStationButton.LoadSprite(gl, mContext, R.drawable.station, "station_buttons.spr");
		sprStationUI.LoadSprite(gl, mContext, R.drawable.station, "station_ui.spr");
		
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
		case 0:
			btnInfo.DrawButtonWithText(gInfo, gl, font);
			btnNews.DrawButtonWithText(gInfo, gl, font);
			btnTrade.DrawButtonWithText(gInfo, gl, font);
			btnManage.DrawButtonWithText(gInfo, gl, font);
			btnExit.DrawButtonWithText(gInfo, gl, font);
			break;
		case 1:
			sprStationUI.PutAni(gInfo, nX, nY, 0, 0);
			break;
		case 2:
			sprStationUI.PutAni(gInfo, nX, nY, 1, 0);
			break;
		case 3:
			sprStationUI.PutAni(gInfo, nX, nY, 2, 0);
			break;
		case 4:
			sprStationUI.PutAni(gInfo, nX, nY, 3, 0);
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
		
		if(nMenu!=0)	// 메뉴선택중 아니면
			return;
		
		btnInfo.ButtonUpdate();
		btnNews.ButtonUpdate();
		btnTrade.ButtonUpdate();
		btnManage.ButtonUpdate();
		btnExit.ButtonUpdate();
		
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
