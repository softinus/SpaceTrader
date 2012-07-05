package com.raimsoft.spacetrader.scene;

import android.content.Context;
import bayaba.engine.lib.GameInfo;

import com.raimsoft.spacetrader.GlobalInput;
import com.raimsoft.spacetrader.R;

public class SWorldMap  extends SBase
{

	public SWorldMap(Context mContext, GameInfo gInfo)
	{
		super(mContext, gInfo);		
	}

	/* (non-Javadoc)
	 * @see com.raimsoft.spacetrader.scene.SBase#LoadData()
	 */
	@Override
	public void LoadData()
	{
		super.LoadData();
		
		GlobalInput.fTouchX_gap= 0.0f;
		gInfo.ScrollX=30;
		
		gInfo.TileData.LoadTile(gl, mContext, R.drawable.planets, 32, 32);
		gInfo.TileData.LoadMap(mContext, "plantes.map");
	}

	/* (non-Javadoc)
	 * @see com.raimsoft.spacetrader.scene.SBase#Render()
	 */
	@Override
	public void Render()
	{
		super.Render();
		gInfo.TileData.DrawMap( 0, gInfo );
	}

	/* (non-Javadoc)
	 * @see com.raimsoft.spacetrader.scene.SBase#Update()
	 */
	@Override
	public void Update()
	{
		super.Update();
		
		if(GlobalInput.bTouch)
		{
			
			
			if(gInfo.ScrollX > 0)
				gInfo.ScrollX += GlobalInput.fTouchX_gap;
			else
				gInfo.ScrollX += 10;
			//gInfo.ScrollY -= GlobalInput.fTouchY_gap;
		}
	}

}
