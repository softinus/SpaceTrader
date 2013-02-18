// test_console1.cpp : 콘솔 응용 프로그램에 대한 진입점을 정의합니다.
//

#include "stdafx.h"
#include <math.h>


int _tmain(int argc, _TCHAR* argv[])
{
	int x= 320;
	int y= 50;
	float fRes= 0.0f;

	for (int p=0; p<=10; ++p)
	{
		fRes= ((x+1)*(y+1)*(p+2) / (( (x+1) % (p+2) )+1)) / 100000.0f;

		int nMultiCalcCount= 0, nLogCalcCount= 0;
		while( fRes < 0.09f )	// 0.05보다 작으면
		{
			//fRes= pow((float)(-1*(p+2)), fRes);
			//fRes= fRes/(p+2);
			fRes *= (p+2);
			++nMultiCalcCount;
		}

		while( fRes > 1.0f )	// 1이 넘어가면
		{
			//fRes= pow((float)(-1*(p+2)), fRes);
			//fRes= fRes/(p+2);
			//fRes /= (p*2);
			fRes= log(fRes);
			++nLogCalcCount;
		}

		if((nMultiCalcCount==0) && (nLogCalcCount==0))
		{
			printf("[%d,%d:%d] %.3f\n", x,y,p, fRes);
		}
		else if (nMultiCalcCount==0)
		{
			printf("[%d,%d:%d] %.3f (LogCount : %d)\n", x,y,p, fRes, nLogCalcCount);
		}
		else if (nLogCalcCount==0)
		{
			printf("[%d,%d:%d] %.3f (MultiCount : %d)\n", x,y,p, fRes, nMultiCalcCount);
		}
		else
		{
			printf("[%d,%d:%d] %.3f (MultiCount : %d)(LogCount : %d)\n", x,y,p, fRes, nMultiCalcCount, nLogCalcCount);
		}
		
	}

	return 0;
}

