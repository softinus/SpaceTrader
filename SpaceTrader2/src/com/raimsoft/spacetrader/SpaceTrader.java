package com.raimsoft.spacetrader;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import bayaba.engine.lib.GameInfo;

import com.kth.baasio.Baas;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.callback.BaasioSignInCallback;
import com.kth.baasio.callback.BaasioSignUpCallback;
import com.kth.baasio.entity.entity.BaasioEntity;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioException;
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
        
        Baas.io().init(this,
        		"https://api.baas.io",
        		"june",
        		"spacetrader");
        
        
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
			if(msg.what==0)
			{
				LoadingDL.hide();
				
				final String strID= SPUtil.getString(getApplicationContext(), Global.SP_LOGIN_ID);
				final String strPW= SPUtil.getString(getApplicationContext(), Global.SP_LOGIN_PW); 
				if( (strID!=null) && (strPW!=null) )	// 저장된 ID가 있으면 로그인한다.
				{
					LoadingHandler.sendEmptyMessage(1);
					
					BaasioUser.signInInBackground(
							getApplicationContext()
					        , strID
					        , strPW
					        , new BaasioSignInCallback() {

					            @Override
					            public void onException(BaasioException e) {
					                if (e.getStatusCode() != null)
					                {
					                    if (e.getErrorCode() == 201)
					                    {
					                        return;
					                    }
					                }
					                //기타 오류
					            }

					            @Override
					            public void onResponse(BaasioUser response)
					            {
					            	if(response==null)
					            	{
		            	        		ShowAlertDialog("[로그인]", "로그인 실패", "확인");
		            	        		SPUtil.putString(getApplicationContext(), Global.SP_LOGIN_ID, null);
		            	        		SPUtil.putString(getApplicationContext(), Global.SP_LOGIN_PW, null);
					            	}
		            	        	else
		            	        	{
		            	        		LoadingHandler.sendEmptyMessage(999);
		            	        		
		            	        		if(!TextUtils.isEmpty(response.getEmail()))	            	        			
		            	        		{
		            	        			ShowAlertDialog("[로그인 성공]", "우주무역 시스템...\n장사꾼 "+strID+"의 정보를 가져왔습니다.\n게임 시작해주세요!", "확인");
		            	        			uInfo.SetLogin(true);
		            	        			uInfo.SetGold(0);
    	            	        			uInfo.SetShipType(EnumShip.E_TRAINING_SHIP_2);
    	            	        			uInfo.SetWorldMapX(138);
    	            	        			uInfo.SetWorldMapY(287);
    	            	        			uInfo.SetSystemMapPlanet(1);
		            	        			//SPUtil.putBoolean(getApplicationContext(), "login", true);
		            	        		}
		            	        		else
		            	        			ShowAlertDialog("[로그인 실패]", response.toString(), "확인");
		            	        	}
					            }
					        });
					
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
    	Baas.io().uninit(this);	// baas.io 종료
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
	            		
	            		
	            		BaasioUser.signInInBackground(
	            				getApplicationContext()
	            		        , EDT_ID.getText().toString()
	            		        , EDT_PW.getText().toString()
	            		        , new BaasioSignInCallback() {

	            		            @Override
	            		            public void onException(BaasioException e)
	            		            {
	            		                if (e.getStatusCode() != null)
	            		                {
	            		                	if (e.getErrorCode() == 201)
     		        		                {
     		        		                	LoadingHandler.sendEmptyMessage(999);
     		        		                	ShowAlertDialog("[로그인 실패]", "잘못된 아이디 또는 패스워드입니다.", "확인");
     		        		                    return;
     		        		                }
     		        		                else
     		        		                {
     		        		                	LoadingHandler.sendEmptyMessage(999);
     		        		                	ShowAlertDialog("[로그인 실패]", "실패코드 : "+e.getErrorCode(), "확인");
     		        		                    return;
     		        		                }
	            		                }

	            		                //기타 오류
	            		            }

	            		            @Override
	            		            public void onResponse(BaasioUser response)
	            		            {
	            		            	if(response==null)
	    	            	        		ShowAlertDialog("[로그인]", "로그인 실패", "확인");
	    	            	        	else
	    	            	        	{
	            	        		LoadingHandler.sendEmptyMessage(3);
    	            	        		
	    	            	        		BaasioEntity entity = new BaasioEntity("ship");
	    	            	        		entity.setUuid(response.getUuid());                               // Entity의 uuid
	    	            	        		entity.getInBackground(new BaasioCallback<BaasioEntity>()
	    	            	        		{

	    	                                    @Override
	    	                                    public void onResponse(BaasioEntity response)
	    	                                    {
	    	                                        if (response != null)
	    	                                        {
	    	                                        	LoadingHandler.sendEmptyMessage(999);
   				        		                		ShowAlertDialog("[정보]", "정보 : "+response.toString(), "확인");
	    	                                        }
	    	                                    }

	    	                                    @Override
	    	                                    public void onException(BaasioException e)
	    	                                    {
	    	                                        Log.e("baas.io", e.toString());
	    	                                        LoadingHandler.sendEmptyMessage(999);
			        		                		ShowAlertDialog("[정보기입 실패]", "실패코드 : "+e.getErrorCode(), "확인");
	    	                                    }
	    	                                });
//	    	            	        		
	    	            	        		
		        			        		BaasioEntity entity2 = new BaasioEntity("ship");
		        			        		entity2.setProperty("username", EDT_ID.getText().toString());
		        			        		entity2.setProperty("GOLD", 300);
		        			        		entity2.setProperty("SHIP_TYPE", 0);
		        			        		entity2.setProperty("SHIP_CROOD", "0-0:0");
		        			        		entity2.saveInBackground(
		        			        		    new BaasioCallback<BaasioEntity>() {

		        			        		            @Override
		        			        		            public void onException(BaasioException e)
		        			        		            {
		        			        		            	LoadingHandler.sendEmptyMessage(999);
	   				        		                		ShowAlertDialog("[정보기입 실패]", "실패코드 : "+e.getErrorCode(), "확인");
		        			        		            }

		        			        		            @Override
		        			        		            public void onResponse(BaasioEntity response) {
		        			        		                if (response != null)
		        			        		                {
		        			        		                	ShowAlertDialog("[정보]", "정보 : "+response.toString(), "확인");
		        			        		                }
		        			        		            }
		        			        		        });

	    	            	        			SPUtil.putString(getApplicationContext(), Global.SP_LOGIN_ID, EDT_ID.getText().toString());
	    	            	        			SPUtil.putString(getApplicationContext(), Global.SP_LOGIN_PW, EDT_PW.getText().toString());
	    	            	        			
	            	        			LoadingHandler.sendEmptyMessage(999);
	    	            	        			
	    	            	        			uInfo.SetLogin(true);
	    	            	        			uInfo.SetGold(0);
	    	            	        			uInfo.SetShipType(EnumShip.E_TRAINING_SHIP_2);
	    	            	        			uInfo.SetWorldMapX(125);
	    	            	        			uInfo.SetWorldMapY(235);
	    	            	        			uInfo.SetSystemMapPlanet(1);
	    	            	        			//SPUtil.putBoolean(getApplicationContext(), "login", true);
	    	            	        			
	    	            	        			ShowAlertDialog("[로그인 성공]", "우주무역 시스템...\n장사꾼 "+EDT_ID.getText().toString()+"의 정보를 가져왔습니다.\n게임 시작해주세요!", "확인");
	    	            	        	}
	            		            }
	            		        });
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
	            		//Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_LONG).show();
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
		        			strNumber= "USIM정보가 없음";
		        		} 
		        		else {
		        			strNumber= TM.getLine1Number();
		        			strIMEI= TM.getDeviceId();
		        		}
		        		
		        		
		        		BaasioUser.signUpInBackground(
		        				EDT_ID.getText().toString()	          	//ID(username)
		        		        , strNumber          					//이름
		        		        , EDT_ID.getText().toString()			//이메일
		        		        , EDT_PW.getText().toString()       	//비밀번호
		        		        , new BaasioSignUpCallback()
		        				{

		        		            @Override
		        		            public void onException(BaasioException e)
		        		            {
		        		                if (e.getErrorCode() == 913)
		        		                {
		        		                	LoadingHandler.sendEmptyMessage(999);
		        		                	ShowAlertDialog("[회원가입 실패]", "이미 가입된 사용자입니다.", "확인");
		        		                    return;
		        		                }
		        		                else
		        		                {
		        		                	LoadingHandler.sendEmptyMessage(999);
		        		                	ShowAlertDialog("[회원가입 실패]", "실패코드 : "+e.getErrorCode(), "확인");
		        		                    return;
		        		                }
		        		            }

		        		            @Override
		        		            public void onResponse(BaasioUser response)
		        		            {
		        		            	LoadingHandler.sendEmptyMessage(999);
		        		                if (response != null)
		        		                {	        		                	
		        			        		
		        			        		ShowAlertDialog("[회원가입 성공]", EDT_ID.getText().toString()+"님 우주무역에 합류!\n다시 한번 로그인해주세요~", "확인");
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
}
