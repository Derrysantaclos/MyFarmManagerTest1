package com.example.myapplication.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.data.DbHandler;
import com.example.myapplication.ui.RabbitListDisplayPage;
import com.google.android.material.badge.BadgeUtils;

public class DeleteDialog {
    private Context context;
    private AlertDialog deleteDialog;
    private AlertDialog.Builder deleteDialogBuilder;
    private View deleteDialogView;
    private Button confirmDeleteButton;
    private Button cancelDeleteButton;
    private DbHandler dbHandler;

    public DeleteDialog(Context context,DbHandler dbHandler) {
        this.context = context;
        this.dbHandler=dbHandler;
    }

    public void showDeleteDialog(int rabbitId){
        deleteDialogView =((Activity)context).getLayoutInflater().inflate(R.layout.confirm_delete,null);
        confirmDeleteButton=deleteDialogView.findViewById(R.id.confirmDeleteButton);
        cancelDeleteButton=deleteDialogView.findViewById(R.id.cancelDeleteButton);

        deleteDialogBuilder =new AlertDialog.Builder(this.context);
        deleteDialogBuilder.setView(deleteDialogView);
        deleteDialog=deleteDialogBuilder.create();
        deleteDialog.show();
        cancelDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });
        confirmDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.deleteRabbit(rabbitId);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        deleteDialog.dismiss();
                        context.startActivity(new Intent(context, RabbitListDisplayPage.class));
                        ((Activity) context).finish();
                    }
                }, 1200);
            }
        });


    }



}
