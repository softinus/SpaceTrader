package com.raimsoft.spacetrader;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import bayaba.engine.lib.GameInfo;

import com.immersion.uhl.Launcher;
import com.raimsoft.spacetrader.scene.EnumScene;
import com.raimsoft.spacetrader.scene.SBase;
import com.raimsoft.spacetrader.scene.SGameWrap;
import com.raimsoft.spacetrader.scene.SMainMenu;

public class Game
{	
	Launcher UHL;
	public Context mContext;
	public GameInfo gInfo;
	private SBase currScene;					// 현재 씬
	

	public Game( Context context, GameInfo info ) 
	{
		mContext = context;
		gInfo = info;
		
		this.ChangeScene();
	}
	
	private void ChangeScene()
	{
		if( currScene == null) 
		{
			currScene= new SMainMenu(mContext, gInfo);
		}
		else if( currScene.GetMode() == EnumScene.E_GAME_WRAP )
		{
			GL10 gl= currScene.backupGL();
			currScene= new SGameWrap(mContext, gInfo);
			currScene.SetGL(gl);
			currScene.LoadData();
		}
	}
	
	public void SetGL(GL10 _gl)
	{
		currScene.SetGL(_gl);
	}
	
	public void LoadData()
	{
		currScene.LoadData();
	}
	

	public void DoGame()
	{	
		if( currScene.bModeChanged )
		{
			this.ChangeScene();
		}
		
		currScene.Update();
		currScene.Render();
	}
	
	
}
