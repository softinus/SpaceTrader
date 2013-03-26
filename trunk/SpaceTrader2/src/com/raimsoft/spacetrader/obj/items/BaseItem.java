package com.raimsoft.spacetrader.obj.items;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;
import bayaba.engine.lib.Sprite;

import com.raimsoft.spacetrader.R;
import com.raimsoft.spacetrader.obj.GameButton;

public class BaseItem extends GameObject
{
	private Sprite sprShopButton= new Sprite();			// 상점 버튼 리스트 (0:그림자, 1:체크보통, 2:체크롤오버, 3:장바구니)
	private GameButton btnShopCheck= new GameButton();	// 구매체크버튼
	private GameObject objShopCheck2= new GameObject(); // 장바구니
	private GameObject objShadow= new GameObject();		// 그림자
	
	private Context mContext;
	public boolean bInit= true;	// 스프라이트 초기화가 제대로 되었는지


	int nMinPrice;
	int nMaxPrice;
	int nFixPrice;
	
	public int nCurrentPrice;
	
	public EItems eType;
	public int nCount;
	public boolean bCheck;		// 상점에서 체크 되었는지 여부
	public boolean bLastCheck;	// 상점에서 체크 되었는데 마지막으로 체크되었을 경우 트루
	
	public BaseItem()
	{
		bInit= false;
	}
	public BaseItem(Context _Context, GL10 _gl, EItems _eType, float fConst)
	{
		mContext= _Context;
		eType= _eType;				
		
		bCheck= false;
		bLastCheck= false;
		
		if(_eType== EItems.E_BOX)
		{
			nMinPrice= 7;
			nFixPrice= 10;
			nMaxPrice= 15;
		}
		else if(_eType== EItems.E_MATERIAL)
		{
			nMinPrice= 32;
			nFixPrice= 45;
			nMaxPrice= 68;
		}
		
		nCurrentPrice = (int)(nMinPrice + (nMaxPrice - nMinPrice) * fConst);
		
		SpriteInit(mContext, _gl);
	}
	public void SpriteInit(Context _Context, GL10 _gl)
	{
		mContext= _Context;
		
		sprShopButton.LoadSprite(_gl, mContext, R.drawable.buttons_2, "station_shop_buttons.spr");	// 버튼 스프라이트 로드
		objShadow.SetObject(sprShopButton, 0, 0, 0, 0, 0, 0);			// 그림자 설정 
		btnShopCheck.SetButton(mContext, sprShopButton, 0, 0, 1,2);		// 체크버튼 설정
		objShopCheck2.SetObject(sprShopButton, 0, 0, 0, 0, 3, 0);		// 장바구니 설정
	}
	
	public void CheckSetting(boolean bCurrent)
	{
		if(bCurrent)	// 지금 이 버튼이 눌렸는데
		{
			if(!bCheck && !bLastCheck)	// 한번도 안눌린 버튼이면
				bLastCheck= true;
			
			if(bCheck)	// 장바구니면
				bLastCheck= true;	// 체크로 다시 바꿈
		}
		else			// 다른 버튼이 눌린건데
		{
			if(bLastCheck)		// 이 버튼이 마지막 눌렸었던 버튼이면
			{
				bLastCheck= false;
				bCheck= true;	// 장바구니로 바꿈
			}
		}
	}
	
	@Override
	public void SetObject(Sprite s_pat, int s_type, float s_layer, float s_x, float s_y, int s_motion, int s_frame)
	{
		super.SetObject(s_pat, s_type, s_layer, s_x, s_y, s_motion, s_frame);
		
		objShadow.x= s_x;
		objShadow.y= s_y;
		
		objShopCheck2.x= s_x;
		objShopCheck2.y= s_y;
		
		btnShopCheck.x= s_x;
		btnShopCheck.y= s_y;		
	}
	@Override
	public void DrawSprite(GameInfo info)
	{
		super.DrawSprite(info);	// 아이템 먼저 그리고
		
		if(bLastCheck)	// 마지막 체크이면
		{
			objShadow.DrawSprite(info);
			btnShopCheck.DrawSprite(info);	// 구매체크버튼
			
		}
		else if(bCheck)	// 그냥 체크면
		{
			objShadow.DrawSprite(info);
			objShopCheck2.DrawSprite(info);	// 장바구니버튼		
		}
		
	}
	
	public void SetItemType(int _nType)
	{
		switch(_nType)
		{
		case 0:
			eType= EItems.E_BOX;
			break;
		case 1:
			eType= EItems.E_MATERIAL;
			break;
		}
	}
}
