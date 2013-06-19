package com.itsp.btmouse;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Splash extends Activity {
	MediaPlayer ourSong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);
		Thread timer;
		
		ourSong=MediaPlayer.create(this, R.raw.clickmouse);
		ourSong.setLooping(true);		
		ourSong.start();
		
		timer = new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try{
						sleep(1000);
						ourSong.release();
						sleep(4000);
					
				}catch(InterruptedException e){
					e.printStackTrace();
				}finally{
					Intent i = new Intent("com.itsp.btmouse.MAINACTIVITY");
					startActivity(i);
				}
		}
			
		};
		timer.start();
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	
		finish();
	}

	
}
