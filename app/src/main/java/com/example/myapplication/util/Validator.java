package com.example.myapplication.util;

import android.widget.EditText;

import com.example.myapplication.models.Rabbit;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Validator {
    public Validator() {
    }
    public boolean inputDateValidator(EditText inputDate)
    {
        return Pattern.matches("^(20)[0-2][0-9]-((1[0-2])|(0[1-9]))-((0[1-9])|([1-2][0-9])|(3[0-1]))$", inputDate.getText().toString());
    }
    public boolean tagExistAlready(EditText inputTag, List<String> rabbitTagList)
    {
        return rabbitTagList.contains(inputTag.getText().toString());

    }
    public boolean updateTagChanged(EditText inputTag, Rabbit rabbit)
    {
        return (!inputTag.getText().toString().equals(rabbit.get_tag()));

    }
    public boolean requiredFieldEmpty(EditText inputTag,EditText inputColour,EditText inputBreed, EditText inputSex)
    {
        return (inputTag.getText().toString().isEmpty()
                |inputBreed.getText().toString().isEmpty()
                |inputSex.getText().toString().isEmpty()
                |inputColour.getText().toString().isEmpty()
                );

    }


}
