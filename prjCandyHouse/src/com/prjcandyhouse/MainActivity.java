package com.prjcandyhouse;

import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
	// inner variables
	private static final String PROJECT_ID = "701006689919";
	private static final String TAG = "MainActivity";
	private String regId = "";
	
	private String registrationStatus = "Not yet registered";
	private String broadcastMessage = "No broadcast message";
	
	IntentFilter gcmFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //
        initXMLComponents();
        
        //
        Intent intent = new Intent(MainActivity.this, NotifyService.class);
        this.startService(intent);
        
        //
        gcmFilter = new IntentFilter();
        gcmFilter.addAction("GCM_RECEIVED_ACTION");
        
        registerClient();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    OnClickListener clickListenerSubmitRegId = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private void registerClient(){
		try{
			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);
			regId = GCMRegistrar.getRegistrationId(this);
			
			if(regId.equals("")){
				registrationStatus = "Registering...";
				
				GCMRegistrar.register(this, PROJECT_ID);
				regId = GCMRegistrar.getRegistrationId(this);
				registrationStatus = "Registration Acquired";
			}
			else{
				registrationStatus = "Already Registered!";
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
			registrationStatus = ex.getMessage();
		}
		
		tvRegStatusResult.setText(registrationStatus);
		Log.d(TAG, regId);
	}
	
	private void sendPrivateIdToServer(final String strId){
		
	}
	
    private void sendRegistrationtoServer(){
    	
    }
    
    
    
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	/**/
	private TextView tvRegStatusResult;
	private TextView tvBroadcastMessage;
	private Button btnSubmitRegId;
	
	private void initXMLComponents(){
		tvRegStatusResult = (TextView) findViewById(R.id.tvRegStatusResult);
		tvBroadcastMessage = (TextView) findViewById(R.id.tvMessage);
		btnSubmitRegId = (Button) findViewById(R.id.btnSubmitRegId);
		btnSubmitRegId.setOnClickListener(clickListenerSubmitRegId);
	}
}
