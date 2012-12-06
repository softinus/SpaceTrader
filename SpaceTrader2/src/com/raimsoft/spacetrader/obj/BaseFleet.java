package com.raimsoft.spacetrader.obj;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;
import bayaba.engine.lib.Sprite;

import com.immersion.uhl.Launcher;
import com.raimsoft.spacetrader.R;
import com.raimsoft.spacetrader.util.SoundManager;

public class BaseFleet extends GameObject
{
	private Random MyRand = new Random();
	private static int MAX_PARTICLE = 20;
	//private GameObject Particle0[] = new GameObject[MAX_PARTICLE];
	private GameObject Particle1[] = new GameObject[MAX_PARTICLE];
	private GameObject Particle2[] = new GameObject[MAX_PARTICLE];
	private GameObject Particle3[] = new GameObject[MAX_PARTICLE];
	
	private Sprite	sprGlow, sprSpark, sprDestroy, sprHP;
	private GameObject objSpark, objDestroy;
	private boolean bCrash= false;
	private float fVelocity= 13.0f;
	private float fHandeling= 2.0f;
	private int nHP= 1000;
	public boolean bDestroyed= false;
	
	private Launcher UHL;
	private SoundManager Sound;
	
//	/**
//	 * @return the nHP
//	 */
//	public int getHP()
//	{
//		return nHP;
//	}
//	
//	
//	/**
//	 * @param nHP the nHP to set
//	 */
//	public void setHP(int nHP)
//	{
//		this.nHP = nHP;
//	}
	
	/**
	 * @return the fHandeling
	 */
	public float getHandeling()
	{
		return fHandeling;
	}



	/**
	 * @param fHandeling the fHandeling to set
	 */
	public void setHandeling(float fHandeling)
	{
		this.fHandeling = fHandeling;
	}


	/**
	 * @return the fVelocity
	 */
	public float GetVelocity()
	{
		return fVelocity;
	}


	/**
	 * @param fVelocity the fVelocity to set
	 */
	public void SetVelocity(float fVelocity)
	{
		this.fVelocity = fVelocity;
	}


	public BaseFleet(GL10 _gl, Context _context)
	{
		super();
		
		UHL= new Launcher(_context);
		
		Sound= new SoundManager(_context);
		Sound.Create();
		Sound.Load(0, R.raw.explode);
		Sound.Load(1, R.raw.spaceship_engine);
		Sound.Load(2, R.raw.spaceship_spark);
		Sound.Load(3, R.raw.systems_online);
		
		sprGlow= new Sprite();
		sprSpark= new Sprite();
		sprDestroy= new Sprite();
		sprHP= new Sprite();
		objDestroy= new GameObject();
		objSpark= new GameObject();
	
		sprHP.LoadSprite(_gl, _context, R.drawable.resource_2, "number.spr");
		sprSpark.LoadSprite(_gl, _context, R.drawable.eff_spark_lightning, "eff_light.spr");
		sprGlow.LoadSprite( _gl, _context, R.drawable.glow, "glow.spr" );
		sprDestroy.LoadSprite( _gl, _context, R.drawable.eff_bomb, "eff_bomb.spr" );
		
		//for ( int i = 0; i < MAX_PARTICLE; i++ ) Particle0[i] = new GameObject();
		for ( int i = 0; i < MAX_PARTICLE; i++ ) Particle1[i] = new GameObject();
		for ( int i = 0; i < MAX_PARTICLE; i++ ) Particle2[i] = new GameObject();
		for ( int i = 0; i < MAX_PARTICLE; i++ ) Particle3[i] = new GameObject();
		
		Sound.Play(3);
	}


	/**
	 * @return the bCrash
	 */
	public boolean isCrash()
	{
		return bCrash;
	}


	/**
	 * 충돌값 세팅시
	 * @param bCrash the bCrash to set
	 */
	public void SetCrash(boolean bCrash, int x, int y)
	{
		this.bCrash = bCrash;
		
		if(this.bDestroyed)
			return;
				
		if(bCrash)
		{
			if(nHP-3 > 0)
			{
				Sound.Play(2,1 );
				nHP -= 3;
			}
			else
			{
				Sound.Play(0);
				nHP= 0;
				this.bDestroyed= true;
				this.objDestroy.x= this.x;
				this.objDestroy.y= this.y;
			}
			
			objSpark.show= true;
			objSpark.x= this.x;
			objSpark.y= this.y;
			this.effect= 1;
			this.fHandeling= 1.0f; 
			this.fVelocity= 3.0f;
			//SetFireScale(0.75f);			
			UHL.play(Launcher.IMPACT_METAL_66);
		}
		else
		{
			objSpark.show= false;
			this.effect= 0;
			this.fHandeling= 2.0f;
			this.fVelocity= 13.0f;
			//SetFireScale(1.0f);
		}
	}


	@Override
	public void SetObject(Sprite s_pat, int s_type, float s_layer, float s_x, float s_y, int s_motion, int s_frame)
	{
		super.SetObject(s_pat, s_type, s_layer, s_x, s_y, s_motion, s_frame);
		objSpark.SetObject(sprSpark, 0, 0, x, y, 0, 0);
//		objFire1.SetObject(sprGlow, 0, 0, this.x-21, this.y+55, 0, 0);
//		objFire2.SetObject(sprGlow, 0, 0, this.x+11, this.y+55, 0, 0);
//		objFire3.SetObject(sprGlow, 0, 0, this.x+25, this.y+55, 0, 0);
		objDestroy.SetObject(sprDestroy, 0, 0, x, y, 0, 0);
		
		//SetFireScale(1.0f);
	}
	
	private void SetGlowParticle()
	{
		int cnt0= 0, cnt1= 0, cnt2=0, cnt3=0;		
		for ( int i = 0; i < MAX_PARTICLE; i++ )
		{
//			if ( Particle0[i].dead == true )
//			{
//				Particle0[i].SetObject( sprGlow, 0, 0, x, y, 0, 8 );	// 위치
//				Particle0[i].dead = false;
//				Particle0[i].angle= 180;						// 각도
//				Particle0[i].angle -= MyRand.nextInt(14)-7;	// 각도범위
//				Particle0[i].speed = 1f+(MyRand.nextInt(5) * 0.5f);	// 나가는속도
//				Particle0[i].effect = 1;
//				Particle0[i].trans= 0.7f;	// 투명시작
//				Particle0[i].scalex= 0.6f;
//				Particle0[i].scaley= 0.6f;
//				if ( ++cnt0 == 2 ) break;
//			}
		}
		for ( int i = 0; i < MAX_PARTICLE; i++ )
		{
			if ( Particle1[i].dead == true )
			{
				Particle1[i].SetObject( sprGlow, 0, 0, x-21, y+30, 0, 7 );	// 위치
				Particle1[i].dead = false;
				Particle1[i].angle= 180;						// 각도
				Particle1[i].angle -= MyRand.nextInt(14)-7;	// 각도범위
				Particle1[i].speed = 6f + (MyRand.nextInt(5) * 0.5f);	// 나가는속도
				Particle1[i].effect = 1;
				Particle1[i].trans= 0.5f;	// 투명시작
				Particle1[i].scalex= 0.4f;
				Particle1[i].scaley= 0.6f;
				if ( ++cnt1 == 2 ) break;
			}
		}
		for ( int i = 0; i < MAX_PARTICLE; i++ )
		{
			if ( Particle2[i].dead == true )
			{
				Particle2[i].SetObject( sprGlow, 0, 0, x+11, y+30, 0, 7 );	// 위치
				Particle2[i].dead = false;
				Particle2[i].angle= 180;						// 각도
				Particle2[i].angle -= MyRand.nextInt(14)-7;	// 각도범위
				Particle2[i].speed = 6f + (MyRand.nextInt(5) * 0.5f);	// 나가는속도
				Particle2[i].effect = 1;
				Particle2[i].trans= 0.5f;	// 투명시작
				Particle2[i].scalex= 0.3f;
				Particle2[i].scaley= 0.4f;
				if ( ++cnt2 == 2 ) break;
			}
		}
		for ( int i = 0; i < MAX_PARTICLE; i++ )
		{
			if ( Particle3[i].dead == true )
			{
				Particle3[i].SetObject( sprGlow, 0, 0, x+25, y+30, 0, 7 );	// 위치
				Particle3[i].dead = false;
				Particle3[i].angle= 180;						// 각도
				Particle3[i].angle -= MyRand.nextInt(14)-7;	// 각도범위
				Particle3[i].speed = 6f + (MyRand.nextInt(5) * 0.5f);	// 나가는속도
				Particle3[i].effect = 1;
				Particle3[i].trans= 0.5f;	// 투명시작
				Particle3[i].scalex= 0.3f;
				Particle3[i].scaley= 0.4f;
				if ( ++cnt3 == 2 ) break;
			}
		}
	}
	
//	private void SetFireScale(float _ratio)
//	{
//		objSpark.scalex= 0.75f;
//		objSpark.scaley= 0.75f;
//				
//		objFire1.scalex= 0.5f * _ratio;
//		objFire1.scaley= 0.5f * _ratio;
//		objFire2.scalex= 0.3f * _ratio;
//		objFire2.scaley= 0.3f * _ratio;
//		objFire3.scalex= 0.3f * _ratio;
//		objFire3.scaley= 0.3f * _ratio;
//	}
//	
//	
	public void MoveSensorX(float fSensorX)
	{
		this.x -= fSensorX;
//		objFire1.x -= fSensorX;
//		objFire2.x -= fSensorX;
//		objFire3.x -= fSensorX;
	}

	

	/* (non-Javadoc)
	 * @see bayaba.engine.lib.GameObject#DrawSprite(bayaba.engine.lib.GameInfo)
	 */
	@Override
	public void DrawSprite(GameInfo info)
	{
		if(this.bDestroyed)
		{
			objDestroy.AddFrame(0.3f);
			objDestroy.DrawSprite(info);
			return;
		}
		sprHP.PutValue(info, 25, 35, 0, nHP);
//		objFire1.AddFrameLoop(1.0f);
//		objFire1.DrawSprite(info);
//		objFire2.AddFrameLoop(1.0f);
//		objFire2.DrawSprite(info);
//		objFire3.AddFrameLoop(1.0f);
//		objFire3.DrawSprite(info);
		SetGlowParticle();
//		for ( int i = 0; i < MAX_PARTICLE; i++ )
//		{
//			if ( Particle0[i].dead == false )
//			{
//				Particle0[i].Zoom( info, 0.03f, 0.03f );				
//				Particle0[i].trans -= 0.05f;
//				if ( Particle0[i].trans <= 0 ) Particle0[i].dead = true;				
//				Particle0[i].MovebyAngle( info, Particle0[i].angle, Particle0[i].speed );
//			}
//		}
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
		for ( int i = 0; i < MAX_PARTICLE; i++ )
		{
			if ( Particle2[i].dead == false )
			{
				Particle2[i].Zoom( info, 0.03f, 0.03f );				
				Particle2[i].trans -= 0.05f;
				if ( Particle2[i].trans <= 0 ) Particle2[i].dead = true;				
				Particle2[i].MovebyAngle( info, Particle2[i].angle, Particle2[i].speed );
			}
		}
		for ( int i = 0; i < MAX_PARTICLE; i++ )
		{
			if ( Particle3[i].dead == false )
			{
				Particle3[i].Zoom( info, 0.03f, 0.03f );				
				Particle3[i].trans -= 0.05f;
				if ( Particle3[i].trans <= 0 ) Particle3[i].dead = true;				
				Particle3[i].MovebyAngle( info, Particle3[i].angle, Particle3[i].speed );
			}
		}
		
		for ( int i = 0; i < MAX_PARTICLE; i++ )
		{
			//if ( Particle0[i].dead == false ) Particle0[i].DrawSprite( info );
			if ( Particle1[i].dead == false ) Particle1[i].DrawSprite( info );
			if ( Particle2[i].dead == false ) Particle2[i].DrawSprite( info );
			if ( Particle3[i].dead == false ) Particle3[i].DrawSprite( info );
		}

		super.DrawSprite(info);		
		
		objSpark.AddFrameLoop(0.4f);
		objSpark.DrawSprite(info);
	}
	
	// 메모리해제
	public void Release()
	{
		sprDestroy.Release();
		sprGlow.Release();
		sprHP.Release();
		sprSpark.Release();
	}
	
	
	
	
}
