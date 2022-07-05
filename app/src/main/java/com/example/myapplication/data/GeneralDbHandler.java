package com.example.myapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class GeneralDbHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION =1;
    public static final String DATABASE_NAME = "animals.db";
    public final String RABBIT_TABLE ="rabbits";
    public final String COLUMN_ID ="_id";
    public final String RABBIT_TAG_COLUMN ="_tag";
    public final String SEX_COLUMN ="_gender";
    public final String DOB_COLUMN ="_date_of_birth";
    public final String AGE_COLUMN ="_age";
    public final String COLOUR_COLUMN = "_colour";
    public final String BREED_COLUMN ="_breed";
    public final String SOURCE_COLUMN ="_source";

    public GeneralDbHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
