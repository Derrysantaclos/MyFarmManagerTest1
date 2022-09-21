package com.example.myapplication.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.DbHandler;
import com.example.myapplication.models.Image;
import com.example.myapplication.models.Rabbit;

import java.io.IOException;

public class RabbitFullDetails extends AppCompatActivity {
    private TextView detailRabbitBreed;
    private TextView detailRabbitAge;
    private TextView detailRabbitTag;
    private TextView detailRabbitColour;
    private TextView detailRabbitDateOfBirth;
    private TextView detailRabbitSex;
    private TextView detailRabbitSource;
    private Button displayPregnancyRecords;
    private Button detailRabbitAddImage;
    private Button detailRabbitSaveImage;
    private ImageView detailRabbitImageView;
    private DbHandler dbHandler;
    private Rabbit selectedRabbit;
    private Bitmap selectedImageBitmap;
    private Image selectedImage;
    //private TextView imageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rabbit_full_details);
        setVariablesAndViews();

        setItemsValues();


        detailRabbitAddImage.setOnClickListener(v -> imageChooser());


        detailRabbitSaveImage.setOnClickListener(v-> saveImage());
        if (selectedRabbit.get_sex().equalsIgnoreCase("male") | selectedRabbit.get_sex().equalsIgnoreCase("buck"))
        {
            displayPregnancyRecords.setVisibility(View.GONE);
        } else
            {
            displayPregnancyRecords.setOnClickListener(v -> {
                Intent pregnancyRecordsIntent = new Intent(RabbitFullDetails.this, PregnancyListPage.class);
                pregnancyRecordsIntent.putExtra("doeTag", selectedRabbit.get_tag());
                startActivity(pregnancyRecordsIntent);
            });


        }
    }


    //save the image into the database
    private void saveImage() {
        if (selectedImageBitmap!=null){
            Image newImage = new Image(selectedRabbit.get_id(), selectedImageBitmap);
            //newImage.setImageBlob(newImage.)
            dbHandler.addImage(newImage);
            Toast.makeText(RabbitFullDetails.this, "Image added"+selectedImageBitmap.toString(), Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(RabbitFullDetails.this, "NO image Selected", Toast.LENGTH_LONG).show();
        }

    }


    //choose image from file
    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }


    //launch image getter from file
    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null
                            && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        try {
                            selectedImageBitmap
                                    = MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(),
                                    selectedImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


            });


    private void setVariablesAndViews(){
        detailRabbitAge =findViewById(R.id.detailRabbitAge);
        detailRabbitBreed=findViewById(R.id.detailRabbitBreed);
        detailRabbitColour=findViewById(R.id.detailRabbitColour);
        detailRabbitDateOfBirth=findViewById(R.id.detailRabbitDOB);
        detailRabbitSex=findViewById(R.id.detailRabbitSex);
        detailRabbitSource=findViewById(R.id.detailRabbitSource);
        detailRabbitTag=findViewById(R.id.detailRabbitTag);

        //images
        detailRabbitAddImage = findViewById(R.id.addImage);
        detailRabbitSaveImage = findViewById(R.id.saveImage);
        detailRabbitImageView = findViewById(R.id.rabbit_images);
        displayPregnancyRecords=findViewById(R.id.displayPregnancyRecords);
       // imageId =findViewById(R.id.imageId);
        dbHandler =new DbHandler(this, null);
    }


    private void setItemsValues(){
        //set the views value to the rabbit from the selected card
        Bundle extras= getIntent().getExtras();
        if(extras!=null) {
           int selectedRabbitAdapterPosition = extras.getInt("adapterPosition");
       selectedRabbit =dbHandler.rabbitArrayList().get(selectedRabbitAdapterPosition);
        detailRabbitAge.setText(selectedRabbit.calculate_age());
            detailRabbitBreed.setText(selectedRabbit.get_breed());
            detailRabbitColour.setText(selectedRabbit.get_colour());
            detailRabbitDateOfBirth.setText(selectedRabbit.get_dateOfBirth().toString());
            detailRabbitSex.setText(selectedRabbit.get_sex());
            detailRabbitSource.setText(selectedRabbit.get_source());
            detailRabbitTag.setText(selectedRabbit.get_tag());

            //get images
            for (Image image: dbHandler.imageArrayList())
            {


                if(image.getRabbitTag()==selectedRabbitAdapterPosition+1){
                    selectedImage=image;
                    Toast.makeText(RabbitFullDetails.this, "image"+selectedImage.getId(), Toast.LENGTH_SHORT).show();
                }
            }
            if(selectedImage!=null) {

                Bitmap bitmap = BitmapFactory.decodeByteArray(selectedImage.getImageBlob(), 0, selectedImage.getImageBlob().length);
                detailRabbitImageView.setImageBitmap(bitmap);
            }
        }





    }
}