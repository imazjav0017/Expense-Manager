package com.example.imazjav0017.expensemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    public void openLogin(View v) {
        Intent i=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void openSignUp(View v)
    {
        Intent i=new Intent(getApplicationContext(),SignUpActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Parse.initialize(this,"dda5075f226aa3b310268d7cce48363b48f18370","32d7b6e329ff9965b7f5f365b18f8111a7bf9afa");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        getSupportActionBar().hide();
        if(ParseUser.getCurrentUser()!=null)
        {
            Intent i=new Intent(getApplicationContext(),ExpenseActivity.class);
            startActivity(i);
        }

    }
}
