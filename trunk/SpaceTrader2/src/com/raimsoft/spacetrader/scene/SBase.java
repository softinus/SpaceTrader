package com.raimsoft.spacetrader.scene;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import bayaba.engine.lib.GameInfo;

public class SBase
{
	protected Context mContext;
	protected GameInfo gInfo;
	protected GL10 gl;
	
	public boolean bModeChanged= false;
	protected EnumScene eMode;
	
	/**
	 * @param mContext
	 * @param gInfo
	 * @param gl
	 */
	public SBase(Context mContext, GameInfo gInfo)
	{
		super();
		this.mContext = mContext;
		this.gInfo = gInfo;
		
		bModeChanged= false;
	}
	
	public void SetGL(GL10 gl)
	{
		this.gl = gl;
	}
	
	public GL10 backupGL()
	{
		return gl;
	}
	
	public void LoadData()
	{	}
	public void Render()
	{	}
	
	public void Update()
	{	}
	
	public void ReleaseMemory()
	{	}
	
	public void onBackPressed()
	{	}
	
	public EnumScene GetMode()
	{
		return eMode;
	}
	
	public void SetScene(EnumScene _eScene)
	{
		bModeChanged= true;
		eMode= _eScene;
	}
}
