package com.example.imazjav0017.expensemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MonthlyExpense extends AppCompatActivity {
    List<Expense> expenseList;
    CustomAdapter adapter;
    String month;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_expense);
        expenseList=new ArrayList<>();
        ListView listView=(ListView)findViewById(R.id.listView3);
        adapter=new CustomAdapter(this,R.layout.list_view,expenseList);
        listView.setAdapter(adapter);
        Intent i=getIntent();
        month=i.getStringExtra("month");
        setTitle("Expenses on "+month);
        switch (month)
        {
            case "Jan":
                setData("Jan");
                break;
            case "Feb":
                setData("Feb");
                break;
        }
    }
    void setData(final String month)
    {
        expenseList.clear();
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Expense");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null) {

                    if (objects.size() > 0) {
                        for (ParseObject instance : objects) {
                            String c = instance.getCreatedAt().toString();
                            c = c.substring(4, 7);
                            if (c.equals(month)) {

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


                    }

                    else
                    {
                        Log.i("n",month);
                    }
                }
            }
        );
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
