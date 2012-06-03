package com.raimsoft.spacetrade.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.raimsoft.spacetrade.Global;


public class Updater
{	
	public void Update()
	{
		this.UpdateBlocks();
		//this.UpdateButtons();
		
		if(Gdx.input.isKeyPressed(Input.Keys.UP))
		{
			
			Global.V_GRAVITY_Y = Global.V_GRAVITY_Y + 1.0f;
			Gdx.app.log("test", "Gravity : "+Global.V_GRAVITY_Y);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
		{
			
			Global.V_GRAVITY_Y = Global.V_GRAVITY_Y - 1.0f;
			Gdx.app.log("test", "Gravity : "+Global.V_GRAVITY_Y);
		}

	}
	
//	private void UpdateButtons()
//	{
//		if(y0<=200 && bMixCount==0)
//		{
//			BMgr.InitBlocks();
//		}
//	}
	
	private void UpdateBlocks()
	{
		
	}
	
}
