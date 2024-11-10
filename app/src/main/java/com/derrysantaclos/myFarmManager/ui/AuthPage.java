package com.derrysantaclos.myFarmManager.ui;

import static com.derrysantaclos.myFarmManager.util.PasswordValidator.getPasswordStrengthScore;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.derrysantaclos.myFarmManager.R;
import com.derrysantaclos.myFarmManager.util.EmailValidator;
import com.derrysantaclos.myFarmManager.util.FirebaseAuthHelper;
import com.derrysantaclos.myFarmManager.util.PasswordValidator;

public class AuthPage extends AppCompatActivity implements View.OnClickListener {
    private Button signUpButton;
    private Button signInButton;
    private Button authButton;
    private View signUpForm;
    private View signInForm;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText fullNameInput;
    private EditText phoneInput;
    private EditText signInEmailInput;
    private EditText signInPasswordInput;
    private TextView passwordStrengthIndicator;
    private TextView resetPasswordText;
    private FirebaseAuthHelper authHelper;
    String signInEmail ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeViews();
        setClickListeners();

        // Add TextWatcher for password strength indication
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordStrengthIndicator.setVisibility(View.VISIBLE);
                updatePasswordStrengthIndicator(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });

        // Set default selected state and form visibility
        signUpButton.setSelected(true);  // Sign up selected by default
        signUpForm.setVisibility(View.VISIBLE);  // Show sign-up form initially
        signInForm.setVisibility(View.GONE);     // Hide sign-in form
    }

    private void initializeViews(){
        // Initialize views and Firebase
        signUpButton = findViewById(R.id.signUpButton);
        signInButton = findViewById(R.id.signInButton);
        authButton = findViewById(R.id.authButton);
        signUpForm = findViewById(R.id.sign_up_form);
        signInForm = findViewById(R.id.sign_in_form);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.password);
        fullNameInput = findViewById(R.id.farmName);
        phoneInput = findViewById(R.id.phoneNumber);
        signInEmailInput = findViewById(R.id.signInEmail);
        signInPasswordInput = findViewById(R.id.signInPassword);
        passwordStrengthIndicator = findViewById(R.id.passwordStrengthIndicator);
        authHelper=new FirebaseAuthHelper(this);
        resetPasswordText = findViewById(R.id.passwordResetText);
    }
    private void setClickListeners() {
        signUpButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        authButton.setOnClickListener(this);
        resetPasswordText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signUpButton) {
            setSelectedButton(signUpButton, signInButton); // Toggle selection
            toggleFormVisibility(true);
        } else if (v.getId() == R.id.signInButton) {
            setSelectedButton(signInButton, signUpButton); // Toggle selection
            toggleFormVisibility(false);
        } else if (v.getId() == R.id.authButton) {
            if (signUpForm.getVisibility() == View.VISIBLE) {
                createUser();
            } else {
                signInUser();
            }
        } else if (v.getId()==R.id.passwordResetText){
            resetPassword();

        }
    }

    // Method to toggle button selections
    private void setSelectedButton(Button selectedButton, Button unselectedButton) {
        selectedButton.setSelected(true);
        unselectedButton.setSelected(false);
    }

    // Method to toggle form visibility
    private void toggleFormVisibility(Boolean showSignUp) {
        if (showSignUp) {
            signUpForm.setVisibility(View.VISIBLE);
            signInForm.setVisibility(View.GONE);
            authButton.setText(R.string.sign_up);
        } else {
            signUpForm.setVisibility(View.GONE);
            signInForm.setVisibility(View.VISIBLE);
            authButton.setText(R.string.sign_in);
        }
    }

    // Method to handle user creation logic
    private void signInUser() {
        signInEmail = signInEmailInput.getText().toString();
        String signInPassword = signInPasswordInput.getText().toString();

        if (EmailValidator.isEmailValid(this, signInEmail)){
            return;
        }
        if (signInPassword.isEmpty()) {
            Toast.makeText(AuthPage.this, "Fill email and password fields", Toast.LENGTH_SHORT).show();
            return;
        }

        authHelper.signInUser(signInEmail,signInPassword, resetPasswordText);
    }

    private void createUser() {
        String userEmail = emailInput.getText().toString();
        String userPassword = passwordInput.getText().toString();
        String fullName = fullNameInput.getText().toString();
        String phone = phoneInput.getText().toString();

        //validate Email
        if (EmailValidator.isEmailValid(this, userEmail)) {
            return;
        }

        // Validate inputs
        if (userPassword.isEmpty() || fullName.isEmpty() || phone.isEmpty()) {
            Toast.makeText(AuthPage.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (getPasswordStrengthScore(userPassword) < 4) {
            Toast.makeText(AuthPage.this, "Password should be stronger (include uppercase, lowercase, numeric, and special characters).", Toast.LENGTH_SHORT).show();
            return;
        }

        // Attempt to create a user with Firebase Authentication
        authHelper.createUser(userEmail,userPassword,fullName,phone);

    }
    private void resetPassword(){
        signInEmail= signInEmailInput.getText().toString();
        if (EmailValidator.isEmailValid(this, signInEmail)){
            return;
        }

        authHelper.resetPassword(signInEmail);

    }

    // Method to update the password strength indicator
    private void updatePasswordStrengthIndicator(String password) {
        if (password.isEmpty()) {
            passwordStrengthIndicator.setText("");
            return;
        }

        int strengthScore = PasswordValidator.getPasswordStrengthScore(password);
        String strengthLabel = PasswordValidator.getStrengthLabel(strengthScore);
        int strengthColor = PasswordValidator.getStrengthColor(strengthScore, this);

        passwordStrengthIndicator.setText(strengthLabel);
        passwordStrengthIndicator.setTextColor(strengthColor);
    }

}
