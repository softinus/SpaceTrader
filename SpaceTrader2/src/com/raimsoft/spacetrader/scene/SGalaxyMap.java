package com.raimsoft.spacetrader.scene;

import android.content.Context;
import bayaba.engine.lib.Font;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.Sprite;

import com.raimsoft.spacetrader.R;
import com.raimsoft.spacetrader.data.GlobalInput;

public class SGalaxyMap extends SBase
{
	float tX= 0.0f, tY= 0.0f;
	int x1,y1, x2,y2, x3,y3, x4,y4;
	Font fntCrood;
	private Sprite sprGalaxyMapBG;

	public SGalaxyMap(Context mContext, GameInfo gInfo)
	{
		super(mContext, gInfo);
		
		fntCrood= new Font();
		sprGalaxyMapBG= new Sprite();
		
	}

	@Override
	public void LoadData()
	{
		super.LoadData();
		sprGalaxyMapBG.LoadSprite(gl, mContext, R.drawable.worldmap_bg, "worldmap_bg.spr");
	}

	@Override
	public void Render()
	{
		super.Render();
		
		sprGalaxyMapBG.PutImage(gInfo, 0, 0);
		
		
		gInfo.DrawLine(gl, x1, y1, x2, y2, 0, 255, 0, 0, 3.5f);
		gInfo.DrawLine(gl, x3, y3, x4, y4, 0, 255, 0, 0, 3.5f);
		
		fntCrood.BeginFont();
		//fntCrood.DrawFont(gl, 0.0f, 0.0f, 25.0f, "("+(int)GlobalInput.fTouchX+", "+(int)GlobalInput.fTouchY+")");
		fntCrood.DrawFont(gl, (int)tX+10, (int)tY-25, 20.0f, "("+(int)tX+", "+(int)tY+")");
		fntCrood.EndFont();
		
	}

	@Override
	public void Update()
	{
		super.Update();
		
		tX= GlobalInput.fTouchX;
		tY= GlobalInput.fTouchY;
		
		x1= 0;
		x2= (int)gInfo.ScreenX;
		
		y1=y2= (int)tY;
		
		x3=x4= (int)tX;
		y3= 0;
		y4= (int)gInfo.ScreenY;
	}
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		
		this.SetScene(EnumScene.E_MAIN);
	}

}
