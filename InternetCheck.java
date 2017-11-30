//REPLACE THIS LINE WITH YOUR PACKAGE NAME
package com.example.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jameskrawczyk on 12/05/16.
 */
public class InternetCheck extends AsyncTask<String, Void, Void> {

    //We need the app context to give some network things context
    Context appContext;

    //We need the app calling activity to end it and create context
    Activity callingActivity;

    //Acitivty to go to after check
    String gotoName;

    //Store result of internet check.
    Boolean internetLive = true;

    //Log ID
    String TAG = "InternetCheck";

    //This will be set depending on whether we are checking on the main activity or on error activty;
    //TRUE means do something if internet is broken, FALSE means do something if internet is working.
    //Normally you would call this activity with True and in the error activity have a recurring task 
    //call it with false to check when the internet comes back online. In the error activity you should pass
    //the activity as you main activity so when the internet comes back online it will send the user back to
    //the main activity.
    boolean isError;

    //Constructor
    public InternetCheck (Activity passedActivity, String whereToGo, boolean onError)
    {
        this.callingActivity = passedActivity;
        this.appContext = callingActivity.getApplicationContext();
        this.gotoName = whereToGo;
        this.isError = onError;
    }

    //Do the actual check.
    @Override
    protected Void doInBackground(String... params)
    {
      
        if(isNetworkAvailable())
        {
            //Check internet by checking a google site. You can subsitute the
            //URL for a site of your choosing but Google is a good choice since
            //they have very high uptime.
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                internetLive = (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0);

            }
            catch (IOException e)
            {
                //Something bad happened, will assume no internet
                Log.e(TAG, "Error checking internet connection", e);
                internetLive = false;
            }

        }
        else
        {
            internetLive = false;
        }

        return null;
    }

    //Check for network existence, e.g. WiFi, Ethernet. This does not mean actual
    //internet access.
    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;

    }

    //Do whatever we want after check. In this case kill activity that created it and start a error
    //activity.
    @Override
    protected void onPostExecute(Void aVoid)
    {
        if(isError)
        {
            if(!internetLive)
            {
                try
                {

                    Intent i = new Intent(appContext, Class.forName(gotoName));
                    callingActivity.startActivity(i);
                    callingActivity.finish();
                }
                catch(ClassNotFoundException e)
                {
                    //If this error is generated it means you don't have a valid
                    //activity for the passed activity.
                    Log.e(TAG, "Go to class couldn't be found.", e);
                }
            }
        }
        else
        {
            if(internetLive)
            {
                try
                {

                    Intent i = new Intent(appContext, Class.forName(gotoName));
                    callingActivity.startActivity(i);
                    callingActivity.finish();
                }
                catch(ClassNotFoundException e)
                {
                    //If this error is generated it means you don't have a valid
                    //activity for the passed activity.
                    Log.e(TAG, "Go to class couldn't be found.", e);
                }
            }
        }

    }


}
