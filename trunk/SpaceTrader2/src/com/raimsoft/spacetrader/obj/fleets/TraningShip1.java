package com.raimsoft.spacetrader.obj.fleets;

import javax.microedition.khronos.opengles.GL10;

import com.immersion.uhl.Launcher;

import android.content.Context;

public class TraningShip1 extends BaseFleet
{
	
	
	
	public TraningShip1(GL10 _gl, Context _context)
	{
		super(_gl, _context);
		
		fVelocity= 13.0f;
		fHandeling= 2.0f;
		nHP= 1000;
	}

	

	

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

}
