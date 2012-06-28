package com.raimsoft.spacetrader;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;
import bayaba.engine.lib.Sprite;

public class GameMain
{
	public float fSensorX = 0; // 가로축 센서 값
	public float fShipVelo = 13.0f; // 함선속도
	
	public GL10 gl;
	public Context MainContext;
	public GameInfo gInfo;
	
	private MediaPlayer Music;
	
	private Sprite main_bg= new Sprite();
	private Sprite sprShip = new Sprite();
	private Sprite sprStar= new Sprite();
	
	private GameObject objShip= new GameObject();
	//private GameObject objStar= new GameObject();
	//private ArrayList<GameObject> arrStar= new ArrayList<GameObject>();
	private Queue<GameObject> qStar= new LinkedList<GameObject>();
	
	private Random rand = new Random();
	
	public GameMain( Context context, GameInfo info )
	{
		MainContext = context;
		gInfo = info;
		
		Music = MediaPlayer.create(context, R.raw.intothedark);
		Music.setLooping(true);
	}
	
	public void LoadData()
	{
		Music.start();
		
		main_bg.LoadSprite( gl, MainContext, R.drawable.main_bg, "main_bg.spr" );
		sprShip.LoadSprite( gl, MainContext, R.drawable.resource_2, "ship_1.spr" );
		sprStar.LoadSprite( gl, MainContext, R.drawable.resource_2, "star_1.spr" );
			
		objShip.SetObject( sprShip, 0, 0, gInfo.ScreenX/2, gInfo.ScreenY-100, 0, 0 );
		
		for(int i=0; i<=30; ++i)
			qStar.offer(new GameObject());
		
		this.MakeStar();
	}
	
	private void MakeStar()
	{
//		GameObject[] arrStar= (GameObject[]) qStar.toArray();
//		
//		for(int i=0; i<arrStar.length; ++i)
//		{
//			arrStar[i].SetObject( sprStar, 0, 0, rand.nextInt((int)gInfo.ScreenX), -1*i*(rand.nextInt(50)+25), 0, 0 );
//			float fRandomScale= rand.nextFloat();
//			arrStar[i].scalex= fRandomScale;
//			arrStar[i].scaley= fRandomScale;
//		}
		
		int nCount= 0;
		for(GameObject GO : qStar)
		{
			++nCount;
			GO.SetObject( sprStar, 0, 0, rand.nextInt((int)gInfo.ScreenX), -1*nCount*(rand.nextInt(50)+25)+gInfo.ScreenY, 0, 0 );
			float fRandomScale= rand.nextFloat();
			GO.scalex= fRandomScale;
			GO.scaley= fRandomScale;
		}
		
		
		
		
	}
	
	public void DoGame()
	{
		
		//sprShip.PutImage(gInfo, ship_x, 300, 0);
		//objShip.AddFrameLoop(0.5f);
		Log.d("objship", Float.toString(objShip.x+objShip.GetXsize()));
		
		this.Update();
		this.Render();
	}
	
	private void Render()
	{		
		//main_bg.PutImage(gInfo, 0, 0, 0);
		
		for(GameObject GO : qStar)
			GO.DrawSprite(gInfo);
				
		objShip.DrawSprite(gInfo);
	}
	
	private void Update()
	{
		this.UpdateStar();
		this.UpdateShip();
	}
	
	private void UpdateShip()
	{
		if(objShip.x-objShip.GetXsize()/2-fSensorX*2.0f < 0)
			return;
		
		if(objShip.x+objShip.GetXsize()/2-fSensorX*2.0f > gInfo.ScreenX)
			return;
		
		//Log.d("DoGame()", Float.toString(fSensorX));		
		objShip.x -= fSensorX * 2.0f;
	}
	
	private void UpdateStar()
	{
		for(GameObject GO : qStar)
			GO.y += fShipVelo;
				
		if( qStar.peek() != null )
		{
			if( qStar.peek().y > gInfo.ScreenY )
			{
				qStar.poll();
				
				GameObject star= new GameObject();
				star.SetObject( sprStar, 0, 0, rand.nextInt((int)gInfo.ScreenX), -1*(rand.nextInt(50)+25), 0, 0 );
				float fRandomScale= rand.nextFloat();
				star.scalex= fRandomScale;
				star.scaley= fRandomScale;
				qStar.offer(star);
			}
		}
	}
}
