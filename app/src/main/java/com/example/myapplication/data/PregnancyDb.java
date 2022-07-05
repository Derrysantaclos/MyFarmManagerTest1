package com.example.myapplication.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.myapplication.models.Pregnancy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PregnancyDb extends AbstractDb{
    public static final int DATABASE_VERSION =1;
    public static final String DATABASE_NAME = "animals.db";
    public static final String PREGNANCY_TABLE= "pregnancy";
    public static final String COLUMN_ID ="_id";
    public static final String DOE_COLUMN ="_doe_tag";
    public static final String BUCK_COLUMN ="_buck_tag";

    public static final String CROSSED_DATE_COLUMN ="_date_mated";
    public static final String PREGNANCY_COUNT_COLUMN ="_number_of_days";
    public static final String PREGNANCY_CONFIRMATION_COLUMN= "_pregnancy_confirmation";
    public static final String MESSAGE ="_message";
    public static final String DELIVERY_DATE_COLUMN ="_delivery_date";


    public PregnancyDb(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

            String rabbitTableQuery = "CREATE TABLE "+PREGNANCY_TABLE + " ("+
                    COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DOE_COLUMN +" TEXT, "+
                    BUCK_COLUMN + " TEXT, "+
                    CROSSED_DATE_COLUMN+ " TEXT, "+
                    PREGNANCY_COUNT_COLUMN+ " INTEGER, " +
                    PREGNANCY_CONFIRMATION_COLUMN + " TEXT, " +
                    MESSAGE + " TEXT, " +
                    DELIVERY_DATE_COLUMN + " TEXT);";

            db.execSQL(rabbitTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        CommonDatabaseQuery databaseQuery =new CommonDatabaseQuery(PREGNANCY_TABLE);
        databaseQuery.dropDb(db);
        onCreate(db);
    }



    @Override
    public void addModel(Object object) {
       Pregnancy pregnancy=(Pregnancy) object;

            ContentValues values = new ContentValues();
            values.put(DOE_COLUMN, pregnancy.getDoeTag() );
            values.put(BUCK_COLUMN, pregnancy.getBuckTag());
            values.put(CROSSED_DATE_COLUMN,pregnancy.getCrossedDate().toString());
            values.put(PREGNANCY_COUNT_COLUMN, pregnancy.calculateNoOfDays());
            values.put(PREGNANCY_CONFIRMATION_COLUMN, pregnancy.getPregnancyConfirmation());
            //values.put(, newRabbit.get_colour());
            SQLiteDatabase db = getWritableDatabase();
            db.insert(PREGNANCY_TABLE,null,values);

    }

    @Override
    public List<String> ObjectList() {
        return null;
    }

    @Override
    public ArrayList<Object> objectstArrayList() {
        return null;
    }

    @Override
    public void updateRabbit(Object selectedObject) {

    }

    @Override
    public int ObjectCount() {
        return 0;
    }
}
