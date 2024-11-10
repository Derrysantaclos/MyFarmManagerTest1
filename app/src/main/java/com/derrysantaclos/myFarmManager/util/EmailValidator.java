package com.derrysantaclos.myFarmManager.util;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

public class EmailValidator {

    /**
     * Validates if the given email is not empty and matches a valid email pattern.
     * If the email is invalid, it shows a toast message automatically.
     *
     * @param context The context used to show the toast.
     * @param email The email string to validate.
     * @return True if the email is valid, false otherwise.
     */
    public static boolean isEmailValid(Context context, String email) {
        if (email == null || email.isEmpty()) {
            Toast.makeText(context, "Email field is empty. Please enter your email.", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Invalid email format. Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false; // Email is valid
    }
}
