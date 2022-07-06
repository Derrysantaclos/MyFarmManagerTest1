package com.example.myapplication.util;

import android.widget.EditText;

import com.example.myapplication.models.Rabbit;

import java.util.List;
import java.util.regex.Pattern;

public class PregnancyFormValidator {
    public boolean inputDateValidator(EditText inputDate)
    {
        return Pattern.matches("^(20)[0-2][0-9]-((1[0-2])|(0[1-9]))-((0[1-9])|([1-2][0-9])|(3[0-1]))$", inputDate.getText().toString());
    }


    public boolean requiredFieldEmpty(EditText crossedDate)
    {
        return (crossedDate.getText().toString().isEmpty()
        );

    }
}
