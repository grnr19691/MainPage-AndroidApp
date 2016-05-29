package com.example.venkatneehar.mainpage;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Sends the Phone number and the registration ID to your server over HTTP where we store the info in a database*/

public class SendMessage extends AsyncTask<String, String, Integer>{

    ProgressDialog pd = null;
    private final String TAG = getClass().getSimpleName();

    private String regid;
    private String phoneNumber;
    private Context mContext;
    private String message;
    private String sPhone;
    public void setContext(Context ctx) {
        mContext = ctx;
    }

    protected void onPreExecute()
    {

    }

    @Override
    protected Integer doInBackground(String... params) {

        String hostName = "students.engr.scu.edu/~smaram/Srinu/gcm.php?push=1";
        phoneNumber=params[0];
        message=params[1];
        sPhone=params[2];

        Log.d(TAG, "check the param here http:"+phoneNumber);
        Log.d(TAG, "check the param here http:"+message);

        Integer result = 1;

        Log.d(TAG, "Sending message in background: "+regid);
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + hostName);

            // Request parameters and other properties.
            List<NameValuePair> postParams = new ArrayList<NameValuePair>(3);
            postParams.add(new BasicNameValuePair("phoneNum", phoneNumber+"\n"));
            postParams.add(new BasicNameValuePair("message", message+"\n"));
            postParams.add(new BasicNameValuePair("sPhone", sPhone+"\n"));
            httppost.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));

            //Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            Log.d(TAG, "POST request return code: " + String.valueOf(response.getStatusLine().getStatusCode()));

            if(response.getStatusLine().getStatusCode()==200 || response.getStatusLine().getStatusCode()== 405){
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    InputStream instream; //entity.getContent();
                    try {
                        String content =  EntityUtils.toString(entity);
                        Log.d(TAG, "Response: " + content);

                        // do something useful
                        // check return
                    } finally {
                        //instream.close();
                    }
                }
                result = 1;
            }
        } catch (IOException e) {
            Log.d(TAG, "error: " + e.toString());
            result = 0;
        }
        return result;
    }


    @Override
    protected void onPostExecute(Integer result)
    {
        //pd.dismiss();

        if (result == 1)
            Toast.makeText(mContext, "message sent successfully", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(mContext, "message send error"  , Toast.LENGTH_LONG).show();

    }
}