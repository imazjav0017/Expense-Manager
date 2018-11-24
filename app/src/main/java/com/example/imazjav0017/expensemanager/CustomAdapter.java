package com.example.imazjav0017.expensemanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by imazjav0017 on 28-01-2018.
 */

public class CustomAdapter extends ArrayAdapter<Expense> {
    List<Expense> expenseList;
    Context context;
    int resource;
    public CustomAdapter(Context context,int resource,List<Expense>expenseList)
    {
        super(context,resource,expenseList);
        this.context=context;
        this.resource=resource;
        this.expenseList=expenseList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(resource,null,false);
        TextView listText=(TextView)view.findViewById(R.id.listText);
        TextView amountRupees=(TextView)view.findViewById(R.id.amountRupees);
        Expense expense=expenseList.get(position);
        listText.setText(expense.getDescription());
        amountRupees.setText(expense.getAmount());

        return view;
    }
}
