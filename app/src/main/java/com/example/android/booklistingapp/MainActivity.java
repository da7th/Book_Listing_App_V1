package com.example.android.booklistingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText editTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void searchBooks(View view){
        editTextView = (EditText) findViewById(R.id.edit_text_view);
        String example = editTextView.getText().toString();

        TextView tv = (TextView) findViewById(R.id.page_title);
        tv.setText(example);
    }
}
