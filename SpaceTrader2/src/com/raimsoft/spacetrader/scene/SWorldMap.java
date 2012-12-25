package com.raimsoft.spacetrader.scene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import android.content.Context;
import android.util.Log;
import bayaba.engine.lib.Font;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;
import bayaba.engine.lib.Sprite;

import com.raimsoft.spacetrader.GlobalInput;
import com.raimsoft.spacetrader.R;
import com.raimsoft.spacetrader.obj.Planet;
import com.raimsoft.spacetrader.util.SoundManager;

public class SWorldMap  extends SBase
{
	Sprite sprBackgroundA= new Sprite();
	Sprite sprPlanets= new Sprite();
	Sprite sprPanel= new Sprite();
	
	private SoundManager Sound;
	
	private int nSelectionIndex= 0; 	// 선택된 행성 인덱스
	private ArrayList<Planet> arrPlanet= new ArrayList<Planet>();
	private GameObject objSelection= new GameObject();
	private GameObject objPanel= new GameObject();
	private Font txtInfo= new Font();
	
	private Random rand = new Random();
	
	private int PLANET_NUMS= 10;
	private int PLANET_TYPES= 4;
	
	private ArrayList<String> arrPlanetName= new ArrayList<String>();

	public SWorldMap(Context mContext, GameInfo gInfo)
	{
		super(mContext, gInfo);
		
		arrPlanetName.add("맛있는 무개념 행성");
		arrPlanetName.add("꼬물거리는 바베큐 행성");
		arrPlanetName.add("맛있는 비타민 행성");
		arrPlanetName.add("오만한 AABB 행성");
		arrPlanetName.add("헬로우 월드 행성");
		arrPlanetName.add("재밋는 이야기 행성");
		arrPlanetName.add("엄청나게 희귀한 행성");
		arrPlanetName.add("더러운 프링글스 행성");
		arrPlanetName.add("행복한 스마트폰 행성");
		arrPlanetName.add("쿼드코어 PC 행성");
		arrPlanetName.add("헵시바 바나바 행성");
		arrPlanetName.add("행복하고 슬픈 행성");
		arrPlanetName.add("배고프고 배부른 행성");
		arrPlanetName.add("착한 풍뎅이 행성");
		arrPlanetName.add("나쁜 프로그래밍 행성");
		
		Collections.shuffle(arrPlanetName);
		
		for(int i=0; i<PLANET_NUMS; ++i)
			arrPlanet.add( new Planet(i, arrPlanetName.get(i)) );
		
		Sound= new SoundManager(mContext);
		
		this.eMode= EnumScene.E_GAME_MAP;
	}

	/* (non-Javadoc)
	 * @see com.raimsoft.spacetrader.scene.SBase#LoadData()
	 */
	@Override
	public void LoadData()
	{
		super.LoadData();
		
		
		Sound.Create();
		Sound.Load(0, R.raw.button1);
		
		GlobalInput.fTouchX_gap= 0.0f;
		gInfo.ScrollX=30;
		
		sprPanel.LoadSprite(gl, mContext, R.drawable.map_panel, "map_panel.spr");
		objPanel.SetObject(sprPanel, 0, 0, 0, gInfo.ScreenY, 0, 0);
		objPanel.trans= 0.5f;
		objPanel.scroll= false;
		
		sprPlanets.LoadSprite(gl, mContext, R.drawable.planets, "planets.spr");		
		for(Planet PN : arrPlanet)
		{
			PN.type= rand.nextInt(PLANET_TYPES);
			
			if(PN.nIndex!=0)	// 이전 행성과 다른 타입으로 설정하기
			{
				while(PN.type == arrPlanet.get(PN.nIndex-1).type)
				{
					PN.type= rand.nextInt(PLANET_TYPES);
				}
			}
				
			float x= rand.nextInt(300) + 350*PN.nIndex;
			float y= 100+rand.nextInt(500);
			Log.d("Planet ["+PN.nIndex+"]"," => X : "+x+"  //  Y : "+y+"  //  type : "+PN.type);
			PN.SetObject(sprPlanets, 0, 0, x, y, PN.type, 0);
			float fRandomScale= 0.5f+rand.nextFloat()/2;
			PN.scalex= fRandomScale;
			PN.scaley= fRandomScale;
		}
		
//		gInfo.TileData.LoadTile(gl, mContext, R.drawable.planets, 32, 32);
//		gInfo.TileData.LoadMap(mContext, "plantes.map");
		
		objSelection.SetObject(sprPlanets, 0, 0, 0, 0, 5, 0);
		objSelection.show= false;
		sprBackgroundA.LoadSprite(gl, mContext, R.drawable.background_a, "background_a.spr");
	}

	/* (non-Javadoc)
	 * @see com.raimsoft.spacetrader.scene.SBase#Render()
	 */
	@Override
	public void Render()
	{
		super.Render();
		sprBackgroundA.PutImage(gInfo, 0, 0, 0);
		gInfo.TileData.DrawMap( 0, gInfo );		
		
				
		for(Planet PN : arrPlanet)
		{			
			if( PN.nIndex == PLANET_NUMS-1 )	// 마지막 행성이면 다음 라인 없음
				break;
			Planet curr= arrPlanet.get(PN.nIndex);
			Planet next= arrPlanet.get(PN.nIndex+1);
			gInfo.DrawLine(gl, (int)(curr.x-gInfo.ScrollX), (int)curr.y, (int)(next.x-gInfo.ScrollX), (int)next.y, 200, 255, 200, 1.0f, 1.0f);
		}
		
		
		for(Planet PN : arrPlanet)
		{
			//if(PN.x <= gInfo.ScrollX)	// 절두체 계산 해보자
			PN.DrawSprite(gInfo);
		}
				
		objSelection.angle+=3;
		objSelection.DrawSprite(gInfo);
		objPanel.DrawSprite(gInfo);
		
		if(objPanel.show)	// 패널이 보일 때만 정보가 보인다.
		{
			txtInfo.BeginFont();
			txtInfo.DrawFont(gl, 20, gInfo.ScreenY-100, 23.0f, "행성-"+nSelectionIndex );
			txtInfo.DrawFont(gl, 20, gInfo.ScreenY-40, 28.0f, arrPlanet.get(nSelectionIndex).strName );
			txtInfo.EndFont();
		}
	}

	/* (non-Javadoc)
	 * @see com.raimsoft.spacetrader.scene.SBase#Update()
	 */
	@Override
	public void Update()
	{
		super.Update();
		
		// 센서로 스크롤하는 부분
		if(gInfo.ScrollX > 0)
			gInfo.ScrollX -= GlobalInput.fSensorX;
		else
			gInfo.ScrollX += 10;
	
		

		
		// Selection Tool
		if(GlobalInput.bTouch)
		{
			int nX= (int)GlobalInput.fTouchX +(int)gInfo.ScrollX;
			int nY= (int)GlobalInput.fTouchY;
			
			for(Planet PN : arrPlanet)
			{
				if(PN.CheckPos(nX, nY))
				{
					if(objSelection.x != PN.x && objSelection.y != PN.y)
						Sound.Play(0);
					
					nSelectionIndex= PN.nIndex;
					objPanel.show= true;
					objSelection.show= true;
					objSelection.x= PN.x;
					objSelection.y= PN.y;
					objSelection.scalex= PN.scalex+0.35f;
					objSelection.scaley= PN.scaley+0.35f;
					break;
				}
				else
				{
					objPanel.show= false;
					objSelection.show= false;
				}
			}
		}
	}
	
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();		
		this.SetScene(EnumScene.E_MAIN);
	}


	@Override
	public void ReleaseMemory()
	{
		super.ReleaseMemory();
		
		gInfo.ScrollX= 0.0f;	// 카메라 원래대로 되돌리자
		sprBackgroundA.Release();
		sprPlanets.Release();
		sprPanel.Release();
		
	}

}