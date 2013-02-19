package com.raimsoft.spacetrader.scene;

import java.util.ArrayList;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import bayaba.engine.lib.Font;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;
import bayaba.engine.lib.Sprite;

import com.raimsoft.spacetrader.R;
import com.raimsoft.spacetrader.data.GlobalInput;
import com.raimsoft.spacetrader.data.UserInfo;
import com.raimsoft.spacetrader.obj.GameButton;
import com.raimsoft.spacetrader.obj.Planet;
import com.raimsoft.spacetrader.obj.RainbowMessageBox;
import com.raimsoft.spacetrader.util.GenConst;
import com.raimsoft.spacetrader.util.PlanetNameMaker;
import com.raimsoft.spacetrader.util.SoundManager;

public class SSystemMap  extends SBase
{
	private UserInfo uInfo;
	private PlanetNameMaker PNM;
	
	Sprite sprBackgroundA= new Sprite();
	Sprite sprPlanets= new Sprite();
	Sprite sprPanel= new Sprite();
	Sprite sprPosMark= new Sprite();
	Sprite sprButtonMove= new Sprite();
	Sprite sprMessage= new Sprite();
	
	private SoundManager Sound;
	
	private int nSelectionIndex= 0; 	// 선택된 행성 인덱스
	private ArrayList<Planet> arrPlanet= new ArrayList<Planet>();
	
	private GameButton btnMove= new GameButton();
	private GameObject objPositionMarker= new GameObject();	// 현재 위치 보여주는 마커
	private ArrayList<GameObject> arrMoveAbleMarker= new ArrayList<GameObject>();	// 이동 가능한 위치 보여주는 마커.	
	private GameObject objSelection= new GameObject();	// 선택된 행성 보여주는 마커
	private GameObject objPanel= new GameObject();
	private RainbowMessageBox msgBox;
	
	private Font txtInfo= new Font();
	
	//private Random rand = new Random();
	private GenConst GC;
	
	
	private int PLANET_NUMS= 10;
	private int PLANET_TYPES= 4;
	private int MARKER_Y= 100;
	
	private int nHalfScreenX;
	private int nMyPos;
	private float fStartX= -1.1f, fCurrX= -1.1f, fScrollDes, fOldX=-0.0f, fGapX= 0.0f;
	private boolean bDirectionR= true;
	//private int nBeforeType= -1; // planet type before
	
	private MediaPlayer Music;
	
	private ArrayList<String> arrPlanetName= new ArrayList<String>();

	public SSystemMap(Context mContext, GameInfo gInfo)
	{
		super(mContext, gInfo);
		
		uInfo= UserInfo.GetInstance();
		PNM= new PlanetNameMaker();
		GC= new GenConst();
		arrPlanetName= PNM.GetNames(10);
		
		//Collections.shuffle(arrPlanetName);
		
		for(int i=0; i<PLANET_NUMS; ++i)
			arrPlanet.add( new Planet(i, arrPlanetName.get(i)) );
		
		for(int i=0; i<4; ++i)
			arrMoveAbleMarker.add(new GameObject());
		
		Sound= new SoundManager(mContext);
		
		this.eMode= EnumScene.E_GAME_SYSTEMMAP;
	}

	/* (non-Javadoc)
	 * @see com.raimsoft.spacetrader.scene.SBase#LoadData()
	 */
	@Override
	public void LoadData()
	{
		super.LoadData();
		
		
		Music = MediaPlayer.create(mContext, R.raw.systemmap_music);
		Music.setLooping(true);
		Music.start();
		
		sprMessage.LoadSprite(gl, mContext, R.drawable.buttons_2, "rainbow_messagebox.spr");
		msgBox= new RainbowMessageBox(gl, mContext);
		msgBox.SetMessageBox(1, sprMessage, 0, 0, gInfo.ScreenX/2, gInfo.ScreenY/2, 0, 0);
		msgBox.scroll= false;
		//msgBox.SetButtonTextScr(24f, "출발", "뒤로");
		
		nHalfScreenX= (int) (gInfo.ScreenX/2);
		nMyPos= nSelectionIndex= uInfo.GetSystemMapPlanet();
				
		Sound.Create();
		Sound.Load(0, R.raw.button1);		
		
		sprButtonMove.LoadSprite(gl, mContext, R.drawable.buttons_2, "systemmap_btn_move.spr");
		btnMove.SetButton(mContext, sprButtonMove, 0, 0, 0);
		btnMove.show= false;
		
		sprPanel.LoadSprite(gl, mContext, R.drawable.map_panel, "map_panel.spr");
		objPanel.SetObject(sprPanel, 0, 0, 0, gInfo.ScreenY+20, 0, 0);
		//objPanel.trans= 0.5f;
		objPanel.scroll= false; 
		
		sprPosMark.LoadSprite(gl, mContext, R.drawable.planets, "position_marker.spr");
		sprPlanets.LoadSprite(gl, mContext, R.drawable.planets, "planets.spr");		
		for(Planet PN : arrPlanet)	// 
		{
			PN.nPlanetType= (int) GC.GetConstF(PN.nIndex, PLANET_TYPES); // rand.nextInt(PLANET_TYPES);

//			if(nBeforeType==-1)	// first loop
//			{
//				nBeforeType= PN.nPlanetType; 
//			}
//			else	// 이전 행성과 다른 타입으로 설정하기
//			{
//				while(PN.nPlanetType == nBeforeType)
//					PN.nPlanetType= rand.nextInt(PLANET_TYPES);
//				nBeforeType= PN.nPlanetType;
//			}
				
			float x= 100+GC.GetConstF(PN.nIndex, 200) + 300*PN.nIndex;
			float y= 200+GC.GetConstF(PN.nIndex, 500);
			
			Log.d("Planet ["+PN.nIndex+"]"," => X : "+x+"  //  Y : "+y+"  //  type : "+PN.nPlanetType);
			
			PN.SetObject(sprPlanets, 0, 0, x, y, PN.nPlanetType, 0);
			float fPlanetScale= 0.5f + GC.GetConstF(PN.nIndex, 0.5f);
			PN.scalex= fPlanetScale;
			PN.scaley= fPlanetScale;
		}
		
//		gInfo.TileData.LoadTile(gl, mContext, R.drawable.planets, 32, 32);
//		gInfo.TileData.LoadMap(mContext, "plantes.map");
		//uInfo.SetPlanets(arrPlanet);
		Planet currPlanet= arrPlanet.get( nMyPos );
		
		objPositionMarker.SetObject(sprPosMark, 0, 0, currPlanet.x, currPlanet.y-(currPlanet.scaley*MARKER_Y), 0, 0);

		if(nMyPos==0)
		{
			arrMoveAbleMarker.get(0).SetObject(sprPosMark, 0, 0, arrPlanet.get(nMyPos+1).x, arrPlanet.get(nMyPos+1).y-(currPlanet.scaley*MARKER_Y), 1, 0);
			arrMoveAbleMarker.get(1).SetObject(sprPosMark, 0, 0, arrPlanet.get(nMyPos+2).x, arrPlanet.get(nMyPos+2).y-(currPlanet.scaley*MARKER_Y), 1, 0);
		}
		else if(nMyPos==1)
		{
			arrMoveAbleMarker.get(0).SetObject(sprPosMark, 0, 0, arrPlanet.get(nMyPos-1).x, arrPlanet.get(nMyPos-1).y-(currPlanet.scaley*MARKER_Y), 1, 0);
			arrMoveAbleMarker.get(1).SetObject(sprPosMark, 0, 0, arrPlanet.get(nMyPos+1).x, arrPlanet.get(nMyPos+1).y-(currPlanet.scaley*MARKER_Y), 1, 0);
			arrMoveAbleMarker.get(2).SetObject(sprPosMark, 0, 0, arrPlanet.get(nMyPos+2).x, arrPlanet.get(nMyPos+2).y-(currPlanet.scaley*MARKER_Y), 1, 0);
		}
		else if(nMyPos==PLANET_NUMS-1)
		{
			arrMoveAbleMarker.get(0).SetObject(sprPosMark, 0, 0, arrPlanet.get(nMyPos-1).x, arrPlanet.get(nMyPos-1).y-(currPlanet.scaley*MARKER_Y), 1, 0);
			arrMoveAbleMarker.get(1).SetObject(sprPosMark, 0, 0, arrPlanet.get(nMyPos-2).x, arrPlanet.get(nMyPos-2).y-(currPlanet.scaley*MARKER_Y), 1, 0);
		}
		else if(nMyPos==PLANET_NUMS-2)
		{
			arrMoveAbleMarker.get(0).SetObject(sprPosMark, 0, 0, arrPlanet.get(nMyPos-2).x, arrPlanet.get(nMyPos-2).y-(currPlanet.scaley*MARKER_Y), 1, 0);
			arrMoveAbleMarker.get(1).SetObject(sprPosMark, 0, 0, arrPlanet.get(nMyPos-1).x, arrPlanet.get(nMyPos-1).y-(currPlanet.scaley*MARKER_Y), 1, 0);
			arrMoveAbleMarker.get(2).SetObject(sprPosMark, 0, 0, arrPlanet.get(nMyPos+1).x, arrPlanet.get(nMyPos+1).y-(currPlanet.scaley*MARKER_Y), 1, 0);
		}
		else
		{
			arrMoveAbleMarker.get(0).SetObject(sprPosMark, 0, 0, arrPlanet.get(nMyPos-2).x, arrPlanet.get(nMyPos-2).y-(currPlanet.scaley*MARKER_Y), 1, 0);
			arrMoveAbleMarker.get(1).SetObject(sprPosMark, 0, 0, arrPlanet.get(nMyPos-1).x, arrPlanet.get(nMyPos-1).y-(currPlanet.scaley*MARKER_Y), 1, 0);
			arrMoveAbleMarker.get(2).SetObject(sprPosMark, 0, 0, arrPlanet.get(nMyPos+1).x, arrPlanet.get(nMyPos+1).y-(currPlanet.scaley*MARKER_Y), 1, 0);
			arrMoveAbleMarker.get(3).SetObject(sprPosMark, 0, 0, arrPlanet.get(nMyPos+2).x, arrPlanet.get(nMyPos+2).y-(currPlanet.scaley*MARKER_Y), 1, 0);
		}
		
		objSelection.SetObject(sprPlanets, 0, 0, 0, 0, 5, 0);
		objSelection.show= false;
		//sprBackgroundA.LoadSprite(gl, mContext, R.drawable.background_a, "background_a.spr");
		sprBackgroundA.LoadSprite(gl, mContext, R.drawable.systemmap_bg, "systemmap_bg.spr");
		
		gInfo.ScrollX= arrPlanet.get( nMyPos ).x-(nHalfScreenX); 	// 처음 스크롤 위치
	}

	/* (non-Javadoc)
	 * @see com.raimsoft.spacetrader.scene.SBase#Render()
	 */
	@Override
	public void Render()
	{
		super.Render();
		
		objSelection.angle+=3;	// 선택 포지션.
		sprBackgroundA.PutImage(gInfo, 0, 0, 0);
		//gInfo.TileData.DrawMap( 0, gInfo );		
		
		for(Planet PN : arrPlanet)
		{			
			if( PN.nIndex == PLANET_NUMS-1 )	// 마지막 행성이면 다음 라인 없음
				break;
			Planet curr= arrPlanet.get(PN.nIndex);
			Planet next= arrPlanet.get(PN.nIndex+1);
			gInfo.DrawLine(gl, (int)(curr.x-gInfo.ScrollX), (int)curr.y, (int)(next.x-gInfo.ScrollX), (int)next.y, 200, 255, 200, 1.0f, 1.0f);
		}
		
		int nSelGap= Math.abs(nSelectionIndex-nMyPos);
		if(nSelGap <= 2)	// 현재 위치와 선택 위치가 2개 차이 이하면 선을 그려줌
		{
			gInfo.DrawLine(gl, (int)(arrPlanet.get(nMyPos).x-gInfo.ScrollX), (int)arrPlanet.get(nMyPos).y, (int)(arrPlanet.get(nSelectionIndex).x-gInfo.ScrollX), (int)arrPlanet.get(nSelectionIndex).y, 230, 255, 0, 50, 6.5f);
		}
		
		for(Planet PN : arrPlanet)	// 행성 그리기
		{
			//if(PN.x <= gInfo.ScrollX)	// 절두체 계산 해보자
			PN.DrawSprite(gInfo);
		}
		
		objSelection.DrawSprite(gInfo);		
		objPositionMarker.DrawSprite(gInfo);
		
		for(GameObject Marker : arrMoveAbleMarker)
		{	
			if(Marker.pattern!=null)
				Marker.DrawSprite(gInfo);
		}
		
		// 패널 그리기				
		objPanel.DrawSprite(gInfo);
		
		// 패널 폰트 그리기
		txtInfo.BeginFont();		
			if(objPanel.show)	// 패널이 보일 때만 정보가 보인다.
			{
				txtInfo.DrawFont(gl, 20, gInfo.ScreenY-100, 23.0f, "행성-"+nSelectionIndex );
				txtInfo.DrawFont(gl, 20, gInfo.ScreenY-40, 28.0f, arrPlanet.get(nSelectionIndex).strName );	
			}		
			
			if(bDirectionR)
				txtInfo.DrawFont(gl, 0, 0, 12f, "Dir : Right");
			else
				txtInfo.DrawFont(gl, 0, 0, 12f, "Dir : Left");
			
			txtInfo.DrawFont(gl, 0, 20, 12f, "startX : "+Float.toString(fStartX));
			txtInfo.DrawFont(gl, 0, 40, 12f, "fCurrX : "+Float.toString(fCurrX));
			txtInfo.DrawFont(gl, 0, 60, 12f, "fOldX : "+Float.toString(fOldX));
			txtInfo.DrawFont(gl, 0, 80, 12f, "fGapX : "+Float.toString(fGapX));
			txtInfo.DrawFont(gl, 0, 100, 12f, "fScrollDes : "+Float.toString(fScrollDes));
			txtInfo.DrawFont(gl, 0, 120, 12f, "ScrollX : "+Float.toString(gInfo.ScrollX));		
		txtInfo.EndFont();
		
		btnMove.DrawSprite(gInfo);	// 이동 버튼
		msgBox.DrawSprite(gInfo);	// 메세지 박스
		
	}
	
	
	// 시스템맵에서 드래그앤드롭 스크롤 구현
	void Scroll()
	{
		if(gInfo.ScrollX < arrPlanet.get(0).x-nHalfScreenX)	// 왼쪽 끝으로 스크롤이 나가는 것을 방지
		{
			fGapX= 0.0f;
			fScrollDes= 0.0f;
			fOldX= 0.0f;
			gInfo.ScrollX = arrPlanet.get(0).x-nHalfScreenX;
			return;
		}
		if(gInfo.ScrollX > arrPlanet.get(arrPlanet.size()-1).x-nHalfScreenX)	// 오른쪽 끝으로 스크롤 방지
		{
			fGapX= 0.0f;
			fScrollDes= 0.0f;
			fOldX= 0.0f;
			gInfo.ScrollX = arrPlanet.get(arrPlanet.size()-1).x-nHalfScreenX;
			return;
		}
//		
		switch(GlobalInput.nTouchEvent)
		{
		case	MotionEvent.ACTION_DOWN	:
		case	MotionEvent.ACTION_POINTER_DOWN :
			if(fStartX==-1.1f)
				fStartX= GlobalInput.fTouchX;
			break;
		case	MotionEvent.ACTION_MOVE	:
			fCurrX= GlobalInput.fTouchX;
			if(fOldX!=0.0f)	// oldX값이 있으면 (처음 start입력이 아닌 경우만)
			{
				fGapX= (fCurrX-fOldX)*-1;
				fScrollDes= fGapX*-2.5f;
			}
			fOldX= GlobalInput.fTouchX;			
			
			//fScrollDes= ((fCurrX - fStartX)/10f) + fGapX*2f;
			//fScrollDes= (fCurrX - fStartX)/2.5f;
			
			
			if((bDirectionR) && (fGapX<0.0f))	// Right로 가고있는데 방향이 바뀌면
				fStartX= GlobalInput.fTouchX;
			
			if((!bDirectionR) && (fGapX>0.0f))
				fStartX= GlobalInput.fTouchX;
			
			// 방향체크
			if(fGapX > 0.0f)
				bDirectionR= true;
			else if(fGapX < 0.0f)
				bDirectionR= false;
		
			if((fGapX!=0.0f))	// 이전 터치하고 차이가 있으면
				if(!(gInfo.ScrollX+fScrollDes*-1 < arrPlanet.get(0).x-nHalfScreenX))	// 왼쪽 방지	
					if(!(gInfo.ScrollX+fScrollDes*-1 > arrPlanet.get(arrPlanet.size()-1).x-nHalfScreenX))	// 오른쪽 방지
						gInfo.ScrollX += fScrollDes*-1;

			break;
		case	MotionEvent.ACTION_UP :
		case	MotionEvent.ACTION_POINTER_UP :
			fScrollDes= 0.0f;
			fStartX= -1.1f;
			fOldX= -0.0f;
			fCurrX= -0.0f;
			
			if(Math.abs(fGapX) > 0.1f)	// 갭이 남아있으면
			{
				fGapX = fGapX * 0.97f;
				gInfo.ScrollX += fGapX/5.0f;
			}
			break;
 		}

	}

	/* (non-Javadoc)
	 * @see com.raimsoft.spacetrader.scene.SBase#Update()
	 */
	@Override
	public void Update()
	{
		super.Update();
		
		msgBox.UpdateObjects(gInfo.ScrollX);
		int nRes= msgBox.CheckOverButtons();
		if(nRes==0)
		{
			uInfo.setSystemMapPlanet_going(nSelectionIndex);
			SetScene(EnumScene.E_GAME_WRAP);
		}
		else if(nRes==1)
		{
			msgBox.SetShow(false);
		}
		if(msgBox.GetShow())	// 메세지박스 떠있으면 전부 무시
			return;

		
		
		
		btnMove.ButtonUpdate(gInfo.ScrollX);
		
		Scroll();		
		if(btnMove.CheckOver())
		{
			msgBox.SetButtonTextScr(22f, "["+arrPlanet.get(nSelectionIndex).strName+"]\n거리 : 123819023812km\n이 행성으로 이동하시겠습니까?", "출발", "뒤로");
			msgBox.SetBoxPosition((int)gInfo.ScrollX);
			msgBox.SetShow(true);
		}
		
		// Selection Tool
		if(GlobalInput.bTouch)
		{
			int nX= (int)GlobalInput.fTouchX +(int)gInfo.ScrollX;
			int nY= (int)GlobalInput.fTouchY;
			
			for(Planet PN : arrPlanet)
			{
				if(PN.CheckPos(nX, nY))	// 선택한 행성 체크
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
					int nSelGap= Math.abs(nSelectionIndex-nMyPos);
					if( (nMyPos!=nSelectionIndex) && (nSelGap<=2) )	// 이동 가능 행성만 버튼 띄워줌
					{
						btnMove.x= PN.x;
						btnMove.y= PN.y;
						btnMove.show= true;
					}
					else
					{
						btnMove.show= false;
					}
					
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
