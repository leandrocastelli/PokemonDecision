package com.lcsmobileapps.pokemon.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;



public class MediaService extends Service implements SoundPlayer{

	public static final String LOG = "Pokemon";
	public class LocalBinder extends Binder
	{
		public SoundPlayer getSoundPlayer() {
			return MediaService.this;
		}
	}
	private MediaPlayer player;
	public final IBinder connection = new LocalBinder();
	
	@Override
	public IBinder onBind(Intent arg0) {
		
		return connection;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		//player = MediaPlayer.create(this,R.raw.green);
		
		
		
	}
	public void startPlaying(int soundID)
	{
		if(player!= null)
		{
			if(player.isPlaying())
			{
				player.stop();
				player.reset();
				
			}
		}
		player = MediaPlayer.create(this,soundID);
		player.setOnCompletionListener(new OnCompletionListener() {
			
			public void onCompletion(MediaPlayer mp) {
				stopSelf();
				
			}
		});
	    player.start();
	  
	}
	
	@Override
	public void onDestroy() {

		super.onDestroy();
		if(player!=null) {
			player.stop();
			player.release();
		}
	}
	
	public int onStartCommand(Intent intent, int flags, int startId)  {
		super.onStartCommand(intent, flags, startId);
	//	startPlaying();
		
		return 0;
	}
}
