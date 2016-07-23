package com.szikora.covid;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private SharedPreferences mPrefs;
    private boolean isEmailExist;
    private Gson gson = new Gson();
    private String userJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = (EditText) findViewById(R.id.emailField);
        password = (EditText) findViewById(R.id.passwordField);
        mPrefs = getPreferences(MODE_PRIVATE);
    }

    public void logInButtonClick(View view) {
        if (isAccountExist()) {
            Toast.makeText(getApplicationContext(), "Successfully logged in!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Wrong email or password!", Toast.LENGTH_LONG).show();
        }
    }

    public void signUpButtonClick(View view) {
        if (areInputsValid()) {
            if (isAccountExist() || isEmailExist) {
                Toast.makeText(getApplicationContext(), "User already exists!", Toast.LENGTH_SHORT).show();
            } else {
                signUpUser(new User(email.getText().toString(), password.getText().toString()));
                Toast.makeText(getApplicationContext(), email.getText().toString() + " successfully signed up!",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean areInputsValid() {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            Toast.makeText(getApplicationContext(), "Wrong email format!", Toast.LENGTH_LONG).show();
            return false;
        } else if (password.getText().toString().length() < 4) {
            Toast.makeText(getApplicationContext(), "Too short password!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean isAccountExist() {
        User user;
        Map<String, ?> usersMap = mPrefs.getAll();
        for (Map.Entry<String, ?> entry : usersMap.entrySet()) {
            userJson = (String) entry.getValue();
            user = gson.fromJson(userJson, User.class);
            if (user.getEmail().equals(email.getText().toString())) {
                isEmailExist = true;
                if (user.getPassword().equals(password.getText().toString())) {
                    return true;
                }
            } else {
                isEmailExist = false;
            }
        }
        return false;
    }

    private void signUpUser(User user) {
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        userJson = gson.toJson(user);
        prefsEditor.putString(user.getEmail(), userJson);
        prefsEditor.apply();
    }
}
