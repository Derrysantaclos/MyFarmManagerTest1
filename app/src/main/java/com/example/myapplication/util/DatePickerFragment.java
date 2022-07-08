package com.example.myapplication.util;

import android.app.DatePickerDialog;


import android.widget.EditText;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;


public class DatePickerFragment {
    final Calendar myCalender = Calendar.getInstance();




    public void useDatePickerDialog(EditText crossDate){


        DatePickerDialog.OnDateSetListener selectDate= (view, year, month, dayOfMonth) -> {
            myCalender.set(Calendar.YEAR, year);
            myCalender.set(Calendar.MONTH, month);
            myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setCrossDate(crossDate);
        };
        crossDate.setOnClickListener(v -> {
            if (!crossDate.getText().toString().equals("")){
                MyDateTimeFormatter myDateTimeFormatter =new MyDateTimeFormatter();
                LocalDate crossDateDate= myDateTimeFormatter.dateStringToLocalDate(crossDate.getText().toString());
                new DatePickerDialog(v.getContext(),selectDate,crossDateDate.getYear(),
                        crossDateDate.getMonthValue()-1,crossDateDate.getDayOfMonth()).show();
            }else
            {new DatePickerDialog(v.getContext(),selectDate,myCalender.get(Calendar.YEAR),
                    myCalender.get(Calendar.MONTH),myCalender.get(Calendar.DAY_OF_MONTH)).show();}

        });


    }
    private void setCrossDate(EditText crossDate){
        String myFormat ="yyyy-MM-dd";
        SimpleDateFormat dateFormat= new SimpleDateFormat(myFormat, Locale.UK);
        crossDate.setText(dateFormat.format(myCalender.getTime()));

    }


}
