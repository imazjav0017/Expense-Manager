package com.example.imazjav0017.expensemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    public void login(View v) throws ParseException {
        loginButton.setClickable(false);
        EditText username=(EditText)findViewById(R.id.loginInput);
        EditText password=(EditText)findViewById(R.id.passwordInput);
        String usernameInput=username.getText().toString();
        String passwordInput=password.getText().toString();
        ParseUser.logInInBackground(usernameInput, passwordInput, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user!=null && e==null)
                {
                    Log.i("Login","Success");
                    Intent i=new Intent(getApplicationContext(),ExpenseActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    loginButton.setClickable(true);
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("LogIn");
        loginButton=(Button)findViewById(R.id.loginButton);
        loginButton.setClickable(true);

    }
}
