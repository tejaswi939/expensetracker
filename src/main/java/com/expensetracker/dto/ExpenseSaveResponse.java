package com.expensetracker.dto;

import com.expensetracker.entity.Expense;

public class ExpenseSaveResponse {

    private Expense expense;
    private String budgetAlert;

    public ExpenseSaveResponse(Expense expense, String budgetAlert) {
        this.expense = expense;
        this.budgetAlert = budgetAlert;
    }

    public Expense getExpense() { return expense; }
    public String getBudgetAlert() { return budgetAlert; }
}
