package com.example.myapplication.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MyDateTimeFormatter {

            DateTimeFormatter myDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            public LocalDate dateStringToLocalDate(String dateString){
                return LocalDate.parse(dateString,myDateTimeFormatter);
            }

}
