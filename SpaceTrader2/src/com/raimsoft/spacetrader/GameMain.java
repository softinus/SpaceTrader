package com.raimsoft.spacetrader;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;
import bayaba.engine.lib.Sprite;

public class GameMain
{
	public float fSensorX = 0; // 가로축 센서 값
	
	public GL10 gl;
	public Context MainContext;
	public GameInfo gInfo;
	
	private Sprite main_bg= new Sprite();
	private Sprite sprShip = new Sprite();
	private GameObject objShip= new GameObject();
	
	public GameMain( Context context, GameInfo info )
	{
		MainContext = context;
		gInfo = info;
	}
	
	public void LoadData()
	{
		//ship_x=gInfo.ScreenX/2;
		main_bg.LoadSprite( gl, MainContext, R.drawable.main_bg, "main_bg.spr" );
		sprShip.LoadSprite( gl, MainContext, R.drawable.resource_2, "ship_1.spr" );
			
		objShip.SetObject( sprShip, 0, 0, 1, 1, 0, 0 );
		//objShip.show= true;
		//objShip.dead= false;
	}
	
	public void DoGame()
	{
		main_bg.PutImage(gInfo, 0, 0, 0);
		//sprShip.PutImage(gInfo, ship_x, 300, 0);
		//objShip.AddFrameLoop(0.5f);		
		objShip.DrawSprite(gInfo);
	}
}
