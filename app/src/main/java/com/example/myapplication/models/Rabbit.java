package com.example.myapplication.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.Period;

public class Rabbit
{

    private String _age;
    private String _sex;
    private LocalDate _dateOfBirth;
    private String _tag;
    private String _breed;
    private String _source;
    private String _colour;
    private int _id;
    //private String Purity;

    //TODO Weaners, when to wean stuff


    public Rabbit(String sex, LocalDate dateOfBirth, String tag, String breed, String source, String colour) {
        _sex = sex;
        _dateOfBirth = dateOfBirth;
        _tag = tag;
        _breed = breed;
        _source = source;
        _colour =colour;
    }
    public Rabbit(int id, String sex, LocalDate dateOfBirth, String tag, String breed, String source, String colour) {
        _sex = sex;
        _dateOfBirth = dateOfBirth;
        _tag = tag;
        _breed = breed;
        _source = source;
        _colour =colour;
        //_age =age;
        _id =id;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String calculate_age()
    {
        int age = Period.between(_dateOfBirth,LocalDate.now()).getDays();
        int months = Period.between(_dateOfBirth,LocalDate.now()).getMonths();
        int Years =Period.between(_dateOfBirth,LocalDate.now()).getYears();
        int totalMonths =months;
        int daysRemainingForWeeks = age%30;
        int weeks = daysRemainingForWeeks/7;
        int days = daysRemainingForWeeks%7;
        this._age = Years +"years"+totalMonths +" Months, "+ weeks +" Weeks, " + days +" Days";
        return this._age;
    }

    public String get_age(){
        return _age;
    }

    public int get_id(){
        return _id;
    }



    public String get_sex() {
        return _sex;
    }

    public void set_sex(String _sex) {
        this._sex = _sex;
    }

    public LocalDate get_dateOfBirth() {
        return _dateOfBirth;
    }

    public void set_dateOfBirth(LocalDate _dateOfBirth) {
        this._dateOfBirth = _dateOfBirth;
    }

    public String get_tag() {
        return _tag;
    }

    public void set_tag(String _tag) {
        this._tag = _tag;
    }

    public String get_breed() {
        return _breed;
    }

    public void set_breed(String _breed) {
        this._breed = _breed;
    }

    public String get_source() {
        return _source;
    }

    public void set_source(String _source) {
        this._source = _source;
    }

    public String get_colour() {
        return _colour;
    }

    public void set_colour(String _colour) {
        this._colour = _colour;
    }
}
