package com.raimsoft.spacetrader.scene;

import android.content.Context;
import bayaba.engine.lib.Font;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;

import com.raimsoft.spacetrader.GlobalInput;

public class SStartStory extends SBase
{
	private float fScrollY= 0.0f;
//	private Sprite sprStory1= new Sprite();
//	private Sprite sprStory2= new Sprite();
//	private Sprite sprStory3= new Sprite();
//	private Sprite sprStory4= new Sprite();
	
	private GameObject objStory1= new GameObject();
	private GameObject objStory2= new GameObject();
	private GameObject objStory3= new GameObject();
	private GameObject objStory4= new GameObject();
	
	private Font font = new Font();
	
	public SStartStory(Context mContext, GameInfo gInfo)
	{
		super(mContext, gInfo);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void LoadData()
	{
		super.LoadData();
		
//		sprStory1.LoadSprite( gl, mContext, R.drawable.story1, "story1.spr" );
//		sprStory2.LoadSprite( gl, mContext, R.drawable.story2, "story2.spr" );
//		sprStory3.LoadSprite( gl, mContext, R.drawable.story3, "story3.spr" );
//		sprStory4.LoadSprite( gl, mContext, R.drawable.story4, "story4.spr" );
	
		//objLogo.SetObject(sprLogo, 0, 0, gInfo.ScreenX/2, gInfo.ScreenY/2, 0, 0);
		
//		objStory1.SetObject(sprStory1,0,0, 0,0, 0,0);
//		objStory2.SetObject(sprStory2,0,0, 0,0, 0,0);
//		objStory3.SetObject(sprStory3,0,0, 0,0, 0,0);
//		objStory4.SetObject(sprStory4,0,0, 0,0, 0,0);
		
//		objStory1.trans= 0.0f;
//		objStory2.trans= 0.0f;
//		objStory3.trans= 0.0f;
//		objStory4.trans= 0.0f;
		
	}

	@Override
	public void Render()
	{
		super.Render();
		
		
		
		if		( (-200.0f < fScrollY) && ( fScrollY <= 0) )
		{
			//objStory1.DrawSprite(gInfo);
		}
		else if ( (-400.0f < fScrollY) && ( fScrollY <= -200) )
		{
			//objStory2.DrawSprite(gInfo);
		}
		else if ( (-600.0f < fScrollY) && ( fScrollY <= -400) )
		{
			//objStory3.DrawSprite(gInfo);
		}
		else if ( (-800.0f < fScrollY) && ( fScrollY <= -600) )
		{
			//objStory4.DrawSprite(gInfo);
		}
		else if ( (-1000.0f < fScrollY) && ( fScrollY <= -800) )
		{
			//objStory1.DrawSprite(gInfo);
		}
		else if ( (-1200.0f < fScrollY) && ( fScrollY <= -1000) )
		{
			//objStory2.DrawSprite(gInfo);
		}
		else if ( (-1400.0f < fScrollY) && ( fScrollY <= -1200) )
		{
			//objStory3.DrawSprite(gInfo);
		}
		else if ( (-1600.0f < fScrollY) && ( fScrollY <= -1400) )
		{
			//objStory4.DrawSprite(gInfo);
		}
		else if ( (-1800.0f < fScrollY) && ( fScrollY <= -1600) )
		{
			//objStory1.DrawSprite(gInfo);
		}
		else
			this.SetScene(EnumScene.E_GAME_WRAP);
			
		font.BeginFont();
		
		//(1:회색빛의 지구를 그림으로)
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY-180, 20.0f, "지구의 광물은 점점 고갈되고...");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY-150, 20.0f, "푸른 행성은 점점 회색 빛으로 변해가고 있었다.");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY-120, 20.0f, "인류는 푸른빛을 원했고...");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY-90, 20.0f,  "더 큰 세상으로 나아가길 원했다.");		 
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY-60, 20.0f,  "시간이 갈수록...");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY-30, 20.0f,  "지구는 인간이 살수없는 곳으로");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY,    20.0f,  "바뀌어 가고있었다.");
		 
		
		//(2: 위 그림 연속)
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+90, 20.0f, "식물은 싹을 피기전에 말라죽거나 기형이 생겼고");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+120 , 20.0f, "먹이 사슬은 뒤 엉켜 인간의 힘으로는 풀수 없었고");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+150, 20.0f, "동물들도 살수 없었다. 그 누구도 말세인 지구를");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+180, 20.0f, " 지켜보며 구원하려 나서지 않았다.");
		
		//(3:테라포밍한 숲의 모습)
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+240, 20.0f, "이렇게 가다간 우리 세대가 10대도 가기전에");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+270, 20.0f, "모두 멸망해 버릴것이라는생각을 했다.");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+300, 20.0f, "모든 국민이, 지도자가, 과학자가 지구를 떠나");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+330, 20.0f, "우주로 나아가자는 이론을  펼쳤다.");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+360, 20.0f, "지구는 변화했다. 녹음없던 행성에 자그만하게나마");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+390, 20.0f, "교과서에서나 볼 수 있던 숲...");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+420, 20.0f, "사람들이 항상 그 숲에 가득했다. ");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+450, 20.0f, "테라포밍한 인조 숲이 생겨났다.");
		
		//(4: 지구를 떠나는 모습)
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+540, 20.0f, "1세대가 지나고 테라포밍에 한계가 오기 시작했다.");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+570, 20.0f, "인간은 우주를 향해 손을 뻗기 위해");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+600, 20.0f, "우주선을 다시 연구하기 시작했다.");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+630, 20.0f, "녹음을 더 보고싶었다. 후대에 더 많이 남겨주고싶었다.");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+660, 20.0f, " 그뿐이였다... ");
		
		//(5: 위 그림 연속)
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+750, 20.0f, "연구는 빠른 속도로 진행되었고, ");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+780, 20.0f, "포화 상태였던 지구를 버리고 첫번째의 수송대함이 발진했다.");
		
		//(6: 새 행성에 정착하는 모습)
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+840, 20.0f, "수 년 안에 함선들은 하나 둘 새로운 행성에 정착을 시작했다.");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+870, 20.0f, "많은 행성들에 정착을 하고 지구에 맞먹는");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+900, 20.0f, "문명을 수 세대 안에 완성시킬수 있었다.");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+930, 20.0f, "지구의 삶보다는 힘들고 윤택하지 못했지만 그들은 만족했다.");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+960, 20.0f, "흙을 밟으며 살수 있기 때문에..");
		
		//(7: 위 그림 연속)
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+1050, 20.0f, "하지만 인간의 욕심은 달라진 환경에서도 그대로였다.");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+1080, 20.0f, "적응력이 떨어진 행성들은 수세대에 걸처");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+1110, 20.0f, " 번영한 행성들에게 잡혀 살았다.");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+1140, 20.0f, " 수많은 군주들이 합쳐 새로운 연합을 창설했다.");
		font.DrawFont(gl, 20, fScrollY+gInfo.ScreenY+1170, 20.0f, " 인간 연합은 과거의 유물이 되었다.");
		
		

		
		font.EndFont();
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		
		this.SetScene(EnumScene.E_MAIN);
	}

	@Override
	public void Update()
	{
		super.Update();
		
		
		if(GlobalInput.bTouch)
			//fScrollY -= 10.00f;
			this.SetScene(EnumScene.E_GAME_WRAP);			
		else
			fScrollY -= 0.50f;
		
		//if(sprStory1)
	}

}
