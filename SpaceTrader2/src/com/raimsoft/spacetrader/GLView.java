package com.raimsoft.spacetrader;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
public class GLView extends GLSurfaceView
{
	Context mContext;	
	public GameMain sImg;

	public GLView( Context context, GameMain img )
	{
		super( context );
		setFocusable( true );
		
		mContext = context;
		sImg = img;
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
					}
					break;
	
			case	MotionEvent.ACTION_UP :
			case	MotionEvent.ACTION_POINTER_UP :
					{
					}
					break;
		}
      	return true;
    }

}

