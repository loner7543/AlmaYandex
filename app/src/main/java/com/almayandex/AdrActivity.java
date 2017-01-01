package com.almayandex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdrActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText searchText;
    private Intent intent;
    private Button SearchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adr);
        searchText = (EditText) findViewById(R.id.search_text);
        SearchBtn = (Button) findViewById(R.id.searchBtn);
        SearchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String addr = searchText.getText().toString();
        if (addr.length()==0){
            Toast toast = Toast.makeText(this,"Введитие адрес",Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            intent = new Intent();
            intent.putExtra("addr",addr);
            setResult(RESULT_OK,intent);
            finish();
        }
    }
}
