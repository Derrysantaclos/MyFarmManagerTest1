package com.derrysantaclos.myFarmManager.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.view.View;

import com.derrysantaclos.myFarmManager.R;
import com.derrysantaclos.myFarmManager.adapters.PregnancyRecyclerAdapter;
import com.derrysantaclos.myFarmManager.data.DbHandler;
import com.derrysantaclos.myFarmManager.models.Pregnancy;
import com.derrysantaclos.myFarmManager.util.PregnancyFormDialog;

import java.util.ArrayList;

public class PregnancyListPage extends AppCompatActivity {

    private DbHandler dbHandler;
    private PregnancyRecyclerAdapter pregnancyRecyclerAdapter;
    private RecyclerView pregnancyRecyclerView;
    private PregnancyFormDialog pregnancyFormDialog;
    private String doeTag;
    private ArrayList<Pregnancy> pregnancyArrayList;
    private ArrayList<Pregnancy> filteredPregnancyArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregnancy_list_page);

        //get doeTag as extras from previous page
        Bundle extras= getIntent().getExtras();
        if(extras!=null) {
           doeTag = extras.getString("doeTag");
        }



        dbHandler = new DbHandler(this,null);

        pregnancyRecyclerAdapter = new PregnancyRecyclerAdapter(this,filterPregnancyArrayList(doeTag), dbHandler);

        pregnancyRecyclerView= findViewById(R.id.pregnancyRecyclerView);
        pregnancyRecyclerView.setHasFixedSize(true);

        pregnancyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        pregnancyRecyclerView.setAdapter(pregnancyRecyclerAdapter);


        pregnancyFormDialog =new PregnancyFormDialog(dbHandler,this,doeTag);


    }
    private ArrayList<Pregnancy> filterPregnancyArrayList(String doeTag) {
        pregnancyArrayList = dbHandler.pregnancyArrayList();
        filteredPregnancyArrayList = new ArrayList<>();
        for (Pregnancy pregnancy : pregnancyArrayList) {
            if (pregnancy.getDoeTag().equals(doeTag)) {
                filteredPregnancyArrayList.add(pregnancy);
            }
        }
        return filteredPregnancyArrayList;
    }



    public void showPregnancyFormDialog(View v){

        pregnancyFormDialog.addPregnancy();

    }
}