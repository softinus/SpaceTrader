package com.raimsoft.spacetrader;

import org.usergrid.android.client.callbacks.ApiResponseCallback;
import org.usergrid.java.client.response.ApiResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import bayaba.engine.lib.GameInfo;

import com.kth.baasio.Baasio;
import com.kth.baasio.auth.AuthUtils;

public class SpaceTrader extends Activity implements SensorEventListener
{
	private GLView GLView;
	private Game game;
	public GameInfo gInfo;
	
	private SensorManager sManager;
	private boolean bJoinFailed= true;
	private String strUserID="test4";
	
	
	
	
	
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);      
        
        Baasio.getInstance().init(this,
        		"https://api.baas.io",
        		"june",
        		"spacetrader");
        
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

        
        AuthUtils.signup(
        	    this,                        // context
        	    strUserID,                     // username  (애플리케이션 내의 유일한 값)
        	    strNumber,              // full name
        	    "test@test.com",             // e-mail    (애플리케이션 내의 유일한 값)
        	    "testtest",                     // password
        	    new ApiResponseCallback() {
        	        @Override
        	        public void onException(Exception e) { }            // Exception 발생

        	        @Override
        	        public void onResponse(ApiResponse response)
        	        {
        	        	if(response==null)
        	        		ShowAlertDialog("[회원가입]", "회원가입 실패", "확인");
        	        	else
        	        	{
        	        		bJoinFailed= false;
        	        		ShowAlertDialog("[회원가입]", response.toString(), "확인");
        	        	}
        	        }    // 결과
        	    });
        
//        if(!bJoinFailed)
//        {
//			String strNumber= null;
//			String strIMEI= null;
//			
//			TelephonyManager TM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE); 
//			if (TM.getSimState() == TelephonyManager.SIM_STATE_ABSENT)
//			{
//				strNumber= "USIM정보가 없음";
//			} 
//			else {
//				strNumber= TM.getLine1Number();
//				strIMEI= TM.getDeviceId();
//			}
//	        
//	        Entity entity = new Entity("samples");         // 저장할 컬랙션 명을 사용하여 Entity 생성
//	        entity.setProperty("userID", strUserID);    // Entity 항목 지정 ( Propery name , Value )
//	        entity.setProperty("phoneNum", strNumber);    // Entity 항목 지정 ( Propery name , Value )
//	        //entity.setProperty("phoneNum", strIMEI);    // Entity 항목 지정 ( Propery name , Value )
//	
//	        Baasio.getInstance().createEntityAsync(entity, new ApiResponseCallback()
//	        {
//	                @Override
//	                public void onException(Exception e) { }            // Exception 발생
//	
//	                @Override
//	                public void onResponse(ApiResponse response) { }    // 결과
//	        });     // Entity 생성 요청
//        }
//        AuthUtils.login(
//        	    this,                    // context
//        	    "raimsoft",                 // username
//        	    "testtest",                 // password
//        	    new ApiResponseCallback()
//        	    {
//        	        @Override
//        	        public void onException(Exception e) { }            // Exception 발생
//
//        	        @Override
//        	        public void onResponse(ApiResponse response)
//        	        {
//        	        	if(response==null)
//        	        		ShowAlertDialog("[로그인]", "로그인 실패", "확인");
//        	        	else
//        	        		ShowAlertDialog("[로그인 성공]", response.toString(), "확인");
//        	        }    // 결과
//        	    });

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
				onCreateDialog(0, null);
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
	    final View textEntryView = factory.inflate(R.layout.login_dialog, null);
	    return new AlertDialog.Builder(SpaceTrader.this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("로그인")
	        .setView(textEntryView)
	        .setPositiveButton("확인", new DialogInterface.OnClickListener()
	        {
	            public void onClick(DialogInterface dialog, int whichButton)
	            {

	                /* User clicked OK so do some stuff */
	            }
	        })
	        .setNegativeButton("닫기", new DialogInterface.OnClickListener()
	        {
	            public void onClick(DialogInterface dialog, int whichButton)
	            {

	                /* User clicked cancel so do some stuff */
	            }
	        })
	        .create();
	}
}