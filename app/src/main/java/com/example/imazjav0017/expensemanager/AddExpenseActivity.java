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
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddExpenseActivity extends AppCompatActivity {
    Button saveButton;
    public void save(View view)
    {
        EditText amountInput=(EditText)findViewById(R.id.amountInput);
        EditText who=(EditText)findViewById(R.id.who);
        EditText where=(EditText)findViewById(R.id.where);
        EditText descriptionInput=(EditText)findViewById(R.id.descriptipon);
        try {
            if (Integer.parseInt(amountInput.getText().toString()) > 1000000) {
                Toast.makeText(this, "Enter a lesser Amount!", Toast.LENGTH_SHORT).show();
                saveButton.setClickable(true);
                throw new Exception("Overflow");
            }

            else
            if (!(who.getText().toString().equals("") && where.getText().toString().equals(""))) {
                saveButton.setClickable(false);
                if (Integer.parseInt(amountInput.getText().toString()) <= 0 || amountInput.getText().toString().matches("")) {
                    Toast.makeText(this, "Amount cannot be 0 or less", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("yes", "yes");
                    ParseObject expense = new ParseObject("Expense");
                    expense.put("username", ParseUser.getCurrentUser().getUsername());
                    expense.put("amount", amountInput.getText().toString());
                    if (!(where.getText().toString().matches("")))
                        expense.put("where", where.getText().toString());
                    if (!(who.getText().toString().matches("")))
                        expense.put("who", who.getText().toString());
                    if (!descriptionInput.getText().toString().matches(""))
                        expense.put("description", descriptionInput.getText().toString());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm");
                    String date = sdf.format(new Date());
                    expense.put("date", date);
                    expense.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(AddExpenseActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else
                                Toast.makeText(AddExpenseActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        }catch(Exception e)
        {
            Toast.makeText(this,"Enter a lesser Value!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(this,ExpenseActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        saveButton=(Button)findViewById(R.id.saveButton);
        saveButton.setClickable(true);
    }
}
