package com.example.imazjav0017.expensemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {
    Button signUpButton;
    public void signUp(View v)
    {
        signUpButton.setClickable(false);
        EditText input1=(EditText)findViewById(R.id.input1);
        EditText input2=(EditText)findViewById(R.id.input2);
        EditText input3=(EditText)findViewById(R.id.input3);
        String username=input1.getText().toString();
        String password=input2.getText().toString();
        if(password.matches(input3.getText().toString()))
        {
            //ParseQuery<ParseUser> query=ParseUser.getQuery();
            final ParseUser newUser=new ParseUser();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null)
                    {
                        newUser.put("Total","0");
                        newUser.put("startDate",newUser.getCreatedAt().toString());
                        newUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e!=null)
                                    Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.i("Signup","Success");
                        Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else {
                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        signUpButton.setClickable(true);

                    }    }
            });
        }
        else
        {
            signUpButton.setClickable(true);
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("SignUp");
        signUpButton=(Button)findViewById(R.id.registerButton);
        signUpButton.setClickable(true);

    }
}
