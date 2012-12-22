package com.raimsoft.spacetrader.scene;

import com.raimsoft.spacetrader.R;

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
	
	private ButtonObject btnInfo= new ButtonObject();
	private ButtonObject btnNews= new ButtonObject();
	private ButtonObject btnTrade= new ButtonObject();
	private ButtonObject btnManager= new ButtonObject();
	private ButtonObject btnExit= new ButtonObject();
	
	private Font font = new Font();
	
	@Override
	public void LoadData()
	{
		super.LoadData();
		
		bg_station.LoadSprite(gl, mContext, R.drawable.station, "bg_station.spr");
		sprStationButton.LoadSprite(gl, mContext, R.drawable.station, "station_buttons.spr");
		
		btnInfo.SetButton(sprStationButton, ButtonType.TYPE_ONE_CLICK, 0, 240, 300, 0);
		btnInfo.SetText(0, 120, 18, 0.75f, 0.75f, 0.75f, 32f, "스테이션 정보");
		
		btnNews.SetButton(sprStationButton, ButtonType.TYPE_ONE_CLICK, 0, 240, 400, 0);
		btnNews.SetText(0, 155, 18, 0.75f, 0.75f, 0.75f, 32f, "월드 뉴스");
		
		btnTrade.SetButton(sprStationButton, ButtonType.TYPE_ONE_CLICK, 0, 240, 500, 0);
		btnTrade.SetText(0, 155, 18, 0.75f, 0.75f, 0.75f, 32f, "상품 거래");
		
		btnManager.SetButton(sprStationButton, ButtonType.TYPE_ONE_CLICK, 0, 240, 600, 0);
		btnManager.SetText(0, 155, 18, 0.75f, 0.75f, 0.75f, 32f, "함선 관리");
		
		btnExit.SetButton(sprStationButton, ButtonType.TYPE_ONE_CLICK, 0, 240, 700, 0);
		btnExit.SetText(0, 105, 18, 0.75f, 0.75f, 0.75f, 32f, "스테이션 나가기");
		
		
	}
	@Override
	public void Render()
	{
		super.Render();
		
		bg_station.PutImage(gInfo, 0, 0);
		
		font.BeginFont();		
			btnInfo.DrawSprite(gl, gInfo, font);
			btnNews.DrawSprite(gl, gInfo, font);
			btnTrade.DrawSprite(gl, gInfo, font);
			btnManager.DrawSprite(gl, gInfo, font);
			btnExit.DrawSprite(gl, gInfo, font);
		font.EndFont();
		
	}
	@Override
	public void Update() 
	{
		super.Update();
		
		
		if( btnInfo.click == ButtonType.STATE_CLK_BUTTON )
		{
			btnInfo.ResetButton();
		}
	}
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		
		this.SetScene(EnumScene.E_MAIN);
	}

	
	
}
