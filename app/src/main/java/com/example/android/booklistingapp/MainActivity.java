package com.example.android.booklistingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText editTextView;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this is to check if the wifi or mobile data services are connected
        final String DEBUG_TAG = "NetworkStatusExample";
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfo.isConnected();
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfo.isConnected();
        Log.d(DEBUG_TAG, "Wifi connected: " + isWifiConn);
        Log.d(DEBUG_TAG, "Mobile connected: " + isMobileConn);

        tv = (TextView) findViewById(R.id.page_title);
        tv.setText("Wifi Connected:" + isWifiConn + "\nMobile connected:" + isMobileConn);

    }


    public void searchBooks(View view){
        editTextView = (EditText) findViewById(R.id.edit_text_view);
        String example = editTextView.getText().toString();

        tv = (TextView) findViewById(R.id.page_title);
        tv.setText(example);
    }
}
