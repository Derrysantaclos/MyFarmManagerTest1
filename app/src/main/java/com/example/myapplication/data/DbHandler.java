package com.example.myapplication.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.os.Build;

import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import com.example.myapplication.models.Image;
import com.example.myapplication.models.Pregnancy;
import com.example.myapplication.models.Rabbit;


public class DbHandler extends SQLiteOpenHelper
    {
        public static final int DATABASE_VERSION =5;
        public static final String DATABASE_NAME = "animals.db";


        //classes
        private final PregnancyDbHelper pregnancyDbHelper =new PregnancyDbHelper();
        private final RabbitDbHelper rabbitDbHelper =new RabbitDbHelper();
        private final ImageDbHelper imageDbHelper= new ImageDbHelper();



        public DbHandler(@Nullable Context context, @Nullable SQLiteDatabase.CursorFactory factory)
        {
            super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            pregnancyDbHelper.createPregnancyTable(db);
            rabbitDbHelper.createRabbitTable(db);
            imageDbHelper.createImageTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
           pregnancyDbHelper.dropPregnancyTable(db);
           rabbitDbHelper.dropRabbitTable(db);
           imageDbHelper.dropImageTable(db);
           onCreate(db);
        }

        //CREATE

        //create rabbit
        //@RequiresApi(api = Build.VERSION_CODES.O)
        public void addRabbit(Rabbit newRabbit)
        {
           SQLiteDatabase db=getWritableDatabase();
           rabbitDbHelper.addRabbit(newRabbit,db);
           db.close();
        }

        //create pregnancy
        public void addPregnancy(Pregnancy newPregnancy){
            SQLiteDatabase writableDb = getWritableDatabase();
            SQLiteDatabase readableDb =getReadableDatabase();
            pregnancyDbHelper.addPregnancy(newPregnancy,writableDb,readableDb);
            readableDb.close();
            writableDb.close();
        }

        //create/ADD image
        public void addImage( Image newImage){
            SQLiteDatabase db = getWritableDatabase();
            imageDbHelper.addImage(newImage, db);
            db.close();
        }

        //RETRIEVE::

        // Rabbit tag List
        public List<String> rabbitList(){
            SQLiteDatabase db =getReadableDatabase();
            List<String> rabbitList = rabbitDbHelper.rabbitList(db);
            db.close();
            return rabbitList;
        }
        //RETRIEVE ALL RABBITS
        public ArrayList<Rabbit> rabbitArrayList()
        {
           SQLiteDatabase db =getReadableDatabase();
            ArrayList<Rabbit> rabbitArrayList= rabbitDbHelper.rabbitArrayList(db);
            db.close();
            return rabbitArrayList;
        }

        //pregnancy tag List
        public List<Integer> pregnancyList(){
            SQLiteDatabase db =getReadableDatabase();
            List<Integer> pregnancyList = pregnancyDbHelper.pregnancyList(db);
            db.close();
            return pregnancyList;
        }
        //pregnancy details
        public ArrayList<Pregnancy> pregnancyArrayList(){
            SQLiteDatabase db =getReadableDatabase();
            ArrayList<Pregnancy> pregnancyArrayList= pregnancyDbHelper.pregnancyArrayList(db);
            db.close();
            return pregnancyArrayList;
        }

        //Retrieve all images
        public ArrayList<Image> imageArrayList(){
           SQLiteDatabase db= getReadableDatabase();
           ArrayList<Image> imageArrayList=imageDbHelper.imageArrayList(db);
           db.close();
           return imageArrayList;
        }
        //rabbit count
        public  int rabbitCount() {
            SQLiteDatabase db = getReadableDatabase();
            int rabbitCount= rabbitDbHelper.rabbitCount(db);
            db.close();
            return rabbitCount;
        }

        //::::::::::::::::::::UPDATE:::::::::::::::::::
        public void updateRabbit(Rabbit selectedRabbit)
        {
            SQLiteDatabase db = getWritableDatabase();
            rabbitDbHelper.updateRabbit(selectedRabbit, db);
            db.close();
        }
        //updatePregnancy
        public void updatePregnancy(Pregnancy selectedPregnancy){
            SQLiteDatabase db =getWritableDatabase();
            pregnancyDbHelper.updatePregnancy(selectedPregnancy,db);
            db.close();
        }

        //::::::::::::::::::DELETE::::::::::::::::::::::::
        public void deleteRabbit(int id)
        {
            SQLiteDatabase db = getWritableDatabase();
            rabbitDbHelper.deleteRabbit(id,db);
            db.close();
        }
        public void deletePregnancy(int id){
            SQLiteDatabase db =getWritableDatabase();
            pregnancyDbHelper.deletePregnancy(id,db);
            db.close();
        }


//                                         IMAGES
        //add images

    }



