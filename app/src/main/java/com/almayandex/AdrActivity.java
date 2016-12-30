package com.almayandex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AdrActivity extends AppCompatActivity {
    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adr);
        searchText = (EditText) findViewById(R.id.search_text);
    }

    public void onStartSearch(View view){
        String addr = searchText.getText().toString();
    }
}
