package com.raimsoft.spacetrader.obj.items;

public class ItemData
{
	public int nMinPrice;
	public int nMaxPrice;
	public int nFixPrice;
	
	public int nCurrentPrice;
	
	public EItems eType;
	public int nCount;
	public String strItemName;
	
	
	/**
	 * @param nMinPrice
	 * @param nMaxPrice
	 * @param nFixPrice
	 * @param nCurrentPrice
	 * @param eType
	 * @param nCount
	 */
	public ItemData()
	{
		super();
		this.nMinPrice = 0;
		this.nMaxPrice = 0;
		this.nFixPrice = 0;
		this.nCurrentPrice = 0;
		this.eType = EItems.E_BOX;
		strItemName="Box";
		this.nCount = 0;
	}



	public void SetItemType(int _nType)
	{
		switch(_nType)
		{
		case 0:
			eType= EItems.E_BOX;
			strItemName="Box";
			break;
		case 1:
			eType= EItems.E_MATERIAL;
			strItemName="Material";
			break;
		}
	}
}
