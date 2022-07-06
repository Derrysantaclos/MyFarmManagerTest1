package com.example.myapplication.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.adapters.PregnancyRecyclerAdapter;
import com.example.myapplication.data.PregnancyDbHandler;
import com.example.myapplication.util.PregnancyFormDialog;

public class PregnancyListPage extends AppCompatActivity {

    private PregnancyDbHandler pregnancyDbHandler;
    private PregnancyRecyclerAdapter pregnancyRecyclerAdapter;
    private RecyclerView pregnancyRecyclerView;
    private PregnancyFormDialog pregnancyFormDialog;
    private String doeTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregnancy_list_page);

        //get doeTag as extras from previous page
        Bundle extras= getIntent().getExtras();
        if(extras!=null) {
            String doeTag = extras.getString("doeTag");
        }


        pregnancyDbHandler = new PregnancyDbHandler(this,null,null,1);
        pregnancyRecyclerAdapter = new PregnancyRecyclerAdapter(this, pregnancyDbHandler);

        pregnancyRecyclerView= findViewById(R.id.pregnancyRecyclerView);
        pregnancyRecyclerView.setHasFixedSize(true);

        pregnancyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        pregnancyRecyclerView.setAdapter(pregnancyRecyclerAdapter);


        pregnancyFormDialog =new PregnancyFormDialog(pregnancyDbHandler,this,doeTag);


    }

    public void showPregnancyFormDialog(View v){
        pregnancyFormDialog.addPregnancy();
    }
}