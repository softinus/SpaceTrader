package com.raimsoft.spacetrader.obj;

import javax.microedition.khronos.opengles.GL10;

import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;

public class Star extends GameObject
{
//	public Star()
//	{
//		
//	}

	public void DrawSprite(GL10 gl, GameInfo info)
	{	
		if( (0-this.GetYsize()*2 < this.y) && (info.ScreenY+this.GetYsize() > this.y) )
			super.DrawSprite(info);
	}
}
