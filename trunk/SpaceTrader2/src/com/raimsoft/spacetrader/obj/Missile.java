package com.raimsoft.spacetrader.obj;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;
import bayaba.engine.lib.Sprite;

import com.raimsoft.spacetrader.R;
import com.raimsoft.spacetrader.util.SoundManager;

public class Missile extends GameObject
{
	private Random MyRand = new Random();
	private static int MAX_PARTICLE = 20;
	
	private GameObject Particle1[] = new GameObject[MAX_PARTICLE];
	
	private Sprite	sprGlow;
	//private boolean bCrash= false;
	private boolean bFired= false;
	private float fVelocity= 5.5f;
	private float fDamage= 280.0f;
	
	private SoundManager Sound;
	
	/**
	 * 
	 */
	public Missile(GL10 _gl, Context _context)
	{
		super();
		
		sprGlow= new Sprite();
		sprGlow.LoadSprite( _gl, _context, R.drawable.glow, "glow.spr" );
		for ( int i = 0; i < MAX_PARTICLE; i++ ) Particle1[i] = new GameObject();
		
		Sound= new SoundManager(_context);
		Sound.Create();
		Sound.Load(0, R.raw.missile_2);
		
		
	}

	
	public float GetVelocity()
	{
		return fVelocity;
	}
	
	public float GetDamage()
	{
		return fDamage;
	}

//	public boolean isCrash()
//	{
//		return bCrash;
//	}
	
	public boolean isFired()
	{
		return bFired;
	}

	
	// 발사 체크
	public void SetFire(boolean _bFire)
	{
		if(_bFire)
		{
			Sound.Play(0);
			Log.d("Missile::", "Fire missile!!!");
		}
		else
		{
			Log.d("Missile::", "Missile offline");
			//this.x= -200;	// 임시로 안보이게 밖으로 보냄
			for ( int i = 0; i < MAX_PARTICLE; i++ )
			{
				Particle1[i].dead= true;
			}
		}
		
		bFired= _bFire;
	}

	@Override
	public void DrawSprite(GameInfo info)
	{
		
		if(!bFired)
			return;
		
		super.DrawSprite(info);
		
		for ( int i = 0; i < MAX_PARTICLE; i++ )
		{
			if ( Particle1[i].dead == false ) Particle1[i].DrawSprite( info );
		}		
	}
	


	@Override
	public void SetObject(Sprite s_pat, int s_type, float s_layer, float s_x,
			float s_y, int s_motion, int s_frame)
	{
		super.SetObject(s_pat, s_type, s_layer, s_x, s_y, s_motion, s_frame);
		
		//this.UpdateObjects(info);
	}


	// 미사일 위치 업데이트
	public void UpdateObjects(GameInfo info)
	{
		if(bFired)
		{
			//Log.d("Missile::", "X: "+this.x+ " Y: "+this.y);
			this.y -= fVelocity;
			
			
			int cnt1= 0;		
			for ( int i = 0; i < MAX_PARTICLE; i++ )
			{
				if ( Particle1[i].dead == true )
				{
					Particle1[i].SetObject( sprGlow, 0, 0, x, y+42, 0, 3 );	// 위치
					Particle1[i].dead = false;
					Particle1[i].angle= 180;						// 각도
					Particle1[i].angle -= MyRand.nextInt(40)-20;	// 각도범위
					Particle1[i].speed = 10f + (MyRand.nextInt(5) * 0.5f);	// 나가는속도
					Particle1[i].effect = 1;
					Particle1[i].trans= 0.5f;	// 투명시작
//					Particle1[i].scalex= 0.75f;
//					Particle1[i].scaley= 0.8f;
					if ( ++cnt1 == 2 ) break;
				}
			}
			
			for ( int i = 0; i < MAX_PARTICLE; i++ )
			{
				if ( Particle1[i].dead == false )
				{
					Particle1[i].Zoom( info, 0.03f, 0.03f );				
					Particle1[i].trans -= 0.05f;
					if ( Particle1[i].trans <= 0 ) Particle1[i].dead = true;				
					Particle1[i].MovebyAngle( info, Particle1[i].angle, Particle1[i].speed );
				}
			}
		}
	}
	
	public void Release()
	{
		sprGlow.Release();
	}

}
