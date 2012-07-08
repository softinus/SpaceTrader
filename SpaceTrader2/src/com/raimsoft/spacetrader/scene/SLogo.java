package com.raimsoft.spacetrader.scene;

import com.raimsoft.spacetrader.R;
import com.raimsoft.spacetrader.util.SoundManager;

import android.content.Context;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;
import bayaba.engine.lib.Sprite;

public class SLogo extends SBase 
{
	Sprite sprLogo= new Sprite();
	GameObject objLogo= new GameObject();
	SoundManager Sound;
	
	boolean bFadeOn= false;
	
	

	public SLogo(Context mContext, GameInfo gInfo)
	{
		super(mContext, gInfo);
		
		Sound= new SoundManager(mContext);
		Sound.Create();
		Sound.Load(0, R.raw.logo);
		
		this.eMode= EnumScene.E_LOGO;
	}

	/* (non-Javadoc)
	 * @see com.raimsoft.spacetrader.scene.SBase#LoadData()
	 */
	@Override
	public void LoadData()
	{
		super.LoadData();
		
		sprLogo.LoadSprite(gl, mContext, R.drawable.ci_5_eng, "logo.spr");
		objLogo.SetObject(sprLogo, 0, 0, gInfo.ScreenX/2, gInfo.ScreenY/2, 0, 0);
		//objLogo.SetObject(sprLogo, 0, 0, 150, 300, 0, 0);
		objLogo.trans= 0.1f;
		objLogo.scalex= 1.2f;
		objLogo.scaley= 1.2f;
		
		Sound.Play(0);
	}

	/* (non-Javadoc)
	 * @see com.raimsoft.spacetrader.scene.SBase#Render()
	 */
	@Override
	public void Render()
	{
		super.Render();
		
		gInfo.BackB=20;
		gInfo.BackG=20;
		gInfo.BackB=20;
		
		//sprLogo.PutImage(gInfo, 0, 0, 0);
		objLogo.DrawSprite(gInfo);
	}

	/* (non-Javadoc)
	 * @see com.raimsoft.spacetrader.scene.SBase#Update()
	 */
	@Override
	public void Update()
	{
		super.Update();
		
		if(objLogo.trans <= 0.0f && bFadeOn)
			SetScene(EnumScene.E_MAIN);
		
		if(bFadeOn)
		{
			objLogo.trans-= 0.008f;
			return;
		}
		
		if(objLogo.trans >= 1.0f)
		{
			bFadeOn= true;			
		}
		else
		{
			objLogo.trans+= 0.008f;
		}
	}

	@Override
	public void ReleaseMemory()
	{
		super.ReleaseMemory();
		
		sprLogo.Release();
		Sound.Destroy();
	}

}
