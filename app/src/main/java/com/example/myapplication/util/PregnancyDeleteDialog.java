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
import com.example.myapplication.data.PregnancyDbHandler2;
import com.example.myapplication.ui.RabbitListDisplayPage;

public class PregnancyDeleteDialog {
    //GENERAL DELETE DIALOG TODO BE USED FOR OTHERS
    private Context context;
    public AlertDialog deleteDialog;
    private AlertDialog.Builder deleteDialogBuilder;
    private View deleteDialogView;
    public Button confirmDeleteButton;
    public Button cancelDeleteButton;
    public int actionInteger;

    public PregnancyDeleteDialog(Context context) {
        this.context = context;

    }

    public void showDeleteDialog() {
        actionInteger = 0;
        deleteDialogView = ((Activity) context).getLayoutInflater().inflate(R.layout.confirm_delete, null);
        confirmDeleteButton = deleteDialogView.findViewById(R.id.confirmDeleteButton);
        cancelDeleteButton = deleteDialogView.findViewById(R.id.cancelDeleteButton);
        deleteDialogBuilder = new AlertDialog.Builder(this.context);
        deleteDialogBuilder.setView(deleteDialogView);
        deleteDialog = deleteDialogBuilder.create();
        deleteDialog.show();
    }


}

