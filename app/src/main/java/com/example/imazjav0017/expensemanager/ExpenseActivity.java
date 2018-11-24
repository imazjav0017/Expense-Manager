package com.example.imazjav0017.expensemanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionBarContextView;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.LayoutInflater;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ExpenseActivity extends AppCompatActivity {
  /* public static ArrayList<String> list=new ArrayList<>();
    ArrayAdapter adapter;*/
    List<Expense> expenseList;
    CustomAdapter adapter;
    TextView totalExpense;
    TextView dateAndTime;
    boolean isReset=false;
    public void setData()
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Expense");
        query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0)
                    {
                        expenseList.clear();
                        for(ParseObject instance:objects)
                        {
                            if(instance.getString("where")==null && instance.getString("who")!=null &&!(instance.getString("who").matches("null")))
                                expenseList.add(new Expense(instance.getString("who"),"₹ "+instance.getString("amount")));
                            else if(instance.getString("where")!=null && instance.getString("who")==null &&!(instance.getString("where").matches("null"))) {
                                expenseList.add(new Expense(instance.getString("where"),"₹ "+instance.getString("amount")));
                            }
                            else if(instance.getString("where")!=null && instance.getString("who")!=null &&(instance.getString("who").matches("null")))
                            {
                                expenseList.add(new Expense(instance.getString("where"),"₹ "+instance.getString("amount")));
                            }
                            else if(instance.getString("where")!=null && instance.getString("who")!=null &&(instance.getString("where").matches("null")))
                            {
                                expenseList.add(new Expense(instance.getString("who"),"₹ "+instance.getString("amount")));
                            }

                            else
                                expenseList.add(new Expense(instance.getString("who"),"₹ "+instance.getString("amount")));
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                else
                    Toast.makeText(ExpenseActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
   public void reset()
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Expense");
        query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    isReset=true;

                    if(objects.size()>0)
                    {

                        for(ParseObject del:objects)
                        {
                            del.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {

                                    if(e!=null) {
                                        e.getMessage();
                                    }
                                    else if(e==null)
                                    {

                                        if(isReset) {
                                            Toast.makeText(ExpenseActivity.this, "Reset Successful!", Toast.LENGTH_SHORT).show();
                                            isReset=false;
                                            ParseUser.getCurrentUser().put("Total","0");
                                            ParseUser.getCurrentUser().saveInBackground();
                                            ParseUser.getCurrentUser().put("startDate",ParseUser.getCurrentUser().getUpdatedAt().toString());
                                            ParseUser.getCurrentUser().saveInBackground();
                                        }
                                        //recreate();
                                        expenseList.clear();
                                        adapter.notifyDataSetChanged();
                                        setTotal();
                                        Log.i("reset","yes");
                                    }
                                }
                            });
                        }
                        recreate();
                    }
                    else
                    {
                        ParseUser.getCurrentUser().put("startDate",ParseUser.getCurrentUser().getUpdatedAt().toString());
                        ParseUser.getCurrentUser().saveInBackground();
                        recreate();
                        expenseList.clear();
                        adapter.notifyDataSetChanged();
                        setTotal();
                        Toast.makeText(ExpenseActivity.this, "Reset Successful!", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                    Toast.makeText(ExpenseActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void addExpense(View v)
    {
        Intent i=new Intent(getApplicationContext(),AddExpenseActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.logout)
        {
            ParseUser.logOut();
            Intent i=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        else if(item.getItemId()==R.id.search)
        {
            Intent i=new Intent(getApplicationContext(),SearchActivity.class);
            startActivity(i);
            return true;
        }
        else if(item.getItemId()==R.id.reset)
        {
            new AlertDialog.Builder(this).setTitle("Reset?").setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage("Are You Sure You Want To Reset?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    reset();
                   // Toast.makeText(ExpenseActivity.this, "Try later Feature Not Available", Toast.LENGTH_SHORT).show();
                }
            }).setNegativeButton("No",null).show();
            return true;
        }
        else if(item.getItemId()==R.id.statisics)
        {
            Intent i=new Intent(getApplicationContext(),StatisticsActivity.class);
            startActivity(i);
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        new AlertDialog.Builder(this).setTitle("Exit?").setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Are You Sure You Want To Exit?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveTaskToBack(true);

            }
        }).setNegativeButton("No",null).show();
    }
    public void setTotal()
    {

            ParseQuery<ParseObject> query=ParseQuery.getQuery("Expense");
            query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(e==null)
                    {
                        if(objects.size()>0)
                        {
                            int total=0;
                            for(ParseObject exp:objects)
                            {
                                String individual=exp.getString("amount");
                                total+=Integer.parseInt(individual);
                            }
                            totalExpense.setText("Total : \u20B9 "+String.valueOf(total));
                            ParseUser.getCurrentUser().put("Total",String.valueOf(total));
                            ParseUser.getCurrentUser().saveInBackground();
                        }
                        else
                        {
                            totalExpense.setText("Total : ₹ 0");
                        }
                    }else
                        Toast.makeText(ExpenseActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
         totalExpense=(TextView)findViewById(R.id.totalExpense);
         dateAndTime=(TextView)findViewById(R.id.dateAndTime);
        expenseList=new ArrayList<>();
        setTotal();
        String date = ParseUser.getCurrentUser().getString("startDate");
         date = date.substring(0, 11);
        dateAndTime.setText("since " + date);
        ListView listView=(ListView)findViewById(R.id.listView);
        adapter=new CustomAdapter(this,R.layout.list_view,expenseList);
        listView.setAdapter(adapter);
       // adapter=new ArrayAdapter(this,R.layout.list_view,R.id.listText,list);
        //listView.setAdapter(adapter);
       // setData();
        setData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(getApplicationContext(),ExpenseDescription.class);
                i.putExtra("pos",position);
                startActivity(i);

            }
        });
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater=mode.getMenuInflater();
                inflater.inflate(R.menu.delete_option,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });


    }
}

