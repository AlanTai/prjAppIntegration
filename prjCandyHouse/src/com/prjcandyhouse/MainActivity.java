package com.prjcandyhouse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	// temp XMPP task
	private class ConnectToXmpp extends AsyncTask<Void, Void, Void> {
	    @Override
	    protected Void doInBackground(Void... params) {
	          ConnectionConfiguration config = new ConnectionConfiguration( "localhost", 0); // server address and port
	          XMPPTCPConnection m_connection = new XMPPTCPConnection(config);
	    try {
	         SASLAuthentication.supportSASLMechanism("PLAIN");
	         config.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);     
	         m_connection.connect();
	        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
	    } catch (Exception e) {
	        e.printStackTrace();
	    } 

	        return null;
	    }

	    @Override
	    protected void onPostExecute(Void result) {

	    }

	}
	
	// inner variables of Bluetooth
	private static final int REQUEST_ENABLE_BT = 0x1;
	private BluetoothAdapter myBluetoothAdapter;
	private Set<BluetoothDevice> pairedDevices;
	private ArrayAdapter<String> BTArrayAdapter;
	
	private void initInnerVariables(){
		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(myBluetoothAdapter == null){
			btnOn.setEnabled(false);
			btnOff.setEnabled(false);
			btnList.setEnabled(false);
			btnFind.setEnabled(false);
			txtStatus.setText("Status: not supported!");
			
			Toast.makeText(getApplicationContext(), "Your device does not support Bluetooth", Toast.LENGTH_LONG).show();
		}
		else{
			initBluetoothComponents();
			BTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
			myListView.setAdapter(BTArrayAdapter);
		}
	}
	
	OnClickListener clickListenerBluetoothOn = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			on(v);
		}
	};
	
	OnClickListener clickListenerBluetoothOff = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			off(v);
		}
	};
	
	OnClickListener clickListenerDevicesList = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			list(v);
		}
	};
	
	OnClickListener clickListenerFind = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			find(v);
		}
	};
	
	private void on(View arg_view){
		if(!myBluetoothAdapter.isEnabled()){
			Intent turnOIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(turnOIntent, REQUEST_ENABLE_BT);
			Toast.makeText(getApplicationContext(), "Bluetooth is ready for turning on", Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(getApplicationContext(), "Bluetooth is already on", Toast.LENGTH_LONG).show();
		}
	}
	
	private void off(View arg_view){
		myBluetoothAdapter.disable();
		txtStatus.setText("Status: Disconnected");
		
		Toast.makeText(getApplicationContext(), "Bluetooth turned off", Toast.LENGTH_LONG).show();
	}
	
	private void find(View arg_view){
		if(myBluetoothAdapter.isDiscovering()){
			myBluetoothAdapter.cancelDiscovery();
		}else{
			BTArrayAdapter.clear();
			myBluetoothAdapter.startDiscovery();
			registerReceiver( bReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
		}
	}
	
	private void list(View arg_view){
		pairedDevices = myBluetoothAdapter.getBondedDevices();
		
		//
		for (BluetoothDevice device : pairedDevices){
			BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
		}
	}
	
	private final BroadcastReceiver bReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			
			//
			if(BluetoothDevice.ACTION_FOUND.equals(action)){
				//bluetooth device
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				
				// bluetooth array adapter
				BTArrayAdapter.add(device.getName() + ";" + device.getAddress());
				BTArrayAdapter.notifyDataSetChanged();
			}
		}
	};
	
	// inner variables of GCM
	private static final String PROJECT_ID = "701006689919"; // google app engine project ID
	private static final String TAG = "MainActivity";
	private String regId = "";
	
	private String registrationStatus = "Not yet registered";
	private String broadcastMessage = "No broadcast message";
	
	IntentFilter gcmFilter;
	private BroadcastReceiver gcmReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			broadcastMessage = intent.getExtras().getString("gcm");
			
			if(broadcastMessage != null){
				tvBroadcastMessage.setText(broadcastMessage);
			}
		}
	};

	
	// override methods
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        

        // view initialization
        initGCMXMLComponents();
        
        /* intent from notification */
        Intent latestIntent = this.getIntent();
        if (latestIntent != null){
            Bundle extras = latestIntent.getExtras();
        	if (extras != null && extras.containsKey("NotificationMessage")){
        		String msg = extras.getString("NotificationMessage");
    			tvBroadcastMessage.setText(msg);
        	}
        }
        
        // bluetooth
        initInnerVariables();
        
        // notification service
        Intent intent = new Intent(MainActivity.this, NotifyService.class);
        this.startService(intent);
        
        // gcm intent filter
        gcmFilter = new IntentFilter();
        gcmFilter.addAction("GCM_RECEIVED_ACTION");
        
        // register user's device
        registerClient();
    }

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
    	if(requestCode == REQUEST_ENABLE_BT){
    		if(myBluetoothAdapter.isEnabled()){
    			txtStatus.setText("Status: Enable");
    		}else{
    			txtStatus.setText("Status: Disable");
    		}
    	}
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
    
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
    	registerReceiver(gcmReceiver, gcmFilter);
		super.onResume();
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		unregisterReceiver(gcmReceiver);
		super.onPause();
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//bluetooth
		myBluetoothAdapter.disable();
		
		//GCM
		GCMRegistrar.onDestroy(this);
		super.onDestroy();
	}


	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		broadcastMessage = savedInstanceState.getString("BroadcastMessage");
		tvBroadcastMessage.setText(broadcastMessage);
	}


	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putString("BroadcastMessage", broadcastMessage);
	}

    // click listener
    OnClickListener clickListenerSubmitRegId = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(regId.equals("")){
				Toast.makeText(getApplicationContext(), "No RegId", Toast.LENGTH_LONG).show();
			}
			else{
				sendPrivateIdToServer(regId);
			}
		}
	};
	
	// register client's device; if registered, show "Already Registered!"
	private void registerClient(){
		try{
			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);
			regId = GCMRegistrar.getRegistrationId(this);
			
			if(regId.equals("")){
				registrationStatus = "Registering...";
				
				GCMRegistrar.register(this, PROJECT_ID);
				// regId = GCMRegistrar.getRegistrationId(this);
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
	
	//  send private id back to server
	private void sendPrivateIdToServer(final String strId){
		new AsyncTask<Void, String, String>(){

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String strResponseCode = "";
				InputStream inputStream = null;
				try{
					// http client
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://gogistics.gogistics-tw.com/gcm_reg_id_handler");
					
					// list pair
					List<NameValuePair> nameValuesPairs = new ArrayList<NameValuePair>();
					nameValuesPairs.add(new BasicNameValuePair("userName", "alantai"));
					nameValuesPairs.add(new BasicNameValuePair("registrationId", strId));
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuesPairs));
					
					// http response
					HttpResponse response = httpClient.execute(httpPost);
					inputStream = response.getEntity().getContent();
					
					// buffer reader
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					StringBuilder sb = new StringBuilder();
					
					String line =null;
					while((line = reader.readLine()) != null){
						sb.append(line + "\n");
					}
					strResponseCode = sb.toString();
					inputStream.close();
				}
				catch(Exception ex){
					ex.printStackTrace();
				}
				return strResponseCode;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
				super.onPostExecute(result);
			}
			
		}.execute(null, null, null);
	}
	
    
	/* XML Components */
	
	//Bluetooth
	private Button btnOn;
	private Button btnOff;
	private Button btnList;
	private Button btnFind;
	private TextView txtStatus;
	private ListView myListView;
	// initialization of bluetooth components
	private void initBluetoothComponents(){

		//Bluetooth
		btnOn = (Button) findViewById(R.id.btnTurnOn);
		btnOn.setOnClickListener(clickListenerBluetoothOn);
		btnOff = (Button) findViewById(R.id.btnTurnOff);
		btnOff.setOnClickListener(clickListenerBluetoothOff);
		btnList = (Button) findViewById(R.id.btnPaired);
		btnList.setOnClickListener(clickListenerDevicesList);
		btnFind = (Button) findViewById(R.id.btnSearchCancel);
		btnFind.setOnClickListener(clickListenerFind);
		
		txtStatus = (TextView) findViewById(R.id.txtBluetoothStatus);
		myListView= (ListView) findViewById(R.id.listView);
	}
	
	
	// GCM
	private TextView tvRegStatusResult;
	private TextView tvBroadcastMessage;
	private Button btnSubmitRegId;
	// initialization of GCM components
	private void initGCMXMLComponents(){
		// GCM
		tvRegStatusResult = (TextView) findViewById(R.id.tvRegStatusResult);
		tvBroadcastMessage = (TextView) findViewById(R.id.tvMessage);
		btnSubmitRegId = (Button) findViewById(R.id.btnSubmitRegId);
		btnSubmitRegId.setOnClickListener(clickListenerSubmitRegId);
	}
}
