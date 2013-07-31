using System;
using System.Collections.Generic;
using System.Text;

namespace WindowsApplication1
{
    enum EItems { E_BOX, E_MAT, E_GAS }

    class BaseItem
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
            if (_eType == EItems.E_BOX)
            {
                nMinPrice = 6;
                nFixPrice = 10;
                nMaxPrice = 16;  
            }
            else if (_eType == EItems.E_MAT)
            {
                nMinPrice = 105;
                nFixPrice = 160;
                nMaxPrice = 230;
            }
            else if (_eType == EItems.E_GAS)
            {
                nMinPrice = 320;
                nFixPrice = 450;
                nMaxPrice = 680;
            }

            nCurrentPrice = (int)(nMinPrice + (nMaxPrice - nMinPrice) * fConst);
        }
    }
}
