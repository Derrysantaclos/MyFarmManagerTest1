package com.example.myapplication.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.models.Image;

import java.util.ArrayList;

public class ImageDbHelper {
    private static final String IMAGE_TABLE = "rabbit_images";
    private static final String IMAGE_COLUMN_ID ="_id";
    private static final String IMAGE_RABBIT_ID_COLUMN ="_rabbit_tag";
    private static final String IMAGE_COLUMN ="_image";


    public void createImageTable(SQLiteDatabase db){
        String createImageTableQuery = "CREATE TABLE "+IMAGE_TABLE+" " +
                "("
                +IMAGE_COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                IMAGE_COLUMN +" BLOB, "+
                IMAGE_RABBIT_ID_COLUMN + " INTEGER" +
                ")";
        db.execSQL(createImageTableQuery);
    }
    public void dropImageTable(SQLiteDatabase db){
            String dropImageTableQuery = "DROP TABLE IF EXISTS '"+ IMAGE_TABLE +"'";
        db.execSQL(dropImageTableQuery);

    }

    public void addImage( Image newImage, SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(IMAGE_RABBIT_ID_COLUMN,newImage.getRabbitTag());
        values.put(IMAGE_COLUMN, newImage.setImageBlob());
        db.insert(IMAGE_TABLE, null,values);
    }

    //::RETRIEVE
    public ArrayList<Image> imageArrayList(SQLiteDatabase db){
        ArrayList<Image> imageArrayList = new ArrayList<>();
        String getImageQuery = "SELECT * FROM "+ IMAGE_TABLE;
        Cursor c=db.rawQuery(getImageQuery,null);
        c.moveToFirst();
        while (!c.isAfterLast())
        {
            {
                int rabbitImageId =c.getInt(c.getColumnIndexOrThrow(IMAGE_RABBIT_ID_COLUMN));
                int imageId =c.getInt(c.getColumnIndexOrThrow(IMAGE_COLUMN_ID));
                byte[] imageByte = c.getBlob(c.getColumnIndexOrThrow(IMAGE_COLUMN));
                Image newImage = new Image(imageId,rabbitImageId,imageByte);
                imageArrayList.add(newImage);
            }
            c.moveToNext();
        }
        c.close();
        db.close();
        return imageArrayList;
    }
}
