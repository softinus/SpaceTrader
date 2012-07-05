package com.raimsoft.spacetrader.scene;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import android.content.Context;
import android.media.MediaPlayer;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;
import bayaba.engine.lib.Sprite;

import com.immersion.uhl.Launcher;
import com.raimsoft.spacetrader.GlobalInput;
import com.raimsoft.spacetrader.R;
import com.raimsoft.spacetrader.obj.BaseFleet;
import com.raimsoft.spacetrader.obj.Meteor;

public class SGameWrap extends SBase
{
	private Sprite sprStar = new Sprite();
	private Sprite sprShip= new Sprite();
	private Sprite sprMetoer= new Sprite();
	
	private BaseFleet objShip;					// 나의 함선
	
	private Queue<GameObject> qStar= new LinkedList<GameObject>();
	private Queue<Meteor> qMetoer= new LinkedList<Meteor>();
	
	private Random rand = new Random();
	private MediaPlayer Music;
	private Launcher UHL;
	
	public SGameWrap(Context _context, GameInfo _Info)
	{
		super(_context, _Info);
		
		UHL= new Launcher(_context);
	}
	
	@Override
	public void LoadData()
	{
		super.LoadData(); 
		
		Music = MediaPlayer.create(mContext, R.raw.intothedark);
		Music.setLooping(true);
		Music.start();
		
		sprShip.LoadSprite( gl, mContext, R.drawable.resource_2, "ship_1.spr" );
		sprStar.LoadSprite( gl, mContext, R.drawable.resource_2, "star_1.spr" );
		sprMetoer.LoadSprite( gl, mContext, R.drawable.resource_2, "meteor.spr" );
	
		objShip= new BaseFleet(gl, mContext);
		objShip.SetObject( sprShip, 0, 0, gInfo.ScreenX/2, gInfo.ScreenY-100, 0, 0 );
		objShip.scalex= 0.5f;
		objShip.scaley= 0.5f;
		
		for(int i=0; i<=50; ++i)
			qStar.offer(new GameObject());
		
		for(int i=0; i<=1; ++i)
			qMetoer.offer(new Meteor( rand.nextInt(7)-3 ));	// 앵글 -4 ~ 4 범위
		
		this.MakeStar();
		this.MakeMetoer();
	}

	@Override
	public void Render()
	{
		super.Render();
		
		for(GameObject GO : qStar)
			GO.DrawSprite(gInfo);
				
		for(Meteor MTO : qMetoer)
			MTO.DrawSprite(gl, gInfo);
				
		objShip.DrawSprite(gInfo);
	}

	@Override
	public void Update()
	{
		super.Update();
		
		this.UpdateStar();
		this.UpdateMetoer();
		this.UpdateShip();
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
		objShip.SetCrash(false, 0, 0); 
		for(Meteor MTO : qMetoer)
		{
			if( objShip.CheckPos( (int)MTO.x , (int)MTO.y ) )
			{
				//gInfo.QuakeX= 500.0f;
				//gInfo.QuakeY= 500.0f;
				//gInfo.QuakeTimer=System.currentTimeMillis()+1000;
				gInfo.SetQuake(1000, 6, 2);
				gInfo.DoQuake();
				objShip.SetCrash(true, (int)MTO.x, (int)MTO.y);
			}
		}
		
		if(objShip.x-objShip.GetXsize()/2-GlobalInput.fSensorX*2.0f < 0)
			return;
		
		if(objShip.x+objShip.GetXsize()/2-GlobalInput.fSensorX*2.0f > gInfo.ScreenX)
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
			if( qMetoer.peek().y > gInfo.ScreenY + qMetoer.peek().GetYsize()/2 )
			{
				qMetoer.poll();
				
				Meteor mto= new Meteor( rand.nextInt(9)-4 );
				mto.SetObject( sprMetoer, 0, 0, rand.nextInt((int)gInfo.ScreenX), -500, rand.nextInt(3), 0 );
				qMetoer.offer(mto);
			}
		}
	}

}
