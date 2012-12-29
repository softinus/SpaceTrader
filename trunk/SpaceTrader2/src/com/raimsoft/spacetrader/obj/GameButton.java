package com.raimsoft.spacetrader.obj;

import javax.microedition.khronos.opengles.GL10;

import com.raimsoft.spacetrader.R;
import com.raimsoft.spacetrader.data.GlobalInput;
import com.raimsoft.spacetrader.util.SoundManager;

import android.content.Context;
import bayaba.engine.lib.ButtonType;
import bayaba.engine.lib.Font;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;
import bayaba.engine.lib.Sprite;

public class GameButton extends GameObject
{
	int nButtonState= ButtonType.STATE_NON_BUTTON;
	int nMouseOverFrame;
	int nClickX, nClickY;
	private SoundManager Sound;
	
	private boolean bTextDraw= false;
	private String strContent= "";
	private int nTextX, nTextY;
	private float fTextSize;
	private float fR= 0.75f, fG= 0.75f, fB=0.75f;

	public void SetButton(Context _context, Sprite spr, int _x, int _y, int s_motion)
	{		
		SetObject(spr, 0, 0, _x, _y, s_motion, 0);
		
		Sound= new SoundManager(_context);
		Sound.Create();
		Sound.Load(0, R.raw.button1);		
		
		nMouseOverFrame= 1;
	}
	public void SetText(int _x, int _y, float _size, String _str)
	{
		strContent= _str;
		nTextX= _x;
		nTextY= _y;
		fTextSize= _size;
	}
	public void SetTextCenter(float _size, String _str)
	{
		strContent= _str;
		nTextX= (int) (this.x + ( this.GetXsize()/2 - (_str.length() * (int)_size*0.45f) ));
		nTextY= (int) (this.y + ( this.GetYsize()/2 - (int)_size*0.38f ));
		fTextSize= _size;
	}
	
	
	public void DrawButtonWithText(GameInfo info, GL10 _gl, Font font)
	{
		super.DrawSprite(info);
		
		//font.DrawFont(_gl, nTextX, nTextY, fTextSize, strContent);
		font.DrawColorFont(_gl, info, nTextX, nTextY, fR, fG, fB, fTextSize, strContent);
	}

	public boolean CheckOver()
	{
		if( !(nButtonState==ButtonType.STATE_CLK_BUTTON) )	// 클릭 상태 아니면 넘어감.
			return false;
		
		nButtonState= ButtonType.STATE_NON_BUTTON;	// 초기상태로 되돌림.
		this.frame= 0;
		return true;		
	}
	
	public void ButtonUpdate()
	{
		int nX= (int) GlobalInput.fTouchX;
		int nY= (int) GlobalInput.fTouchY;
		boolean bTouch= GlobalInput.bTouch;
		
		if( CheckPos(nX, nY) && bTouch )
		{
			nClickX= nX;
			nClickY= nY;
			nButtonState= ButtonType.STATE_DOWN_BUTTON;
			this.frame= nMouseOverFrame;
		}
		
		if(ButtonType.STATE_DOWN_BUTTON == nButtonState)
		{
			if( CheckPos(nX, nY) )	// 범위 안에 있으면
				this.frame= 1;
			else
				this.frame= 0;
			
			if(!bTouch)	// 버튼 누른 상태에서 떼었을 때
			{
				if( CheckPos(nX, nY) )	// 범위 안에 있으면
				{
					nButtonState= ButtonType.STATE_CLK_BUTTON;
					this.frame= 0;
					Sound.Play(0);
				}
				else
				{
					nButtonState= ButtonType.STATE_NON_BUTTON;	// 초기상태로 되돌림.
					this.frame= 0;
				}
			}
		}
	}
	
}
