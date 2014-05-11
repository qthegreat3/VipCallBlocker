package com.example.vipcallblocker;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class PhoneCallReceiver extends BroadcastReceiver {

	private Intent _intent;
	
	PhoneCallReceiver(Intent intent){
		super();
		_intent = intent;
	}
	
	@Override
	public void onReceive(final Context context, Intent arg1) {
		// TODO Auto-generated method stub
		
		final ArrayList<String> phoneList = _intent.getStringArrayListExtra("PhoneList");
		
		TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		
		telephony.listen(new PhoneStateListener(){
			@Override
			public void onCallStateChanged(int state, String incomingNumber){
				AudioManager audioManager= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

				if (state == TelephonyManager.CALL_STATE_RINGING){
					
									
					if (incomingNumber.length() > 12)
					{
						incomingNumber = incomingNumber.substring(incomingNumber.length() - 11);
					}
					
										
					if (phoneList.contains(incomingNumber))
					{
						audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);						
					}
					else
					{					
						audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
					}
				}
			}
		}, PhoneStateListener.LISTEN_CALL_STATE);
	}

}
