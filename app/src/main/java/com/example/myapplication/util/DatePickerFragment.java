package com.example.myapplication.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //lets make an instance, so dat once it is called it sets as this
        final Calendar c = Calendar.getInstance();
        int year = c.YEAR;
        int month=c.MONTH;
        int days = c.DAY_OF_WEEK;

        return new DatePickerDialog(requireContext(),this,year,month,days);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}
