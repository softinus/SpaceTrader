package com.raimsoft.spacetrader.scene;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.ProgressBar;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;
import bayaba.engine.lib.Sprite;

import com.immersion.uhl.Launcher;
import com.raimsoft.spacetrader.GlobalInput;
import com.raimsoft.spacetrader.R;
import com.raimsoft.spacetrader.obj.BaseFleet;
import com.raimsoft.spacetrader.obj.Meteor;
import com.raimsoft.spacetrader.obj.ProgressMeter;
import com.raimsoft.spacetrader.obj.Radar;
import com.raimsoft.spacetrader.util.SoundManager;

public class SGameWrap extends SBase
{
	private Sprite sprStar = new Sprite();
	private Sprite sprShip= new Sprite();
	private Sprite sprMetoer= new Sprite();
	
	private boolean bReleased= false;
	private int nMeteorCount= 0;
	
	private BaseFleet objShip;					// 나의 함선
	private Radar objRader;
	private ProgressMeter objProgress;
	
	private Queue<GameObject> qStar= new LinkedList<GameObject>();
	private Queue<Meteor> qMetoer= new LinkedList<Meteor>();
	
	private Random rand = new Random();
	private MediaPlayer Music;
	private Launcher UHL;
	
	public SGameWrap(Context _context, GameInfo _Info)
	{
		super(_context, _Info);
		
		UHL= new Launcher(_context);
		this.eMode= EnumScene.E_GAME_WRAP;
	}
	
	@Override
	public void LoadData()
	{
		super.LoadData(); 
		
		Music = MediaPlayer.create(mContext, R.raw.game);
		Music.setLooping(true);
		Music.start();
		
		
		sprShip.LoadSprite( gl, mContext, R.drawable.resource_2, "ship_1.spr" );
		sprStar.LoadSprite( gl, mContext, R.drawable.resource_2, "star_1.spr" );
		sprMetoer.LoadSprite( gl, mContext, R.drawable.resource_2, "meteor.spr" );
		
		objRader= new Radar(gl, gInfo, mContext);
		objProgress= new ProgressMeter(gl, gInfo, mContext);
	
		objShip= new BaseFleet(gl, mContext);
		objShip.SetObject( sprShip, 0, 0, gInfo.ScreenX/2, gInfo.ScreenY-100, 0, 0 );
		objShip.scalex= 0.5f;
		objShip.scaley= 0.5f;
		
		for(int i=0; i<=50; ++i)
			qStar.offer(new GameObject());
		
		for(int i=0; i<=10; ++i)
		{
			++nMeteorCount;
			qMetoer.offer(new Meteor( rand.nextInt(7)-3 ));	// 앵글 -4 ~ 4 범위
		}
		
		this.MakeStar();
		this.MakeMetoer();
	}

	@Override
	public void Render()
	{
		if(bReleased) return;
		
		super.Render();
		
		for(GameObject GO : qStar)
			GO.DrawSprite(gInfo);
				
		for(Meteor MTO : qMetoer)
			MTO.DrawSprite(gl, gInfo);
				
		objShip.DrawSprite(gInfo);
		objRader.DrawObjects(gInfo);
		objProgress.DrawObjects(gInfo);
	}

	@Override
	public void Update()
	{
		if(bReleased) return;
		
		super.Update();
		
		this.UpdateStar();
		this.UpdateMetoer();
		this.UpdateShip();
		
		objRader.UpdateObjects();
		objProgress.UpdateObjects();
	}
	
	private void MakeStar()
	{		
		int nCount= 0;
		for(GameObject GO : qStar)
		{
			++nCount;
			GO.SetObject( sprStar, 0, 0, rand.nextInt((int)gInfo.ScreenX), -1*nCount*(rand.nextInt(50)+25)+gInfo.ScreenY, 0, 0 );
			float fRandomScale= rand.nextFloat();
			GO.scalex= fRandomScale;
			GO.scaley= fRandomScale;
		}
	}
	
	private void MakeMetoer()
	{		
		int nCount= 0;
		for(Meteor MTO : qMetoer)
		{
			++nCount;
			MTO.SetObject( sprMetoer, 0, 0, rand.nextInt((int)gInfo.ScreenX), -1*nCount*500, rand.nextInt(3), 0 );
		}
	}
	
	

	private void UpdateShip()
	{
		if(objShip.bDestroyed)	// 터지면 함선 업데이트 안함
			return;
		
		objProgress.fCurr += objShip.GetVelocity();	// 프로그레스바에 속도만큼 계산해준다.
		
		objShip.SetCrash(false, 0, 0); 
		for(Meteor MTO : qMetoer)
		{
			if( objShip.CheckPos( (int)MTO.x , (int)MTO.y ) )
			{
				gInfo.SetQuake(1000, 6, 2);
				gInfo.DoQuake();
				objShip.SetCrash(true, (int)MTO.x, (int)MTO.y);
			}
		}
		
		// 화면 밖으로 나가는 것 체크
		if(objShip.x-GlobalInput.fSensorX*2.0f < 0)
			return;
		
		if(objShip.x-GlobalInput.fSensorX*2.0f > gInfo.ScreenX)
			return;
		
		objShip.MoveSensorX( GlobalInput.fSensorX * objShip.getHandeling() );

		
	}
	
	private void UpdateStar()
	{
		for(GameObject GO : qStar)
			GO.y += objShip.GetVelocity();
				
		if( qStar.peek() != null )
		{
			if( qStar.peek().y > gInfo.ScreenY )
			{
				qStar.poll();
				
				GameObject star= new GameObject();
				star.SetObject( sprStar, 0, 0, rand.nextInt((int)gInfo.ScreenX), -1*(rand.nextInt(50)+25), 0, 0 );
				float fRandomScale= rand.nextFloat();
				star.scalex= fRandomScale;
				star.scaley= fRandomScale;
				qStar.offer(star);
			}
		}
	}
	
	private void UpdateMetoer()
	{
		for(Meteor MTO : qMetoer)
		{
			MTO.angle += MTO.lfAngle;
			MTO.y += objShip.GetVelocity()/2;
		}
				
		if( qMetoer.peek() != null )
		{
			//Log.d("qMetoer.peek()", "X : "+qMetoer.peek().x+"    Y : "+qMetoer.peek().y);
			if( qMetoer.peek().y > gInfo.ScreenY + qMetoer.peek().GetYsize()/2 )
			{
				++nMeteorCount;	// 메테오 지금까지 나온 카운트
				qMetoer.poll();
				
				Meteor mto= new Meteor( rand.nextInt(9)-4 );
				mto.SetObject( sprMetoer, 0, 0, rand.nextInt((int)gInfo.ScreenX), -1* rand.nextInt(2500)-150, rand.nextInt(3), 0 );
				Log.d("Create Meteor", "------> offer ------> Y : "+mto.y);
				qMetoer.offer(mto);
				
//				for(int i=0; i<nMeteorCount/10; ++i)
//				{
//					int nRatio= 1;
//					if(nMeteorCount > 10)
//						nRatio= (int) nMeteorCount/10;
//					Meteor mto2= new Meteor( rand.nextInt(9)-4 );
//					mto2.SetObject( sprMetoer, 0, 0, rand.nextInt((int)gInfo.ScreenX), -1* rand.nextInt(2500/nRatio)-150, rand.nextInt(3), 0 );
//					Log.d("Create Meteor", "------> offer ------> Y : "+mto2.y);
//					qMetoer.offer(mto2);
//				}
			}
		}
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		
		this.SetScene(EnumScene.E_MAIN);
	}

	@Override
	public void ReleaseMemory()
	{
		super.ReleaseMemory();
		
		bReleased= true;
		
		objShip.Release();
		sprMetoer.Release();
		sprShip.Release();
		sprStar.Release();
		Music.release();
	}

}
