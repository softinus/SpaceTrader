package com.raimsoft.spacetrader;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import bayaba.engine.lib.GameInfo;

import com.raimsoft.spacetrader.scene.EnumScene;
import com.raimsoft.spacetrader.scene.SBase;
import com.raimsoft.spacetrader.scene.SGalaxyMap;
import com.raimsoft.spacetrader.scene.SGameWrap;
import com.raimsoft.spacetrader.scene.SLogo;
import com.raimsoft.spacetrader.scene.SMainMenu;
import com.raimsoft.spacetrader.scene.SStartStory;
import com.raimsoft.spacetrader.scene.SStation;
import com.raimsoft.spacetrader.scene.SSystemMap;

public class Game
{	
	//Launcher UHL;
	public Context mContext;
	public GameInfo gInfo;
	private SBase currScene;					// 현재 씬
	

	public Game( Context context, GameInfo info ) 
	{
		mContext = context;
		gInfo = info;
		
		this.ChangeScene();
	}
	
	public void ChangeScene()
	{
		if( currScene == null) 	// 처음 시작시 어디로 갈 것인가?
		{
			currScene= new SLogo(mContext, gInfo);
			//currScene= new SMainMenu(mContext, gInfo);
		}
		else if( currScene.GetMode() == EnumScene.E_MAIN )
		{
			GL10 gl= currScene.backupGL();
			currScene.ReleaseMemory();
			currScene= new SMainMenu(mContext, gInfo);
			currScene.SetGL(gl);
			currScene.LoadData();
		}
		else if( currScene.GetMode() == EnumScene.E_GAME_STORY )
		{
			GL10 gl= currScene.backupGL();
			currScene.ReleaseMemory();
			currScene= new SStartStory(mContext, gInfo);
			currScene.SetGL(gl);
			currScene.LoadData();
		}
		else if( currScene.GetMode() == EnumScene.E_GAME_WRAP )
		{
			GL10 gl= currScene.backupGL();
			currScene.ReleaseMemory();
			currScene= new SGameWrap(mContext, gInfo);
			currScene.SetGL(gl);
			currScene.LoadData();
		}
		else if( currScene.GetMode() == EnumScene.E_GAME_DOCKING )
		{
			GL10 gl= currScene.backupGL();
			currScene.ReleaseMemory();
			currScene= new SStation(mContext, gInfo);
			currScene.SetGL(gl);
			currScene.LoadData();
		}
		else if( currScene.GetMode() == EnumScene.E_GAME_SYSTEMMAP )
		{
			GL10 gl= currScene.backupGL();
			currScene.ReleaseMemory();
			currScene= new SSystemMap(mContext, gInfo);
			currScene.SetGL(gl);
			currScene.LoadData();
		}
		else if( currScene.GetMode() == EnumScene.E_GAME_GALAXYMAP )
		{
			GL10 gl= currScene.backupGL();
			currScene.ReleaseMemory();
			currScene= new SGalaxyMap(mContext, gInfo);
			currScene.SetGL(gl);
			currScene.LoadData();
		}
	}
	
	public void onBackPressed()
	{
		currScene.onBackPressed();
	}
	
	public void onTouchEvent()
	{
		currScene.onTouchEvent();
	}
	
	// 설정한 씬으로 강제 전환 (상위클래스에서 주로 이용)
	public void ChangeScene(EnumScene eMode)
	{
		currScene.SetScene(eMode);
		this.ChangeScene();
	}
	
	public void SetGL(GL10 _gl)
	{
		currScene.SetGL(_gl);
	}
	
	public void LoadData()
	{
		currScene.LoadData();
	}
	

	// 현재 씬 상태를 가져옴
	public EnumScene GetCurrSceneEnum()
	{
		return currScene.GetMode();
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
