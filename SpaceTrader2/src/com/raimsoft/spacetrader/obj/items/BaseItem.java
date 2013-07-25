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

	public ItemData itemData= new ItemData();
	
//	public boolean bCheck;		// 상점에서 체크 되었는지 여부
//	public boolean bLastCheck;	// 상점에서 체크 되었는데 마지막으로 체크되었을 경우 트루
	
	public BaseItem()
	{
		bInit= false;
	}
	public BaseItem(Context _Context, GL10 _gl, EItems _eType, float fConst)
	{
		mContext= _Context;
		itemData.eType= _eType;				
		
//		bCheck= false;
//		bLastCheck= false;
		
		if(_eType== EItems.E_BOX)
		{
			itemData.nMinPrice= 7;
			itemData.nFixPrice= 10;
			itemData.nMaxPrice= 15;
		}
		else if(_eType== EItems.E_MATERIAL)
		{
			itemData.nMinPrice= 32;
			itemData.nFixPrice= 45;
			itemData.nMaxPrice= 68;
		}
		
		itemData.nCurrentPrice = (int)(itemData.nMinPrice + (itemData.nMaxPrice - itemData.nMinPrice) * fConst);
		
		SpriteInit(mContext, _gl);
	}
	public void SpriteInit(Context _Context, GL10 _gl)
	{
		mContext= _Context;
		
		sprShopButton.LoadSprite(_gl, mContext, R.drawable.buttons_2, "station_shopbtn.spr");	// 버튼 스프라이트 로드
		objShadow.SetObject(sprShopButton, 0, 0, this.x, this.y, 3, 0);			// 그림자 설정 
		btnShopCheck.SetButton(mContext, sprShopButton, this.x, this.y, 1,2);		// 체크버튼 설정
		objShopCheck2.SetObject(sprShopButton, 0, 0, this.x, this.y, 0, 0);		// 장바구니 설정
	}
	
//	/**
//	 * Shop Item이면 요걸 쓴다.
//	 * @param bCurrent
//	 */
//	public void CheckSettingShop(boolean bCurrent)
//	{
//		if(bCurrent)	// 지금 이 버튼이 눌렸으면
//		{
//			bLastCheck= true;
//		}
//		else
//		{
//			bLastCheck= false;
//		}
//	}
//	
//	/**
//	 * Inventory Item이면 요걸 쓴다.
//	 * @param bCurrent
//	 */
//	public void CheckSettingInventory(boolean bCurrent)
//	{
//		if(bCurrent)	// 지금 이 버튼이 눌렸는데
//		{
//			if(!bCheck && !bLastCheck)	// 한번도 안눌린 버튼이면
//				bLastCheck= true;
//			
//			if(bCheck)	// 장바구니면
//				bLastCheck= true;	// 체크로 다시 바꿈
//		}
//		else			// 다른 버튼이 눌린건데
//		{
//			if(bLastCheck)		// 이 버튼이 마지막 눌렸었던 버튼이면
//			{
//				bLastCheck= false;
//				bCheck= true;	// 장바구니로 바꿈
//			}
//		}
//	}
	
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
		btnShopCheck.bSound= false;
	}
	@Override
	public void DrawSprite(GameInfo info)
	{		
		super.DrawSprite(info);	// 아이템 먼저 그리고
		
//		if(bLastCheck)	// 마지막 체크이면
//		{
//			objShadow.DrawSprite(info);
//			btnShopCheck.DrawSprite(info);	// 구매체크버튼			
//			
//			btnShopCheck.ButtonUpdate(0.0f);
//		}
//		else if(bCheck)	// 그냥 체크면
//		{
//			objShadow.DrawSprite(info);
//			objShopCheck2.DrawSprite(info);	// 장바구니버튼		
//		}
		
	}
	
	/**
	 * 버튼 체크인지
	 * @return
	 */
	public boolean CheckOver()
	{		
		if(btnShopCheck.CheckOver())
			return true;
		else
			return false;	
	}
	

	

}
