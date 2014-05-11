package com.example.vipcallblocker;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import com.vipcallblocker.R;


public class MenuActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        final Button blockButton = (Button) findViewById (R.id.button1);

        blockButton.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
        		Intent callListActivity = new Intent(MenuActivity.this, ListActivity.class);
        		MenuActivity.this.startActivity(callListActivity);				
			}
		});
        
        final Button unblockButton = (Button) findViewById (R.id.contactsButton);
        
        unblockButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
        		Intent callExitActivity = new Intent(MenuActivity.this, ExitActivity.class);
        		MenuActivity.this.startActivity(callExitActivity);
			}
		});
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    
}

