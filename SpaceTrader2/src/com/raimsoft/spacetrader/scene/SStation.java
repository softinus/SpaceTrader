package com.raimsoft.spacetrader.scene;

import com.raimsoft.spacetrader.R;
import com.raimsoft.spacetrader.obj.GameButton;

import android.content.Context;
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
	
	private GameButton btnInfo= new GameButton();
	private GameButton btnNews= new GameButton();
	private GameButton btnTrade= new GameButton();
	private GameButton btnManager= new GameButton();
	private GameButton btnExit= new GameButton();
	
	private Font font = new Font();
	
	@Override
	public void LoadData()
	{
		super.LoadData();
		
		bg_station.LoadSprite(gl, mContext, R.drawable.station, "bg_station.spr");
		sprStationButton.LoadSprite(gl, mContext, R.drawable.station, "station_buttons.spr");
		
		btnInfo.SetButton(mContext, sprStationButton, 240, 300, 0);
		btnInfo.SetTextCenter(32f, "스테이션 정보");
		
		btnNews.SetButton(mContext, sprStationButton, 240, 400, 0);
		btnNews.SetTextCenter(32f, "월드 뉴스");
		
		btnTrade.SetButton(mContext, sprStationButton, 240, 500, 0);
		btnTrade.SetTextCenter(32f, "상품 거래");
		
		btnManager.SetButton(mContext, sprStationButton, 240, 600, 0);
		btnManager.SetTextCenter(32f, "함선 관리");
		
		btnExit.SetButton(mContext, sprStationButton, 240, 700, 0);
		btnExit.SetTextCenter(32f, "스테이션 나가기");
		
		
	}
	@Override
	public void Render()
	{
		super.Render();
		
		bg_station.PutImage(gInfo, 0, 0);
		
		font.BeginFont();		
			btnInfo.DrawButtonWithText(gInfo, gl, font);
			btnNews.DrawButtonWithText(gInfo, gl, font);
			btnTrade.DrawButtonWithText(gInfo, gl, font);
			btnManager.DrawButtonWithText(gInfo, gl, font);
			btnExit.DrawButtonWithText(gInfo, gl, font);
		font.EndFont();
		
	}
	@Override
	public void Update() 
	{
		super.Update();
		
		btnInfo.ButtonUpdate();
		btnNews.ButtonUpdate();
		btnTrade.ButtonUpdate();
		btnManager.ButtonUpdate();
		btnExit.ButtonUpdate();
		
		if(btnExit.CheckOver())
		{
			SetScene(EnumScene.E_GAME_SYSTEMMAP);
		}
	}
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		
		this.SetScene(EnumScene.E_MAIN);
	}

	
	
}
