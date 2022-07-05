package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.data.DbHandler;
import com.example.myapplication.util.RabbitFormDialog;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private RabbitFormDialog rabbitFormDialog;
    private DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        byPassMainActivity();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHandler = new DbHandler(this,null,null,1);

        setSupportActionBar(binding.toolbar);
        rabbitFormDialog =new RabbitFormDialog(this,dbHandler);



        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show the form as a Dialog
                //todo check if item exists already, so as to go straight to details

                rabbitFormDialog.showRabbitFormDialog();

                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    private void byPassMainActivity() {
        DbHandler dbHandler = new DbHandler(this,null,null,1);
        int rabbitCount = dbHandler.rabbitCount();
        if (rabbitCount>0)
        {
            Intent rabbitPage = new Intent(MainActivity.this, RabbitListDisplayPage.class);
            startActivity(rabbitPage);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}