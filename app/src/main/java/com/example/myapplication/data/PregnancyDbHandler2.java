package com.example.myapplication.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.myapplication.models.Pregnancy;
import com.example.myapplication.models.Rabbit;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PregnancyDbHandler2 extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION =1;
    public static final String DATABASE_NAME = "animals_pregnancy.db";
    public static final String PREGNANCY_TABLE= "pregnancy";
    public static final String COLUMN_ID ="_id";
    public static final String DOE_COLUMN ="_doe_tag";
    public static final String BUCK_COLUMN ="_buck_tag";

    public static final String CROSSED_DATE_COLUMN ="_date_mated";
    public static final String PREGNANCY_COUNT_COLUMN ="_number_of_days";
    public static final String PREGNANCY_CONFIRMATION_COLUMN= "_pregnancy_confirmation";
    public static final String MESSAGE ="_message";
    public static final String DELIVERY_DATE_COLUMN ="_delivery_date";
    public static final String DOE_PREGNANCY_COUNT_COLUMN ="_doe_pregnancy_count";


    public PregnancyDbHandler2(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String rabbitTableQuery = "CREATE TABLE "+ PREGNANCY_TABLE + " ("+
                COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DOE_COLUMN +" TEXT, "+
                BUCK_COLUMN + " TEXT, "+
                CROSSED_DATE_COLUMN + " TEXT, "+
                PREGNANCY_CONFIRMATION_COLUMN + " TEXT, " +
                MESSAGE + " TEXT, " +
                PREGNANCY_COUNT_COLUMN + " INT, " +
                DOE_PREGNANCY_COUNT_COLUMN+ " INT, "+
                DELIVERY_DATE_COLUMN+ " TEXT);";

        db.execSQL(rabbitTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //String deleteQuery ="DROP TABLE IF EXISTS "+ TABLE_ANIMALS;
        String deleteRabbitTableQuery = "DROP TABLE IF EXISTS "+ PREGNANCY_TABLE;
        // db.execSQL(deleteQuery);
        db.execSQL(deleteRabbitTableQuery);
        onCreate(db);
    }

    //CREATE
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addPregnancy(Pregnancy newPregnancy)
    {
        ContentValues values = new ContentValues();
        values.put(DOE_COLUMN, newPregnancy.getDoeTag());
        values.put(BUCK_COLUMN, newPregnancy.getBuckTag());
        values.put(CROSSED_DATE_COLUMN,newPregnancy.getCrossedDate().toString());
        values.put(PREGNANCY_CONFIRMATION_COLUMN, newPregnancy.getPregnancyConfirmation());
        //values.put(MESSAGE, newRabbit.calculate_age());
        values.put(DELIVERY_DATE_COLUMN, newPregnancy.getDeliveryDate());
        values.put(DOE_PREGNANCY_COUNT_COLUMN, doePregnancyCount(newPregnancy.getDoeTag())+1);
        //values.put(PREGNANCY_COUNT_COLUMN, newPregnancy.calculateNoOfDays());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(PREGNANCY_TABLE,null,values);
    }


    //GET DOE PREGNANCY COUNT
    private int doePregnancyCount(String doeTag){
        SQLiteDatabase db =getReadableDatabase();
        String query ="SELECT * FROM PREGNANCY TABLE WHERE "+DOE_COLUMN+" = \""+doeTag+"\";";
        Cursor c =db.rawQuery(query,null);
        int count =c.getCount();
        c.close();
        return count;
    }
    //DELETE



    //RETRIEVE::Rabbit List
    public List<String> pregnancyList(){
        List<String> rabbitList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String rabbitSelectionQuery = "SELECT * FROM " +PREGNANCY_TABLE;
        Cursor c =db.rawQuery(rabbitSelectionQuery,null);
        c.moveToFirst();
        while(!c.isAfterLast())
        {
            if(c.getString(c.getColumnIndexOrThrow(COLUMN_ID))!= null)
            //todo check if the if statement is neccessary
            {
                String currentTag = c.getString(c.getColumnIndexOrThrow(COLUMN_ID));
                rabbitList.add(currentTag);

            }
            c.moveToNext();
        }
        c.close();
        db.close();
        return rabbitList;
    }

    //RETRIEVE ALL RABBITS
    public ArrayList<Pregnancy> pregnancyArrayList()
    {
        ArrayList<Pregnancy> pregnancyArrayList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String pregnancySelectionQuery = "SELECT * FROM " +PREGNANCY_TABLE;
        Cursor myCursor =db.rawQuery(pregnancySelectionQuery,null);
        myCursor.moveToFirst();
            while (!myCursor.isAfterLast())
            {
                if (myCursor.getString(myCursor.getColumnIndexOrThrow(COLUMN_ID)) != null) {
                    int id = myCursor.getInt(myCursor.getColumnIndexOrThrow(COLUMN_ID));
                    String doeTag = myCursor.getString(myCursor.getColumnIndexOrThrow(DOE_COLUMN));
                    String buckTag = myCursor.getString(myCursor.getColumnIndexOrThrow(BUCK_COLUMN));

                    String crossedDateString = myCursor.getString(myCursor.getColumnIndexOrThrow(CROSSED_DATE_COLUMN));
                    //int numberOfDays = myCursor.getInt(myCursor.getColumnIndexOrThrow(PREGNANCY_COUNT_COLUMN));
                    String pregnancyConfirmation = myCursor.getString(myCursor.getColumnIndexOrThrow(PREGNANCY_CONFIRMATION_COLUMN));


                    String message = myCursor.getString(myCursor.getColumnIndexOrThrow(MESSAGE));
                    String deliveryDateString = myCursor.getString(myCursor.getColumnIndexOrThrow(DELIVERY_DATE_COLUMN));
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate crossedDate = LocalDate.parse(crossedDateString, formatter);

                    Integer doePregnancyCount = myCursor.getInt(myCursor.getColumnIndexOrThrow(DOE_PREGNANCY_COUNT_COLUMN));

                    Pregnancy pregnancy = new Pregnancy(id, doeTag, buckTag, crossedDate, pregnancyConfirmation,
                            message, deliveryDateString,doePregnancyCount);

                    pregnancyArrayList.add(pregnancy);

                }
                myCursor.moveToNext();
            }
        myCursor.close();
        db.close();


        return pregnancyArrayList;
    }
    //UPDATE
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updatePregnancy(Pregnancy selectedPregnancy)
    {
        ContentValues values = new ContentValues();
        values.put(DOE_COLUMN, selectedPregnancy.getDoeTag());
        values.put(BUCK_COLUMN, selectedPregnancy.getBuckTag());
        values.put(CROSSED_DATE_COLUMN,selectedPregnancy.getCrossedDate().toString());
        values.put(PREGNANCY_CONFIRMATION_COLUMN, selectedPregnancy.getPregnancyConfirmation());
        //values.put(MESSAGE, newRabbit.calculate_age());
        values.put(DELIVERY_DATE_COLUMN, selectedPregnancy.getDeliveryDate());
        //values.put(PREGNANCY_COUNT_COLUMN, newPregnancy.calculateNoOfDays());
        SQLiteDatabase db = getWritableDatabase();
        db.update(PREGNANCY_TABLE,values,COLUMN_ID+"=?",new String[]{String.valueOf(selectedPregnancy.getId())});

    }
    //rabbit count
    public  int pregnancyCount()
    {
        String countQuery ="SELECT * FROM "+ PREGNANCY_TABLE;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(countQuery,null);
        return c.getCount();
    }

    //DELETE
    public void deletePregnancy(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        String deleteRabbitQuery = "DELETE FROM "+PREGNANCY_TABLE+
                " WHERE " + COLUMN_ID+ " =\""+id+"\";";
        db.execSQL(deleteRabbitQuery);
    }


}
