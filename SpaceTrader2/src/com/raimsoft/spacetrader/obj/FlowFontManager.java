package com.raimsoft.spacetrader.obj;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

import com.raimsoft.spacetrader.R;
import com.raimsoft.spacetrader.util.SoundManager;

public class FlowFontManager
{
	ArrayList<FlowFont> arrFlow= new ArrayList<FlowFont>();
	GL10 mGL= null;
	Context mContext= null;
	SoundManager soundMgr;
	
	public FlowFontManager(GL10 mGL, Context context)
	{
		super();
		this.mGL = mGL; 
		this.mContext= context;
		
		 soundMgr= new SoundManager(mContext);
		 
		 soundMgr.Create();
		 soundMgr.Load(0, R.raw.pick_items);
	}

	public void AddFlowFont(float x, float y, String strContent)
	{
		soundMgr.Play(0);
		
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
			
			if(FF.nStartY - FF.y >= 38)
				FF.bDraw= false;
			
			FF.y -= 2.5f;
		}
	}
	
	public void Release()
	{
		for(FlowFont FF : arrFlow)
		{
			FF= null;
		}		
		arrFlow.clear();
		
		mContext= null;
		mGL= null;		
	}
}
