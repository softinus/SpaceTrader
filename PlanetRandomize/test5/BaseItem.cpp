#include "StdAfx.h"
#include "BaseItem.h"


CBaseItem::CBaseItem(int _nLow, int _nFix, int _nHigh)
{
	m_nLowPrice= _nLow;
	m_nFixPrice= _nFix;
	m_nHighPrice= _nHigh;

}


CBaseItem::~CBaseItem(void)
{
}
