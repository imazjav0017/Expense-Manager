package com.example.imazjav0017.expensemanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExpenseDescription extends AppCompatActivity {
    EditText amountDisplay;
    EditText whoTextView;
    EditText whereTextView;
    EditText descriptionText;
    Button saveButton;
    String objectId;
    public void delete()
    {
       final int pos=getPos();
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Expense");
        query.orderByDescending("createdAt");
        query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0) {
                        int c=1;
                        for (ParseObject instance : objects) {
                            if(c==pos)
                            {
                                instance.deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e==null)
                                        {
                                            Toast.makeText(ExpenseDescription.this, "Deleted!", Toast.LENGTH_SHORT).show();
                                            onBackPressed();
                                        }
                                        else
                                        {
                                            Toast.makeText(ExpenseDescription.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                //Log.i("info",instance.getString("amount"));

                            }
                            c++;
                        }
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.delete_option,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.deleteOption)
        {
            new AlertDialog.Builder(this).setTitle("Delete?").setIcon(R.drawable.ic_action_delete)
                    .setMessage("Are You Sure You Want To Delete this Expense?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    delete();
                }
            }).setNegativeButton("No",null).show();
            return true;
        }
        else return false;
    }

    public int getPos()
    {
        Intent i=getIntent();
        int pos=i.getIntExtra("pos",-1);
        pos++;
        return pos;
    }
    public void saveChanges(View view)
    {
        saveButton.setClickable(false);
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Expense");
        query.whereEqualTo("objectId",objectId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0)
                    {
                     for(ParseObject instance:objects)
                     {
                         try {
                             if(Integer.parseInt(amountDisplay.getText().toString())>1000000)
                             {
                                 throw new Exception("overflow");
                             }
                             if(!(whoTextView.getText().toString().matches("") && whereTextView.getText().toString().matches(""))){
                                 Log.i("in","yes");
                                 instance.put("amount", amountDisplay.getText().toString());
                                 if (!(whoTextView.getText().toString().equals(""))) {
                                     instance.put("who", whoTextView.getText().toString());
                                 } else if ((whoTextView.getText().toString().equals(""))) {
                                     Log.i("y", "y");
                                     instance.put("who", "null");
                                 }
                                 if (!(whoTextView.getText().toString().equals(""))) {
                                     instance.put("where", whereTextView.getText().toString());
                                 } else if ((whereTextView.getText().toString().equals(""))) {
                                     Log.i("y1", "y1");
                                     instance.put("where", "null");
                                 }
                                 if (!(whoTextView.getText().toString().equals(""))) {
                                     instance.put("description", descriptionText.getText().toString());
                                 } else if ((descriptionText.getText().toString().equals(""))) {
                                     instance.put("description", "null");
                                 }
                                 instance.saveInBackground(new SaveCallback() {
                                     @Override
                                     public void done(ParseException e) {
                                         if (e == null) {
                                             Toast.makeText(ExpenseDescription.this, "Saved Changes", Toast.LENGTH_SHORT).show();
                                             onBackPressed();
                                         } else {
                                             saveButton.setClickable(true);
                                             Toast.makeText(ExpenseDescription.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                         }
                                     }
                                 });
                                 break;
                             }
                             else
                                 saveButton.setClickable(true);
                         }
                         catch (Exception ex)
                         {
                             Toast.makeText(ExpenseDescription.this,ex.getMessage(), Toast.LENGTH_SHORT).show();
                             saveButton.setClickable(true);
                         }
                     }
                    }
                }else
                    Toast.makeText(ExpenseDescription.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void calcPosition(final int pos)
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Expense");
        query.orderByDescending("createdAt");
        query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
       query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0) {
                        int c=1;
                        for (ParseObject instance : objects) {
                            if(c==pos)
                            {
                                objectId=instance.getObjectId();
                                TextView dateText=(TextView)findViewById(R.id.dateTextView1);
                                amountDisplay.setText(instance.getString("amount"));
                                dateText.setText("On: "+instance.getString("date"));
                                if(instance.getString("who")!=null &&!(instance.getString("who").matches("null"))) {
                                    whoTextView.setText(instance.getString("who"));
                                }
                                if(instance.getString("where")!=null&&!(instance.getString("where").matches("null")))
                                {
                                    whereTextView.setText(instance.getString("where"));
                                }
                                if(instance.getString("description")!=null &&!(instance.getString("description").matches("null")))
                                {
                                    descriptionText.setText(instance.getString("description"));
                                }



                                //Log.i("info",instance.getString("amount"));

                            }
                            c++;
                        }
                    }
                }
            }
        });

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
        setContentView(R.layout.activity_expense_description);
        saveButton=(Button)findViewById(R.id.saveButton);
        saveButton.setClickable(true);
        amountDisplay=(EditText) findViewById(R.id.amountDisplay);
        whoTextView=(EditText) findViewById(R.id.whoTextView);
        whereTextView=(EditText) findViewById(R.id.whereTextView);
        descriptionText=(EditText) findViewById(R.id.descriptionText);
       Intent i=getIntent();
        int pos=i.getIntExtra("pos",-1);
        pos++;
      calcPosition(pos);

    }
}
