package com.example.imazjav0017.expensemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    ArrayList<String> list=new ArrayList<>();
    ArrayAdapter adapter;
     TextView searchTotal;
    EditText searchInput;
    Button searchButton;
    public void performSearch(View v)
    {
        searchButton.setClickable(false);
        final Button searchButton =(Button)findViewById(R.id.searchButton);
        list.clear();
        final String searchText=searchInput.getText().toString();
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Expense");
        query.orderByDescending("createdAt");
        query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    int total=0;
                    if(objects.size()>0) {
                        for (ParseObject instance : objects) {
                            if((instance.getString("who")!=null && instance.getString("where")!=null)&&
                                    instance.getString("who").equalsIgnoreCase(searchText))
                            {
                                list.add("₹ " + instance.getString("amount") + " for " + instance.getString("who") + " at " +
                                        instance.getString("where") + " on " + instance.getString("date"));
                                int subTotal=Integer.parseInt(instance.getString("amount"));
                                total+=subTotal;
                            }
                            else if((instance.getString("who")!=null &&instance.getString("where")==null)&&
                                    instance.getString("who").equalsIgnoreCase(searchText))
                            {
                                int subTotal=Integer.parseInt(instance.getString("amount"));
                                total+=subTotal;
                                list.add("₹ "+instance.getString("amount")+" for "+instance.getString("who")+" on "+instance.getString("date"));
                            }
                            else if((instance.getString("where")!=null && instance.getString("who")==null)&&
                                    instance.getString("where").equalsIgnoreCase(searchText))
                            {
                                int subTotal=Integer.parseInt(instance.getString("amount"));
                                total+=subTotal;
                                list.add("₹ " + instance.getString("amount") + " at " + instance.getString("where")
                                        + " on " + instance.getString("date"));
                            }
                        }
                    }
                    else
                        list.add("No Result");
                    adapter.notifyDataSetChanged();
                    searchTotal.setText("₹ "+String.valueOf(total));
                    searchTotal.setVisibility(View.VISIBLE);
                }
                else
                    Toast.makeText(SearchActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
         searchInput=(EditText)findViewById(R.id.searchInput);
        searchButton=(Button)findViewById(R.id.searchButton);
        getSupportActionBar().hide();
        ListView listView=(ListView)findViewById(R.id.searchedList);
        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        searchTotal=(TextView)findViewById(R.id.searchTotal);
        searchTotal.setVisibility(View.INVISIBLE);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchButton.setClickable(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
