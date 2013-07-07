package com.raimsoft.spacetrader.scene;

import android.content.Context;
import bayaba.engine.lib.Font;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.Sprite;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.raimsoft.spacetrader.R;
import com.raimsoft.spacetrader.data.EnumShip;
import com.raimsoft.spacetrader.data.Global;
import com.raimsoft.spacetrader.data.GlobalInput;
import com.raimsoft.spacetrader.data.UserInfo;
import com.raimsoft.spacetrader.obj.GameButton;
import com.raimsoft.spacetrader.obj.RainbowMessageBox;

public class SGalaxyMap extends SBase
{
	float tX= 0.0f, tY= 0.0f;
	int x1,y1, x2,y2, x3,y3, x4,y4;
	Font fntCrood;
	
	private Sprite sprGalaxyMapBG;
	private Sprite sprButtonMove= new Sprite();
	Sprite sprMessage= new Sprite();
		
	private GameButton btnMove= new GameButton();
	private RainbowMessageBox msgBox;
	
	private UserInfo uInfo;

	public SGalaxyMap(Context mContext, GameInfo gInfo)
	{
		super(mContext, gInfo);
		
		fntCrood= new Font();
		sprGalaxyMapBG= new Sprite();
		
		uInfo= UserInfo.GetInstance();
		
	}

	@Override
	public void LoadData()
	{
		super.LoadData();		
		
		sprGalaxyMapBG.LoadSprite(gl, mContext, R.drawable.worldmap_bg, "worldmap_bg.spr");
		
		sprButtonMove.LoadSprite(gl, mContext, R.drawable.buttons_2, "systemmap_btn_move.spr");
		btnMove.SetButton(mContext, sprButtonMove, gInfo.ScreenX-135, gInfo.ScreenY-50, 0);	// 설정 버튼
		
		sprMessage.LoadSprite(gl, mContext, R.drawable.buttons_2, "rainbow_messagebox.spr");
		msgBox= new RainbowMessageBox(gl, mContext);
		msgBox.SetMessageBox(1, sprMessage, 0, 0, gInfo.ScreenX/2, gInfo.ScreenY/2, 0, 0);
		msgBox.scroll= false;
	}

	@Override
	public void Render()
	{
		super.Render();
		
		sprGalaxyMapBG.PutImage(gInfo, 0, 0);
		
		
		gInfo.DrawLine(gl, x1, y1, x2, y2, 0, 255, 0, 0, 3.0f);
		gInfo.DrawLine(gl, x3, y3, x4, y4, 0, 255, 0, 0, 3.0f);
		
		fntCrood.BeginFont();
		//fntCrood.DrawFont(gl, 0.0f, 0.0f, 25.0f, "("+(int)GlobalInput.fTouchX+", "+(int)GlobalInput.fTouchY+")");
		fntCrood.DrawColorFont(gl, 5, 10, 0.9f, 0.9f, 0.9f, 17.0f, "안녕하세요. 사령관님. 우주무역에 처음 오신 것을 환영합니다.");
		fntCrood.DrawColorFont(gl, 5, 35, 0.9f, 0.9f, 0.9f, 17.0f, "우주 지도에서 앞으로 활동하시게 될 지역을 설정하셔야 합니다.");
		fntCrood.DrawColorFont(gl, 5, 60, 0.9f, 0.9f, 0.9f, 17.0f, "좌표 설정 후 Move 버튼을 눌러주세요! +_+~");
		
		fntCrood.DrawFont(gl, (int)tX+10, (int)tY-25, 20.0f, "("+(int)tX+", "+(int)tY+")");
		fntCrood.EndFont();
		
		btnMove.DrawSprite(gInfo);	// 이동 버튼
		msgBox.DrawSprite(gInfo);	// 메세지 박스
		
	}

	@Override
	public void Update()
	{
		super.Update();
		
		msgBox.UpdateObjects(0.0f);	// 메세지박스 동작
		int nRes= msgBox.CheckOverButtons();		
		if(nRes==0)	// 선택 완료되면
		{
			ParseUser currentUser = ParseUser.getCurrentUser();
			if (currentUser != null)
			{
				ParseObject userInfo = new ParseObject("UserInfo");
				userInfo.put(Global.PO_USER_ID, currentUser);
				userInfo.put(Global.PO_MOENY, 350);
				userInfo.put(Global.PO_SHIP_TYPE, EnumShip.E_TRAINING_SHIP_1.ordinal());
				userInfo.put(Global.PO_SHIP_HULL, 3500);
				userInfo.put(Global.PO_CROOD_WORLD_X, (int)tX);
				userInfo.put(Global.PO_CROOD_WORLD_Y, (int)tY);
				userInfo.put(Global.PO_CROOD_SYSTEM_MAP_PLANET, 1);
				userInfo.saveInBackground();
				
				uInfo.SetLogin(true);	// 로그인을 설정하면 게임 넘어감
				uInfo.SetGold(350);
				uInfo.SetShipType(EnumShip.E_TRAINING_SHIP_1);
				uInfo.SetCurrHull(3500);
				uInfo.SetWorldMapX((int)tX);
				uInfo.SetWorldMapY((int)tY);
				uInfo.SetSystemMapPlanet(1);
					
				SetScene(EnumScene.E_GAME_SYSTEMMAP);
			} else {
			  // show the signup or login screen
			}
			
		}
		else if(nRes==1)
		{
			msgBox.SetShow(false);
		}
		if(msgBox.GetShow())	// 메세지박스 떠있으면 전부 무시
			return;


		
		btnMove.ButtonUpdate(0.0f);			
		if(btnMove.CheckOver())
		{
			msgBox.SetButtonTextScr(22f, "당신의 홈좌표를\n( "+(int)tX+" : "+(int)tY+" )\n좌표로 설정하시겠습니까?", "확인", "취소");
			msgBox.SetBoxPosition(0);
			msgBox.SetShow(true);
		}
		
		if( GlobalInput.fTouchY > gInfo.ScreenY-100 )
			return;
		
		if( GlobalInput.fTouchY < 100 )
			return;
		
		tX= GlobalInput.fTouchX;
		tY= GlobalInput.fTouchY;
		
		x1= 0;
		x2= (int)gInfo.ScreenX;
		
		y1=y2= (int)tY;
		
		x3=x4= (int)tX;
		y3= 0;
		y4= (int)gInfo.ScreenY;
		
	}
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		
		this.SetScene(EnumScene.E_MAIN);
	}

}
