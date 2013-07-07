package com.raimsoft.spacetrader;

import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import bayaba.engine.lib.GameInfo;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SignUpCallback;
import com.raimsoft.spacetrader.data.EnumShip;
import com.raimsoft.spacetrader.data.Global;
import com.raimsoft.spacetrader.data.GlobalInput;
import com.raimsoft.spacetrader.data.UserInfo;
import com.raimsoft.spacetrader.util.SPUtil;

public class SpaceTrader extends Activity implements SensorEventListener
{
	private GLView GLView;
	private Game game;
	public GameInfo gInfo;
	
	private ProgressDialog LoadingDL;
	private SensorManager sManager;
	private UserInfo uInfo;
	
	AccountManager mgr;
    Account[] accts;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);      
        
        uInfo= UserInfo.GetInstance();
        
        mgr= AccountManager.get(getApplicationContext());
        accts= mgr.getAccounts();
        
        LoadingDL = new ProgressDialog(this);
        
        Parse.initialize(this, "vA3nQDWd1bB1m9mWqktDWcOU1VYV1JPU7JtEczLd", "cOJJqTw8dc69kGpwtR5PpYhle1ECx3SnxNOudcWJ");
        ParseAnalytics.trackAppOpened(getIntent());
        
        
        getWindow().addFlags( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON );
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setVolumeControlStream( AudioManager.STREAM_MUSIC );
        
        sManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        
        gInfo = new GameInfo( 480, 800 );
        gInfo.ScreenXsize = super.getWindowManager().getDefaultDisplay().getWidth();
        gInfo.ScreenYsize = super.getWindowManager().getDefaultDisplay().getHeight();
        gInfo.SetScale();
        
        game = new Game( this, gInfo );
        GLView = new GLView( this, game );
        GLView.setRenderer( new SurfaceClass(game) );
        
        setContentView( GLView );
        
       
    }
    
    
    public Handler LoadingHandler = new Handler()
	{
//		OnMeepleInteraction callback= new OnMeepleInteraction()
//		{	
//			@Override
//			public void OnRespound(boolean bAccept)	// 상대방을 수락하거나 거절하면 일로 넘어온다.
//			{
//				//LoadingMeeplesInfo();				
//			}
//		};
		
		public void handleMessage(Message msg)
		{			
			if(msg.what==0)	// Play 버튼 누르면!
			{
				LoadingDL.hide();
				
				final String strID= SPUtil.getString(getApplicationContext(), Global.SP_LOGIN_ID);
				final String strPW= SPUtil.getString(getApplicationContext(), Global.SP_LOGIN_PW); 
				if( (strID!=null) && (strPW!=null) )	// 저장된 ID가 있으면 로그인한다.
				{
					LoadingHandler.sendEmptyMessage(1);
					            		
            		SignIn(strID, strPW);
					
				}
				else
				{
					showDialog(R.layout.login_dialog);
				}
			}
			if(msg.what==1)
			{
				LoadingDL.setMessage("로그인 중...");
		        LoadingDL.show();
			}
			if(msg.what==2)
			{
				LoadingDL.setMessage("회원가입 중...");
		        LoadingDL.show();
			}
			if(msg.what==3)
			{
				LoadingDL.setMessage("정보를 가져오는 중...");
		        LoadingDL.show();
			}
			if(msg.what==999)
			{
				LoadingDL.hide();
			}
		}
	};
    
        
    
    @Override
	public void onBackPressed()
	{
    	game.onBackPressed();
	}

	@Override
	public void onSensorChanged( SensorEvent event )
	{
		synchronized (this)
		{
			switch ( event.sensor.getType() )
			{
				case	Sensor.TYPE_ACCELEROMETER:
						GlobalInput.fSensorX = event.values[0];
						break;
			}
		}
	}
    
    
    @Override
    protected void onDestroy()
    {
    	//Baas.io().uninit(this);	// baas.io 종료
        super.onDestroy();
    };

	@Override
	public void onResume()
	{
		super.onResume();
		
		sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
		sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
	}
	
    @Override
    protected void onPause()
    {
    	super.onPause();
    }
    
	@Override
	public void onStop()
	{
		super.onStop();
		
		sManager.unregisterListener(this, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
		sManager.unregisterListener(this, sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION));
	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}  
	
	
	private void ShowAlertDialog(String strTitle, String strContent, String strButton)
	{
		new AlertDialog.Builder(this)
		.setTitle( strTitle )
		.setMessage( strContent )
		.setPositiveButton( strButton , null)
		.setCancelable(false)
		.create()
		.show();
	}
	
	
	
	@Override
	protected Dialog onCreateDialog(int id, Bundle args)
	{
		LayoutInflater factory = LayoutInflater.from(this);
	    final View LoginDialogView = factory.inflate(id, (ViewGroup) findViewById(R.layout.login_dialog));
	    
	    final EditText EDT_ID= (EditText) LoginDialogView.findViewById(R.id.edt_username);
    	final EditText EDT_PW= (EditText) LoginDialogView.findViewById(R.id.edt_password);
    	
    	for(Account acct : accts)	// 가져온 계정 돌면서 이메일형식이 있으면 기본입력해줌
    	{
	    	if(acct.name.contains("@"))
	    	{
	    		EDT_ID.setText(acct.name);
	    		break;
	    	}
    	}
    	
	    return new AlertDialog.Builder(SpaceTrader.this)
	        .setIcon(android.R.drawable.ic_dialog_info)
	        .setTitle("로그인 또는 회원가입")
	        .setView(LoginDialogView)
	        .setPositiveButton("로그인", new DialogInterface.OnClickListener()
	        {
	            public void onClick(DialogInterface dialog, int whichButton)
	            {
	            	
	            	
	            	if( TextUtils.isEmpty(EDT_ID.getText()) )
	            	{
	            		ShowAlertDialog("로그인 실패", "아이디를 입력해주세요", "확인");
	            		showDialog(R.layout.login_dialog);
	            		//Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_LONG).show();
	            	}
	            	else if( TextUtils.isEmpty(EDT_PW.getText()) )	            		
	            	{
	            		ShowAlertDialog("로그인 실패", "패스워드를 입력해주세요", "확인");
	            		showDialog(R.layout.login_dialog);
	            		//Toast.makeText(getApplicationContext(), "패스워드를 입력해주세요.", Toast.LENGTH_LONG).show();
	            	}
	            	else
	            	{	            	
	            		LoadingHandler.sendEmptyMessage(1);
	            		
	            		SignIn(EDT_ID.getText().toString(), EDT_PW.getText().toString());
	            		
	            	}
	            	
	            	
	            }
	        })
	        .setNeutralButton("회원가입", new DialogInterface.OnClickListener()
	        {
	            public void onClick(DialogInterface dialog, int whichButton)
	            {
	            	if( TextUtils.isEmpty(EDT_ID.getText()) )
	            	{
	            		ShowAlertDialog("회원가입 실패", "아이디를 입력해주세요", "확인");
	            	}
	            	else if( !EDT_ID.getText().toString().contains("@") )
	            	{
	            		ShowAlertDialog("회원가입 실패", "아이디를 이메일 형식으로 입력해주세요 :)", "확인");
	            	}
	            	else if( TextUtils.isEmpty(EDT_PW.getText()) )	            		
	            	{
	            		ShowAlertDialog("회원가입 실패", "패스워드를 입력해주세요", "확인");
	            		//Toast.makeText(getApplicationContext(), "패스워드를 입력해주세요.", Toast.LENGTH_LONG).show();
	            	}
	            	else if( (4>EDT_PW.getText().length()) || (10<EDT_PW.getText().length()) )	            		
	            	{
	            		ShowAlertDialog("회원가입 실패", "패스워드는 4~10자리 입니다.", "확인");
	            		
	            	}
	            	else
	            	{
	            		LoadingHandler.sendEmptyMessage(2);
	            		
		        		String strNumber= null;
		        		String strIMEI= null;
		        		
		        		TelephonyManager TM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE); 
		        		if (TM.getSimState() == TelephonyManager.SIM_STATE_ABSENT)
		        		{
		        			strNumber= "usim_absent";
		        		} 
		        		else {
		        			strNumber= TM.getLine1Number();
		        			strIMEI= TM.getDeviceId();
		        		}
		        		
		        		ParseUser user = new ParseUser();
		        		user.setUsername(EDT_ID.getText().toString());
		        		user.setPassword(EDT_PW.getText().toString()	);
		        		user.setEmail(EDT_ID.getText().toString());
		        		 

		        		if(strNumber==null)
		        			user.put("phone", "usim_null");
		        		else
		        			user.put("phone", strNumber);
		        		 
		        		user.signUpInBackground(new SignUpCallback()
		        		{
		        		  public void done(ParseException e)
		        		  {
		        		    if (e == null)
		        		    {
		        		    	LoadingHandler.sendEmptyMessage(999);
		        		    	ShowAlertDialog("[회원가입 성공]", "Welcome abroad!\n다시 로그인해주세요 +_+", "확인");
    		                    return;
		        		    } else {
		        		    	LoadingHandler.sendEmptyMessage(999);
    		                	ShowAlertDialog("[회원가입 실패]", "실패코드 : "+e.getCode(), "확인");
    		                    return;
		        		    }
		        		  }
		        		});
		        		
		        		
		        		 
	            	}
	            }
	        })
	        .setNegativeButton("취소", new DialogInterface.OnClickListener()
	        {
	            public void onClick(DialogInterface dialog, int whichButton)
	            {

	                /* User clicked cancel so do some stuff */
	            }
	        })
	        .create();
	}

	private void SignIn(final String strID, final String strPW)
	{
		ParseUser.logInInBackground(strID, strPW
				,new LogInCallback()
		{
		  public void done(ParseUser user, ParseException e)
		  {
		    if (user != null)
		    {
		    	LoadingHandler.sendEmptyMessage(3);
		    	
		    	SPUtil.putString(getApplicationContext(), Global.SP_LOGIN_ID, strID);
				SPUtil.putString(getApplicationContext(), Global.SP_LOGIN_PW, strPW);    	        			
				
				ParseQuery<ParseObject> query= ParseQuery.getQuery("UserInfo");	// 유저 데이터를 찾는다.
				query.whereEqualTo("user_id", user);
				query.findInBackground(new FindCallback<ParseObject>()
						{			
							@Override
							public void done(List<ParseObject> list, ParseException e)
							{
								for(ParseObject PO : list)
								{									
									uInfo.SetLogin(true);
									uInfo.SetGold( PO.getInt( Global.PO_MOENY ) );
									uInfo.SetShipType( PO.getInt(Global.PO_SHIP_TYPE) );
									uInfo.SetShipHull( PO.getInt(Global.PO_SHIP_HULL) );
		    	        			uInfo.SetWorldMapX( PO.getInt(Global.PO_CROOD_WORLD_X));
		    	        			uInfo.SetWorldMapY( PO.getInt(Global.PO_CROOD_WORLD_Y));
		    	        			uInfo.SetSystemMapPlanet( PO.getInt(Global.PO_CROOD_SYSTEM_MAP_PLANET));
								}
							}
						});
				
				if(!uInfo.GetLogin())	// 해당하는 정보가 없으면... 모두 NULL 처리하고 겜시작시 튜토리얼 시작함.
				{
					uInfo.SetLogin(true);	// 로그인을 설정하면 게임 넘어감
					uInfo.SetGold(-1);
					uInfo.SetShipType(EnumShip.E_NULL_INFO);
					uInfo.SetWorldMapX(-1);
					uInfo.SetWorldMapY(-1);
					uInfo.SetSystemMapPlanet(-1);
				}
				
				LoadingHandler.sendEmptyMessage(999);
				
				
				SPUtil.putString(getApplicationContext(), Global.SP_LOGIN_ID, strID);
		    	SPUtil.putString(getApplicationContext(), Global.SP_LOGIN_PW, strPW);
				
		    	//SPUtil.putBoolean(getApplicationContext(), Global.SP_LOGIN_SUCCESS, true);	
		    	
				ShowAlertDialog("[로그인 성공]", "우주무역 시스템...\n장사꾼 "+strID+"의 정보를 가져왔습니다.\n게임 시작해주세요!", "확인");        	        			
				
		    } else {
		    	LoadingHandler.sendEmptyMessage(999);
		    	ShowAlertDialog("[로그인 실패]", "잘못된 아이디 또는 패스워드입니다.", "확인");
		    	
		    	SPUtil.putString(getApplicationContext(), Global.SP_LOGIN_ID, null);
		    	SPUtil.putString(getApplicationContext(), Global.SP_LOGIN_PW, null);
		    	// 로그인 실패하면 저장된 정보를 지움.
		    	
		        return;
		    }
		  }
		});
	}
}
