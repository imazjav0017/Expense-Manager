package com.example.imazjav0017.expensemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {
BarChart chart;
    ArrayList<BarEntry> yvals;
    List<String> amounts;
    List<Expense> expenseList;
    ArrayList<String> labels;
    CustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        setTitle("Monthly Expenses");
        expenseList=new ArrayList<>();
        amounts=new ArrayList<>();
        labels = new ArrayList<>();
        chart=(BarChart)findViewById(R.id.chart);
        chart.getDescription().setEnabled(false);
         yvals=new ArrayList<>();
        ListView listView=(ListView)findViewById(R.id.listView2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(getApplicationContext(),MonthlyExpense.class);
                Expense e=expenseList.get(position);
                i.putExtra("month",e.getDescription());
                startActivity(i);
            }
        });
        adapter=new CustomAdapter(this,R.layout.list_view,expenseList);
        listView.setAdapter(adapter);
        findMonth("Jan",0);
        findMonth("Feb",1);
        labels=new ArrayList<>();
        labels.add("January");
        labels.add("February");
        /*findMonth("Mar",2);
        findMonth("Apr",3);
        findMonth("May",4);
        findMonth("Jun",5);
        findMonth("Jul",6);
        findMonth("Aug",7);
        findMonth("Sep",8);
        findMonth("Oct",9);
        findMonth("Nov",10);
        findMonth("Dec",11);*/
       // getData();
        chart.setFitBars(true);


    }


    void findMonth(final String month,final int ix)
    {

        ParseQuery<ParseObject> query=ParseQuery.getQuery("Expense");
        query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null) {

                    if (objects.size() > 0) {
                        int totalCost = 0;
                        for (ParseObject i : objects) {
                            String c = i.getCreatedAt().toString();
                            c = c.substring(4, 7);
                            if (c.equals(month)) {
                                totalCost += Integer.parseInt(i.getString("amount"));
                            }
                        }
                        expenseList.add(new Expense(month, "₹ " + String.valueOf(totalCost)));
                        adapter.notifyDataSetChanged();
                        yvals.add(new BarEntry(ix, totalCost));
                        BarDataSet set = new BarDataSet(yvals, "Months");
                        set.setColors(ColorTemplate.COLORFUL_COLORS);
                        set.setDrawValues(true);
                        BarData data = new BarData(set);
                        chart.setData(data);
                        chart.invalidate();
                        XAxis xAxis=chart.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                        chart.animateY(1700);
                    }

                    else
                    {
                        Log.i("n",month);
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

}
