package com.example.ex04;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {
    Intent intent;
    AddressHelper helper;
    SQLiteDatabase db;
    Cursor cursor;
    EditText name, phone, juso;
    ImageView photo;
    String strPhoto = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        getSupportActionBar().setTitle("정보수정");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        juso = findViewById(R.id.juso);
        photo = findViewById(R.id.photo);

        helper = new AddressHelper(this);
        db = helper.getWritableDatabase();

        intent = getIntent();
        int id = intent.getIntExtra("id", 0);

        String sql = "select * from address where _id=" + id;
        cursor = db.rawQuery(sql, null);
        if(cursor.moveToNext()){
            name.setText(cursor.getString(1));
            phone.setText(cursor.getString(2));
            juso.setText(cursor.getString(3));
            strPhoto = cursor.getString(4);
            if(strPhoto==""){
                photo.setImageResource(R.drawable.baseline_tag_faces_24);
            }else {
                photo.setImageBitmap(BitmapFactory.decodeFile(strPhoto));
            }
        }
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });
        findViewById(R.id.btnUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UpdateActivity.this)
                        .setTitle("질의")
                        .setMessage("수정하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strName = name.getText().toString();
                                String strPhone = phone.getText().toString();
                                String strJuso = juso.getText().toString();
                                String sql = "update address set ";
                                sql += "name= '" + strName + "',";
                                sql += "phone= '" + strPhone + "',";
                                sql += "juso= '" + strJuso + "', ";
                                sql += "photo= '" + strPhoto + "'";
                                sql += " where _id=" + id;
                                db.execSQL(sql);
                                Toast.makeText(UpdateActivity.this, "수정완료", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();
            }
        });
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
        cursor.moveToFirst();
        strPhoto = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        //System.out.println("......" + strPhoto);
        photo.setImageBitmap(BitmapFactory.decodeFile(strPhoto));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}