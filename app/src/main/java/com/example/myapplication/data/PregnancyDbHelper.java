package com.example.myapplication.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.models.Pregnancy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PregnancyDbHelper {
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



    public void createPregnancyTable(SQLiteDatabase db){
        String pregnancyTableQuery = "CREATE TABLE "+ PREGNANCY_TABLE + " ("+
                COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DOE_COLUMN +" TEXT, "+
                BUCK_COLUMN + " TEXT, "+
                CROSSED_DATE_COLUMN + " TEXT, "+
                PREGNANCY_CONFIRMATION_COLUMN + " TEXT, " +
                MESSAGE + " TEXT, " +
                PREGNANCY_COUNT_COLUMN + " INT, " +
                DOE_PREGNANCY_COUNT_COLUMN+ " INT, "+
                DELIVERY_DATE_COLUMN+ " TEXT);";

        db.execSQL(pregnancyTableQuery);
    }
    public void dropPregnancyTable(SQLiteDatabase db){
       String dropPregnancyTableQuery = "DROP TABLE IF EXISTS '"+ PREGNANCY_TABLE +"'";
       db.execSQL(dropPregnancyTableQuery);

    }

    //create new pregnancy
    public void addPregnancy(Pregnancy newPregnancy, SQLiteDatabase writableDb, SQLiteDatabase readableDb){
        ContentValues values = new ContentValues();
        values.put(DOE_COLUMN, newPregnancy.getDoeTag());
        values.put(BUCK_COLUMN, newPregnancy.getBuckTag());
        values.put(CROSSED_DATE_COLUMN,newPregnancy.getCrossedDate().toString());
        values.put(PREGNANCY_CONFIRMATION_COLUMN, newPregnancy.getPregnancyConfirmation());
        //values.put(MESSAGE, newRabbit.calculate_age());
        values.put(DELIVERY_DATE_COLUMN, newPregnancy.getDeliveryDate());
        values.put(DOE_PREGNANCY_COUNT_COLUMN, doePregnancyCount(newPregnancy.getDoeTag(),readableDb)+1);
        //values.put(PREGNANCY_COUNT_COLUMN, newPregnancy.calculateNoOfDays());

        writableDb.insert(PREGNANCY_TABLE,null,values);
    }

    //GET DOE PREGNANCY COUNT
    private int doePregnancyCount(String doeTag, SQLiteDatabase db){
        String query ="SELECT * FROM " +PREGNANCY_TABLE +" WHERE "+DOE_COLUMN+" = \""+doeTag+"\";";
        Cursor c =db.rawQuery(query,null);
        int count =c.getCount();
        c.close();
        return count;
    }

    public List<Integer> pregnancyList(SQLiteDatabase db){
        List<Integer> rabbitList = new ArrayList<>();
        ArrayList<Pregnancy> pregnancyArrayList = pregnancyArrayList(db);
        for(Pregnancy eachPregnancy:pregnancyArrayList){
            rabbitList.add(eachPregnancy.getId());
        }
        return rabbitList;
    }
    //Pregnancy Details
    public ArrayList<Pregnancy> pregnancyArrayList(SQLiteDatabase db)
    {
        ArrayList<Pregnancy> pregnancyArrayList = new ArrayList<>();

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

                int doePregnancyCount = myCursor.getInt(myCursor.getColumnIndexOrThrow(DOE_PREGNANCY_COUNT_COLUMN));

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
    //pregnancy Count
    public  int pregnancyCount(SQLiteDatabase db)
    {
        String countQuery ="SELECT * FROM "+ PREGNANCY_TABLE;
        Cursor c = db.rawQuery(countQuery,null);
        int pregnancyCount =c.getCount();
        c.close();
        return pregnancyCount;
    }

    public void updatePregnancy(Pregnancy selectedPregnancy, SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();
        values.put(DOE_COLUMN, selectedPregnancy.getDoeTag());
        values.put(BUCK_COLUMN, selectedPregnancy.getBuckTag());
        values.put(CROSSED_DATE_COLUMN,selectedPregnancy.getCrossedDate().toString());
        values.put(PREGNANCY_CONFIRMATION_COLUMN, selectedPregnancy.getPregnancyConfirmation());
        //values.put(MESSAGE, newRabbit.calculate_age());
        values.put(DELIVERY_DATE_COLUMN, selectedPregnancy.getDeliveryDate());
        //values.put(PREGNANCY_COUNT_COLUMN, newPregnancy.calculateNoOfDays());
        db.update(PREGNANCY_TABLE,values,COLUMN_ID+"=?",new String[]{String.valueOf(selectedPregnancy.getId())});

    }


    public void deletePregnancy(int id,SQLiteDatabase db)
    {
        String deleteRabbitQuery = "DELETE FROM "+PREGNANCY_TABLE+
                " WHERE " + COLUMN_ID+ " =\""+id+"\";";
        db.execSQL(deleteRabbitQuery);
    }

}
