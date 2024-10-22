package com.example.myapplication.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;

public class AuthPage extends AppCompatActivity implements View.OnClickListener {
    private Button signUpButton;
    private Button signInButton;
    private Button authButton;
    private View signUpForm;
    private View signInForm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize buttons
        signUpButton = findViewById(R.id.signUpButton);
        signInButton = findViewById(R.id.signInButton);
        authButton= findViewById(R.id.authButton);
        signInForm = findViewById(R.id.sign_in_form);
        signUpForm= findViewById(R.id.sign_up_form);


        // Set default selected state
        signUpButton.setSelected(true); // Sign up selected by default

        // Set click listeners
        signUpButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);

        // Set default form visibility
        signUpForm.setVisibility(View.VISIBLE);  // Show sign-up form initially
        signInForm.setVisibility(View.GONE);     // Hide sign-in form
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signUpButton) {
            setSelectedButton(signUpButton, signInButton); // Toggle selection
            toggleFormVisibility(true);

        } else if (v.getId() == R.id.signInButton) {
            setSelectedButton(signInButton, signUpButton); // Toggle selection
            toggleFormVisibility(false);
        }
    }

    // Helper method to toggle button selections
    private void setSelectedButton(Button selectedButton, Button unselectedButton) {
        selectedButton.setSelected(true);
        unselectedButton.setSelected(false);
    }
    private void toggleFormVisibility(Boolean showSignUp){
        if (showSignUp){
            signUpForm.setVisibility(View.VISIBLE);
            signInForm.setVisibility(View.GONE);
            authButton.setText(R.string.sign_up);
        }else{
            signUpForm.setVisibility(View.GONE);
            signInForm.setVisibility(View.VISIBLE);
            authButton.setText(R.string.sign_in);

        }
    }

}
