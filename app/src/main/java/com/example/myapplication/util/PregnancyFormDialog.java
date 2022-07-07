package com.example.myapplication.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.myapplication.R;
import com.example.myapplication.data.DbHandler;
import com.example.myapplication.data.PregnancyDbHandler;
import com.example.myapplication.data.PregnancyDbHandler2;
import com.example.myapplication.models.Pregnancy;
import com.example.myapplication.models.Rabbit;
import com.example.myapplication.ui.RabbitListDisplayPage;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PregnancyFormDialog
{
    private AlertDialog.Builder pregnancyAlertDialogBuilder;
    private AlertDialog pregnancyAlertDialog;
    private Spinner buckTag;
    private EditText crossDate;
    private Spinner pregnancyConfirmation;
    private EditText deliveryDate;
    private PregnancyDbHandler2 pregnancyDbHandler;
    private Context context;
    private String doeTag;
    private View pregnancyDialogView;
    private Button savePregnancyButton;

    private MyDateTimeFormatter myDateTimeFormatter=new MyDateTimeFormatter();
    private PregnancyFormValidator pregnancyFormValidator = new PregnancyFormValidator();

    public PregnancyFormDialog(PregnancyDbHandler2 pregnancyDbHandler, Context context, String doeTag) {
        this.pregnancyDbHandler = pregnancyDbHandler;
        this.context = context;
        this.doeTag=doeTag;
    }
//assign all the fields
    private void setViews()
    {
        pregnancyDialogView=((Activity)context).getLayoutInflater().inflate(R.layout.pregancy_form_dialog, null);
        crossDate=pregnancyDialogView.findViewById(R.id.pregnancyDialogFormCrossDate);
        pregnancyConfirmation=pregnancyDialogView.findViewById(R.id.pregnancyDialogFormConfirmation);
        deliveryDate =pregnancyDialogView.findViewById(R.id.pregnancyDialogFormDeliveryDate);
        buckTag = pregnancyDialogView.findViewById(R.id.pregnancyDialogFormBuckTag);
        savePregnancyButton =pregnancyDialogView.findViewById(R.id.savePregnancyButton);

        //Dialog builder
        pregnancyAlertDialogBuilder =new AlertDialog.Builder(context);
        pregnancyAlertDialogBuilder.setView(pregnancyDialogView);
        pregnancyAlertDialog=pregnancyAlertDialogBuilder.create();

        //get a list of all male rabbits tags for the autoComplete text View
        DbHandler dbHandler = new DbHandler(context, null,null,1);
        List<String> buckRabbitList = new ArrayList<>() ;
        for(Rabbit rabbit:dbHandler.rabbitArrayList()){
            if (rabbit.get_sex().equalsIgnoreCase("buck")| rabbit.get_sex().equalsIgnoreCase("male")){
                buckRabbitList.add(rabbit.get_tag());
            }
        }
        buckRabbitList.add("Others");

        //set the buckTag as an Adapter
        ArrayAdapter buckTagsAdapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1,buckRabbitList);
        buckTag.setAdapter(buckTagsAdapter);
    }

    //the add Pregnancy action

    public void addPregnancy(){
        setViews();
        pregnancyAlertDialog.show();
        savePregnancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePregnancy(v);

            }
        });
    }

    public void savePregnancy(View view){
        if (pregnancyFormValidator.requiredFieldEmpty(crossDate)){
            Snackbar.make(view, "Tag Exists Already", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        //ensure delivery date is in right format or Nil
        else if(!pregnancyFormValidator.inputDateValidator(deliveryDate)& !deliveryDate.getText().toString().equalsIgnoreCase("nil")){
            Snackbar.make(view, "Delivery Date not in right format. Leave as Nil if not yet delivered", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else if (pregnancyFormValidator.inputDateValidator(crossDate)){
            String newPregnancyBuckTag = buckTag.getSelectedItem().toString();
            Boolean newPregnancyConfirmation = pregnancyConfirmation.getSelectedItem().toString().equalsIgnoreCase("true");
            LocalDate newPregnancyCrossDate = myDateTimeFormatter.dateStringToLocalDate(crossDate.getText().toString());
            String newPregnancyDeliveryDate = deliveryDate.getText().toString();


            //LocalDate newPregnancyDeliveryDate = myDateTimeFormatter.dateStringToLocalDate(deliveryDate.getText().toString());


            Pregnancy newPregnancy = new Pregnancy(doeTag,newPregnancyBuckTag,newPregnancyCrossDate,
                    newPregnancyConfirmation,"",newPregnancyDeliveryDate);

            pregnancyDbHandler.addPregnancy(newPregnancy);
            Snackbar.make(view, "SAVED SUCCESSFULLY", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    //refresh page
                    pregnancyAlertDialog.dismiss();

                    //TODO change back to d pregnancy det
                    Intent refresh = new Intent(context, RabbitListDisplayPage.class);
                    context.startActivity(refresh);

                    ((Activity) context).finish();

                }
            }, 1200);


        }

    }


}

