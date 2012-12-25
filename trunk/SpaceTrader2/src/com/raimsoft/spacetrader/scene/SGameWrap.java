package com.raimsoft.spacetrader.scene;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;
import bayaba.engine.lib.Sprite;

import com.immersion.uhl.Launcher;
import com.raimsoft.spacetrader.GlobalInput;
import com.raimsoft.spacetrader.R;
import com.raimsoft.spacetrader.obj.BaseFleet;
import com.raimsoft.spacetrader.obj.Meteor;
import com.raimsoft.spacetrader.obj.Missile;
import com.raimsoft.spacetrader.obj.ProgressMeter;
import com.raimsoft.spacetrader.obj.Radar;
import com.raimsoft.spacetrader.obj.Star;

public class SGameWrap extends SBase
{
	private static int MAX_PARTICLE = 14;
	private Random MyRand = new Random();
	
	private Sprite sprStar = new Sprite();
	private Sprite sprShip= new Sprite();
	private Sprite sprMetoer= new Sprite();
	private Sprite sprMissile= new Sprite();
	private Sprite sprStation= new Sprite();
	private Sprite sprGlow= new Sprite();
	
	private boolean bReleased= false;
	private int nMeteorCount= 0;
	
	private BaseFleet objShip;					// 나의 함선
	private Missile	objMissile;					// 미사일
	private Radar objRader;						// 레이더
	private ProgressMeter objProgress;			// 프로그레스바
	private GameObject	objStation;				// 스테이션
	
	private GameObject Particle_BOOM[] = new GameObject[MAX_PARTICLE];
	
	private Queue<Star> qStar= new LinkedList<Star>();
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
		
		Music = MediaPlayer.create(mContext, R.raw.space_music);
		Music.setLooping(true);
		Music.start();
		
		
		sprShip.LoadSprite( gl, mContext, R.drawable.resource_2, "ship_1.spr" );
		sprStar.LoadSprite( gl, mContext, R.drawable.resource_2, "star_1.spr" );
		sprMetoer.LoadSprite( gl, mContext, R.drawable.resource_2, "meteor.spr" );
		sprMissile.LoadSprite(gl, mContext, R.drawable.resource_2, "missile_1.spr");
		sprStation.LoadSprite(gl, mContext, R.drawable.station_dummy1, "station_1.spr");
		sprGlow.LoadSprite(gl, mContext, R.drawable.glow, "glow.spr");
		
		objRader= new Radar(gl, gInfo, mContext);
		objProgress= new ProgressMeter(gl, gInfo, mContext);
	
		objShip= new BaseFleet(gl, mContext);
		objShip.SetObject( sprShip, 0, 0, gInfo.ScreenX/2, gInfo.ScreenY+60, 0, 0 );
		objShip.scalex= 0.5f;
		objShip.scaley= 0.5f;

		objMissile= new Missile(gl, mContext);
		objMissile.SetObject(sprMissile, 0, 0, objShip.x, objShip.y, 0, 0);
		objMissile.scalex= 0.5f;
		objMissile.scaley= 0.5f;
		
		objStation= new GameObject();
		objStation.SetObject(sprStation, 0, 0, 100, -320, 0, 0);
//		objStation.scalex= 0.85f;
//		objStation.scaley= 0.85f;
		
		for ( int i = 0; i < MAX_PARTICLE; i++ ) Particle_BOOM[i] = new GameObject();
		
		
		
		for(int i=0; i<=50; ++i)
			qStar.offer(new Star());
		
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

		
		for(Star GO : qStar)
			GO.DrawSprite(gInfo);
		
		if(objProgress.GetPercentToDestination() >= 70.0f)
			objStation.DrawSprite(gInfo);		
				
		for(Meteor MTO : qMetoer)
			MTO.DrawSprite(gl, gInfo);
				
		objMissile.DrawSprite(gInfo);
		objShip.DrawSprite(gInfo);
		
		for ( int i = 0; i < MAX_PARTICLE; i++ )	// 파괴 이펙트
			if ( Particle_BOOM[i].dead == false ) Particle_BOOM[i].DrawSprite( gInfo );
		
		objRader.DrawObjects(gInfo);
		objProgress.DrawObjects(gInfo);
		
		
	}

	@Override
	public void Update()
	{
		if(bReleased) return;
		
		super.Update();
		
		if(!objProgress.bArrived)
			this.UpdateStar();
		
		this.UpdateMetoer();
		this.UpdateShip();
		this.UpdateMissile();
		this.UpdateGlow();
		
		objRader.UpdateObjects();
		objProgress.UpdateObjects();
		objMissile.UpdateObjects(gInfo);
	}
	
	private void UpdateGlow()
	{		
		for ( int i = 0; i < MAX_PARTICLE; i++ )
		{
			if ( Particle_BOOM[i].dead == false )
			{
				Particle_BOOM[i].Zoom( gInfo, 0.02f, 0.02f );
				
				Particle_BOOM[i].trans -= 0.05f;
				if ( Particle_BOOM[i].trans <= 0 ) Particle_BOOM[i].dead = true;			
				
				Particle_BOOM[i].MovebyAngle( gInfo, Particle_BOOM[i].angle, 7.0f );
			}
		}
	}
	
	private void MakeGlow(float x, float y)
	{		
		for ( int i = 0; i < MAX_PARTICLE; i++ )
		{
			if ( Particle_BOOM[i].dead == true )
			{
				Particle_BOOM[i].SetObject( sprGlow, 0, 0, x, y, 0, 8 );
				Particle_BOOM[i].dead = false;
				Particle_BOOM[i].angle = (float)MyRand.nextInt( 360 );
				Particle_BOOM[i].effect = 1;
			}
		}

	}
	
	// 별 만듦
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
	
	// 소행성 만든다.
	private void MakeMetoer()
	{		
		int nCount= 0;
		for(Meteor MTO : qMetoer)
		{
			++nCount;
			MTO.SetObject( sprMetoer, 0, 0, rand.nextInt((int)gInfo.ScreenX), -1*nCount*500, rand.nextInt(3), 0 );
		}
	}
	
	// 미사일 발사
	private void FireMissile()
	{
		if(!objMissile.isFired())	// 발사안했으면
		{
			objMissile.SetObject(sprMissile, 0, 0, objShip.x, objShip.y, 0, 0);
			objMissile.SetFire(true, false);
		}
	}
	
	private void UpdateMissile()
	{
		if(objMissile.y < -2*objMissile.GetYsize())
			objMissile.SetFire(false, false);
		
		for(Meteor MTO : qMetoer)	// 미사일과 메테오 충돌체크
		{
			if(!objMissile.isFired() || MTO.dead)	// 발사중 아니면 || 메테오 터진거면 충돌체크안함. 
				continue;
			
			if( gInfo.CrashCheck(MTO, objMissile, -5, -5) )	// 메테오와 미사일 충돌체크
			{
				this.MakeGlow(MTO.x, MTO.y);
				objMissile.SetFire(false, true);
				MTO.SetCrash(true, (int)MTO.x, (int)MTO.y);
			}
		}
	}
	
	

	private void UpdateShip()
	{
		if(objShip.bDestroyed)	// 터지면 함선 업데이트 안함
			return;
		
		if(objProgress.GetPercentToDestination() >= 70.0f)
		{
			objStation.y += objShip.GetVelocity()/12.0f;	// 스테이션 보여줌
		}		
		
		if(objProgress.GetPercentToDestination() >= 95.0f)
		{			
			objShip.SetVelocity( objShip.GetVelocity()*0.99f );	// 서서히 속도를 줄인다.
			float fVolumn= ((100.0f-objProgress.GetPercentToDestination())*20.0f)/100.0f;
			if(fVolumn<= 0.0f)
				Music.setVolume(0.0f, 0.0f);
			else
				Music.setVolume(fVolumn, fVolumn);	// 볼륨도 서서히 줄임
			
		}
		
		if(objProgress.bArrived)
		{			
			if(-1500 <= objShip.y)	// 끝
				objShip.y -= 12f;
			else
				SetScene(EnumScene.E_GAME_DOCKING);
			
			return;			
		}
		
		//info.MoveCamera(0.0f, -200.0f, 0.5f);
		if( (objShip.nEventCount==0) && (gInfo.ScreenY-300 <= objShip.y) )	// 처음에 아래에서 나오는 연출
		{
			objShip.fEventSpped = objShip.fEventSpped * 0.979f;
			objShip.y -= objShip.fEventSpped;
		}
		else
		{
			if((objShip.nEventCount==0))
			{
				objShip.nEventCount= 1;
				objShip.fEventSpped= 4.65f;
			}			
		}
		if( (objShip.nEventCount==1) && (gInfo.ScreenY-100 > objShip.y) )
		{
			objShip.fEventSpped = objShip.fEventSpped * 0.978f;
			objShip.y += objShip.fEventSpped; 
		}
		else if((objShip.nEventCount==1) && (gInfo.ScreenY-100 <= objShip.y))
		{
			objShip.nEventCount= 2;			
		}
		
		if((objShip.nEventCount==2))	// 다 내려왓음
		{
			objShip.bSheild= false;
			objShip.bControlable= true;
		}
		
		objProgress.fCurr += objShip.GetVelocity();	// 프로그레스바에 속도만큼 계산해준다.
		
		objShip.SetCrash(false, 0, 0); 
		for(Meteor MTO : qMetoer)
		{
			if(MTO.dead)	// 터진 메테오는 체크 안함.
				continue;
			
			if(!objShip.bControlable)	// 조종 불가능시에는 맞지 않음
				continue;
			
			if( objShip.CheckPos( (int)MTO.x , (int)MTO.y ) && !objProgress.bArrived )	// 메테오와 함선 충돌체크
			{
				gInfo.SetQuake(1000, 6, 2);
				gInfo.DoQuake();
				objShip.SetCrash(true, (int)MTO.x, (int)MTO.y);
			}
		}
		
		if(GlobalInput.bTouch)	// 터치하면 미사일 나감.
			FireMissile();
		
		// 화면 밖으로 나가는 것 체크
		if(objShip.x-GlobalInput.fSensorX*2.0f < 0)
			return;
		
		if(objShip.x-GlobalInput.fSensorX*2.0f > gInfo.ScreenX)
			return;
		
		if(objShip.bControlable)
			objShip.MoveSensorX( GlobalInput.fSensorX * objShip.getHandeling() );	// 함선 좌우 이동

		
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
				
				Star star= new Star();
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
			//if(MTO.dead)
				//return;
			
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
				
 				if(objProgress.GetPercentToDestination() <= 60.0f)	// 목적지에 90% 이상 왔으면 더 이상 메테오 없음.
				{
					Meteor mto= new Meteor( rand.nextInt(9)-4 );
					mto.SetObject( sprMetoer, 0, 0, rand.nextInt((int)gInfo.ScreenX), -1* rand.nextInt(2500)-150, rand.nextInt(3), 0 );
					Log.d("Create Meteor", "------> offer ------> Y : "+mto.y);
					qMetoer.offer(mto);
				}
				
				
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
		objMissile.Release();
		sprMetoer.Release();
		sprShip.Release();
		sprStar.Release();
		Music.release();
	}

}

