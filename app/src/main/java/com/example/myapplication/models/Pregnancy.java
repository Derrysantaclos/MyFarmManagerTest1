package com.example.myapplication.models;

import java.time.LocalDate;
import java.time.Period;

public class Pregnancy
{
    //Keeps a track of pregnancy to weaned stage
    private int id;
    private String doeTag;
    private String buckTag;
    private LocalDate crossedDate;
    private int numberOfDays;
    private Boolean pregnancyConfirmation;
    private String message;
    private LocalDate deliveryDate;

    public Pregnancy(String doeTag, String buckTag, LocalDate crossedDate, Boolean pregnancyConfirmation, String message, LocalDate deliveryDate) {
        this.doeTag = doeTag;
        this.buckTag = buckTag;
        this.crossedDate = crossedDate;
        this.pregnancyConfirmation = pregnancyConfirmation;
        this.message = message;
        this.deliveryDate = deliveryDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoeTag() {
        return doeTag;
    }

    public void setDoeTag(String doeTag) {
        this.doeTag = doeTag;
    }

    public String getBuckTag() {
        return buckTag;
    }

    public void setBuckTag(String buckTag) {
        this.buckTag = buckTag;
    }

    public LocalDate getCrossedDate() {
        return crossedDate;
    }

    public void setCrossedDate(LocalDate crossedDate) {
        this.crossedDate = crossedDate;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public int calculateNoOfDays() {
        int months =Period.between(crossedDate,LocalDate.now()).getMonths();
        int days = Period.between(crossedDate,LocalDate.now()).getDays();
        this.numberOfDays = days+(months*30);
        return this.numberOfDays;

    }

    public Boolean getPregnancyConfirmation() {
        return pregnancyConfirmation;
    }

    public void setPregnancyConfirmation(Boolean pregnancyConfirmation) {
        this.pregnancyConfirmation = pregnancyConfirmation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
