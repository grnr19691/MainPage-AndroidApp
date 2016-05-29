package com.example.venkatneehar.mainpage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import de.greenrobot.event.EventBus;


public class Register extends ActionBarActivity {

    static final String TAG = "GCMDemo";
    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PROPERTY_PHONE_ID = "phone_id";
    public static String SENDER_ID = "748551893309";
    String regId="";
    GoogleCloudMessaging gcm;
    Button registerGCM;
    TextView Key;
    Context context;
    EditText phone;  // phone number given by the user
    EditText recPhone;
    EditText sendMsg;
    Button SendMessage;
    private BroadcastReceiver mMessageReceiver;
    private EventBus bus = EventBus.getDefault();
    private DataBaseHandler mydb ;


    private EditText number;
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        number=(EditText)findViewById(R.id.numberText);
        register = (Button) findViewById(R.id.registerBtn);
        number.setText("");

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();
    }

    public void getGCMId(View v){
        if(number.getText().toString().equals("")) return;
        storePhoneId(context, number.getText().toString());
        gcm = GoogleCloudMessaging.getInstance(this);
        regId = getRegistrationId(context);

        Log.i(TAG, "key id:" + regId);
        if (regId.isEmpty()) {
            Log.i(TAG, "Registration not found");
            registerInBackground();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcm.register(SENDER_ID);
                    msg = "" + regId;

                    sendRegistrationIdToServer();

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            private void sendRegistrationIdToServer() {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String phonePlusKey=number.getText().toString();
                        phonePlusKey+="::";
                        phonePlusKey+=regId;
                        Log.i(TAG, "eroo1111rrr:"+phonePlusKey);
                        // phonePlusKey+=regId;
                        myHttp h1 = new myHttp();
                        h1.setContext( context );
                        //   h1.execute(regId);
                        h1.execute( phonePlusKey );

                    }
                });

            }

            @Override
            protected void onPostExecute(final String msg) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      //  Key.append(msg + "\n");
                        Log.i(TAG, "Updating key from Inside!Yay!");

                        finish();
                    }
                });
            }
        }.execute(null, null, null);
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.commit();
    }

    private void storePhoneId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_PHONE_ID, regId);
        editor.commit();
    }



    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        return "";
        //  return registrationId;
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
