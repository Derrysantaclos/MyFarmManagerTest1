package com.example.myapplication.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.myapplication.models.Rabbit;


public class DbHandler extends SQLiteOpenHelper
    {
        public static final int DATABASE_VERSION =1;
        public static final String DATABASE_NAME = "animals.db";
        public static final String RABBIT_TABLE ="rabbits";
        public static final String COLUMN_ID ="_id";
        public static final String RABBIT_TAG_COLUMN ="_tag";
        public static final String SEX_COLUMN ="_gender";
        public static final String DOB_COLUMN ="_date_of_birth";
        public static final String AGE_COLUMN ="_age";
        public static final String COLOUR_COLUMN = "_colour";
        public static final String BREED_COLUMN ="_breed";
        public static final String SOURCE_COLUMN ="_source";


        public DbHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version)
        {
            super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            String rabbitTableQuery = "CREATE TABLE "+ RABBIT_TABLE + " ("+
                    COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RABBIT_TAG_COLUMN +" TEXT, "+
                    BREED_COLUMN + " TEXT, "+
                    COLOUR_COLUMN + " TEXT, "+
                    SEX_COLUMN + " TEXT, " +
                    DOB_COLUMN + " TEXT, " +
                    SOURCE_COLUMN + " TEXT, " +
                    AGE_COLUMN + " TEXT);";

            db.execSQL(rabbitTableQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            //String deleteQuery ="DROP TABLE IF EXISTS "+ TABLE_ANIMALS;
            String deleteRabbitTableQuery = "DROP TABLE IF EXISTS "+ RABBIT_TABLE;
            // db.execSQL(deleteQuery);
            db.execSQL(deleteRabbitTableQuery);
            onCreate(db);
        }

        //CREATE
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void addRabbit(Rabbit newRabbit)
        {
            ContentValues values = new ContentValues();
            values.put(RABBIT_TAG_COLUMN, newRabbit.get_tag());
            values.put(SEX_COLUMN, newRabbit.get_sex());
            values.put(BREED_COLUMN,newRabbit.get_breed());
            values.put(DOB_COLUMN, newRabbit.get_dateOfBirth().toString());
            //values.put(AGE_COLUMN, newRabbit.calculate_age());
            values.put(SOURCE_COLUMN, newRabbit.get_source());
            values.put(COLOUR_COLUMN, newRabbit.get_colour());
            SQLiteDatabase db = getWritableDatabase();
            db.insert(RABBIT_TABLE,null,values);
        }

        //DELETE



        //RETRIEVE::Rabbit List
        public List<String> rabbitList(){
            List<String> rabbitList = new ArrayList<>();
            SQLiteDatabase db = getWritableDatabase();
            String rabbitSelectionQuery = "SELECT * FROM " +RABBIT_TABLE;
            Cursor c =db.rawQuery(rabbitSelectionQuery,null);
            c.moveToFirst();
            while(!c.isAfterLast())
            {
                if(c.getString(c.getColumnIndexOrThrow(RABBIT_TAG_COLUMN))!= null)
                //todo check if the if statement is neccessary
                {
                    String currentTag = c.getString(c.getColumnIndexOrThrow(RABBIT_TAG_COLUMN));
                    rabbitList.add(currentTag);

                }
                c.moveToNext();
            }
            c.close();
            db.close();
            return rabbitList;
        }

        //RETRIEVE ALL RABBITS
        public ArrayList<Rabbit> rabbitArrayList()
        {
            ArrayList<Rabbit> rabbitArrayList = new ArrayList<>();
            SQLiteDatabase db = getWritableDatabase();
            String rabbitSelectionQuery = "SELECT * FROM " +RABBIT_TABLE;
            Cursor c =db.rawQuery(rabbitSelectionQuery,null);
            c.moveToFirst();
            while(!c.isAfterLast())
            {
                if(c.getString(c.getColumnIndexOrThrow(RABBIT_TAG_COLUMN))!= null)
                //todo check if the if statement is neccessary
                {

                    String currentRabbitTag = c.getString(c.getColumnIndexOrThrow(RABBIT_TAG_COLUMN));
                    String dateOfBirthString=c.getString(c.getColumnIndexOrThrow(DOB_COLUMN));
                    String sex =c.getString(c.getColumnIndexOrThrow(SEX_COLUMN));
                    DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate dateOfBirth= LocalDate.parse(dateOfBirthString,formatter);
                    String breed =c.getString(c.getColumnIndexOrThrow(BREED_COLUMN));
                    String source =c.getString(c.getColumnIndexOrThrow(SOURCE_COLUMN));
                    String colour =c.getString(c.getColumnIndexOrThrow(COLOUR_COLUMN));

                    //String age =c.getString(c.getColumnIndexOrThrow(AGE_COLUMN));
                    Integer id = c.getInt(c.getColumnIndexOrThrow(COLUMN_ID));

                    //MAKE THE RABBIT
                    Rabbit newRabbit = new Rabbit(id,sex,dateOfBirth,currentRabbitTag,breed,source,colour);
                    rabbitArrayList.add(newRabbit);



                }
                c.moveToNext();
            }
            c.close();
            db.close();


            return rabbitArrayList;
        }
        //UPDATE
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void updateRabbit(Rabbit selectedRabbit)
        {
            ContentValues values = new ContentValues();
            values.put(RABBIT_TAG_COLUMN, selectedRabbit.get_tag());
            values.put(SEX_COLUMN, selectedRabbit.get_sex());
            values.put(BREED_COLUMN,selectedRabbit.get_breed());
            values.put(SOURCE_COLUMN,selectedRabbit.get_source());
            values.put(DOB_COLUMN, selectedRabbit.get_dateOfBirth().toString());
            //values.put(AGE_COLUMN, selectedRabbit.calculate_age());
            values.put(COLOUR_COLUMN, selectedRabbit.get_colour());
            SQLiteDatabase db = getWritableDatabase();
            db.update(RABBIT_TABLE,values,COLUMN_ID+ "=?",new String[]{String.valueOf(selectedRabbit.get_id())});
        }
        //rabbit count
        public  int rabbitCount()
        {
            String countQuery ="SELECT * FROM "+ RABBIT_TABLE;
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery(countQuery,null);
            return c.getCount();
        }

        //DELETE
        public void deleteRabbit(int id)
        {
            SQLiteDatabase db = getWritableDatabase();
            String deleteRabbitQuery = "DELETE FROM "+RABBIT_TABLE+
                    " WHERE " + COLUMN_ID+ " =\""+id+"\";";
            db.execSQL(deleteRabbitQuery);
        }


    }



