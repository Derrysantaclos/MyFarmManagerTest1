package com.derrysantaclos.myFarmManager.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.derrysantaclos.myFarmManager.R;
import com.derrysantaclos.myFarmManager.adapters.RabbitRecyclerAdapter;
import com.derrysantaclos.myFarmManager.data.DbHandler;
import com.derrysantaclos.myFarmManager.models.Image;
import com.derrysantaclos.myFarmManager.models.Rabbit;
import com.derrysantaclos.myFarmManager.util.RabbitFormDialog;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class RabbitListDisplayPage extends AppCompatActivity {
    private DbHandler dbHandler;
    private ArrayList<Rabbit> rabbitArrayList;
    private RabbitRecyclerAdapter rabbitRecyclerAdapter;
    private RecyclerView rabbitRecyclerView;
    private RabbitFormDialog rabbitFormDialog;
    private ArrayList<Image> imageArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rabbit_list_display_page);

        dbHandler = new DbHandler(this, null);
        rabbitArrayList=dbHandler.rabbitArrayList();

        //setContentView(R.layout.content_rabbit_list_page);

        rabbitRecyclerView =findViewById(R.id.RabbitRecylerDisplay);
        rabbitRecyclerView.setHasFixedSize(true);
        rabbitRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        rabbitRecyclerAdapter =new RabbitRecyclerAdapter(this,rabbitArrayList,dbHandler);
        rabbitRecyclerView.setAdapter(rabbitRecyclerAdapter);
        rabbitFormDialog =new RabbitFormDialog(this,dbHandler);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menuSearch);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void filter(String input){
        ArrayList<Rabbit> filteredRabbitArrayList = new ArrayList<>();
        for (Rabbit rabbit : rabbitArrayList){
            if (rabbit.get_breed().toLowerCase().contains(input.toLowerCase())|
                    rabbit.get_sex().toLowerCase().contains(input.toLowerCase())|
                    rabbit.get_age().toLowerCase().contains(input.toLowerCase())|
                    rabbit.get_source().toLowerCase().contains(input.toLowerCase())|
                    rabbit.get_tag().toLowerCase().contains(input.toLowerCase())){
                    filteredRabbitArrayList.add(rabbit);
            }
        }
        if (filteredRabbitArrayList.isEmpty()){
            Snackbar.make(this, rabbitRecyclerView,"NO MATCH FOUND",Snackbar.LENGTH_LONG).show();
        }

        rabbitRecyclerAdapter.filterArrayList(filteredRabbitArrayList);

    }

    //rabbitDialog
    public void popUpRabbitFormDialog(View v){
        rabbitFormDialog.showRabbitFormDialog();
    }
}