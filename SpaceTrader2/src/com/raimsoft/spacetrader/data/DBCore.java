package com.raimsoft.spacetrader.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

class DBCore
{

	protected DataBaseHelper DBhelper;
	
	protected SQLiteDatabase DB;
	
	protected DBCore(Context _context)
	{
		DBhelper= new DataBaseHelper(_context);
		
//		try
//{
		DB = DBhelper.getWritableDatabase();
//}
//		catch(Exception e) // 권한이나 디스크 공간문제로 오류가 날 수 있으므로 방어코드로 Readable을 넣는다.
//		{
//			DB = DBhelper.getReadableDatabase();
//		}
		
		Log.d(Global.LOG_TAG, ":::BibleQuiz::: --> DB open");
	}

	/**
	 * 데이터를 가져온다.
	 * @param _strCol : 원하는 데이터의 칼럼.
	 * @return : 원하는 데이터의 커서가 리턴됨.
	 */
	protected Cursor GetCursorFromDB(String _strTable, String[] _strCol)
	{		
		Cursor CS= DB.query(_strTable, _strCol, null, null, null, null, null);
		
		return CS;
	}
	
	/**
	 * 문자열 데이터를 가져온다.
	 * @return
	 */
	public String GetStrDataFromDB(String _strTable, String strLabel, int nIdx)
	{
		String strRes = null;
		String[] strCol= {strLabel.toString()};
		
		Cursor CS= this.GetCursorFromDB(_strTable, strCol);
		
		//CS.moveToFirst(); 
		CS.move(nIdx);
		strRes= CS.getString(0);
		
		CS.close();
		
		return strRes;
	}
	
	/**
	 * 문자열 데이터를 가져온다.
	 * @return
	 */
	public String GetStrDataFromDB2(String _strTable, String strLabel, int nIdx)
	{
		String strRes = null;
		String[] strCol= {strLabel.toString()};
		
		Cursor CS= this.GetCursorFromDB(_strTable, strCol);
		
		CS.moveToFirst(); 
		CS.move(nIdx);
		strRes= CS.getString(0);
		
		CS.close();
		
		return strRes;
	}
	
	/**
	 * 문자열 데이터를 가져온다. (데이터가 한개인 경우)
	 * @return
	 */
	public String GetStrDataFromDB(String _strTable, String strLabel)
	{
		String strRes = null;
		String[] strCol= {strLabel.toString()};
		
		Cursor CS= this.GetCursorFromDB(_strTable, strCol);
		
		while( CS.moveToNext() )
			strRes= CS.getString(0);
		
		return strRes;
	}
	
	/**
	 * 정수형 데이터를 가져온다.
	 * @param _strTable
	 * @param strLabel
	 * @param nIdx
	 * @return
	 */
	public int GetIntDataFromDB(String _strTable, String strLabel, int nIdx)
	{
		int nRes = 0;		
		String[] strCol= {strLabel.toString()};
		
		Cursor CS= this.GetCursorFromDB(_strTable, strCol);
		
		CS.moveToFirst();
		CS.move(nIdx);
		
		try
		{
		nRes= CS.getInt(0);
		}catch(Exception e){	// 오류 발생율이 높다.
			Log.e(Global.LOG_TAG, e.toString());
			return 0;
		}
		
		CS.close();	// 이게 오류가 아닐까 추가해봄
		
		return nRes;
	}
	
	/**
	 * 문자열 데이터를 넣는다.
	 * @param strLabel : key값을 넣는다. (_id, level, money 등...)
	 * @param strData : 정보값을 넣는다.
	 */
	public void DBInsert(String _strTable, String strLabel, String strData)
	{
		ContentValues CV= new ContentValues();
		CV.put(strLabel, strData);
		
		Long lRow= DB.insert(_strTable, "0", CV);
		
		if (lRow < 0)
			Log.e(Global.LOG_TAG,"DBInsert error!");
		
		CV.clear();
	}
	
	/**
	 * 정수 데이터를 넣는다.
	 * @param strLabel : key값을 넣는다. (_id, level, money 등...)
	 * @param strData : 정보값을 넣는다.
	 */
	public void DBInsert(String _strTable, String strLabel, int nData)
	{
		ContentValues CV= new ContentValues();
		CV.put(strLabel, nData);
		
		DB.insert(_strTable, "0", CV);
		
		CV.clear();
	}
	
	/**
	 * ContentValues 객체를 insert 한다.
	 * @param _strTable : 대상 테이블
	 * @param _CV : ContentsValues
	 */
	public void DBInsertCV(String _strTable, ContentValues _CV)
	{
		DB.insert(_strTable, null, _CV);
	}
	
	/**
	 * ContentValues 객체를 update 한다.
	 * @param _strTable : 대상 테이블
	 * @param _cv : ContentsValues
	 */
	public void DBUpdateCV(String _strTable, ContentValues _cv, int nWhere)
	{
		DB.update(_strTable, _cv, "_id = "+nWhere, null);
	}
	
	/**
	 * 문자열 데이터를 수정한다.
	 * @param strLabel : key값을 넣는다. (_id, level, money 등...)
	 * @param strData : 정보값을 넣는다.
	 */
	public void DBUpdate(String _strTable, String strLabel, String strData)
	{
		ContentValues CV= new ContentValues();
		CV.put(strLabel, strData);
		
		DB.update(_strTable, CV, null, null);
		
		CV.clear();
	}
	
	/**
	 * 조건에 맞는 문자열 데이터를 수정한다.
	 * @param strLabel : key값을 넣는다. (_id, level, money 등...)
	 * @param strData : 정보값을 넣는다.
	 * @param strWhere : 조건식
	 */
	public void DBUpdate(String _strTable, String strLabel, String strData, String strWhere)
	{
		ContentValues CV= new ContentValues();
		CV.put(strLabel, strData);
		
		DB.update(_strTable, CV, strWhere, null);
		
		CV.clear();
	}
	
	/**
	 * 정수 데이터를 수정한다.
	 * @param strLabel : key값을 넣는다. (_id, level, money 등...)
	 * @param strData : 정보값을 넣는다.
	 */
	public void DBUpdate(String _strTable, String strLabel, int nData)
	{
		ContentValues CV= new ContentValues();
		CV.put(strLabel, nData);
		
		DB.update(_strTable, CV, null, null);
		
		CV.clear();
	}
	
	/**
	 * 
	 * @param _strTable : 테이블명
	 * @param strLabel : 컬럼명
	 * @param nData : 수정할 데이터
	 * @param strWhere : 조건
	 */
	public void DBUpdate(String _strTable, String strLabel, int nData, String strWhere)
	{
		ContentValues CV= new ContentValues();
		CV.put(strLabel, nData);
		
		DB.update(_strTable, CV, strWhere, null);
		
		CV.clear();
	}
	
	public int CountDBRows(String _strTable, String strLabel)
	{
		String[] strCol= {strLabel.toString()};
		Cursor CS= this.GetCursorFromDB(_strTable, strCol);
		
		return CS.getCount();
	}
	
	/**
	 * DB작업을 마치고 닫아준다.
	 */
	public void DBClose()
	{
		if( this.DBhelper != null )
			DBhelper.close();
		
		Log.d(Global.LOG_TAG, ":::BibleQuiz::: --> DB close");
	}
	
	
	
	
	protected class DataBaseHelper extends SQLiteOpenHelper
	{

		protected DataBaseHelper(Context context)
		{
			super(context, Global.DB_NAME, null, Global.DB_VERSION);
		}

		/**
		 * DB가 처음 만들어질 때 호출된다.
		 */
		@Override
		public void onCreate(SQLiteDatabase _DB)
		{
			CreateItemsInfoTable(_DB);
			
			Log.d(Global.LOG_TAG, "Call DB TABLE onCreate Complete");
		}



		/*
		 * DB버전이 올라가면 호출된다.(non-Javadoc)
		 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
		 */
		@Override
		public void onUpgrade(SQLiteDatabase _DB, int oldVer, int newVer) 
		{
//			Log.w(Global.LOG_TAG, "Upgrading database : " + oldVer + " to "+newVer + ", which will destroy all old data");
//			
/////////////////////////////////////////// DATA BACKUP /////////////////////////////////////////
//			
//			DBBackup.g_bBackuped_MY= true;		// 백업 시작
//			DBBackup.g_bBackuped_QUIZ= true;
//			
//try {
//			if(oldVer>=37)	// 키가 생기기 전
//			{
//				// MYINFO 백업할 데이터들
//				String[] strCol={"level", "exp", "money", "solved_total", "solved_o", "solved_x", "today_quiz_solved", "today_quiz_money","do_donation"};
//				Cursor CS= _DB.query(Global.DB_TABLE_MYINFO, strCol, null, null, null, null, null);
//			
//				CS.moveToFirst();
//				DBBackup.g_Bundle_MY.putInt("level", CS.getInt(0));
//				DBBackup.g_Bundle_MY.putInt("exp", CS.getInt(1));
//				DBBackup.g_Bundle_MY.putInt("money", CS.getInt(2));
//				DBBackup.g_Bundle_MY.putInt("solved_total", CS.getInt(3));
//				DBBackup.g_Bundle_MY.putInt("solved_o", CS.getInt(4));
//				DBBackup.g_Bundle_MY.putInt("solved_x", CS.getInt(5));
//				DBBackup.g_Bundle_MY.putString("today_quiz_solved", CS.getString(6));
//				DBBackup.g_Bundle_MY.putInt("today_quiz_money", CS.getInt(7));
//				DBBackup.g_Bundle_MY.putString("do_donation", CS.getString(8));
//				CS.close();
//			}
//			else
//			{
//				// MYINFO 백업할 데이터들
//				String[] strCol={"level", "exp", "key", "money", "solved_total", "solved_o", "solved_x", "today_quiz_solved", "today_quiz_money","do_donation"};
//				Cursor CS= _DB.query(Global.DB_TABLE_MYINFO, strCol, null, null, null, null, null);
//			
//				CS.moveToFirst();
//				DBBackup.g_Bundle_MY.putInt("level", CS.getInt(0));
//				DBBackup.g_Bundle_MY.putInt("exp", CS.getInt(1));
//				DBBackup.g_Bundle_MY.putInt("money", CS.getInt(2));
//				DBBackup.g_Bundle_MY.putInt("key", CS.getInt(3));
//				DBBackup.g_Bundle_MY.putInt("solved_total", CS.getInt(4));
//				DBBackup.g_Bundle_MY.putInt("solved_o", CS.getInt(5));
//				DBBackup.g_Bundle_MY.putInt("solved_x", CS.getInt(6));
//				DBBackup.g_Bundle_MY.putString("today_quiz_solved", CS.getString(7));
//				DBBackup.g_Bundle_MY.putInt("today_quiz_money", CS.getInt(8));
//				DBBackup.g_Bundle_MY.putString("do_donation", CS.getString(9));
//				CS.close();
//			}
//			
//			
//}catch(Exception e)
//{
//	DBBackup.g_bBackuped_MY= false;		// 내 정보 백업 실패
//	Log.e("DBCore::onUpgrade", "MYINFO_DB upgrade failed");
//}
//
//
//try {
//			 // QUIZINFO 백업할 데이터들
//			String[] strCol2={"char_name", "score", "locked", "char_solved_o", "char_solved_x"};
//			Cursor CS2= _DB.query(Global.DB_TABLE_QUIZINFO, strCol2, null, null, null, null, null);
//			
//			CS2.moveToFirst();
//			while( CS2.isAfterLast() == false )
//			{
//				Bundle bundle= new Bundle();
//				bundle.putString("char_name", CS2.getString(0));
//				bundle.putInt("score", CS2.getInt(1));
//				bundle.putString("locked", String.valueOf(CS2.getString(2)));
//				bundle.putInt("char_solved_o", CS2.getInt(3));
//				bundle.putInt("char_solved_x", CS2.getInt(4));
//				DBBackup.g_DB_QUIZ.add(bundle);		// QUIZINFO DB백업
//				
//				CS2.moveToNext();
//			}
//			CS2.close();
//			
////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////// DROP TABLE /////////////////////////////////////////
//			
//			_DB.execSQL("DROP TABLE IF EXISTS "+ Global.DB_TABLE_MYINFO);
//			_DB.execSQL("DROP TABLE IF EXISTS "+ Global.DB_TABLE_QUIZINFO);
//			_DB.execSQL("DROP TABLE IF EXISTS "+ Global.DB_TABLE_THEOLOGY);
//			_DB.execSQL("DROP TABLE IF EXISTS "+ Global.DB_TABLE_HELPSET);
//			_DB.execSQL("DROP TABLE IF EXISTS "+ "biblequiz");
//
//			
//}catch(Exception e)
//{
//	DBBackup.g_bBackuped_QUIZ= false;		// 퀴즈 푼 정보 백업 실패
//	Log.e("DBCore::onUpgrade", "QUIZ_DB upgrade failed");
//}
//
////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////// CREATE TABLE ///////////////////////////////////////			
//            onCreate(_DB);	
////////////////////////////////////////////////////////////////////////////////////////////////
		}
		
		@Override
		public void onOpen(SQLiteDatabase db)
		{
			super.onOpen(db);
		}
		
		@Override
		public synchronized void close()
		{
			super.close();
		}
		
		
		
//		private void CreateMyInfoTable(SQLiteDatabase _DB)
//		{
//			//_DB.execSQL("CREATE TABLE IF NOT EXISTS "+DB_NAME+" ("+
//			_DB.execSQL("CREATE TABLE "+Global.DB_TABLE_MYINFO+" ("+	// 나의 정보 테이블을 만든다.
//					"_id INTEGER PRIMARY KEY AUTOINCREMENT, " + // _id(int,기본키,자동증가값)
//					"level INTEGER, " +							// level(int,레벨)
//					"exp INTEGER, " +							// 지식, 경험치(int)
//					"money INTEGER, " +							// money(int,돈)
//					"key INTEGER, " +							// key(int,열쇠)
//					"solved_total INTEGER, " +					// 총 푼 문제
//					"solved_o INTEGER, " +						// 총 맞은 문제
//					"solved_x INTEGER, " +						// 총 틀린 문제
//					"first_run char(1)," +						// 첫 실행 여부
//					"today_quiz_solved char(1),"+				// 오늘의 퀴즈 풀었는지 여부
//					"today_quiz_money INTEGER,"+				// 오늘의 퀴즈 달란트 액수
//					"option_sound char(1)," +					// 소리 옵션
//					"option_vibrate char(1)," +					// 진동 옵션
//					"option_alarm char(1)," +					// 오늘의 퀴즈 알림 옵션
//					"option_quality char(1)," +					// 퀄리티 옵션
//					"do_donation char(1)" +					// 기부 여부
//					")");
			
//			ContentValues CV= new ContentValues();			
//			if (!DBBackup.g_bBackuped_MY) // 백업 데이터가 없을 경우에는 기본값
//			{
//				CV.put("level", 1);
//				CV.put("money", 300);
//				CV.put("key", 1);
//				CV.put("solved_total", 0);
//				CV.put("solved_o", 0);
//				CV.put("solved_x", 0);
//				CV.put("first_run", "T");
//				CV.put("today_quiz_solved", "F");
//				CV.put("today_quiz_money", 100);
//				CV.put("option_sound", "T");
//				CV.put("option_vibrate", "T");
//				CV.put("option_alarm", "T");
//				CV.put("option_quality", "T");
//				CV.put("do_donation", "F");
//			}else{
//				CV.put("level", DBBackup.g_Bundle_MY.getInt("level") );
//				CV.put("money", DBBackup.g_Bundle_MY.getInt("money"));
//				CV.put("key", DBBackup.g_Bundle_MY.getInt("key"));				
//				CV.put("solved_total", DBBackup.g_Bundle_MY.getInt("solved_total"));
//				CV.put("solved_o", DBBackup.g_Bundle_MY.getInt("solved_o"));
//				CV.put("solved_x", DBBackup.g_Bundle_MY.getInt("solved_x"));
//				CV.put("first_run", "F");
//				CV.put("today_quiz_solved", DBBackup.g_Bundle_MY.getString("today_quiz_solved"));
//				CV.put("today_quiz_money", DBBackup.g_Bundle_MY.getInt("today_quiz_money"));
//				CV.put("option_sound", "T");
//				CV.put("option_vibrate", "T");
//				CV.put("option_alarm", "T");
//				CV.put("option_quality", "T");
//				CV.put("do_donation", DBBackup.g_Bundle_MY.getString("do_donation"));
//			}
//			
//			_DB.insert(Global.DB_TABLE_MYINFO, null, CV); // DB초기값 insert
//			DBBackup.g_bBackuped_MY= false;		// DB백업 끝
//			}
		
		
		private void CreateItemsInfoTable(SQLiteDatabase _DB)
		{
			//_DB.execSQL("CREATE TABLE IF NOT EXISTS "+DB_NAME+" ("+
			_DB.execSQL("CREATE TABLE "+Global.DB_TABLE_ITEMSINFO+" ("+	// 퀴즈 정보 테이블을 만든다.
					"_id INTEGER PRIMARY KEY AUTOINCREMENT, " + // _id(int,기본키,자동증가값)
					"type INTEGER, " +							// 아이템 타입(int)
					"count INTEGER, " +							// 아이템 개수 (int)
					"price INTEGER" +							// 살 당시의 개당 가격(int)
					")");

			
		}
		
		
	}
			
			
}

