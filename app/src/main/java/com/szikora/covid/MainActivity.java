package com.szikora.covid;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button logInButton;
    private Button signUpButton;
    private SharedPreferences mPrefs;
    private boolean isEmailExist;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization(); }

    private void initialization() {
        email = (EditText) findViewById(R.id.emailField);
        password = (EditText) findViewById(R.id.passwordField);
        logInButton= (Button) findViewById(R.id.logInButton);
        signUpButton= (Button) findViewById(R.id.signUpButton);
        mPrefs = getPreferences(MODE_PRIVATE);
        gson = new Gson();
        logInButtonClick();
        signUpButtonClick(); }

    private void logInButtonClick() {
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAccountExist()) {
                    Toast.makeText(getApplicationContext(), "Successfully logged in!", Toast.LENGTH_LONG).show(); }
                else { Toast.makeText(getApplicationContext(), "Wrong email or password!", Toast.LENGTH_LONG).show(); }
            } }); }

    private void signUpButtonClick() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAccountExist() && !isEmailExist) { validateInputs();
                } else {
                    Toast.makeText(getApplicationContext(), "User already exists!", Toast.LENGTH_SHORT).show(); }
            } }); }

    private void validateInputs() {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            Toast.makeText(getApplicationContext(), "Wrong email format!", Toast.LENGTH_LONG).show();
        } else if (
            password.getText().toString().length() < 4) {
            Toast.makeText(getApplicationContext(), "Too short password!", Toast.LENGTH_LONG).show();
        } else {
            putUserToSharedPref(new User(email.getText().toString(), password.getText().toString()));
            Toast.makeText(getApplicationContext(), email.getText().toString() + " successfully signed up!", Toast.LENGTH_LONG).show(); } }

    private void putUserToSharedPref(User user) {
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        String userJson = gson.toJson(user);
        prefsEditor.putString(user.getEmail(), userJson);
        prefsEditor.apply(); }

    private boolean isAccountExist() {
        Map<String,?> usersMap = mPrefs.getAll();
        for(Map.Entry<String,?> entry : usersMap.entrySet()){
            String userJson = (String) entry.getValue();
            User user = gson.fromJson(userJson, User.class);
            isEmailExist = user.getEmail().equals(email.getText().toString());
            if(user.getEmail().equals(email.getText().toString())
            && user.getPassword().equals(password.getText().toString())){
                return true; } }
        return false; }
}
