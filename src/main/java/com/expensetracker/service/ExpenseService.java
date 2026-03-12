package com.expensetracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;   // ✅ Missing import
import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.repository.UserRepository;

@Service
public class ExpenseService {

    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    UserRepository userRepository;

    public Expense saveExpense(Expense expense){

        // Fetch user from database
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Default user not found. Please register a user first."));

        // Attach user to expense
        expense.setUser(user);

        return expenseRepository.save(expense);
    }

    public List<Expense> getAllExpenses(){
        return expenseRepository.findAll();
    }
}