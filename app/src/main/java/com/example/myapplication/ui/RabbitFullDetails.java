package com.example.myapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private Button displayPregnancyRecords;
    private DbHandler dbHandler;
    private Rabbit selectedRabbit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rabbit_full_details);
        setVariablesAndViews();

        setItemsValues();
        if (selectedRabbit.get_sex().equalsIgnoreCase("male") | selectedRabbit.get_sex().equalsIgnoreCase("buck"))
        {
            displayPregnancyRecords.setVisibility(View.GONE);
        } else
            {
            displayPregnancyRecords.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    Intent pregnacyRecordsIntent = new Intent(RabbitFullDetails.this, PregnancyListPage.class);
                    pregnacyRecordsIntent.putExtra("doeTag", selectedRabbit.get_tag());
                    startActivity(pregnacyRecordsIntent);
                }
            });


        }
    }
    private void setVariablesAndViews(){
        detailRabbitAge =findViewById(R.id.detailRabbitAge);
        detailRabbitBreed=findViewById(R.id.detailRabbitBreed);
        detailRabbitColour=findViewById(R.id.detailRabbitColour);
        detailRabbitDateOfBirth=findViewById(R.id.detailRabbitDOB);
        detailRabbitSex=findViewById(R.id.detailRabbitSex);
        detailRabbitSource=findViewById(R.id.detailRabbitSource);
        detailRabbitTag=findViewById(R.id.detailRabbitTag);
        displayPregnancyRecords=findViewById(R.id.displayPregnancyRecords);
        dbHandler =new DbHandler(this,null,null,1);
    }
    private void setItemsValues(){
        //set the views value to the rabbit from the selected card
        Bundle extras= getIntent().getExtras();
        if(extras!=null) {
           int selectedRabbitId = extras.getInt("adapterPosition");
       selectedRabbit =dbHandler.rabbitArrayList().get(selectedRabbitId);
        detailRabbitAge.setText(selectedRabbit.calculate_age());
            detailRabbitBreed.setText(selectedRabbit.get_breed());
            detailRabbitColour.setText(selectedRabbit.get_colour());
            detailRabbitDateOfBirth.setText(selectedRabbit.get_dateOfBirth().toString());
            detailRabbitSex.setText(selectedRabbit.get_sex());
            detailRabbitSource.setText(selectedRabbit.get_source());
            detailRabbitTag.setText(selectedRabbit.get_tag());
        }





    }
}