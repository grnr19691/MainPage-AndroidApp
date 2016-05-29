package com.example.venkatneehar.mainpage;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.NoSubscriberEvent;

public class GCMNotificationIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    public static final String PROPERTY_PHONE_ID = "phone_id";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    public static final String TAG = "GCMNotificationIntentService";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                sendNotification("Deleted messages on server: "
                        + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {

                //check
                Bundle payload = intent.getExtras();
                Node notification = new Node(payload);

                DataBaseHandler db = new DataBaseHandler(
                        getApplicationContext());
// inserting new label into database

                //db.insertMessage(notification.getsPhone(),getPhoneId(this.getApplicationContext()),notification.getData());
                Log.d("got this",notification.getsPhone()+" "+MainActivity.SENDER_ID+" "+ notification.getData());

                if(notification!=null) {
                    String rec = notification.getsPhone().replaceAll("[^0-9a-zA-Z]+", "");
                    // String sec=notification.getsPhone().replaceAll("[^0-9a-zA-Z]+","");
                    String msg = notification.getData().replaceAll("[^0-9a-zA-Z ]+", "");
                    db.insertMessage(rec, MainActivity.SENDER_ID, msg);
                    //     db.insertMessage(MainActivity.SENDER_ID,notification.getsPhone(),notification.getData());
                }
                EventBus.getDefault().register(this);
                EventBus.getDefault().post(notification);


                //check

                for (int i = 0; i < 3; i++) {
                    Log.i(TAG,
                            "Working... " + (i + 1) + "/5 @ "
                                    + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }

                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                EventBus.getDefault().unregister(this);
/*
                sendNotification("Message Received from Google GCM Server: "
                        + extras.get(Config.MESSAGE_KEY)); */
                 Log.i(TAG, "Received: " + extras.toString());
            }

        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {
        Log.d(TAG, "Preparing to send notification...: " + msg);
        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.ball)
                .setContentTitle("GCM Notification")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        Log.d(TAG, "Notification sent successfully.");
    }

    public void onEvent(NoSubscriberEvent event){
        if (event.originalEvent instanceof Node)  {

            sendNotification(((Node) event.originalEvent).getData());
        }
    }
    private String getPhoneId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String phoneId = prefs.getString(PROPERTY_PHONE_ID, "");
        if (phoneId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        return phoneId;
    }
    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

}