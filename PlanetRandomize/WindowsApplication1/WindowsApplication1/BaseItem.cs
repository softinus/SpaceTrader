using System;
using System.Collections.Generic;
using System.Text;

namespace WindowsApplication1
{
    enum EItems { E_BOX, E_MATERIAL }

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
            if (eType == EItems.E_BOX)
            {
                nMinPrice = 7;
                nFixPrice = 10;
                nMaxPrice = 15;                
            }
            else if (eType == EItems.E_MATERIAL)
            {
                nMinPrice = 32;
                nFixPrice = 45;
                nMaxPrice = 68;
            }

            nCurrentPrice = (int)(nMinPrice + (nMaxPrice - nMinPrice) * fConst);
        }
    }
}
