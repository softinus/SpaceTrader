package com.raimsoft.spacetrader;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import bayaba.engine.lib.GameInfo;

public class SpaceTrader extends Activity implements SensorEventListener
{
	private GLView GLView;
	private GameMain gMain;
	public GameInfo gInfo;
	
	private SensorManager sManager;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        

        super.onCreate(savedInstanceState);
        getWindow().addFlags( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON );
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setVolumeControlStream( AudioManager.STREAM_MUSIC );
        
        sManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        
        gInfo = new GameInfo( 480, 800 );
        gInfo.ScreenXsize = super.getWindowManager().getDefaultDisplay().getWidth();
        gInfo.ScreenYsize = super.getWindowManager().getDefaultDisplay().getHeight();
        gInfo.SetScale();
        
        gMain = new GameMain( this, gInfo );
        GLView = new GLView( this, gMain );
        GLView.setRenderer( new SurfaceClass(gMain) );
        
        setContentView( GLView );
    }
    
    
    @Override
	public void onBackPressed()
	{
    	System.exit(0);
	}

	@Override
	public void onSensorChanged( SensorEvent event )
	{
		synchronized (this)
		{
			switch ( event.sensor.getType() )
			{
				case	Sensor.TYPE_ACCELEROMETER:
						gMain.fSensorX = event.values[0];
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
}