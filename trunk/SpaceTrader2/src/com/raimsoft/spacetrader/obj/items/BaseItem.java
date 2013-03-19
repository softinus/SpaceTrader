package com.raimsoft.spacetrader.obj.items;

import bayaba.engine.lib.GameObject;

public class BaseItem extends GameObject
{
	int nMinPrice;
	int nMaxPrice;
	int nFixPrice;
	
	public int nCurrentPrice;
	
	public EItems eType;
	public int nCount;
	
	public BaseItem()
	{
		
	}
	public BaseItem(EItems _eType, float fConst)
	{
		eType= _eType;				
		
		if(_eType== EItems.E_BOX)
		{
			nMinPrice= 7;
			nFixPrice= 10;
			nMaxPrice= 15;
		}
		else if(_eType== EItems.E_MATERIAL)
		{
			nMinPrice= 32;
			nFixPrice= 45;
			nMaxPrice= 68;
		}
		
		nCurrentPrice = (int)(nMinPrice + (nMaxPrice - nMinPrice) * fConst);
	}
	
	public void SetItemType(int _nType)
	{
		switch(_nType)
		{
		case 0:
			eType= EItems.E_BOX;
			break;
		case 1:
			eType= EItems.E_MATERIAL;
			break;
		}
	}
}
