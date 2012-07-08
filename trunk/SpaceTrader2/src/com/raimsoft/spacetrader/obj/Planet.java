package com.raimsoft.spacetrader.obj;

import bayaba.engine.lib.GameObject;

public class Planet extends GameObject
{
	public int nIndex;
	public String strName="";

	/**
	 * @param nIndex
	 */
	public Planet(int nIndex, String strName)
	{
		super();
		this.nIndex = nIndex;
		this.strName= strName; 
	}
	
	

}
