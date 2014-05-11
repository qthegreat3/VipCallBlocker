package com.example.vipcallblocker;

import android.app.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.IBinder;
import android.widget.Toast;

import java.util.ArrayList;
import android.media.AudioManager;

public class BlockService extends Service {

	private BroadcastReceiver phoneCallReceiver;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate(){
		// TODO Put Ringer To Silent
		
	    AudioManager audioManager= (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
	    audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);	    
	}

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		//Create Broadcast Receiver
		IntentFilter filter = new IntentFilter();
		filter.addAction(TELEPHONY_SERVICE);
    	filter.addAction("android.intent.action.PHONE_STATE");
    	filter.addCategory("android.intent.category.DEFAULT");
    	phoneCallReceiver = new PhoneCallReceiver(intent);	
    	registerReceiver(phoneCallReceiver, filter);
    	    	
		return START_NOT_STICKY;
	}
	
	@Override
	public void onDestroy(){
		unregisterReceiver(phoneCallReceiver);
	}

}
