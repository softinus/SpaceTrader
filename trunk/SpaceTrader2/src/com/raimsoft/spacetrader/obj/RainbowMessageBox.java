package com.raimsoft.spacetrader.obj;

import javax.microedition.khronos.opengles.GL10;

import com.raimsoft.spacetrader.R;

import android.content.Context;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;
import bayaba.engine.lib.Sprite;

public class RainbowMessageBox extends GameObject
{
	Sprite sprButton;
	GameButton btnPositive;
	GameButton btnNegative;
	boolean bShowMsg= false;
	
	/**
	 * 레인보우UI스타일의 메세지 박스 사용
	 * @param _gl
	 * @param _context
	 * @param _nBoxType : 버튼한개사용(0), 버튼두개사용(1)
	 */
	public RainbowMessageBox(GL10 _gl, Context _context, int _nBoxType)
	{
		super();
		
		sprButton= new Sprite();
		btnPositive= new GameButton();
		btnNegative= new GameButton();
		
		sprButton.LoadSprite(_gl, _context, R.drawable.buttons_2, "rainbow_messagebox_button.spr");
		
		if(_nBoxType==0)	// OK only
		{
			btnPositive.SetButton(_context, sprButton, this.GetXsize()/2, this.GetYsize()-100, 0);
		}
		else if (_nBoxType==1)	// 2 Button
		{
			btnPositive.SetButton(_context, sprButton, this.GetXsize()-100, this.GetYsize()-100, 0);
			btnNegative.SetButton(_context, sprButton, this.GetXsize()+100, this.GetYsize()-100, 0);
		}
	}
	
	/**
	 * 메세지박스의 버튼 라벨 삽입
	 * @param _fSize : 폰트 크기
	 * @param _str1 : 첫번째 버튼 이름
	 * @param _str2 : 두번쨰 버튼 이름 (형식이 다르면 그냥 무시된다.
	 */	
	public void SetButtonTextScr(float _fSize, String _str1, String _str2)
	{
		btnPositive.SetTextCenter(_fSize, _str1);
		btnPositive.SetTextCenter(_fSize, _str2);
	}
	
	/**
	 * 이 메세지박스를 사용하면 해당 Scene의 update메서드에 실행.
	 */
	public void UpdateObjects()
	{
		if(!bShowMsg)
			return;
			
		btnPositive.ButtonUpdate(0f);
		btnNegative.ButtonUpdate(0f);
	}
	
	/**
	 * 어느 버튼이 눌렸는지 확인하는 메서드
	 * @return 첫번째버튼(0), 두번째버튼(1), 아무버튼안눌렸음(-1)
	 */
	public int CheckOverButtons()
	{
		if(!bShowMsg)
			return -1;
		
		if(btnPositive.CheckOver())
		{
			return 0;
		}
		if(btnNegative.CheckOver())
		{
			return 1;
		}
		return -1;
	}
	
	public void SetShow(boolean _b)
	{
		bShowMsg= _b;
	}

	@Override
	public void DrawSprite(GameInfo info)
	{
		if(bShowMsg)
		{
			super.DrawSprite(info);
			btnPositive.DrawSprite(info);
			btnNegative.DrawSprite(info);
		}
	}
	
	
	
}
