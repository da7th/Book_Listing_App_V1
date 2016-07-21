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
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    //the string that will hold the search query
    private String searchQuery;

    //the editText view where the search query will be entered
    private EditText searchQueryBox;

    //the fixed url
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchQuery = "";
        searchQueryBox = (EditText) findViewById(R.id.search_query_box);
        url = "https://www.googleapis.com/books/v1/volumes?q=";
    }


    //this method is used to check if that application is connected to the internet
    public boolean isOnline() {

        //initiate an instance of connectivity manager that accesses the connectivity services, permissions
        //must be added in the manifest file
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //create an instance of network Info based on the initiated connectivity manager
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //return true if there is firstly network info and secondly the network info is connected
        return (networkInfo != null && networkInfo.isConnected());

    }


    //this method is to  append the text in the edit text field to the String Variable searchQuery
    public void submitSearch(View view) {

        //append the entered text to the String variable
        searchQuery = searchQueryBox.getText().toString();
        //replace the spaces in the text with (+) signs
        searchQuery = searchQuery.replace(" ", "+");

        //check if the search query contains any text
        if (searchQuery.length() > 0) {
            //if the search query contains text then check if the application is connected and online
            if (isOnline()) {
                //if the application is connected perform the search

                //full search URL
                String searchUrl = url + searchQuery + "+intitle:" + searchQuery + "&debug.key=AIzaSyCdeM9NxPI07KoBSP9pp9UPJHH78vsqolo";

                //getBookInfo(searchUrl)
                new getBookInfo().execute(searchUrl);

                Toast testToast = Toast.makeText(this, "get book info executed and finished", Toast.LENGTH_LONG);
                testToast.show();

            } else {
                //there is no connectivity message to be displayed
                Toast disconnectionToast = Toast.makeText(this, "Error: There is no network connectivity.", Toast.LENGTH_SHORT);
                disconnectionToast.show();
            }
        }
    }

    public void stringToJArray(String bookBuilder) {

        final ArrayList<Book> bookList = new ArrayList<Book>();

        try {
            JSONObject jObject = new JSONObject(bookBuilder);
            JSONArray jArray = jObject.getJSONArray("items");

            for (int i = 0; i < jArray.length(); i++) {

                JSONObject item = jArray.getJSONObject(i);

                JSONObject volumeInfo = item.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");


                JSONArray authors = volumeInfo.getJSONArray("authors");
                String author = authors.getString(0);


                bookList.add(new Book(title, author));

                Log.v("DEBUGGING", "a book was added to the list");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        BookAdapter adapter = new BookAdapter(this, bookList);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

    }

    //this method will be used to get the book information
    public class getBookInfo extends AsyncTask<String, Void, String> {

        private String userString;

        //the following method will use receiver the url and return the search result as string
        @Override
        protected String doInBackground(String... BookURLs) {

            StringBuilder bookBuilder = new StringBuilder();

            //this loop receives the search urls if we had multiple search
            for (String bookSearchURL : BookURLs) {


                //for any IO operation there is a chance for error so we use try and catch
                try {

                    //this is the http client that will request data
                    URL url = new URL(bookSearchURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    final String DEBUG_TAG = "ATTENTION";
                    Log.e(DEBUG_TAG, "the response is 200" + conn.getResponseCode());


                    if (conn.getResponseCode() == 200) {

                        //once the entity is acquried, read the content into a buffered reader for processing
                        InputStream bookContent = conn.getInputStream();
                        InputStreamReader bookInput = new InputStreamReader(bookContent);
                        BufferedReader bookReader = new BufferedReader(bookInput);

                        //single string line for the loop
                        String lineIn;

                        //append the bookreader content to the bookbuilder one line at a time
                        while ((lineIn = bookReader.readLine()) != null) {
                            bookBuilder.append(lineIn);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                userString = bookBuilder.toString();



            }

            return bookBuilder.toString();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            stringToJArray(userString);
        }
    }

}
