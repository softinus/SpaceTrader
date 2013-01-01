package com.raimsoft.spacetrader.scene;

import android.content.Context;
import android.media.MediaPlayer;
import bayaba.engine.lib.Font;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.Sprite;

import com.raimsoft.spacetrader.R;
import com.raimsoft.spacetrader.SpaceTrader;
import com.raimsoft.spacetrader.data.UserInfo;
import com.raimsoft.spacetrader.obj.GameButton;

public class SMainMenu extends SBase
{
	public SMainMenu(Context mContext, GameInfo gInfo)
	{
		super(mContext, gInfo);
	}

	private Sprite main_bg= new Sprite();
	private Sprite sprTitle= new Sprite();
	private Sprite sprButton= new Sprite();
	private GameButton btnPlay= new GameButton();
	private GameButton btnHelp= new GameButton();
	private GameButton btnRank= new GameButton();
	private GameButton btnOption= new GameButton();
	private GameButton btnExit= new GameButton();
	
	private MediaPlayer Music;
	
	private UserInfo s_uInfo;
	
	private Font font = new Font();

	
	@Override
	public void LoadData()
	{
		super.LoadData();
		
		s_uInfo= UserInfo.GetInstance();
		
		Music = MediaPlayer.create(mContext, R.raw.main_theme);
		Music.setLooping(true);
		Music.start();
		

		
		
		main_bg.LoadSprite( gl, mContext, R.drawable.main_bg, "main_bg.spr" );		
		sprTitle.LoadSprite( gl, mContext, R.drawable.main_buttons, "main_buttons.spr" );
		sprButton.LoadSprite( gl, mContext, R.drawable.main_buttons, "main_buttons.spr" );
		
		
		btnPlay.SetButton(mContext, sprButton, 152, 330, 1);
		btnHelp.SetButton(mContext, sprButton, 152, 400, 3);
		btnRank.SetButton(mContext, sprButton, 152, 470, 5);
		btnOption.SetButton(mContext, sprButton, 152, 540, 7);
		btnExit.SetButton(mContext, sprButton, 152, 610, 9);
		
	}

	@Override
	public void Update()
	{
		super.Update();
		
		
		btnPlay.ButtonUpdate(0);
		btnHelp.ButtonUpdate(0);
		btnRank.ButtonUpdate(0);
		btnOption.ButtonUpdate(0);
		btnExit.ButtonUpdate(0);

				
		if( btnPlay.CheckOver() )
		{
			if(s_uInfo.GetLogin())
				this.SetScene(EnumScene.E_GAME_SYSTEMMAP);
			else
				((SpaceTrader)mContext).LoadingHandler.sendEmptyMessage(0);
		}
		
		if( btnHelp.CheckOver() )
		{
			this.SetScene(EnumScene.E_GAME_STORY);
		}
		
		if( btnRank.CheckOver() )
		{
			this.SetScene(EnumScene.E_GAME_GALAXYMAP);
		}
		
		if( btnOption.CheckOver() )
		{
			//this.SetScene(EnumScene.E_GAME_SYSTEMMAP);
		}
		
		if( btnExit.CheckOver() )
		{
			System.exit(0);
		}
		
//		nX= (int) GlobalInput.fTouchX_End;
//		nY= (int) GlobalInput.fTouchY_End;
//		
//		Log.d("GLView::onTouchEvent", "X_END : "+nX + "     Y_END : "+nY);
//		
//		if( btnPlay.CheckPos(nX, nY) && btnPlay.motion==2 )
//			this.SetMode(EnumScene.E_GAME_WRAP);
//
//		if( btnHelp.CheckPos(nX, nY) && btnHelp.motion==4 )
//			this.SetMode(EnumScene.E_GAME_MAP);
//		
//		if( btnRank.CheckPos(nX, nY) && btnRank.motion==6 )
//		
//		if( btnOption.CheckPos(nX, nY) && btnOption.motion==8 )
//		
//		if( btnExit.CheckPos(nX, nY) && btnExit.motion==10 )
//			System.exit(0);
	}

	

	@Override
	public void Render()
	{
		super.Render();
		
		main_bg.PutImage(gInfo, 0, 0);
		sprTitle.PutImage(gInfo, 113, 55, 0);
		
		btnPlay.DrawSprite(gInfo);
		btnHelp.DrawSprite(gInfo);
		btnRank.DrawSprite(gInfo);
		btnOption.DrawSprite(gInfo);
		btnExit.DrawSprite(gInfo);
		
		font.BeginFont();
			font.DrawFont(gl, 0, 0, 16f, "Dev version : "+mContext.getString(R.string.version));
		font.EndFont();
	}
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		
		System.exit(0);
	}
	
	@Override
	public void ReleaseMemory()
	{
		super.ReleaseMemory();
		
		sprButton.Release();
		sprTitle.Release();
		//Sound.Destroy();
		Music.release();
		
	}
}
