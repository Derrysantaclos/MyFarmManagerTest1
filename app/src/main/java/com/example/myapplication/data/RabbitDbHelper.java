package com.example.myapplication.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.models.Rabbit;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RabbitDbHelper {
    public static final String RABBIT_TABLE ="rabbits";
    public static final String COLUMN_ID ="_id";
    public static final String RABBIT_TAG_COLUMN ="_tag";
    public static final String SEX_COLUMN ="_gender";
    public static final String DOB_COLUMN ="_date_of_birth";
    public static final String AGE_COLUMN ="_age";
    public static final String SOURCE_COLUMN ="_source";
    public static final String COLOUR_COLUMN = "_colour";
    public static final String BREED_COLUMN ="_breed";



    public void createRabbitTable(SQLiteDatabase db){
        String createRabbitTableQuery ="CREATE TABLE "+ RABBIT_TABLE + " ("+
                COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RABBIT_TAG_COLUMN +" TEXT, "+
                BREED_COLUMN + " TEXT, "+
                COLOUR_COLUMN + " TEXT, "+
                SEX_COLUMN + " TEXT, " +
                DOB_COLUMN + " TEXT, " +
                SOURCE_COLUMN + " TEXT, " +
                AGE_COLUMN + " TEXT);";
        db.execSQL(createRabbitTableQuery);
    }

    //CREATE MODEL:RABBIT
    public void addRabbit(Rabbit newRabbit, SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(RABBIT_TAG_COLUMN, newRabbit.get_tag());
        values.put(SEX_COLUMN, newRabbit.get_sex());
        values.put(BREED_COLUMN,newRabbit.get_breed());
        values.put(DOB_COLUMN, newRabbit.get_dateOfBirth().toString());
        //values.put(AGE_COLUMN, newRabbit.calculate_age());
        values.put(SOURCE_COLUMN, newRabbit.get_source());
        values.put(COLOUR_COLUMN, newRabbit.get_colour());
        db.insert(RABBIT_TABLE,null,values);
    }


    //:::::::::retrieve::::::::::::::::::
    //tag List
    public List<String> rabbitList(SQLiteDatabase db){
        List<String> rabbitList = new ArrayList<>();
        ArrayList<Rabbit> rabbitArrayList = rabbitArrayList(db);
        for (Rabbit eachRabbit:rabbitArrayList) {
            rabbitList.add(eachRabbit.get_tag());
        }
        return rabbitList;
    }
    //array List
    public ArrayList<Rabbit> rabbitArrayList(SQLiteDatabase db)
    {
        ArrayList<Rabbit> rabbitArrayList = new ArrayList<>();
        String rabbitSelectionQuery = "SELECT * FROM " +RABBIT_TABLE;
        Cursor c =db.rawQuery(rabbitSelectionQuery,null);
        c.moveToFirst();
        while(!c.isAfterLast())
        {
            if(c.getString(c.getColumnIndexOrThrow(RABBIT_TAG_COLUMN))!= null)
            //todo check if the if statement is necessary
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
                int id = c.getInt(c.getColumnIndexOrThrow(COLUMN_ID));

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
    //rabbit count
    public  int rabbitCount(SQLiteDatabase db)
    {
        String countQuery ="SELECT * FROM "+ RABBIT_TABLE;
        Cursor c = db.rawQuery(countQuery,null);
        int count = c.getCount();
        c.close();
        return count;
    }

    //update Rabbit
    public void updateRabbit(Rabbit selectedRabbit, SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();
        values.put(RABBIT_TAG_COLUMN, selectedRabbit.get_tag());
        values.put(SEX_COLUMN, selectedRabbit.get_sex());
        values.put(BREED_COLUMN,selectedRabbit.get_breed());
        values.put(SOURCE_COLUMN,selectedRabbit.get_source());
        values.put(DOB_COLUMN, selectedRabbit.get_dateOfBirth().toString());
        //values.put(AGE_COLUMN, selectedRabbit.calculate_age());
        values.put(COLOUR_COLUMN, selectedRabbit.get_colour());
        db.update(RABBIT_TABLE,values,COLUMN_ID+ "=?",new String[]{String.valueOf(selectedRabbit.get_id())});
    }

    //delete a Rabbit
    public void deleteRabbit(int id,SQLiteDatabase db)
    {
        String deleteRabbitQuery = "DELETE FROM "+RABBIT_TABLE+
                " WHERE " + COLUMN_ID+ " =\""+id+"\";";

        db.execSQL(deleteRabbitQuery);
        db.close();
    }
    public void dropRabbitTable(SQLiteDatabase db){
        String dropRabbitTableQuery = "DROP TABLE IF EXISTS '"+ RABBIT_TABLE +"'";
        db.execSQL(dropRabbitTableQuery);

    }

}
