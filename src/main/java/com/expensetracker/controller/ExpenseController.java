package com.expensetracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.expensetracker.entity.Expense;
import com.expensetracker.service.ExpenseService;

@RestController
@RequestMapping("/expenses")
@CrossOrigin
public class ExpenseController {

    @Autowired
    ExpenseService service;

    @PostMapping
    public Expense addExpense(@RequestBody Expense expense) {
        return service.saveExpense(expense);
    }

    @GetMapping
    public List<Expense> getExpenses(){
        return service.getAllExpenses();
    }
}