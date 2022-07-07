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

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.PregnancyRecyclerAdapter;
import com.example.myapplication.data.DbHandler;
import com.example.myapplication.data.PregnancyDbHandler;
import com.example.myapplication.data.PregnancyDbHandler2;
import com.example.myapplication.models.Pregnancy;
import com.example.myapplication.models.Rabbit;
import com.example.myapplication.ui.RabbitListDisplayPage;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PregnancyFormDialog
{
    private AlertDialog.Builder pregnancyAlertDialogBuilder;
    public AlertDialog pregnancyAlertDialog;
    private Spinner buckTag;
    private EditText crossDate;
    private Spinner pregnancyConfirmation;
    private EditText deliveryDate;
    private PregnancyDbHandler2 pregnancyDbHandler;
    private Context context;
    private String doeTag;
    private View pregnancyDialogView;
    private Button savePregnancyButton;
    private ArrayAdapter buckTagsAdapter;
    private ArrayAdapter pregnancyConfirmationAdapter;

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
        deliveryDate.setText("Nil");

        //get a list of all male rabbits tags for the autoComplete text View
        DbHandler dbHandler = new DbHandler(context, null,null,1);
        List<String> buckRabbitList = new ArrayList<>() ;
        for(Rabbit rabbit:dbHandler.rabbitArrayList()){
            if (rabbit.get_sex().equalsIgnoreCase("buck")| rabbit.get_sex().equalsIgnoreCase("male")){
                buckRabbitList.add(rabbit.get_tag());
            }
        }
        buckRabbitList.add("Others");

        String[] confirmationStatusOptions = {"True", "False", "Unconfirmed"};

        //set the buckTag as an Adapter
        buckTagsAdapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1,buckRabbitList);
        buckTag.setAdapter(buckTagsAdapter);
        pregnancyConfirmationAdapter=new ArrayAdapter(context,android.R.layout.simple_list_item_1,confirmationStatusOptions);
        pregnancyConfirmation.setAdapter(pregnancyConfirmationAdapter);
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

    public void savePregnancy(View view)
    {
        if (pregnancyFormValidator.requiredFieldEmpty(crossDate)){
            Snackbar.make(view, "Ensure all fields are filled", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        //ensure delivery date is in right format or Nil
        else if(!pregnancyFormValidator.inputDateValidator(deliveryDate)& !deliveryDate.getText().toString().equalsIgnoreCase("nil")){
            Snackbar.make(view, "Delivery Date not in right format. Leave as Nil if not yet delivered", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else if (!pregnancyFormValidator.inputDateValidator(crossDate))
        {
            Snackbar.make(view, "Ensure mate date is in right format YYYY-MM-DD", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else
            {
                String newPregnancyBuckTag = buckTag.getSelectedItem().toString();
                String newPregnancyConfirmation = pregnancyConfirmation.getSelectedItem().toString();
                LocalDate newPregnancyCrossDate = myDateTimeFormatter.dateStringToLocalDate(crossDate.getText().toString());
                String newPregnancyDeliveryDate = deliveryDate.getText().toString();


                //LocalDate newPregnancyDeliveryDate = myDateTimeFormatter.dateStringToLocalDate(deliveryDate.getText().toString());


                Pregnancy newPregnancy = new Pregnancy(doeTag,newPregnancyBuckTag,newPregnancyCrossDate,
                        newPregnancyConfirmation,"",newPregnancyDeliveryDate);

                pregnancyDbHandler.addPregnancy(newPregnancy);
                Snackbar.make(view, "SAVED SUCCESSFULLY"+doeTag, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //refresh page
                        pregnancyAlertDialog.dismiss();
                        Intent refresh = new Intent(context,context.getClass());
                        context.startActivity(refresh);

                        ((Activity) context).finish();

                    }
                }, 1200);


            }
        }



    public void editPregnancy(Pregnancy selectedPregnancy,PregnancyRecyclerAdapter pra, int adapterposition){
        setViews();
        crossDate.setText(selectedPregnancy.getCrossedDate().toString());
        buckTag.setSelection(buckTagsAdapter.getPosition(selectedPregnancy.getBuckTag()));
        pregnancyConfirmation.setSelection(pregnancyConfirmationAdapter.getPosition(selectedPregnancy.getPregnancyConfirmation()));
        deliveryDate.setText(selectedPregnancy.getDeliveryDate());
        pregnancyAlertDialog.show();
        savePregnancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePregnancy(selectedPregnancy,v,pra,adapterposition);
            }
        });
    }
    public void updatePregnancy(Pregnancy selectedPregnancy, View view, PregnancyRecyclerAdapter pra,int adapterPosition){
        if(!pregnancyFormValidator.inputDateValidator(crossDate))
        {
            Snackbar.make(view, "Ensure mate date is in right format YYYY-MM-DD", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else if(!pregnancyFormValidator.inputDateValidator(deliveryDate)& !deliveryDate.getText().toString().equalsIgnoreCase("nil")){
            Snackbar.make(view, "Delivery Date not in right format. Leave as Nil if not yet delivered", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else if(pregnancyFormValidator.requiredFieldEmpty(crossDate)){
            Snackbar.make(view, "Ensure all fields are filled", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else{
            selectedPregnancy.setBuckTag(buckTag.getSelectedItem().toString());
            selectedPregnancy.setCrossedDate(myDateTimeFormatter.dateStringToLocalDate(crossDate.getText().toString()));
            selectedPregnancy.setPregnancyConfirmation(pregnancyConfirmation.getSelectedItem().toString());
            selectedPregnancy.setDeliveryDate(deliveryDate.getText().toString());
            pregnancyDbHandler.updatePregnancy(selectedPregnancy);


                    pregnancyAlertDialog.dismiss();
                    pra.notifyItemChanged(adapterPosition);

                    //viewh.notifyItemChanged(adapterPosition);




        }


    }


}

