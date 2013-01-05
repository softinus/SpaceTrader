package com.raimsoft.spacetrader.obj.fleets;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;
import bayaba.engine.lib.Sprite;

import com.immersion.uhl.Launcher;
import com.raimsoft.spacetrader.R;
import com.raimsoft.spacetrader.data.UserInfo;
import com.raimsoft.spacetrader.util.SoundManager;

public class BaseFleet extends GameObject
{
	
	
	//private GameObject Particle0[] = new GameObject[MAX_PARTICLE];

	
	protected Sprite	sprSpark, sprHP, sprShield;// sprDestroy
	protected GameObject objSpark, objDestroy, objShield;
	protected UserInfo uInfo;
	
	public 	float fEventSpeed= 8.5f;
	public  int nEventCount= 0;	// 연출0번 --(함선가속)--> 연출1번 --(함선감속)--> 연출2번 --> (시작)
	

	
	protected boolean bCrash= false;		// 충돌 여부
	public boolean bDestroyed= false;	// 파괴당한 여부
	public boolean bControlable= false;	// 조종 가능 여부
	public boolean bSheild= true;		// 보호막 여부
	
	protected float fVelocity= 13.0f;
	protected float fHandeling= 2.0f;
	public int nHP= 5000;
	
	protected Launcher UHL;
	protected SoundManager Sound;
	
	protected String strShipName;
	
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
	

	public BaseFleet(GL10 _gl, Context _context)
	{
		super();
		
		uInfo= UserInfo.GetInstance();
		
		UHL= new Launcher(_context);
		
		Sound= new SoundManager(_context);
		Sound.Create();
		Sound.Load(0, R.raw.explode);
		Sound.Load(1, R.raw.spaceship_engine);
		Sound.Load(2, R.raw.spaceship_spark);
		Sound.Load(3, R.raw.systems_online);
		
		
		sprSpark= new Sprite();
//		sprDestroy= new Sprite();
		sprHP= new Sprite();
		sprShield= new Sprite();
		
		objDestroy= new GameObject();
		objSpark= new GameObject();
		objShield= new GameObject();
		
	
		sprHP.LoadSprite(_gl, _context, R.drawable.resource_2, "number.spr");
		sprSpark.LoadSprite(_gl, _context, R.drawable.eff_spark_lightning, "eff_light.spr");
		
		sprShield.LoadSprite(_gl, _context, R.drawable.resource_2, "shield.spr");
	//	sprDestroy.LoadSprite( _gl, _context, R.drawable.eff_bomb, "eff_bomb.spr" );
		
		//for ( int i = 0; i < MAX_PARTICLE; i++ ) Particle0[i] = new GameObject();

		
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
			//this.effect= 1;
			this.fHandeling= 1.09f; 
			this.fVelocity= 3.5f;
			//SetFireScale(0.75f);
			//if(UHL.)
			UHL.play(Launcher.IMPACT_METAL_66);
		}
		else
		{
			if(this.fHandeling==1.09f)	// 부딪힌 바로 다음에만 변경해줌
			{
				objSpark.show= false;
				//this.effect= 0;
				this.fHandeling= 2.0f;
				this.fVelocity= 13.0f;
			}
			//SetFireScale(1.0f);
		}
	}


	@Override
	public void SetObject(Sprite s_pat, int s_type, float s_layer, float s_x, float s_y, int s_motion, int s_frame)
	{
		super.SetObject(s_pat, s_type, s_layer, s_x, s_y, s_motion, s_frame);
		objSpark.SetObject(sprSpark, 0, 0, x, y, 0, 0);
		objShield.SetObject(sprShield, 0, 0, x, y, 0, 0);
//		objFire1.SetObject(sprGlow, 0, 0, this.x-21, this.y+55, 0, 0);
//		objFire2.SetObject(sprGlow, 0, 0, this.x+11, this.y+55, 0, 0);
//		objFire3.SetObject(sprGlow, 0, 0, this.x+25, this.y+55, 0, 0);
	//	objDestroy.SetObject(sprDestroy, 0, 0, x, y, 0, 0);
		
		//SetFireScale(1.0f);
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

		super.DrawSprite(info);		
		
		if(objSpark.show)
		{
			objSpark.AddFrameLoop(0.4f);
			objSpark.DrawSprite(info);
		}
		
		if(bSheild)
		{
			objShield.x= this.x;
			objShield.y= this.y;
			objShield.DrawSprite(info);
		}
	}
	
	// 메모리해제
	public void Release()
	{
		//sprDestroy.Release();
		sprHP.Release();
		sprSpark.Release();
	}


	public float GetVelocity()
	{
		return 0f;
	}


	public void SetVelocity(float f)
	{
		
	}


	public float getHandeling()
	{
		return 0;
	}
	
	
	
	
}
