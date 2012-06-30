package com.raimsoft.spacetrader.obj;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

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


	public void DrawSprite(GL10 gl, GameInfo info)
	{
		super.DrawSprite(info);
		
		
		//gl.glTranslatef(0.0f,0.0f,-10.0f);
	    gl.glClearColor(0.0f, 0.0f,0.0f, 1.0f);//  Black 0.0f ~ Color 1.0  Background
		gl.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);  //  Line Color
		   
		float verts[] = { -1.0f, -1.0f, 0.0f, 0.0f };
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, createFloatBuffer(verts));
		gl.glDrawArrays(GL10.GL_LINES, 0, verts.length / 3); // verts.length / 3 -> 한점을 3개의 포인터로 나타냄
		//gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
	
	private FloatBuffer createFloatBuffer(float[] array)
	{
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(array.length * 4);// == array.length << 2
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		floatBuffer.put(array);
		floatBuffer.position(0);

		return floatBuffer;
	}
}
