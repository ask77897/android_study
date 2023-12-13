package com.example.ex03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class InsertActivity extends AppCompatActivity {
    EditText name, phone, juso;
    AddressHelper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        getSupportActionBar().setTitle("주소등록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        helper = new AddressHelper(this);
        db = helper.getReadableDatabase();

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        juso = findViewById(R.id.juso);
        findViewById(R.id.btnInsert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strName = name.getText().toString();
                String strPhone = phone.getText().toString();
                String strJuso = juso.getText().toString();
                if(strName.equals("")||strPhone.equals("")||strJuso.equals("")){
                    new AlertDialog.Builder(InsertActivity.this)
                            .setTitle("질의")
                            .setMessage("이름, 전화번호, 주소를 입력하세요")
                            .setPositiveButton("확인", null)
                            .show();
                }else{
                    String sql = "insert into address(name, phone, juso) values(";
                    sql += "'" + strName + "',";
                    sql += "'" + strPhone + "',";
                    sql += "'" + strJuso + "')";
                    db.execSQL(sql);
                    finish();
                }
            }
        });
    }//onCreate

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}