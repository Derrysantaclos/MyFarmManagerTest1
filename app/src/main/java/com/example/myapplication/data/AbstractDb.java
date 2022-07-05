package com.example.myapplication.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.myapplication.models.Rabbit;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDb extends SQLiteOpenHelper{

        public static final int DATABASE_VERSION =1;
        public static final String DATABASE_NAME = "animals.db";
        //public static final String OBJECT_TABLE;
//        public static final String RABBIT_TABLE ="rabbits";
//        public static final String COLUMN_ID ="_id";
//        public static final String RABBIT_TAG_COLUMN ="_tag";
//        public static final String SEX_COLUMN ="_gender";
//        public static final String DOB_COLUMN ="_date_of_birth";
//        public static final String AGE_COLUMN ="_age";
//        public static final String COLOUR_COLUMN = "_colour";
//        public static final String BREED_COLUMN ="_breed";
//        public static final String SOURCE_COLUMN ="_source";


        public AbstractDb(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory,
                          int version)
        {
            super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        }


        @Override
        public abstract void onCreate(SQLiteDatabase db);


        @Override
        public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);



        //CREATE
        @RequiresApi(api = Build.VERSION_CODES.O)
        public abstract void addModel(Object object);

        //DELETE



        //RETRIEVE::List
        public abstract List<String> ObjectList();


        //RETRIEVE ALL RABBITS
        public abstract ArrayList<Object> objectstArrayList();

        //UPDATE Object
        @RequiresApi(api = Build.VERSION_CODES.O)
        public abstract void updateRabbit(Object selectedObject);


        //rabbit count
        public abstract int ObjectCount();

        public void deleteRabbit(int id,String TABLE,String ID_COLUMN)
        {
            SQLiteDatabase db = getWritableDatabase();
            String deleteRabbitQuery = "DELETE FROM "+TABLE+
                    " WHERE " + ID_COLUMN+ " =\""+id+"\";";
            db.execSQL(deleteRabbitQuery);
        }


    }





