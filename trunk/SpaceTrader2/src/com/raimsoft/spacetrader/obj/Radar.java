package com.raimsoft.spacetrader.obj;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;
import bayaba.engine.lib.Sprite;

import com.raimsoft.spacetrader.R;

public class Radar
{
	private GameObject objBoard, objArrow;
	private Sprite sprRadar= new Sprite();

	/**
	 * 
	 */
	public Radar(GL10 gl, GameInfo gInfo, Context mContext)
	{
		super();
				
		sprRadar.LoadSprite( gl, mContext, R.drawable.radar, "radar.spr" );
		
		objBoard= new GameObject();
		objBoard.SetObject( sprRadar, 0, 0, gInfo.ScreenX/2, gInfo.ScreenY/2, 0, 0);
		objArrow= new GameObject();
		objArrow.SetObject( sprRadar, 0, 0, gInfo.ScreenX/2, gInfo.ScreenY/2, 1, 0);
		
		objBoard.x= gInfo.ScreenX - 120 * gInfo.ScalePx;
		objBoard.y= 120 * gInfo.ScalePy;
		objArrow.x= gInfo.ScreenX - 120 * gInfo.ScalePx;
		objArrow.y= 120 * gInfo.ScalePy;
		
		objBoard.scalex= 0.3f;
		objBoard.scaley= 0.3f;
		objArrow.scalex= 0.3f;
		objArrow.scaley= 0.3f;
		
		objBoard.trans= 0.5f;
		objArrow.trans= 0.5f;
	}
	
	public void UpdateObjects()
	{
		objArrow.angle += 2.5f;
	}
	
	public void DrawObjects(GameInfo gInfo)
	{
		objBoard.DrawSprite(gInfo);
		objArrow.DrawSprite(gInfo);
	}
	

}
