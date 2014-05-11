package com.example.vipcallblocker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;


import java.util.ArrayList;

import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.View;
import android.widget.*;
import com.vipcallblocker.R;

public class ListActivity extends Activity {
	
	private static ArrayList<String> vipPhoneList = new ArrayList<String>();;
	private static ArrayAdapter<String> phoneListAdapter;

    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	//vipPhoneList = new ArrayList<String>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        
        final EditText phoneNumberField = (EditText) findViewById(R.id.editText1);
        final ListView phoneNumberListView = (ListView) findViewById(R.id.phoneListView);
        phoneListAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, vipPhoneList);
        phoneNumberListView.setAdapter(phoneListAdapter);
        phoneNumberListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
                
        phoneNumberListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> adapterView, View clickedView, int position, long id){
        		vipPhoneList.remove(position);
        		phoneListAdapter.notifyDataSetChanged();
        	}
        	
        });
        
        final Button addNumberButton = (Button) findViewById(R.id.button1);
        addNumberButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				String phoneNumber = phoneNumberField.getText().toString();
				if (((phoneNumber.length() == 10) || (phoneNumber.length() == 11)) && (!vipPhoneList.contains(phoneNumber)) && (phoneNumber != "")){
					// TODO If Number doesnt start with 1, add a 1 to phone number
					if (phoneNumber.charAt(0) != '1')
					{
						phoneNumber = '1' + phoneNumber;
					}
					
					vipPhoneList.add(phoneNumber);
					phoneListAdapter.notifyDataSetChanged();
				}
				else
				{
					if ((phoneNumber.length() < 10) || (phoneNumber.length() > 11))
					{
						Toast.makeText(getBaseContext(), "Phone Number Must Have At Least 10 Numbers", Toast.LENGTH_SHORT).show();
					}
				}
				
				phoneNumberField.setText("");
			}
		});
        
        final Button contactsButton = (Button) findViewById(R.id.contactsButton);
        contactsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
	            Intent i = new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
	            startActivityForResult(i,1);
			}
		});
        
        final Intent intent = new Intent(ListActivity.this, BlockService.class);
        
        final int mId = 1;
        
		final NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(getBaseContext())
		        .setSmallIcon(R.drawable.ic_launcher_viplogo)
		        .setContentTitle("VIP Call Blocker")
		        .setContentText("Blocking Calls!");
		// Creates an explicit intent for an Activity in your app
		final Intent resultIntent = new Intent(getBaseContext(), ListActivity.class);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(getBaseContext());
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(ListActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		
        final Button blockButton = (Button) findViewById(R.id.button3);
        blockButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				// TODO Store Numbers
				//Kick Off Service
				//Intent intent = new Intent(ListActivity.this, BlockService.class);
				intent.putStringArrayListExtra("PhoneList", vipPhoneList);
				startService(intent);
				
				// TODO Kill activity to go back to home screen
				Intent toHomeIntent = new Intent(Intent.ACTION_MAIN);
				toHomeIntent.addCategory(Intent.CATEGORY_HOME);
				startActivity(toHomeIntent);
				
				// mId allows you to update the notification later on.
				NotificationManager mNotificationManager =
					    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);				
				mNotificationManager.notify(mId, mBuilder.build());
			}
		});
        
        final Button stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//Intent intent = new Intent(ListActivity.this, BlockService.class);
			    AudioManager audioManager= (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
			    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				
			    stopService(intent);
				
				Intent endAppIntent = new Intent(Intent.ACTION_MAIN);
				endAppIntent.addCategory(Intent.CATEGORY_HOME);
				endAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(endAppIntent);
				
				NotificationManager mNotificationManager =
					    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				mNotificationManager.cancel(mId);
				
				//finish();
			    
			    System.exit(1);
			}
		});
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
    	if (resultCode == RESULT_OK){
    		getContactData(data);
    	}
    }
    
    private void getContactData(Intent data){
        ContentResolver cr = getContentResolver();

        Uri contactData = data.getData();
        Cursor c = cr.query(contactData, null, null, null, null);

        if(c.moveToFirst()){
             String  id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
           
             String  name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
           

            if (Integer.parseInt(c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) 
            {
                    Cursor pCur = cr.query(Phone.CONTENT_URI,null,Phone.CONTACT_ID +" = ?", new String[]{id}, null);

              // TODO Get All Numbers
              int numRows = pCur.getCount();
              for(int index =0; index < numRows; index++){
            	  if (pCur.moveToPosition(index)) //Got phone Number
                  {
            		String phone = pCur.getString(pCur.getColumnIndex(Phone.NUMBER));
            		
            		phone = removePartsOfString(phone);
            		
            		if (phone.charAt(0) != '1')
            		{
            			phone = '1' + phone;
            		}
       			 	            		
                	 vipPhoneList.add(phone);
                	 phoneListAdapter.notifyDataSetChanged();            		  
                  }
              }

            }
        }
    }
    
    private String removePartsOfString(String subject)
    {
    	String result = "";
    	
    	for (int stringIndex = 0; stringIndex < subject.length(); stringIndex++)
    	{
    		if ( (subject.charAt(stringIndex) >= 48) &&(subject.charAt(stringIndex) <= 57 ))
    		{
    			result = result + subject.charAt(stringIndex);
    		}
    	}
    	
    	return result;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }	
}
