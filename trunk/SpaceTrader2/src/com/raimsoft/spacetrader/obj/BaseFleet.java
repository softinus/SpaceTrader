package com.raimsoft.spacetrader.obj;

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
	private Sprite	sprFire, sprSpark, sprDestroy, sprHP;
	private GameObject objFire1, objFire2, objFire3, objSpark, objDestroy;
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
		
		sprFire= new Sprite();
		sprSpark= new Sprite();
		sprDestroy= new Sprite();
		sprHP= new Sprite();
		objFire1= new GameObject();
		objFire2= new GameObject();
		objFire3= new GameObject();
		objDestroy= new GameObject();
		objSpark= new GameObject();
	
		sprHP.LoadSprite(_gl, _context, R.drawable.resource_2, "number.spr");
		sprSpark.LoadSprite(_gl, _context, R.drawable.eff_spark_lightning, "eff_light.spr");
		sprFire.LoadSprite( _gl, _context, R.drawable.fire, "fire.spr" );
		sprDestroy.LoadSprite( _gl, _context, R.drawable.eff_bomb, "eff_bomb.spr" );
		
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
			SetFireScale(0.75f);
			
			UHL.play(Launcher.IMPACT_METAL_66);
		}
		else
		{
			objSpark.show= false;
			this.effect= 0;
			this.fHandeling= 2.0f;
			this.fVelocity= 13.0f;
			SetFireScale(1.0f);
		}
	}


	@Override
	public void SetObject(Sprite s_pat, int s_type, float s_layer, float s_x, float s_y, int s_motion, int s_frame)
	{
		super.SetObject(s_pat, s_type, s_layer, s_x, s_y, s_motion, s_frame);
		objSpark.SetObject(sprSpark, 0, 0, x, y, 0, 0);
		objFire1.SetObject(sprFire, 0, 0, this.x-21, this.y+55, 0, 0);
		objFire2.SetObject(sprFire, 0, 0, this.x+11, this.y+55, 0, 0);
		objFire3.SetObject(sprFire, 0, 0, this.x+25, this.y+55, 0, 0);
		objDestroy.SetObject(sprDestroy, 0, 0, x, y, 0, 0);
		
		SetFireScale(1.0f);
	}
	
	private void SetFireScale(float _ratio)
	{
		objSpark.scalex= 0.75f;
		objSpark.scaley= 0.75f;
				
		objFire1.scalex= 0.5f * _ratio;
		objFire1.scaley= 0.5f * _ratio;
		objFire2.scalex= 0.3f * _ratio;
		objFire2.scaley= 0.3f * _ratio;
		objFire3.scalex= 0.3f * _ratio;
		objFire3.scaley= 0.3f * _ratio;
	}
	
	
	public void MoveSensorX(float fSensorX)
	{
		this.x -= fSensorX;
		objFire1.x -= fSensorX;
		objFire2.x -= fSensorX;
		objFire3.x -= fSensorX;
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
		objFire1.AddFrameLoop(1.0f);
		objFire1.DrawSprite(info);
		objFire2.AddFrameLoop(1.0f);
		objFire2.DrawSprite(info);
		objFire3.AddFrameLoop(1.0f);
		objFire3.DrawSprite(info);

		super.DrawSprite(info);		
		
		objSpark.AddFrameLoop(0.4f);
		objSpark.DrawSprite(info);
	}
	
	// 메모리해제
	public void Release()
	{
		sprDestroy.Release();
		sprFire.Release();
		sprHP.Release();
		sprSpark.Release();
	}
	
	
	
	
}
