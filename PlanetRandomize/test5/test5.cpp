// test_console1.cpp : 콘솔 응용 프로그램에 대한 진입점을 정의합니다.
//

#include "stdafx.h"
#include <math.h>
#include <ctime>
#include <iostream>

using namespace std;

#define P_COUNT 10

int _tmain(int argc, _TCHAR* argv[])
{
	time_t cur_time;
	struct tm* pTime;

	time(&cur_time);
	pTime = localtime(&cur_time);

	cout << "월 : " << pTime->tm_mon+1 << endl;
	cout << "일 : " << pTime->tm_mday << endl;
	cout << "시 : " << pTime->tm_hour << endl;
	cout << "분 : " << pTime->tm_min << endl;
	cout << "초 : " << pTime->tm_sec << endl;


	int x= 141;
	int y= 254;
	float fRes= 0.0f;
	float fSummary= 0.0f;
	float fAverage= 0.0f;

	CBase


	for (int p=0; p<P_COUNT; ++p)
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

		//fRes *= 200;
		fSummary += fRes;

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

	printf("[%d,%d:*] 합계 : %.3f\n", x,y, fSummary);
	printf("[%d,%d:*] 평균 : %.3f\n", x,y, fSummary/P_COUNT);

	return 0;
}

