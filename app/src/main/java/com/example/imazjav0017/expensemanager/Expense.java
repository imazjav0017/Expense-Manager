package com.example.imazjav0017.expensemanager;

/**
 * Created by imazjav0017 on 28-01-2018.
 */

public class Expense {
    String description,amount;
    public Expense(String description,String amount)
    {
        this.description=description;
        this.amount=amount;
    }

    public String getDescription() {
        return description;
    }

    public String getAmount() {
        return amount;
    }
}
