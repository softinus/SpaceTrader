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

	cout << "Current time : " << asctime(pTime) << endl;


	int x= 341;
	int y= 754;
	float fRes= 0.0f;
	float fSummary= 0.0f;
	float fAverage= 0.0f;

	for (int p=0; p<=P_COUNT; ++p)
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

	printf("[%d,%d:*] SUM : %.3f\n", x,y, fSummary);
	printf("[%d,%d:*] AVR : %.3f\n", x,y, fSummary/P_COUNT);

	int sum_p= 0;
	int sum_a= 0;
	int count_p= 0;

	//for(int y= 2012; y<=2014; ++y)
	//{
	//	for(int m=1; m<=12; ++m)
	//	{
	//		for(int d= 1; d<=30; ++d)
	//		{
	//			for(int h=1; h<=24; ++h)
	//			{
	//				int price= (int) ( (y+(m*12))%(d*h)	);
	//				printf("[%d년 %d월 %d일 %d시 -> %d \n", y, m, d, h, price);
	//				sum_p+= price;
	//				++count_p;
	//			}
	//		}
	//	}
	//}

	printf("(%d개)평균 -> %d \n", count_p, sum_p/count_p);

	return 0;
}

