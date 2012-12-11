package com.raimsoft.spacetrader.obj;

import javax.microedition.khronos.opengles.GL10;

import com.raimsoft.spacetrader.R;

import android.content.Context;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;
import bayaba.engine.lib.Sprite;

public class ProgressMeter
{
	Sprite sprProgress= new Sprite();
	GameObject objOutline= new GameObject();
	GameObject objProgress= new GameObject();
	GameObject objFlag= new GameObject();
	
	public float fDistanceKM= 2200000.0f;	//움직이는거리
	public float fCurr= 0.0f;
	private float fDistance= 0.0f;
	public boolean bArrived= false;
	
	
	public ProgressMeter(GL10 gl, GameInfo gInfo, Context mContext)
	{
		sprProgress.LoadSprite(gl, mContext, R.drawable.progress, "progress.spr");
		
		objOutline.SetObject(sprProgress, 0, 0, 30, gInfo.ScreenY-25, 0, 0);
		objProgress.SetObject(sprProgress, 0, 0, 30,  gInfo.ScreenY-25, 1, 0);
		objFlag.SetObject(sprProgress, 0, 0, 40, gInfo.ScreenY-255, 2, 0);
		
		objProgress.trans= 0.5f;
		objOutline.trans= 0.5f;
		objFlag.trans= 0.5f;
	}
	
	public void UpdateObjects()
	{
		if(bArrived)	// 도착하면 끝
			return;
		
		if(fDistance >= 1.0f)
			bArrived= true;
			
		
		fDistance= fCurr / fDistanceKM * 100;
		objProgress.scaley= fDistance;
	}
	
	public void DrawObjects(GameInfo gInfo)
	{
		objOutline.DrawSprite(gInfo);
		objProgress.DrawSprite(gInfo);
		objFlag.DrawSprite(gInfo);
	}
	
}
