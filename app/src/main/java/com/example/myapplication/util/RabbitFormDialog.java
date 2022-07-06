package com.example.myapplication.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.loader.app.LoaderManager;

import com.example.myapplication.R;
import com.example.myapplication.data.DbHandler;
import com.example.myapplication.models.Rabbit;
import com.example.myapplication.ui.RabbitListDisplayPage;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

public class RabbitFormDialog {
private AlertDialog rabbitFormDialog;
private AlertDialog.Builder rabbitFormDialogBuilder;
View rabbitFormDialogView;
private EditText rabbitTag;
private EditText rabbitDateofBirth;
private AutoCompleteTextView rabbitBreed;
private EditText rabbitSource;
private EditText rabbitColour;
private EditText rabbitSex;
private Button rabbitSaveButton;
private Context context;
private DbHandler dbHandler;



    public RabbitFormDialog(Context context,DbHandler dbHandler)
    {
        this.context = context;
        this.dbHandler =dbHandler;

    }
//TODO SHORTEN THIS CODE
private final Validator validator= new Validator();
    private void setViewFields(){
        //the views on the dialog form
        rabbitFormDialogView = ((Activity)context).getLayoutInflater().inflate(R.layout.rabbit_form_dialog,null);
        rabbitTag =rabbitFormDialogView.findViewById(R.id.tag);
        rabbitDateofBirth =rabbitFormDialogView.findViewById(R.id.dateOfBirth);
        rabbitBreed =rabbitFormDialogView.findViewById(R.id.breed);
        rabbitSource =rabbitFormDialogView.findViewById(R.id.source);
        rabbitSex =rabbitFormDialogView.findViewById(R.id.sex);
        rabbitColour =rabbitFormDialogView.findViewById(R.id.colour);

        //the dialog
        rabbitFormDialogBuilder =new AlertDialog.Builder(this.context);
        rabbitFormDialogBuilder.setView(rabbitFormDialogView);
        rabbitFormDialog =rabbitFormDialogBuilder.create();

        //save
        rabbitSaveButton =rabbitFormDialogView.findViewById(R.id.saveRabbitButton);

        //BREED OPTIONS
        String[] breedOptions={"New Zealand White", "English Spot","Checkered Giant","Dutch","Hyla Max",
                                "Hyla NG","California White", "American Chinchilla","Angoran White","Coloured Angoran"};
        ArrayAdapter breedAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1,breedOptions);
        rabbitBreed.setAdapter(breedAdapter);
    }

    public void showRabbitFormDialog()
    {
        setViewFields();
        rabbitFormDialog.show();

        rabbitSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRabbit(v);
            }
        });


                /*.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dateDialog = new DatePickerFragment();
                dateDialog.onCreateDialog(null);
            }
        });*/
    }

    private void saveRabbit(View view)
    {
        List<String> rabbitTagList =dbHandler.rabbitList();
        String newRabbitTag =rabbitTag.getText().toString();
        String newRabbitBreed =rabbitBreed.getText().toString();
        String newRabbitColour =rabbitColour.getText().toString();
        String newRabbitSex =rabbitSex.getText().toString();
        String newRabbitSource =rabbitSource.getText().toString();
        String newRabbitDOBString =rabbitDateofBirth.getText().toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (!validator.inputDateValidator(rabbitDateofBirth))//input date Validator
        {
            Toast.makeText(context,"Invalid",Toast.LENGTH_LONG).show();
            Snackbar.make(view, "Ensure Proper Date Format(yyyy-mm-dd)", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else if (validator.tagExistAlready(rabbitTag,rabbitTagList))
        {
            Snackbar.make(view, "Tag Exists Already", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else if(validator.requiredFieldEmpty(rabbitTag,rabbitColour,rabbitBreed,rabbitSex))
        {
            Snackbar.make(view, "Make Sure All Required Fields are filled", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

        else
            {
            LocalDate newRabbitDob = LocalDate.parse(newRabbitDOBString, formatter);


            Rabbit newRabbit = new Rabbit(newRabbitSex, newRabbitDob, newRabbitTag, newRabbitBreed, newRabbitSource, newRabbitColour);

            dbHandler.addRabbit(newRabbit);
            Snackbar.make(view, "SAVED SUCCESSFULLY", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    //refresh page
                    rabbitFormDialog.dismiss();
                    Intent refresh = new Intent(context, RabbitListDisplayPage.class);
                    context.startActivity(refresh);

                    ((Activity) context).finish();

                }
            }, 1200);
        }

    }

    public void showCurrentRabbitDialog(Rabbit rabbit){
        //get the details of the rabbit and fills it
        setViewFields();
        rabbitTag.setText(rabbit.get_tag());
        rabbitBreed.setText(rabbit.get_breed());
        rabbitColour.setText(rabbit.get_colour());
        rabbitSex.setText(rabbit.get_sex());
        rabbitDateofBirth.setText(rabbit.get_dateOfBirth().toString());
        rabbitSource.setText(rabbit.get_source());
        rabbitFormDialog.show();
        rabbitSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItem(v, rabbit);
            }
        });


    }





    //FOR UPDATING ITEMS
    private void updateItem(View view, Rabbit rabbit){

        //change to new rabbit so as to be able to combine

                //rabbit tag cannot be changed while editing
                if (validator.updateTagChanged(rabbitTag,rabbit))
                {
                    Snackbar.make(view, "You cannot change a rabbits tag", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    rabbitTag.setText(rabbit.get_tag());

                }
                else if(!validator.inputDateValidator(rabbitDateofBirth))
                {
                    Snackbar.make(view, "Ensure Proper Date Format(yyyy-mm-dd)", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else if(validator.requiredFieldEmpty(rabbitTag,rabbitColour,rabbitBreed,rabbitSex))
                {
                    {
                        Snackbar.make(view, "Make Sure All Required Fields are filled", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
                else
                {
                    DateTimeFormatter myFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate formattedDateOfBirth = LocalDate.parse(rabbitDateofBirth.getText().toString(), myFormatter);
                    rabbit.set_breed(rabbitBreed.getText().toString());
                    rabbit.set_source(rabbitSource.getText().toString());
                    rabbit.set_sex(rabbitSex.getText().toString());
                    rabbit.set_colour(rabbitColour.getText().toString());
                    rabbit.set_dateOfBirth(formattedDateOfBirth);
                    rabbit.calculate_age();

                    dbHandler.updateRabbit(rabbit);
                    Snackbar.make(view, "Updated Successfully", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rabbitFormDialog.dismiss();
                                context.startActivity(new Intent(context, RabbitListDisplayPage.class));
                            ((Activity) context).finish();
                        }
                    }, 1200);
                }

    }
}
