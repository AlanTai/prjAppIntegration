package com.prjcandyhouse;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService{
	// final variables
	private final static String PROJECT_ID = "701006689919";
	private final static String TAG = "GCMIntentService";

	public GCMIntentService(){
		super(PROJECT_ID);
		Log.d(TAG, "Init GCMIntentService...");
	}
	
	@Override
	protected void onError(Context ctx, String str_error) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Error: " + str_error);
	}

	@Override
	protected void onMessage(Context ctx, Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Message Received");
		String message = intent.getStringExtra("message");
		sendGCMIntent(ctx, message);
	}

	@Override
	protected void onRegistered(Context ctx, String regId) {
		// TODO Auto-generated method stub
		Log.d(TAG, regId);
	}

	@Override
	protected void onUnregistered(Context ctx, String regId) {
		// TODO Auto-generated method stub
		Log.d(TAG, regId);
	}
	
	private void sendGCMIntent(Context ctx, String message){
		Intent broadcsatIntent = new Intent();
		broadcsatIntent.setAction("GCM_RECEIVED_ACTION");
		broadcsatIntent.putExtra("gcm", message);
		
		// send broadcast
		ctx.sendBroadcast(broadcsatIntent);
	}

}
