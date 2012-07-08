package com.raimsoft.spacetrader.util;

import java.util.Collection;
import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;


public class SoundManager
{
	private static final String LOG_TAG = SoundManager.class.getSimpleName();

	private boolean bAlreadyPlaying= false;
	
	private Context mContext;
	private SoundPool mSoundPool;
	private HashMap<Integer, Integer> mSoundPoolMap;
	private AudioManager  mAudioManager;

	public static boolean bSoundOpt= true;

	public SoundManager(Context context)
	{
		mContext = context;
	}

	public void Create()
	{
		mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		mSoundPoolMap = new HashMap<Integer, Integer>();
		mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
	}

	public void Destroy()
	{
		if (mSoundPoolMap != null) {
			Collection<Integer> soundIds = mSoundPoolMap.values();
			for (int soundId : soundIds) {
				mSoundPool.unload(soundId);
				Log.d(LOG_TAG, "destroy sound id " + soundId);
			}
			mSoundPoolMap = null;
		}
	}

	public void Load(int key, int resId)
	{
		Log.d(LOG_TAG, "load...START");

		if (!mSoundPoolMap.containsKey(key)) {
			mSoundPoolMap.put(key, mSoundPool.load(mContext, resId, 1));
		}

		Log.d(LOG_TAG, "load...END");
	}

	public void Play(int key)
	{
		Log.d(LOG_TAG, "play...START");

		int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(
				mSoundPoolMap.get(key),
				streamVolume, streamVolume,
				1, 0, 1f);

		Log.d(LOG_TAG, "play...END");
	}
	
	public void Play(int key, int volumn)
	{
		Log.d(LOG_TAG, "play...START");

		mSoundPool.play(
				mSoundPoolMap.get(key),
				volumn, volumn,
				1, 0, 1f);

		Log.d(LOG_TAG, "play...END");
	}


	public void PlayLoop(int key)
	{
		Log.d(LOG_TAG, "playLoop...START");

		int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

		mSoundPool.play(
				mSoundPoolMap.get(key),
				streamVolume, streamVolume,
				1, -1, 1f);

		Log.d(LOG_TAG, "playLoop...END");
	}
	

	public void Stop(int key)
	{
		mSoundPool.stop(mSoundPoolMap.get(key));
	}

	public void Pause(int key)
	{
		mSoundPool.pause(mSoundPoolMap.get(key));
	}

	public void Resume(int key)
	{
		mSoundPool.resume(mSoundPoolMap.get(key));
	}

}

