package com.raimsoft.spacetrader.scene;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;
import bayaba.engine.lib.Sprite;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.raimsoft.spacetrader.R;
import com.raimsoft.spacetrader.data.EnumShip;
import com.raimsoft.spacetrader.data.Global;
import com.raimsoft.spacetrader.data.GlobalInput;
import com.raimsoft.spacetrader.data.UserInfo;
import com.raimsoft.spacetrader.obj.CrashParticlesManager;
import com.raimsoft.spacetrader.obj.Meteor;
import com.raimsoft.spacetrader.obj.Missile;
import com.raimsoft.spacetrader.obj.ProgressMeter;
import com.raimsoft.spacetrader.obj.Radar;
import com.raimsoft.spacetrader.obj.Star;
import com.raimsoft.spacetrader.obj.fleets.BaseFleet;
import com.raimsoft.spacetrader.obj.fleets.TraningShip1;
import com.raimsoft.spacetrader.obj.fleets.TraningShip2;
import com.raimsoft.spacetrader.obj.items.BaseItem;
import com.raimsoft.spacetrader.obj.items.EItems;
import com.raimsoft.spacetrader.util.ParseConnector;

public class SGameWrap extends SBase
{	
	private UserInfo uInfo;
	private int nWhereIgoing;	// 내가 어디로 가고있나요? (워프)
	//private int nDestinationDistanceg;	// 목적지까지의 거리
	
	private Sprite sprStar = new Sprite();
	private Sprite sprShip= new Sprite();
	private Sprite sprMetoer= new Sprite();
	private Sprite sprMissile= new Sprite();
	private Sprite sprStation= new Sprite();	
	private Sprite sprItems= new Sprite();	// 드롭아이템들

	
	private boolean bReleased= false;
	private int nMeteorCount= 0;
	
	private BaseFleet objShip;					// 나의 함선
	private Missile	objMissile;					// 미사일
	private Radar objRader;						// 레이더
	private ProgressMeter objProgress;			// 프로그레스바
	private GameObject	objStation;				// 스테이션
	private List<BaseItem> arrItems= new ArrayList<BaseItem>();			// 드롭아이템들
	
	
	private CrashParticlesManager crashMgr;		// 파티클 매니저
	
	private Queue<Star> qStar= new LinkedList<Star>();
	private Queue<Meteor> qMetoer= new LinkedList<Meteor>();
	
	private Random rand = new Random();
	private MediaPlayer Music;
	//private Launcher UHL;
		
	public SGameWrap(Context _context, GameInfo _Info)
	{
		super(_context, _Info);
		
		//UHL= new Launcher(_context);
		this.eMode= EnumScene.E_GAME_WRAP;
		
		uInfo= UserInfo.GetInstance();
	}
	
	@Override
	public void LoadData()
	{
		super.LoadData();
		
		nWhereIgoing= uInfo.GetSystemMapPlanet_going();	// 내가 어디로 가고있나염
		
		Music = MediaPlayer.create(mContext, R.raw.warp);
		Music.setLooping(true);
		Music.start();
		
		if(uInfo.GetShipType() == EnumShip.E_TRAINING_SHIP_1)
		{
			sprShip.LoadSprite(gl, mContext, R.drawable.resource_2, "ship_1.spr");
			objShip= new TraningShip1(gl, mContext);
		}
		else if(uInfo.GetShipType() == EnumShip.E_TRAINING_SHIP_2)
		{
			sprShip.LoadSprite(gl, mContext, R.drawable.resource_2, "ship_2.spr");
			objShip= new TraningShip2(gl, mContext);
		}
		objShip.SetObject( sprShip, 0, 0, gInfo.ScreenX/2, gInfo.ScreenY+60, 0, 0 );
		objShip.nHP= uInfo.GetCurrHull();	// 저장된 체력을 불러옴
		objShip.scalex= 0.6f;
		objShip.scaley= 0.6f;
		
		sprStar.LoadSprite( gl, mContext, R.drawable.resource_2, "star_1.spr" );
		sprMetoer.LoadSprite( gl, mContext, R.drawable.resource_2, "meteor.spr" );
		sprMissile.LoadSprite(gl, mContext, R.drawable.resource_2, "missile_1.spr");
		sprStation.LoadSprite(gl, mContext, R.drawable.station_dummy1, "station_1.spr");
		//sprCrashEffect.LoadSprite(gl, mContext, R.drawable.meteor, "meteor_crash.spr");
		sprItems.LoadSprite(gl, mContext, R.drawable.list_items, "list_items.spr");
		crashMgr= new CrashParticlesManager(gl, mContext, gInfo);
		
		objRader= new Radar(gl, gInfo, mContext);
		objProgress= new ProgressMeter(gl, gInfo, mContext);
		objProgress.SetDistance( uInfo.GetDestinationDistance() );	// 거리 설정해줌.
	
		objMissile= new Missile(gl, mContext);
		objMissile.SetObject(sprMissile, 0, 0, objShip.x, objShip.y, 0, 0);
		
		objStation= new GameObject();
		objStation.SetObject(sprStation, 0, 0, 100, -320, 0, 0);
//		objStation.scalex= 0.85f;
//		objStation.scaley= 0.85f;
		
		
		
		
		
		for(int i=0; i<=50; ++i)
			qStar.offer(new Star());
		
		for(int i=0; i<=10; ++i)
		{
			++nMeteorCount;
			qMetoer.offer(new Meteor( rand.nextInt(7)-3 ));	// 앵글 -4 ~ 4 범위
		}
		
		this.MakeStar();
		this.MakeMetoer();
	}

	@Override
	public void Render()
	{
		if(bReleased) return;
		
		super.Render();

		
		for(Star GO : qStar)
			GO.DrawSprite(gInfo);
		
		if(objProgress.GetPercentToDestination() >= 70.0f)	// 70% 이상 도착하면 스테이션 보여줌
			objStation.DrawSprite(gInfo);		
				
		for(BaseItem BI : arrItems)
			BI.DrawSprite(gInfo);			
		
		for(Meteor MTO : qMetoer)
			MTO.DrawSprite(gl, gInfo);
				
		objMissile.DrawSprite(gInfo);
		objShip.DrawSprite(gInfo);
		
		crashMgr.RenderEffect();
		
		
		objRader.DrawObjects(gInfo);
		objProgress.DrawObjects(gInfo);
		
		
	}

	@Override
	public void Update()
	{
		if(bReleased) return;
		
		super.Update();
		
		if(!objProgress.bArrived)	// 도착할 때까지 별 업데이트
			this.UpdateStar();
		
		this.UpdateDropItems();
		this.UpdateMetoer();
		this.UpdateShip();
		this.UpdateMissile();
		crashMgr.UpdateParticleEffects();
		
		objRader.UpdateObjects();
		objProgress.UpdateObjects();
		objMissile.UpdateObjects(gInfo);
	}
	
	
	
	
	// 별 만듦
	private void MakeStar()
	{		
		int nCount= 0;
		for(GameObject GO : qStar)
		{
			++nCount;
			GO.SetObject( sprStar, 0, 0, rand.nextInt((int)gInfo.ScreenX), -1*nCount*(rand.nextInt(50)+25)+gInfo.ScreenY, 0, 0 );
			float fRandomScale= rand.nextFloat();
			GO.scalex= fRandomScale;
			GO.scaley= fRandomScale;
		}
	}
	
	// 소행성 만든다.
	private void MakeMetoer()
	{		
		int nCount= 0;
		for(Meteor MTO : qMetoer)
		{
			++nCount;
			MTO.SetObject( sprMetoer, 0, 0, rand.nextInt((int)gInfo.ScreenX), -1*nCount*500, rand.nextInt(3), 0 );
			//MTO.SetObject( sprMetoer, 0, 0, 225, -1*nCount*500, rand.nextInt(3), 0 );
		}
	}
	
	// 미사일 발사
	private void FireMissile()
	{
		if(!objMissile.isFired())	// 발사안했으면
		{
			objMissile.SetObject(sprMissile, 0, 0, objShip.x+objShip.pMissileStart.x, objShip.y+objShip.pMissileStart.y, 0, 0);
			objMissile.scalex= 0.65f;
			objMissile.scaley= 0.65f;
			objMissile.SetFire(true, false);
		}
	}
	
	private void UpdateMissile()
	{
		if(objMissile.y < -2*objMissile.GetYsize())
			objMissile.SetFire(false, false);
		
		for(Meteor MTO : qMetoer)	// 미사일과 메테오 충돌체크
		{
			if(!objMissile.isFired() || MTO.dead)	// 발사중 아니면 || 메테오 터진거면 충돌체크안함. 
				continue;
			
			if( gInfo.CrashCheck(MTO, objMissile, -3, -3) )	// 메테오와 미사일 충돌체크
			{
				crashMgr.MakeEffect(MTO.x, MTO.y);
				objMissile.SetFire(false, true);
				MTO.SetCrash(true, (int)MTO.x, (int)MTO.y);
				
				DropItem(MTO);				
			}
		}
		
		for(Meteor MTO : qMetoer)	// 메테오 큐 돌면서
		{
			boolean bCheck= crashMgr.IsNeedParticleCrashCheck();
			if(!bCheck) break;	// 파티클 아무것도 없으면 검사안함.
			
			GameObject[] arrParticleGroup= crashMgr.GetFristParticleGroup();
			
			for(GameObject BOOM : arrParticleGroup)
			{
				if( gInfo.CrashCheck(BOOM, MTO, 0, 0) && (MTO.dead==false) && (BOOM.dead==false)  )			// 파편과 메테오 충돌했을 때 파편의 방향대로 메테오 나감.
				{
//					MTO.bMoveByAngle=true;
//					MTO.fAngle= BOOM.angle;
//					MTO.fDistance= BOOM.trans*5.0f;
//					BOOM.dead= true;
					MTO.SetCrash(true, (int)MTO.x, (int)MTO.y); 
					crashMgr.MakeEffect(MTO.x, MTO.y);
					
					DropItem(MTO);
				}
			}
		}
	}

	private void DropItem(Meteor MTO)
	{
		BaseItem BI= new BaseItem();
		//BI.SetObject(sprItems, 0, 0, MTO.x, MTO.y, 0, EItems.E_MATERIAL.ordinal());
		Random rnd= new Random();
		int nItemType= rnd.nextInt(2);
		BI.SetObject(sprItems, 0, 0, MTO.x, MTO.y, 0, nItemType);
		BI.itemData.SetItemType(nItemType);
		BI.scalex= 0.6f; BI.scaley= 0.6f;
		arrItems.add(BI);
	}
	
	

	private void UpdateShip()
	{
		if(objShip.bDestroyed)	// 터지면 함선 업데이트 안함
			return;
		
		if(objProgress.GetPercentToDestination() >= 70.0f)
		{
			objStation.y += objShip.GetVelocity()/12.0f;	// 스테이션 보여줌
		}		
		
		if(objProgress.GetPercentToDestination() >= 95.0f)
		{			
			objShip.SetVelocity( objShip.GetVelocity()*0.99f );	// 서서히 속도를 줄인다.
			float fVolumn= ((100.0f-objProgress.GetPercentToDestination())*20.0f)/100.0f;
			if(fVolumn<= 0.0f)
				Music.setVolume(0.0f, 0.0f);
			else
				Music.setVolume(fVolumn, fVolumn);	// 볼륨도 서서히 줄임
			
		}
		
		if(objProgress.bArrived)
		{			
			if(-1500 <= objShip.y)	// 끝
			{
				objShip.y -= 12f;
				objShip.HyperspaceSound();
			}
			else
			{
				ParseUser currentUser = ParseUser.getCurrentUser();
				if (currentUser != null)
				{
					ParseQuery<ParseObject> query= ParseQuery.getQuery("UserInfo");	// 유저 데이터를 찾는다.
					query.whereEqualTo("user_id", currentUser);	// 해당되는 유저의
					query.findInBackground(new FindCallback<ParseObject>()
							{			
								@Override
								public void done(List<ParseObject> list, ParseException e)
								{
									for(ParseObject PO : list)
									{
										PO.put(Global.PO_SHIP_HULL, objShip.nHP);
										PO.put(Global.PO_CROOD_SYSTEM_MAP_PLANET, nWhereIgoing);	// 시스템 좌표 변경
										PO.saveInBackground();
									}
								}
							});
				} else {
				  // show the signup or login screen
				}
				
				
				
				uInfo.SetSystemMapPlanet(nWhereIgoing);	// 도착한 곳!
				uInfo.SetCurrHull(objShip.nHP);	// 도착했을 때 Hull 저장.				
				SetScene(EnumScene.E_GAME_DOCKING);
			}
			
			return;			
		}
		
		StartDisplay();
		
		objProgress.fCurr += objShip.GetVelocity();	// 프로그레스바에 속도만큼 계산해준다.
		
		objShip.SetCrash(false, 0, 0); 
		for(Meteor MTO : qMetoer)
		{
			if(MTO.dead)	// 터진 메테오는 체크 안함.
				continue;
			
			if(!objShip.bControlable)	// 조종 불가능시에는 맞지 않음
				continue;
			if(objShip.bSheild)	// 쉴드 상태에서는 맞지 않음
				continue;
			
			if( objShip.CheckPos( (int)MTO.x , (int)MTO.y ) && !objProgress.bArrived )	// 메테오와 함선 충돌체크
			{
				gInfo.SetQuake(1000, 6, 2);
				gInfo.DoQuake();
				objShip.SetCrash(true, (int)MTO.x, (int)MTO.y);
			}
		}
		
		if(GlobalInput.bTouch)	// 터치하면 미사일 나감.
			FireMissile();
		
		// 화면 밖으로 나가는 것 체크
		if(objShip.x-GlobalInput.fSensorX*2.0f < 0)
			return;
		
		if(objShip.x-GlobalInput.fSensorX*2.0f > gInfo.ScreenX)
			return;
		
		if(objShip.bControlable)
			objShip.MoveSensorX( GlobalInput.fSensorX * objShip.getHandeling() );	// 함선 좌우 이동

		
	}

	/**
	 * 시작 연출 부분
	 */
	private void StartDisplay()
	{
		//info.MoveCamera(0.0f, -200.0f, 0.5f);
		if( (objShip.nEventCount==0) && (gInfo.ScreenY-300 <= objShip.y) )	// 처음에 아래에서 나오는 연출
		{
			objShip.fEventSpeed = objShip.fEventSpeed * 0.979f;
			objShip.y -= objShip.fEventSpeed;
		}
		else	// y 300 도달하면
		{
			if((objShip.nEventCount==0))	// 다음 액션
			{
				objShip.nEventCount= 1;
				objShip.fEventSpeed= 0.55f;
			}			
		}
		if( (objShip.nEventCount==1) && (gInfo.ScreenY-200 > objShip.y) )	// 두번째 액션
		{
			objShip.fEventSpeed = objShip.fEventSpeed * 1.024f;
			objShip.y += objShip.fEventSpeed; 
		}
		else if((objShip.nEventCount==1) && (gInfo.ScreenY-200 <= objShip.y))	// 200까지 도달하면
		{
			objShip.nEventCount= 2;		// 다음 액션
			objShip.bControlable= true;
		}
		
		if( (objShip.nEventCount==2) && (gInfo.ScreenY-100 > objShip.y) )	// 세번째 액션
		{
			objShip.fEventSpeed = objShip.fEventSpeed * 0.971f;
			objShip.y += objShip.fEventSpeed; 
		}
		else if((objShip.nEventCount==2) && (gInfo.ScreenY-100 <= objShip.y))	// 100까지 도달하면
		{
			objShip.nEventCount= 3;		// 다음 액션
		}		
		
		if((objShip.nEventCount==3))	// 다 내려왓음
		{
			objShip.bSheild= false;
		}
	}
	
	private void UpdateStar()
	{
		for(GameObject GO : qStar)
			GO.y += objShip.GetVelocity();
				
		if( qStar.peek() != null )
		{
			if( qStar.peek().y > gInfo.ScreenY )
			{
				qStar.poll();
				
				Star star= new Star();
				star.SetObject( sprStar, 0, 0, rand.nextInt((int)gInfo.ScreenX), -1*(rand.nextInt(50)+25), 0, 0 );
				float fRandomScale= rand.nextFloat();
				star.scalex= fRandomScale;
				star.scaley= fRandomScale;
				qStar.offer(star);
			}
		}
	}
	
	private void UpdateDropItems()
	{
		for(int i=0; i<arrItems.size(); ++i)
		{
			BaseItem BI= arrItems.get(i);
			if( BI.y > gInfo.ScreenY + BI.GetYsize()/2 )	// 끝까지 내려왔으면 제거
			{
				arrItems.remove(i);
				return;
			}
			
			BI.y+= objShip.GetVelocity()/2.5f;
			
			if( objShip.CheckPos( (int)BI.x , (int)BI.y ) && !objProgress.bArrived )	// 메테오와 함선 충돌체크
			{
				uInfo.BuyItems(BI.itemData.eType.ordinal(), 1);
				ParseConnector PC= new ParseConnector();
				PC.GetItem(BI.itemData.eType.ordinal(), 1);
				
				arrItems.remove(i);
				return;
			}
		}	
		
	}
	
	private void UpdateMetoer()
	{		
		for(Meteor MTO : qMetoer)
		{			
			MTO.MoveUpdate(gInfo, objShip.GetVelocity());	// 메테오 이동
		}
				
		if( qMetoer.peek() != null )
		{
			//Log.d("qMetoer.peek()", "X : "+qMetoer.peek().x+"    Y : "+qMetoer.peek().y);
			if( qMetoer.peek().y > gInfo.ScreenY + qMetoer.peek().GetYsize()/2 )
			{
				++nMeteorCount;	// 메테오 지금까지 나온 카운트
				qMetoer.poll();
				
 				if(objProgress.GetPercentToDestination() <= 60.0f)	// 목적지에 90% 이상 왔으면 더 이상 메테오 없음.
				{
					Meteor mto= new Meteor( rand.nextInt(9)-4 );
					mto.SetObject( sprMetoer, 0, 0, rand.nextInt((int)gInfo.ScreenX), -1* rand.nextInt(2500)-150, rand.nextInt(3), 0 );
					//mto.SetObject( sprMetoer, 0, 0, 225, -1* rand.nextInt(2500)-150, rand.nextInt(3), 0 );
					Log.d("Create Meteor", "------> offer ------> Y : "+mto.y);
					qMetoer.offer(mto);
				}
				
				
//				for(int i=0; i<nMeteorCount/10; ++i)
//				{
//					int nRatio= 1;
//					if(nMeteorCount > 10)
//						nRatio= (int) nMeteorCount/10;
//					Meteor mto2= new Meteor( rand.nextInt(9)-4 );
//					mto2.SetObject( sprMetoer, 0, 0, rand.nextInt((int)gInfo.ScreenX), -1* rand.nextInt(2500/nRatio)-150, rand.nextInt(3), 0 );
//					Log.d("Create Meteor", "------> offer ------> Y : "+mto2.y);
//					qMetoer.offer(mto2);
//				}
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
		
		bReleased= true;
		
		objShip.Release();
		objMissile.Release();
		sprMetoer.Release();
		sprShip.Release();
		sprStar.Release();
		Music.release();
	}

}


