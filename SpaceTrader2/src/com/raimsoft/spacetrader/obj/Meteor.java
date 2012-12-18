package com.raimsoft.spacetrader.obj;

import javax.microedition.khronos.opengles.GL10;

import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;

public class Meteor extends GameObject
{	
	/**
	 * @param lfAngle
	 */
	public Meteor(double lfAngle)
	{
		super();
		this.lfAngle = lfAngle;
	}

	public double lfAngle= 0.0f;		// 처음으로 지정되서 렌더시 계속 쓰는 앵글값

	/**
	 * 충돌값 세팅시
	 * @param bCrash the bCrash to set
	 */
	public void SetCrash(boolean bCrash, int x, int y)
	{		
		if(this.dead)
			return;
				
		if(bCrash)
		{
			dead= true;
		}
		else
		{
			
		}
	}
	
	
	
	public void DrawSprite(GL10 gl, GameInfo info)
	{
		if(this.dead)
		{
			return;
		}
		
		if( -50 < this.y )//if( (0-this.GetYsize()*2 < this.y) && (info.ScreenY+this.GetYsize()*2 > this.y) )
			super.DrawSprite(info);
		
//		int xSize= this.GetXsize()/2;
//		int ySize= this.GetYsize()/2;
//		
//		info.DrawLine(gl, (int)x-xSize, (int)y-ySize, (int)x+xSize, (int)y-ySize, 255, 0, 255, 1, 3);
//		info.DrawLine(gl, (int)x+xSize, (int)y-ySize, (int)x+xSize, (int)y+ySize, 255, 0, 255, 1, 3);
//		info.DrawLine(gl, (int)x+xSize, (int)y+ySize, (int)x-xSize, (int)y+ySize, 255, 0, 255, 1, 3);
//		info.DrawLine(gl, (int)x-xSize, (int)y+ySize, (int)x-xSize, (int)y-ySize, 255, 0, 255, 1, 3);
		//info.DrawLine(gl, (int)x-30, (int)y-30, (int)x+30, (int)y+30, 255, 0, 255, 1, 3);
		//info.DrawLine(gl, (int)x-30, (int)y+30, (int)x+30, (int)y-30, 255, 0, 255, 1, 3);
		
//		//gl.glTranslatef(0.0f,0.0f,-10.0f);
//	    gl.glClearColor(0.0f, 0.0f,0.0f, 1.0f);//  Black 0.0f ~ Color 1.0  Background
//		gl.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);  //  Line Color
//		   
//		float verts[] = { -1.0f, -1.0f, 0.0f, 0.0f };
//		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, createFloatBuffer(verts));
//		gl.glDrawArrays(GL10.GL_LINES, 0, verts.length / 3); // verts.length / 3 -> 한점을 3개의 포인터로 나타냄
//		//gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
//	
//	private FloatBuffer createFloatBuffer(float[] array)
//	{
//		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(array.length * 4);// == array.length << 2
//		byteBuffer.order(ByteOrder.nativeOrder());
//		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
//		floatBuffer.put(array);
//		floatBuffer.position(0);
//
//		return floatBuffer;
//	}
}
