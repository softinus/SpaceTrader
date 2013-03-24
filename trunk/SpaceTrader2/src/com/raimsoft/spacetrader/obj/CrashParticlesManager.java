package com.raimsoft.spacetrader.obj;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;
import bayaba.engine.lib.Sprite;

import com.raimsoft.spacetrader.R;

class CrashParticleGroup
{
	boolean bParticleEnd= true;			// 파티클 재생이 끝낫나?
	int nParticleIndex= 0;				// 이 파티클의 인덱스
	private static int MAX_PARTICLE = 8;// 파티클 그룹의 개수
	
	private GameObject Particle_BOOM[] = new GameObject[MAX_PARTICLE];
	private Sprite sprCrashEffect= new Sprite();
	
	
	private Random MyRand = new Random();
	
	
	CrashParticleGroup(GL10 _gl, int _nIndex, Context mContext)//float fMakeX, float fMakeY)
	{
		for ( int i = 0; i < MAX_PARTICLE; i++ ) Particle_BOOM[i] = new GameObject();
		nParticleIndex= _nIndex;
		
		bParticleEnd= true;	// 파티클 처음에는 멈춰있는상태
		//MakeGlow(fMakeX, fMakeY);
		
		sprCrashEffect.LoadSprite(_gl, mContext, R.drawable.glow, "glow.spr");
	}
	
	void RenderParticles(GameInfo gInfo)
	{
		for ( int i = 0; i < MAX_PARTICLE; i++ )	// 파괴 이펙트
			if ( Particle_BOOM[i].dead == false ) Particle_BOOM[i].DrawSprite( gInfo );
	}
	
	void UpdateParticleEffect(GameInfo gInfo)
	{		
		for ( int i = 0; i < MAX_PARTICLE; i++ )
		{
			if ( Particle_BOOM[i].dead == false && Particle_BOOM[i] != null)
			{
				Particle_BOOM[i].Zoom( gInfo, 0.02f, 0.02f );
				
				Particle_BOOM[i].trans -= 0.042f;
				Particle_BOOM[i].scalex -= 0.075f;
				Particle_BOOM[i].scaley -= 0.075f; 
						
				if ( Particle_BOOM[i].trans <= 0 ) Particle_BOOM[i].dead = true;			
				
				Particle_BOOM[i].MovebyAngle( gInfo, Particle_BOOM[i].angle, 3.5f);
			}
		}
//		boolean bParticleAllDead= false;
//		for ( int i = 0; i < MAX_PARTICLE; i++ )
//		{
//			if( Particle_BOOM[i].dead )
//				bParticleAllDead= true;
//		}
		
		if(Particle_BOOM[0].dead)
		{
			bParticleEnd= true;	// 파티클 끝남
			Log.d("arrParticles", nParticleIndex+"th Particle end!");
		}
		
		
	}
	
	void MakeGlow(float x, float y)
	{		
		for ( int i = 0; i < MAX_PARTICLE; i++ )
		{
			if ( Particle_BOOM[i].dead == true )
			{
				Particle_BOOM[i].SetObject( sprCrashEffect, 0, 0, x, y, 0, MyRand.nextInt(8) );
				Particle_BOOM[i].dead = false;
				Particle_BOOM[i].angle = i*44.9f;
				//Particle_BOOM[i].effect = 1;
			}
		}
	}
	
	GameObject[] GetParticleGroup()
	{
		return Particle_BOOM;
	}
	
}

/**
 * Particle들을 관리해주는 클래스
 * @author ChoiJunHyeok
 *
 */
public class CrashParticlesManager
{
	//private ArrayList<CrashParticleGroup> arrParticles= new ArrayList<CrashParticleGroup>();
	private CrashParticleGroup arrParticles[]= new CrashParticleGroup[5];
	// 이 부분을 배열로 바꿔서 5개 정도 메모리 로딩 미리 해놓고 이펙트 나올 때마다 업데이트 되는 식으로 함 해보자
	private GL10 mGL;
	private Context mContext;
	private GameInfo gInfo;
	private Sprite sprCrashEffect= new Sprite();
	
	public CrashParticlesManager(GL10 _gl, Context _Context, GameInfo _gInfo)
	{
		mGL= _gl;
		mContext= _Context;
		gInfo= _gInfo;
		
		for(int i=0; i<arrParticles.length; ++i)
		{
			arrParticles[i]= new CrashParticleGroup(mGL, i, mContext);
		}
		
		//arrParticles.add(new CrashParticleGroup(_gl, 0, mContext));
	}
	
	public void UpdateParticleEffects()
	{
		//boolean bFirstParticleGroupEnd= false;
		for(CrashParticleGroup CP : arrParticles)	// 파티클그룹 돌면서 시작한게 있으면 업데이트
		{
			if( !CP.bParticleEnd )
				//bFirstParticleGroupEnd = true;
			//else
				CP.UpdateParticleEffect(gInfo);
		}
		
//		if(bFirstPGEnd)
//		{
//			arrParticles.remove(0);
//			Log.d("arrParticles", "First Particle Group remove!");
//		}
	}
	
	public void MakeEffect(float fX, float fY)
	{
//		for(CrashParticleGroup CP : arrParticles)
//		{
//			if( CP.bParticleEnd )
//		}
		//arrParticles.add(new CrashParticleGroup(mGL, arrParticles.size()-1, mContext, fX, fY));
		//Log.d("arrParticles", (arrParticles.size()-1)+"th Particle added!");
		
		for(CrashParticleGroup CP : arrParticles)
		{
			if( CP.bParticleEnd )	// 업데이트 중이지 않은 파티클이면
			{
				CP.bParticleEnd= false;
				CP.MakeGlow(fX, fY);
				Log.d("arrParticles", CP.nParticleIndex+"th Particle added!");
				return;	// 하나 만들었으면 끝냄
			}
		}
	}
	
	/**
	 * 첫번째 활성화된 파티클그룹 가져온다.
	 * @return
	 */
	public GameObject[] GetFristParticleGroup()
	{
//		if(!arrParticles.isEmpty())
//			return arrParticles.get(0).GetParticleGroup();
//		else
//			return null;
		return arrParticles[0].GetParticleGroup();
	}
	
	// 파티클과 메테오 충돌검사가 필요한지 여부를 가져온다.
	public boolean IsNeedParticleCrashCheck()
	{
		return (!arrParticles[0].bParticleEnd);	// 첫번째 파티클그룹이 끝나있으면 충돌검사 필요없음.
	}
	
	public void RenderEffect()
	{
		for(CrashParticleGroup CP : arrParticles)
		{
			if( !CP.bParticleEnd )
				CP.RenderParticles(gInfo);
		}
	}
	
	
}