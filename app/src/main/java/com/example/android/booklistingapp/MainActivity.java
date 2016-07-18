package com.example.android.booklistingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editTextView;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    //this is the method executed with the button press
    public void searchBooks(View view){
        editTextView = (EditText) findViewById(R.id.edit_text_view);
        String apiURL = "http://www.google.com";

        //check if the app is online or not
        if (isOnline()) {
            String searchTerm = editTextView.getText().toString();
            tv = (TextView) findViewById(R.id.page_title);
            tv.setText(searchTerm);

            new connectToAPITask().execute(apiURL);


        } else {
            tv.setText("No network Connection Available");
        }

    }

    //this is to check whether the app is connected online or not
    //this is to check if the wifi or mobile data services are connected and prints to the log

    public boolean isOnline() {
        final String DEBUG_TAG = "NetworkStatusExample";

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        NetworkInfo networkInfoWifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfoWifi.isConnected();

        NetworkInfo networkInfoMobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfoMobile.isConnected();

        Log.d(DEBUG_TAG, "Wifi connected: " + isWifiConn);
        Log.d(DEBUG_TAG, "Mobile connected: " + isMobileConn);

        return (networkInfo != null && networkInfo.isConnected());

    }

    private String bookListingTask(String apiurl) throws IOException {
        final String DEBUG_TAG = "NetworkStatusExample";
        InputStream is = null;
        int count = 10;

        try {
            URL url = new URL(apiurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, count);
            Log.e(DEBUG_TAG, "The response is: " + contentAsString);
            return contentAsString;

        } finally {
            if (is != null) {
                is.close();
            }

        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    private class connectToAPITask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... apiURL) {
            try {
                return bookListingTask(apiURL[0]);
            } catch (IOException e) {
                return "Unable to retrieve Booklisting.";
            }
        }
    }


}
