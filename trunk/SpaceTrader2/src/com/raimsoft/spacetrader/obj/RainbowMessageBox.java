package com.raimsoft.spacetrader.obj;

import java.util.StringTokenizer;

import javax.microedition.khronos.opengles.GL10;

import com.raimsoft.spacetrader.R;

import android.content.Context;
import bayaba.engine.lib.Font;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;
import bayaba.engine.lib.Sprite;

public class RainbowMessageBox extends GameObject
{
	private boolean bShowMsg= false;
	
	private Sprite sprButton;
	private GameButton btnPositive;
	private GameButton btnNegative;
	
	private Context mContext;
	private GL10 mGL;
	private Font mFont= new Font();
	
	private int nScroll= 0;		// 스크롤값
	private int nBoyType= 0;
	private String strContent="";
	private float fContentTextSize= 26f;
	private float fButtonTextSize= 20f;
	
	/**
	 * 레인보우UI스타일의 메세지 박스 사용
	 * @param _gl
	 * @param _context
	 * @param _nBoxType : 버튼한개사용(0), 버튼두개사용(1)
	 */
	public RainbowMessageBox(GL10 _gl, Context _context)
	{
		super();
		
		mContext= _context;
		mGL= _gl;
		
		sprButton= new Sprite();
		btnPositive= new GameButton();
		btnNegative= new GameButton();
		btnPositive.scroll= false;
		btnNegative.scroll= false;
		
		//sprButton.LoadSprite(_gl, _context, R.drawable.buttons_2, "rainbow_messagebox_button.spr");
		sprButton.LoadSprite(_gl, mContext, R.drawable.buttons_2, "rainbow_messagebox2.spr");
		
	} 
	
	/**
	 * 
	 * @param _nBoxType
	 * @param s_pat
	 * @param s_type
	 * @param s_layer
	 * @param s_x
	 * @param s_y
	 * @param s_motion
	 * @param s_frame
	 */
	public void SetMessageBox(int _nBoxType, Sprite s_pat, int s_type, float s_layer, float s_x, float s_y, int s_motion, int s_frame)
	{
		super.SetObject(s_pat, s_type, s_layer, s_x, s_y, s_motion, s_frame);
		
		nBoyType= _nBoxType;
		
		if(nBoyType==0)	// OK only
		{
			btnPositive.SetButton(mContext, sprButton, 0, 0, 0);
		}
		else if (nBoyType==1)	// 2 Button
		{
			btnPositive.SetButton(mContext, sprButton, 0, 0, 0); 
			btnNegative.SetButton(mContext, sprButton, 0, 0, 0);
		}
	}
	
	/**
	 * 메세지 박스 위치 조정
	 * @param _nScroll : 스크롤이 있다면 스크롤값
	 */
	public void SetBoxPosition(int _nScroll)
	{
		nScroll= _nScroll;
		
		if(nBoyType==0)	// OK only
		{
			btnPositive.x= nScroll+240;
			btnPositive.y= 470;
		}
		else if (nBoyType==1)	// 2 Button
		{
			btnPositive.x= nScroll+240-90;
			btnPositive.y= 470;
			
			btnNegative.x= nScroll+240+90;
			btnNegative.y= 470;			
		}
				
	}

	/**
	 * 메세지박스의 버튼 라벨 삽입
	 * @param _fSize : 폰트 크기
	 * @param _str1 : 첫번째 버튼 이름
	 * @param _str2 : 두번째 버튼 이름 (형식이 다르면 그냥 무시된다.)
	 */	
	public void SetButtonTextScr(float _fSize, String _strContent, String _str1, String _str2)
	{	
		fContentTextSize= _fSize;
		
		btnPositive.SetTextCenter2(_fSize, _str1);
		btnNegative.SetTextCenter2(_fSize, _str2);
		
		strContent= _strContent;
	}
	
	/**
	 * 이 메세지박스를 사용하면 해당 Scene의 update메서드에 실행.
	 */
	public void UpdateObjects(float _fScroll)
	{
		if(!bShowMsg)
			return;
			
		btnPositive.ButtonUpdate(_fScroll);
		
		if(nBoyType!=0)
			btnNegative.ButtonUpdate(_fScroll);
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
	
	public boolean GetShow()
	{
		return bShowMsg;
	}

	@Override
	public void DrawSprite(GameInfo info)
	{
		if(bShowMsg)
		{
			mFont.BeginFont();
			
			super.DrawSprite(info);
			
			StringTokenizer strToken= new StringTokenizer(strContent,"\n");
			int nCount= 0;
			while(strToken.hasMoreElements())
			{
				String str= strToken.nextToken();
				mFont.DrawColorFont(mGL, this.x-150, this.y-95+(nCount*fContentTextSize), 0.75f, 0.75f, 0.75f, fContentTextSize, str);
				++nCount;
			}
			
			
			btnPositive.DrawButtonWithText2(info, mGL, mFont, -nScroll-5, -5);
			
			if(nBoyType!=0)
				btnNegative.DrawButtonWithText2(info, mGL, mFont, -nScroll-5, -5);
			
			
			
//			int nTextPer1Line= (int) (this.GetXsize() / fContentTextSize);	nTextPer1Line -= 2;
//			int nLineCount= strContent.length() / nTextPer1Line;
//			
//			for(int i=0; i<nLineCount; ++i)
//			{
//				String str;
//				str= strContent.substring(nTextPer1Line*i, nTextPer1Line*(i+1)).toString();
//				mFont.DrawColorFont(mGL, this.x-150, this.y-95+(i*fContentTextSize), 0.75f, 0.75f, 0.75f, fContentTextSize, str);
//			}
			
			mFont.EndFont();
		}
	}
	
	
	
}
