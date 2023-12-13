package com.example.ex03;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AddressHelper extends SQLiteOpenHelper {
    public AddressHelper(@Nullable Context context) {
        super(context, "address.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table address(_id integer primary key autoincrement, name text, phone text, juso text)");
        db.execSQL("insert into address values(null, '홍길동', '010-1010-1010', '인천')");
        db.execSQL("insert into address values(null, '철수', '010-2020-2020', '서울')");
        db.execSQL("insert into address values(null, '영희', '010-3030-3030', '대전')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
