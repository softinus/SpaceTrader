package com.raimsoft.spacetrader.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RandomNameMaker
{
	int a=0, b=0, c=0;
	ArrayList<String> arrWord1, arrWord2, arrWord3, arrName;
	Random rand= new Random();

	public RandomNameMaker()
	{
		super();
		
		arrWord1= new ArrayList<String>();
		arrWord2= new ArrayList<String>();
		arrWord3= new ArrayList<String>();
		arrName= new ArrayList<String>();
		
		arrWord1.add("울퉁불퉁한");
		arrWord1.add("엉망진창인");
		arrWord1.add("감성적인");
		arrWord1.add("향긋한");
		arrWord1.add("청초한");
		arrWord1.add("박력있는");
		arrWord1.add("조심스런");
		arrWord1.add("혼미한");
		arrWord1.add("뇌쇄적인");
		arrWord1.add("흐리멍텅한");
		arrWord1.add("느린");
		arrWord1.add("친절한");
		arrWord1.add("거대한");
		arrWord1.add("멋진");
		arrWord1.add("졸린");
		arrWord1.add("맛있는");
		arrWord1.add("민첩한");
		arrWord1.add("행복한");
		arrWord1.add("더러운");
		
		arrWord2.add("금색");
		arrWord2.add("은색");
		arrWord2.add("잠꾸러기");
		arrWord2.add("일등");
		arrWord2.add("황토색");
		arrWord2.add("검은색");
		arrWord2.add("완전소중한");
		arrWord2.add("보라색");
		arrWord2.add("회색");
		arrWord2.add("귀족의");
		arrWord2.add("섹시한");
		arrWord2.add("초록색");
		arrWord2.add("천재");
		arrWord2.add("개구장이");
		arrWord2.add("비밀");
		arrWord2.add("똥색");
		arrWord2.add("졸린");
		
		arrWord3.add("야생화");
		arrWord3.add("거북이");
		arrWord3.add("유니콘");
		arrWord3.add("이카루스");
		arrWord3.add("스포츠카");
		arrWord3.add("펭귄");
		arrWord3.add("보아뱀");
		arrWord3.add("피라냐");
		arrWord3.add("메뚜기");
		arrWord3.add("딸기");
		arrWord3.add("꽃");
		arrWord3.add("순찰자");
		arrWord3.add("앵무새");
		arrWord3.add("카우보이");
		arrWord3.add("기관차");
		
		Collections.shuffle(arrWord1);
		Collections.shuffle(arrWord2);
		Collections.shuffle(arrWord3);
		

		
		
	}
	
	public ArrayList<String> GetNames(int nCount)
	{
		MakingName(nCount);
		
		return arrName;
	}
	
	private String ShuffleNames()
	{
		a= rand.nextInt(arrWord1.size());
		b= rand.nextInt(arrWord2.size());
		c= rand.nextInt(arrWord3.size()); 
		
		return ( arrWord1.get(a) +" "+ arrWord2.get(b) +" "+ arrWord3.get(c)+ " 행성" );
	}
	
	private void MakingName(int nCount)
	{
		if(arrName.size()==0)
			arrName.add(ShuffleNames());
			
		while(arrName.size() <= nCount)	// 이름 개수가 지정 카운트만큼 넘어야 통과
		{			
			for(String name : arrName)
			{			
				String str= ShuffleNames();
				
				while(str==name)
				{
					str= ShuffleNames();
				}
				arrName.add(str);
				break;
			}
		}
	}
	
}
