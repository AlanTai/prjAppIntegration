package com.prjcandyhouse;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

public class NotifyService extends Service{
	//inner variables
	final static String ACTION="";
	final static String STOP_SERVICE="";
	final static int RQS_STOP_SERVICE=0x1;
	final static int RQS_SEND_NOTIFICATION_1=0x2;
	final static int RQS_SEND_NOTIFICATION_2=0x3;
	
	private static final int MY_NOTIFICATION_ID_1=0x1;
	private static final int MY_NOTIFICATION_ID_2=0x2;
	private static final int MY_GCM_NOTIFICATION = 0x5;
	private NotificationManager notificationManager=null;
	private Notification myNotification=null;
	
	Context myContext;
	String myNotificationTitle="Notification Sample";
	NotifyServiceReceiver notifyServeiceReceiver;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		notifyServeiceReceiver = new NotifyServiceReceiver();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.unregisterReceiver(notifyServeiceReceiver);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		// intent
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION);
		intentFilter.addAction("GCM_RECEIVED_ACTION");
		this.registerReceiver(notifyServeiceReceiver, intentFilter);
		
		// send notification
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		myNotification = new Notification(R.drawable.ic_launcher, "Notification is coming...", System.currentTimeMillis());
		//myNotification=new Notification.Builder(myContext).setContentTitle("Notification from GAE").setContentText("Notification is coming...").setSmallIcon(R.drawable.ic_launcher).setTicker("Ticker").build();
		
		myContext = getApplicationContext();
		myNotification.defaults |= Notification.DEFAULT_SOUND;
		myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		return super.onStartCommand(intent, flags, startId);
	}

	// inner class
	public class NotifyServiceReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int rqs = intent.getIntExtra("RQS", 0);
			if(rqs == RQS_STOP_SERVICE){
				stopSelf();
			}
			else if(rqs == RQS_SEND_NOTIFICATION_1){
				String myTarget = intent.getStringExtra("TARGET");
				sendNotification(MY_NOTIFICATION_ID_1, myTarget);
			}
			else if(rqs == RQS_SEND_NOTIFICATION_2){
				String myTarget = intent.getStringExtra("TARGET");
				sendNotification(MY_NOTIFICATION_ID_2, myTarget);
			}
			
			// get broadcast message
			String broadcastMessage = intent.getExtras().getString("gcm");
			if(broadcastMessage != null){
				sendNotification(MY_GCM_NOTIFICATION, broadcastMessage);
			}
		}
		
	}
	
	//
	private void sendNotification(int id, String strGCMTxt){
		String notificationTxt = strGCMTxt;
		//Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myTarget));
		Intent myIntent = new Intent(myContext, MainActivity.class);

		// open activity from notification and pass message to activity
		myIntent.putExtra("NotificationMessage", notificationTxt);
		myIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// pending intent
		PendingIntent pendingIntent = PendingIntent.getActivity(myContext, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		myNotification.setLatestEventInfo(myContext, myNotificationTitle, notificationTxt, pendingIntent);
		//myNotification=new Notification.Builder(myContext).setContentTitle(myNotificationTitle).setContentText(notificatioTxt).setContentIntent(pendingIntent).build();
		notificationManager.notify(id, myNotification);
	}

}
