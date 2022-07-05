package com.example.myapplication.data;

import android.database.sqlite.SQLiteDatabase;

public class CommonDatabaseQuery {
    private String objectTable;

    public CommonDatabaseQuery(String objectTable) {
        this.objectTable = objectTable;
    }
    public void dropDb(SQLiteDatabase db)
    {

        //TODO, PROPER UPDATING
        //String deleteQuery ="DROP TABLE IF EXISTS "+ TABLE_ANIMALS;
        String deleteRabbitTableQuery = "DROP TABLE IF EXISTS "+ objectTable;
        // db.execSQL(deleteQuery);
        db.execSQL(deleteRabbitTableQuery);
    }

}
