package com.raimsoft.spacetrader.obj;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class FlowFontManager
{
	ArrayList<FlowFont> arrFlow= new ArrayList<FlowFont>();
	GL10 mGL= null;
	
	public FlowFontManager(GL10 mGL)
	{
		super();
		this.mGL = mGL;
	}

	public void AddFlowFont(float x, float y, String strContent)
	{
		FlowFont font= new FlowFont();
		font.AddFlowFont(x, y, strContent);
		
		arrFlow.add(font);
	}
	
	public void DrawFlowFonts()
	{
		for(FlowFont FF : arrFlow)
		{
			if(!FF.bDraw)	// 그리는 상태가 아니면 건너 뛴다.
				continue;
			
			FF.BeginFont();
			
			FF.DrawColorFont(mGL, FF.x, FF.y, 0.9f, 0.8f, 0.8f, 22.0f, FF.strContent);
			
			FF.EndFont();
		}
	}
	
	public void UpdateFlowFonts()
	{
		for(FlowFont FF : arrFlow)
		{
			if(!FF.bDraw)	// 그리는 상태가 아니면 건너 뛴다.
				continue;
			
			if(FF.nStartY - FF.y >= 30)
				FF.bDraw= false;
			
			FF.y -= 3;
		}
	}
}
