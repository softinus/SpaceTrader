package com.raimsoft.spacetrader.scene;

import android.content.Context;
import android.media.MediaPlayer;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.Sprite;

import com.raimsoft.spacetrader.GlobalInput;
import com.raimsoft.spacetrader.R;
import com.raimsoft.spacetrader.obj.GameButton;
import com.raimsoft.spacetrader.util.SoundManager;

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
	private SoundManager Sound;

	
	@Override
	public void LoadData()
	{
		super.LoadData();
		
		Music = MediaPlayer.create(mContext, R.raw.main_theme);
		Music.setLooping(true);
		Music.start();
		
		Sound= new SoundManager(mContext);
		Sound.Create();
		Sound.Load(0, R.raw.button1);		
		
		
		main_bg.LoadSprite( gl, mContext, R.drawable.main_bg, "main_bg.spr" );		
		sprTitle.LoadSprite( gl, mContext, R.drawable.main_buttons, "main_buttons.spr" );
		sprButton.LoadSprite( gl, mContext, R.drawable.main_buttons, "main_buttons.spr" );
		
		btnPlay.SetObject(sprButton, 0, 0, 152, 330, 1	, 0);
		btnHelp.SetObject(sprButton, 0, 0, 152, 400, 3, 0);
		btnRank.SetObject(sprButton, 0, 0, 152, 470, 5, 0);
		btnOption.SetObject(sprButton, 0, 0, 152, 540, 7, 0);
		btnExit.SetObject(sprButton, 0, 0, 152, 610, 9, 0);
	}

	@Override
	public void Update()
	{
		super.Update();
		
		int nX= (int) GlobalInput.fTouchX;
		int nY= (int) GlobalInput.fTouchY;
		boolean bTouch= GlobalInput.bTouch;
				
		if( btnPlay.CheckPos(nX, nY) && bTouch )
		{
			btnPlay.motion= 2;
			Sound.Play(0);
			this.SetScene(EnumScene.E_GAME_WRAP);
		}
		else
			btnPlay.motion= 1;

		if( btnHelp.CheckPos(nX, nY) && bTouch )
		{
			Sound.Play(0);
			btnHelp.motion= 4;
			this.SetScene(EnumScene.E_GAME_MAP);
		}
		else
			btnHelp.motion= 3;
		
		if( btnRank.CheckPos(nX, nY) && bTouch )
		{
			Sound.Play(0);
			btnRank.motion= 6;
		}
		else
			btnRank.motion= 5;
		
		if( btnOption.CheckPos(nX, nY) && bTouch )
		{
			Sound.Play(0);
			btnOption.motion= 8;
		}
		else
			btnOption.motion= 7;
		
		if( btnExit.CheckPos(nX, nY) && bTouch )
		{
			Sound.Play(0);
			btnExit.motion= 10;
			System.exit(0);
		}
		else
			btnExit.motion= 9;
		
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
		Sound.Destroy();
		Music.release();
		
	}
}
