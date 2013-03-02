#pragma once
class CBaseItem
{
public:
	int m_nLowPrice;
	int m_nFixPrice;
	int m_nHighPrice;

public:
	CBaseItem(int,int,int);
	~CBaseItem(void);
};

