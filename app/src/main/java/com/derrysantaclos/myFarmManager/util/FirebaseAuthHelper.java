package com.derrysantaclos.myFarmManager.util;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.derrysantaclos.myFarmManager.ui.MenuPage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.derrysantaclos.myFarmManager.models.User;

public class FirebaseAuthHelper {
    private final FirebaseAuth myAuth;
    private final FirebaseFirestore firestore;
    private final Activity activity;
    private final Intent MenuPageIntent;


    public FirebaseAuthHelper(Activity activity) {
        this.myAuth = FirebaseAuth.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
        this.activity = activity;
       this.MenuPageIntent = new Intent(activity,MenuPage.class);
    }

    public void createUser(String email, String password, String fullName, String phone) {
        myAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = myAuth.getCurrentUser();
                        if (user != null) {
                            saveUserDetails(user, fullName, phone);
                            //send Verification Email and move to next page
                            sendVerificationEmail(user);

                        }
                    } else {
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthException) {
                            FirebaseAuthException authException = (FirebaseAuthException) exception;
                            String errorCode = authException.getErrorCode();
                            Log.e("Auth Error", "createUserWithEmail failed: " + errorCode + " - " + exception.getMessage());
                            Toast.makeText(activity, "Error: " + errorCode, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w("User", "createUserWithEmail:failure", exception);
                            assert exception != null;
                            Toast.makeText(activity, "Authentication failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signInUser(String email, String password, TextView errorTextView) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(activity, "Fill email and password fields", Toast.LENGTH_SHORT).show();
            return;
        }

        myAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = myAuth.getCurrentUser();
                        if (user != null) {
                            if (user.isEmailVerified()) {
                                Toast.makeText(activity, "User Logged In", Toast.LENGTH_SHORT).show();
                                // TODO: Send User to the next page
                                activity.startActivity(MenuPageIntent);
                                activity.finish();
                            } else {
                                Toast.makeText(activity, "Please verify your email address", Toast.LENGTH_SHORT).show();
                                activity.startActivity(MenuPageIntent);
                                activity.finish();
                            }
                        }
                    } else {
                        Exception exception = task.getException();

                        if (exception instanceof FirebaseAuthException) {
                            FirebaseAuthException authException = (FirebaseAuthException) exception;
                            String errorCode = authException.getErrorCode();
                            Log.e("Auth Error", "signInWithEmail failed: " + errorCode + " - " + exception.getMessage());

                            if ("ERROR_WRONG_PASSWORD".equals(errorCode)) {
                                errorTextView.setVisibility(View.VISIBLE); // Make the TextView visible
                                Toast.makeText(activity, "Incorrect password", Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(activity, "Error: " + errorCode, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Log.w("Sign In", "signInWithEmail:failure", exception);
                            assert exception != null;
                            Toast.makeText(activity, "Sign-in failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void resetPassword(String email) {
        myAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(activity, "Please follow the link in your email to reset your password", Toast.LENGTH_SHORT).show();
            } else {
                Exception exception = task.getException();
                if (exception instanceof FirebaseAuthException) {
                    FirebaseAuthException authException = (FirebaseAuthException) exception;
                    String errorCode = authException.getErrorCode();
                    Log.e("Reset Password Error", "resetPassword failed: " + errorCode + " - " + exception.getMessage());
                    Toast.makeText(activity, "Error: " + errorCode, Toast.LENGTH_SHORT).show();
                } else {
                    Log.w("Reset Password", "resetPassword:failure", exception);
                    assert exception != null;
                    Toast.makeText(activity, "An error occurred: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveUserDetails(FirebaseUser user, String fullName, String phone) {
        User userDetails = new User(fullName, phone);
        firestore.collection("users").document(user.getUid())
                .set(userDetails)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "User details saved successfully!"))
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error saving user details", e);
                    Toast.makeText(activity, "Error saving user details", Toast.LENGTH_SHORT).show();
                });
    }

    private void sendVerificationEmail(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Email", "Verification email sent.");
                        Toast.makeText(activity, "Verification email sent.", Toast.LENGTH_SHORT).show();
                        activity.startActivity(MenuPageIntent);
                        activity.finish();

                    } else {
                        Log.w("Email", "Failed to send verification email", task.getException());
                        Toast.makeText(activity, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
