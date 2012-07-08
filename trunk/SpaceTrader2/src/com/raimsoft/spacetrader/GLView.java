package com.raimsoft.spacetrader;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
public class GLView extends GLSurfaceView
{
	Context mContext;	
	public Game game;
	private float fTouchX_before= 0.0f;
	private float fTouchY_before= 0.0f;

	public GLView( Context context, Game img )
	{
		super( context );
		setFocusable( true );
		
		mContext = context;
		game = img;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
    {
		final int action = event.getAction();
		final int count = event.getPointerCount();
		
		switch ( action & MotionEvent.ACTION_MASK )
		{
			case	MotionEvent.ACTION_DOWN	:
			case	MotionEvent.ACTION_POINTER_DOWN :
					{
						
						GlobalInput.bTouch= true;
						GlobalInput.fTouchX= event.getX() * game.gInfo.ScalePx;
						GlobalInput.fTouchY= event.getY() * game.gInfo.ScalePy;
					}
					break;
			case	MotionEvent.ACTION_MOVE	:
					{
						GlobalInput.bTouch= true;
						GlobalInput.fTouchX= event.getX() * game.gInfo.ScalePx;
						GlobalInput.fTouchY= event.getY() * game.gInfo.ScalePy;
		
						GlobalInput.fTouchX_gap= GlobalInput.fTouchX - fTouchX_before;
						GlobalInput.fTouchY_gap= GlobalInput.fTouchY - fTouchY_before;
						//Log.d("GLView::onTouchEvent", "X_GAP : "+GlobalInput.fTouchX_gap + "     Y_GAP : "+GlobalInput.fTouchY_gap);
						
						fTouchX_before= GlobalInput.fTouchX;
						fTouchY_before= GlobalInput.fTouchY;
					}
					break;
	
			case	MotionEvent.ACTION_UP :
			case	MotionEvent.ACTION_POINTER_UP :
					{
						GlobalInput.bTouch= false;
						GlobalInput.fTouchX_End= event.getX() * game.gInfo.ScalePx;
						GlobalInput.fTouchY_End= event.getX() * game.gInfo.ScalePy;
//						GlobalInput.fTouchX= event.getX() * game.gInfo.ScalePx;
//						GlobalInput.fTouchY= event.getY() * game.gInfo.ScalePy;
					}
					break;
		}
      	return true;
    }

}

