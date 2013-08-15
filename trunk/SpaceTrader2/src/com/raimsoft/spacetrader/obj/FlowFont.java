package com.raimsoft.spacetrader.obj;

import bayaba.engine.lib.Font;


public class FlowFont extends Font
{
	float nStartX, nStartY;
	float x,y;
	String strContent;
	boolean bDraw= false;

	public void AddFlowFont(float x, float y, String strContent)
	{
		this.strContent= strContent;
		this.nStartX= this.x= x;
		this.nStartY= this.y= y;
		
		bDraw= true;
	}
	
	@Override
	public void BeginFont()
	{
		super.BeginFont();
	}
	@Override
	public void EndFont()
	{
		// TODO Auto-generated method stub
		super.EndFont();
	}
	
}
