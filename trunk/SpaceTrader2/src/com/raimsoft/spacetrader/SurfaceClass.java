package com.raimsoft.spacetrader;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SurfaceClass implements android.opengl.GLSurfaceView.Renderer
{
	public GameMain sImg;
	public float scaleX, scaleY, sy;

	public SurfaceClass( GameMain dImg )
	{
		sImg = dImg;
	}
	
	@Override
	public void onSurfaceCreated( GL10 gl, EGLConfig config )
	{
		gl.glClearColor( sImg.gInfo.BackR, sImg.gInfo.BackG, sImg.gInfo.BackB, 1.0f );
		gl.glClearDepthf( 1.0f );
		gl.glMatrixMode( GL10.GL_PROJECTION );
		gl.glHint( GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST );
		
		scaleX = sImg.gInfo.ScreenXsize * (sImg.gInfo.ScreenXsize / sImg.gInfo.ScreenX);
		scaleY = sImg.gInfo.ScreenYsize * (sImg.gInfo.ScreenYsize / sImg.gInfo.ScreenY);
		sy = sImg.gInfo.ScreenYsize - scaleY;

		gl.glOrthof( 0.0f, scaleX, scaleY, 0.0f, 1.0f, 1.0f );
		gl.glViewport( 0, 0, (int)scaleX, (int)scaleY );

		sImg.gl = gl;
		sImg.LoadData();
	}
	
	@Override
	public void onSurfaceChanged( GL10 gl, int width, int height )
	{
		gl.glOrthof( 0.0f, sImg.gInfo.ScreenXsize, sImg.gInfo.ScreenYsize, 0.0f, 1.0f, -1.0f );
		gl.glMatrixMode( GL10.GL_MODELVIEW );
		gl.glViewport( 0, (int)sy, (int)scaleX, (int)scaleY );
		
		gl.glEnable( GL10.GL_TEXTURE_2D );
		gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
		gl.glEnableClientState( GL10.GL_TEXTURE_COORD_ARRAY );
		gl.glEnable( GL10.GL_BLEND );
		gl.glBlendFunc( GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA );
	}
	
	@Override
	public void onDrawFrame( GL10 gl )
	{
		synchronized ( gl )
		{
			gl.glClear( GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT );
			gl.glLoadIdentity();
			
			sImg.DoGame();
		}
	}
}
