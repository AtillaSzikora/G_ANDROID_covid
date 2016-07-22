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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText) findViewById(R.id.emailField);
        password = (EditText) findViewById(R.id.passwordField);
        logInButton= (Button) findViewById(R.id.logInButton);
        signUpButton= (Button) findViewById(R.id.signUpButton);
        logInButtonClick();
        signUpButtonClick();
    }

    private void logInButtonClick() {
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (readUserFromSharedPref()) {
                    Toast.makeText(getApplicationContext(), "Successful log in!", Toast.LENGTH_LONG).show(); }
                else { Toast.makeText(getApplicationContext(), "Wrong email or password!", Toast.LENGTH_LONG).show(); }
            } }); }

    private void signUpButtonClick() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInputs();
            } }); }

    private void validateInputs() {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                Toast.makeText(getApplicationContext(), "Wrong email format!", Toast.LENGTH_LONG).show(); }
            else if (
                    password.getText().toString().length() < 4) {
            Toast.makeText(getApplicationContext(), "Too short password!", Toast.LENGTH_LONG).show(); }
        else {
            writeUserToSharedPref(new User(email.getText().toString(), password.getText().toString()));
            Toast.makeText(getApplicationContext(), email.getText().toString() + " successfully signed up!", Toast.LENGTH_LONG).show(); }
    }

    private void writeUserToSharedPref(User user) {
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString(user.getEmail(), json);
        prefsEditor.apply();
    }

    private boolean readUserFromSharedPref() {
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        String userJson;
        User user;

        Map<String,?> usersMap = mPrefs.getAll();
        for(Map.Entry<String,?> entry : usersMap.entrySet()){
            userJson = (String) entry.getValue();
            user = gson.fromJson(userJson, User.class);
            if(user.getEmail().equals(email.getText().toString())
            && user.getPassword().equals(password.getText().toString())){
                return true; } }
        return false;
    }
}
