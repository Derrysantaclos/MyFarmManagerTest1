package com.example.myapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.data.DbHandler;
import com.example.myapplication.models.Rabbit;

public class RabbitFullDetails extends AppCompatActivity {
    private TextView detailRabbitBreed;
    private TextView detailRabbitAge;
    private TextView detailRabbitTag;
    private TextView detailRabbitColour;
    private TextView detailRabbitDateOfBirth;
    private TextView detailRabbitSex;
    private TextView detailRabbitSource;
    private DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rabbit_full_details);
        setVariablesAndViews();

        setItemsValues();

    }
    private void setVariablesAndViews(){
        detailRabbitAge =findViewById(R.id.detailRabbitAge);
        detailRabbitBreed=findViewById(R.id.detailRabbitBreed);
        detailRabbitColour=findViewById(R.id.detailRabbitColour);
        detailRabbitDateOfBirth=findViewById(R.id.detailRabbitDOB);
        detailRabbitSex=findViewById(R.id.detailRabbitSex);
        detailRabbitSource=findViewById(R.id.detailRabbitSource);
        detailRabbitTag=findViewById(R.id.detailRabbitTag);
        dbHandler =new DbHandler(this,null,null,1);
    }
    private void setItemsValues(){
        //set the views value to the rabbit from the selected card
        Bundle extras= getIntent().getExtras();
        if(extras!=null) {
           int selectedRabbitId = extras.getInt("adapterPosition");
        Rabbit selectedRabbit =dbHandler.rabbitArrayList().get(selectedRabbitId);
        detailRabbitAge.setText(selectedRabbit.get_age());
            detailRabbitBreed.setText(selectedRabbit.get_breed());
            detailRabbitColour.setText(selectedRabbit.get_colour());
            detailRabbitDateOfBirth.setText(selectedRabbit.get_dateOfBirth().toString());
            detailRabbitSex.setText(selectedRabbit.get_sex());
            detailRabbitSource.setText(selectedRabbit.get_source());
            detailRabbitTag.setText(selectedRabbit.get_tag());
        }





    }
}