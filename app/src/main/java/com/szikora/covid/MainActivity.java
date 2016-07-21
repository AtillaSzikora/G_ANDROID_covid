package com.szikora.covid;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = (EditText) findViewById(R.id.emailField);
        password = (EditText) findViewById(R.id.passwordField);
    }

    public void signUpButtonClick(View view) {
        writeUserToSharedPref(getUserFromInput());
    }

    private User getUserFromInput() {
        return new User(email.getText().toString(), password.getText().toString());
    }

    public void logInButtonClick (View view) {
        if (readUserFromSharedPref()) {
            Toast.makeText(getApplicationContext(), "Successful log in!", Toast.LENGTH_LONG).show(); }
        else { Toast.makeText(getApplicationContext(), "Wrong email or password!", Toast.LENGTH_LONG).show(); }
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
            Log.i("Ez itt az", user.getEmail());
            if(user.getEmail().equals(email.getText().toString())){ return true; } }
        return false;
    }
}
