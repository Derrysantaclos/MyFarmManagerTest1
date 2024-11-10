package com.derrysantaclos.myFarmManager.util;

import android.content.Context;

import androidx.core.content.ContextCompat;

public class PasswordValidator {
    public static int getPasswordStrengthScore(String password) {
        int score = 0;

        if (password.length() >= 6) score++;
        if (password.length() >= 10) score++;
        if (password.matches(".*[A-Z].*")) score++;
        if (password.matches(".*[a-z].*")) score++;
        if (password.matches(".*\\d.*")) score++;
        if (password.matches(".*[@$!%*?&].*")) score++;

        return score;
    }

    public static String getStrengthLabel(int score) {
        if (score < 3) {
            return "Weak";
        } else if (score < 5) {
            return "Moderate";
        } else {
            return "Strong";
        }
    }

    public static int getStrengthColor(int score, Context context) {
        if (score < 3) {
            return ContextCompat.getColor(context, android.R.color.holo_red_dark);
        } else if (score < 5) {
            return ContextCompat.getColor(context, android.R.color.holo_orange_dark);
        } else {
            return ContextCompat.getColor(context, android.R.color.holo_green_dark);
        }
    }
}
