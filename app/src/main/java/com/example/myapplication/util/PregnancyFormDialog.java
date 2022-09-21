package com.example.myapplication.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
//import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.myapplication.R;
import com.example.myapplication.adapters.PregnancyRecyclerAdapter;
import com.example.myapplication.data.DbHandler;

import com.example.myapplication.models.Pregnancy;
import com.example.myapplication.models.Rabbit;
import com.example.myapplication.ui.PregnancyListPage;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.util.ArrayList;
//import java.util.Calendar;
import java.util.List;

public class PregnancyFormDialog
{
    public AlertDialog pregnancyAlertDialog;
    private Spinner buckTag;
    private EditText crossDate;
    private Spinner pregnancyConfirmation;
    private CheckBox hasDelivered;
    private EditText deliveryDate;
    private final DbHandler dbHandler;
    private final Context context;
    private final String doeTag;
    private Button savePregnancyButton;
    private ArrayAdapter buckTagsAdapter;
    private ArrayAdapter pregnancyConfirmationAdapter;
    //final Calendar myCalender = Calendar.getInstance();
    private final DatePickerFragment datePickerFragment=new DatePickerFragment();

    private final MyDateTimeFormatter myDateTimeFormatter=new MyDateTimeFormatter();
    private final PregnancyFormValidator pregnancyFormValidator = new PregnancyFormValidator();

    public PregnancyFormDialog(DbHandler dbHandler, Context context, String doeTag) {
        this.dbHandler = dbHandler;
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

        hasDelivered=pregnancyDialogView.findViewById(R.id.deliveryCheckBox);
        hasDelivered.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                    datePickerFragment.useDatePickerDialog(deliveryDate);
                }
            });


        //Dialog builder
        AlertDialog.Builder pregnancyAlertDialogBuilder = new AlertDialog.Builder(context);
        pregnancyAlertDialogBuilder.setView(pregnancyDialogView);
        pregnancyAlertDialog= pregnancyAlertDialogBuilder.create();

        //get a list of all male rabbits tags for the autoComplete text View
        DbHandler dbHandler = new DbHandler(context, null);
        List<String> buckRabbitList = new ArrayList<>() ;
        buckRabbitList.add("Others");
        for(Rabbit rabbit:dbHandler.rabbitArrayList()){
            if (rabbit.get_sex().equalsIgnoreCase("buck")| rabbit.get_sex().equalsIgnoreCase("male")){
                buckRabbitList.add(rabbit.get_tag());
            }
        }


        String[] confirmationStatusOptions = {"Unconfirmed","True", "False"};

        //set the buckTag as an Adapter
        buckTagsAdapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1,buckRabbitList);
        buckTag.setAdapter(buckTagsAdapter);
        pregnancyConfirmationAdapter=new ArrayAdapter(context,android.R.layout.simple_list_item_1,confirmationStatusOptions);
        pregnancyConfirmation.setAdapter(pregnancyConfirmationAdapter);

    }
//    private void setCrossDate(){
//        String myFormat ="yyyy-MM-dd";
//        SimpleDateFormat dateFormat= new SimpleDateFormat(myFormat, Locale.UK);
//        crossDate.setText(dateFormat.format(myCalender.getTime()));
//
//    }


    //the add Pregnancy action

    public void addPregnancy(){
        setViews();
        pregnancyAlertDialog.show();
        crossDate.setText(LocalDate.now().toString());
        savePregnancyButton.setOnClickListener(this::savePregnancy);
    }

    public void savePregnancy(View view)
    {
        if (pregnancyFormValidator.requiredFieldEmpty(crossDate)){
            Snackbar.make(view, "Ensure all fields are filled", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        //ensure delivery date is in right format or Nil
        else if(!pregnancyFormValidator.inputDateValidator(deliveryDate)& !deliveryDate.getText().toString().equalsIgnoreCase("")){
            Snackbar.make(view, "Delivery Date not in right format. Should be Empty if Doe Has not delivered", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else if (!pregnancyFormValidator.inputDateValidator(crossDate))
        {
            Snackbar.make(view, "Ensure mate date is in right format YYYY-MM-DD", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else
            {
                String newPregnancyDeliveryDate =deliveryDate.getText().toString();
                if (!hasDelivered.isChecked()){
                    newPregnancyDeliveryDate ="";
                }
                String newPregnancyBuckTag = buckTag.getSelectedItem().toString();
                String newPregnancyConfirmation = pregnancyConfirmation.getSelectedItem().toString();
                LocalDate newPregnancyCrossDate = myDateTimeFormatter.dateStringToLocalDate(crossDate.getText().toString());

                Pregnancy newPregnancy = new Pregnancy(doeTag,newPregnancyBuckTag,newPregnancyCrossDate,
                        newPregnancyConfirmation,"",newPregnancyDeliveryDate);

                dbHandler.addPregnancy(newPregnancy);
                Snackbar.make(view, "SAVED SUCCESSFULLY"+doeTag, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                //refresh
                new Handler().postDelayed(() -> {

                    //refresh page
                    pregnancyAlertDialog.dismiss();
                    Intent refresh = new Intent(context, PregnancyListPage.class);
                    refresh.putExtra("doeTag",newPregnancy.getDoeTag() );
                    context.startActivity(refresh);

                    ((Activity) context).finish();

                }, 1200);




            }
        }



    public void editPregnancy(Pregnancy selectedPregnancy,PregnancyRecyclerAdapter pra, int adapterposition){
        setViews();
        crossDate.setText(selectedPregnancy.getCrossedDate().toString());
        buckTag.setSelection(buckTagsAdapter.getPosition(selectedPregnancy.getBuckTag()));
        pregnancyConfirmation.setSelection(pregnancyConfirmationAdapter.getPosition(selectedPregnancy.getPregnancyConfirmation()));
        deliveryDate.setText(selectedPregnancy.getDeliveryDate());
        if (!selectedPregnancy.getDeliveryDate().isEmpty()){
            hasDelivered.setChecked(true);
        }
        pregnancyAlertDialog.show();


        savePregnancyButton.setOnClickListener(v -> updatePregnancy(selectedPregnancy,v,pra,adapterposition));
    }
    public void updatePregnancy(Pregnancy selectedPregnancy, View view, PregnancyRecyclerAdapter pra,int adapterPosition){
        if(!pregnancyFormValidator.inputDateValidator(crossDate))
        {
            Snackbar.make(view, "Ensure mate date is in right format YYYY-MM-DD", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else if(hasDelivered.isChecked()& deliveryDate.getText().toString().equals("")){
            Snackbar.make(view, "Delivery Date cannot be empty if check box is checked ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else if(pregnancyFormValidator.requiredFieldEmpty(crossDate)){
            Snackbar.make(view, "Ensure all fields are filled", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else{
            String pregnancyDeliveryDate =deliveryDate.getText().toString();
            if (!hasDelivered.isChecked()){
                pregnancyDeliveryDate ="";
            }
            String selectedPregnancyConfirmation = pregnancyConfirmation.getSelectedItem().toString();
            if (!pregnancyDeliveryDate.equals("")){
                selectedPregnancyConfirmation="True";
            }

            selectedPregnancy.setBuckTag(buckTag.getSelectedItem().toString());
            selectedPregnancy.setCrossedDate(myDateTimeFormatter.dateStringToLocalDate(crossDate.getText().toString()));
            selectedPregnancy.setPregnancyConfirmation(selectedPregnancyConfirmation);
            selectedPregnancy.setDeliveryDate(pregnancyDeliveryDate);
            dbHandler.updatePregnancy(selectedPregnancy);


                    pregnancyAlertDialog.dismiss();
                    pra.notifyItemChanged(adapterPosition);

                    //viewh.notifyItemChanged(adapterPosition);




        }


    }


}

