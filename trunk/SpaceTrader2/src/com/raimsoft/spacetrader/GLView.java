package com.raimsoft.spacetrader;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
public class GLView extends GLSurfaceView
{
	Context mContext;	
	public Game game;

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
			case	MotionEvent.ACTION_MOVE	:
					{
						GlobalInput.bTouch= true;
						GlobalInput.fTouchX= event.getX() * game.gInfo.ScalePx;
						GlobalInput.fTouchY= event.getY() * game.gInfo.ScalePy;
					}
					break;
	
			case	MotionEvent.ACTION_UP :
			case	MotionEvent.ACTION_POINTER_UP :
					{
						GlobalInput.bTouch= false;
						GlobalInput.fTouchX= event.getX() * game.gInfo.ScalePx;
						GlobalInput.fTouchY= event.getY() * game.gInfo.ScalePy;
					}
					break;
		}
      	return true;
    }

}

