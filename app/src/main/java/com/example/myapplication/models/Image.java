package com.example.myapplication.models;

import android.content.ContentValues;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class Image {
    
    private int id;
    private int rabbitTag;
    private byte[] imageBlob;
    private Bitmap imageBitmap;

    public Image(int rabbitTag, Bitmap imageBitmap){
        this.imageBitmap=imageBitmap;
        this.rabbitTag =rabbitTag;
    }

    public Image(int id, int rabbitTag, byte[] imageBlob) {
        this.id = id;
        this.rabbitTag = rabbitTag;
        this.imageBlob = imageBlob;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRabbitTag() {
        return rabbitTag;
    }

    public void setRabbitTag(int rabbitTag) {
        this.rabbitTag = rabbitTag;
    }

    public byte[] getImageBlob() {
        return imageBlob;
    }

    public byte[] setImageBlob() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG,0, byteArrayOutputStream);
        this.imageBlob = byteArrayOutputStream.toByteArray();
        return this.imageBlob;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}
