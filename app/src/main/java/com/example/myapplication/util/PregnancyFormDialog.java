package com.example.myapplication.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.myapplication.R;
import com.example.myapplication.adapters.PregnancyRecyclerAdapter;
import com.example.myapplication.data.DbHandler;

import com.example.myapplication.data.PregnancyDbHandler2;
import com.example.myapplication.models.Pregnancy;
import com.example.myapplication.models.Rabbit;
import com.example.myapplication.ui.PregnancyListPage;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PregnancyFormDialog
{
    public AlertDialog pregnancyAlertDialog;
    private Spinner buckTag;
    private EditText crossDate;
    private Spinner pregnancyConfirmation;
    private EditText deliveryDate;
    private final PregnancyDbHandler2 pregnancyDbHandler;
    private final Context context;
    private final String doeTag;
    private Button savePregnancyButton;
    private ArrayAdapter buckTagsAdapter;
    private ArrayAdapter pregnancyConfirmationAdapter;
    final Calendar myCalender = Calendar.getInstance();
    private final DatePickerFragment datePickerFragment=new DatePickerFragment();

    private final MyDateTimeFormatter myDateTimeFormatter=new MyDateTimeFormatter();
    private final PregnancyFormValidator pregnancyFormValidator = new PregnancyFormValidator();

    public PregnancyFormDialog(PregnancyDbHandler2 pregnancyDbHandler, Context context, String doeTag) {
        this.pregnancyDbHandler = pregnancyDbHandler;
        this.context = context;
        this.doeTag=doeTag;
    }
//assign all the fields
    @SuppressLint("SetTextI18n")
    private void setViews()
    {
        View pregnancyDialogView = ((Activity) context).getLayoutInflater().inflate(R.layout.pregancy_form_dialog, null);
        crossDate= pregnancyDialogView.findViewById(R.id.pregnancyDialogFormCrossDate);
        pregnancyConfirmation= pregnancyDialogView.findViewById(R.id.pregnancyDialogFormConfirmation);
        deliveryDate = pregnancyDialogView.findViewById(R.id.pregnancyDialogFormDeliveryDate);
        buckTag = pregnancyDialogView.findViewById(R.id.pregnancyDialogFormBuckTag);
        savePregnancyButton = pregnancyDialogView.findViewById(R.id.savePregnancyButton);
        datePickerFragment.useDatePickerDialog(crossDate);

//        DatePickerDialog.OnDateSetListener selectDate= new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                myCalender.set(Calendar.YEAR, year);
//                myCalender.set(Calendar.MONTH,month);
//                myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                setCrossDate();
//            }
//        };
//        crossDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new DatePickerDialog(context,selectDate,myCalender.get(Calendar.YEAR),
//                        myCalender.get(Calendar.MONTH),myCalender.get(Calendar.DAY_OF_MONTH)).show();
//            }
//        });



        //Dialog builder
        AlertDialog.Builder pregnancyAlertDialogBuilder = new AlertDialog.Builder(context);
        pregnancyAlertDialogBuilder.setView(pregnancyDialogView);
        pregnancyAlertDialog= pregnancyAlertDialogBuilder.create();
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

        String[] confirmationStatusOptions = {"Unconfirmed","True", "False"};

        //set the buckTag as an Adapter
        buckTagsAdapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1,buckRabbitList);
        buckTag.setAdapter(buckTagsAdapter);
        pregnancyConfirmationAdapter=new ArrayAdapter(context,android.R.layout.simple_list_item_1,confirmationStatusOptions);
        pregnancyConfirmation.setAdapter(pregnancyConfirmationAdapter);

    }
    private void setCrossDate(){
        String myFormat ="yyyy-MM-dd";
        SimpleDateFormat dateFormat= new SimpleDateFormat(myFormat, Locale.UK);
        crossDate.setText(dateFormat.format(myCalender.getTime()));

    }


    //the add Pregnancy action

    public void addPregnancy(){
        setViews();
        pregnancyAlertDialog.show();
        crossDate.setText(LocalDate.now().toString());
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

                //refresh
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //refresh page
                        pregnancyAlertDialog.dismiss();
                        Intent refresh = new Intent(context, PregnancyListPage.class);
                        refresh.putExtra("doeTag",newPregnancy.getDoeTag() );
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
        savePregnancyButton.setOnClickListener(v -> updatePregnancy(selectedPregnancy,v,pra,adapterposition));
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

